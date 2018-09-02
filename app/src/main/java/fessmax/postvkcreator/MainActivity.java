package fessmax.postvkcreator;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BackgroundAdapter.OnItemClickListener, EditTextNoTouch.OnTextChangeListener, TrashImageView.OnTrashView {

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private final int SELECT_PICTURE = 1001;
    private final int SELECT_STICKER = 1002;

    private ViewGroup mainLayout;
    private ViewGroup stickersLayout;
    private ArrayList<StickerView> views = new ArrayList<>();
    private ImageButton addStickerButton;
    private ImageButton changeStyle;
    private Button saveButton;
    private EditTextNoTouch editText;
    private TrashImageView trash;

    private Background[] backgrounds;
    private RecyclerView backgroundsRV;
    private RecyclerView.LayoutManager backgroundsLM;
    private RecyclerView.Adapter backgroundsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        setupViews();
    }

    private void setupViews() {
        mainLayout = findViewById(R.id.main_layout);

        editText = findViewById(R.id.edit_text);
        editText.addOnTextChangedListener(this);
        stickersLayout = findViewById(R.id.stickers_layout);

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);

        trash = findViewById(R.id.trash);
        trash.setOnTrashViewListener(this);
        stickersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            }
        });
        backgrounds = Background.generateBackgrounds();
        backgroundsRV = findViewById(R.id.backgrounds_rv);
        backgroundsRV.setHasFixedSize(true);

        backgroundsLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        backgroundsRV.setLayoutManager(backgroundsLM);

        backgroundsAdapter = new BackgroundAdapter(this, backgrounds, this);
        backgroundsRV.setAdapter(backgroundsAdapter);

        changeBackground(backgrounds[0].bigImageResId);
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);

            Toolbar parent = (Toolbar) getSupportActionBar().getCustomView().getParent();
            parent.setContentInsetsAbsolute(0, 0);

            View actionBarView = getSupportActionBar().getCustomView();
            addStickerButton = actionBarView.findViewById(R.id.stickers_button);
            addStickerButton.setOnClickListener(this);

            changeStyle = findViewById(R.id.change_style_button);
            changeStyle.setOnClickListener(this);
        }
    }

    private View viewForSave;

    private void tryToSaveImageFromView(View view) {
        viewForSave = view;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return;
        }
        CommonHelper.saveImageFromView(this, view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CommonHelper.saveImageFromView(this, viewForSave);
                } else {
                    Toast.makeText(this, "Please grant permission", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(changeStyle)) {
            editText.changeStyle();
        } else if (view.equals(addStickerButton)) {
            callSelectSticker();
        } else if (view.equals(saveButton)) {
            tryToSaveImageFromView(mainLayout);
        }
    }

    private void callSelectSticker() {

        Intent intent = new Intent(this, StickersActivity.class);

        startActivityForResult(intent, SELECT_STICKER);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_stay);
    }

    private void createSticker(int resId) {
        StickerView stickerView = new StickerView(this, resId, trash);
        views.add(stickerView);
        stickersLayout.addView(stickerView);
    }

    private void deleteSticker(View view){
        views.remove(view);
        stickersLayout.removeView(view);
    }

    private void changeBackground(Drawable drawable) {
        stickersLayout.setBackground(drawable);
    }

    private void changeBackground(int resId) {
        stickersLayout.setBackgroundResource(resId);
    }

    private void callSelectionGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select background"), SELECT_PICTURE);
    }

    @Override
    public void onItemClick(Background item) {
        if (item.fromGallery) {
            callSelectionGallery();
        } else {
            changeBackground(item.bigImageResId);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                Drawable bg;
                try {
                    if (selectedImageUri != null) {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
                        changeBackground(bg);
                    }
                } catch (Exception e) {
                    Log.e("onActivityResult", e.getMessage(), e);
                }
            } else if (requestCode == SELECT_STICKER) {
                if (data != null) {
                    int stickerResId = data.getIntExtra(StickersActivity.SELECTED_STICKER_ID, 0);
                    createSticker(stickerResId);
                }
            }
        }
    }

    @Override
    public void OnTextChanged(boolean isNotEmpty) {
        saveButton.setEnabled(isNotEmpty);
    }

    @Override
    public void trashView(View view) {
        deleteSticker(view);
    }
}
