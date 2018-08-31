package fessmax.postvkcreator;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private ViewGroup stickersLayout;
    private ArrayList<StickerView> views = new ArrayList<>();
    private ImageButton addStickerButton;
    private ImageButton changeStyle;
    private Button saveButton;
    private EditText editText;

    private ArrayList<TextStyle> textStyles;
    private int currentIdStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        setupViews();

        textStyles = TextStyle.generateSyles();
        currentIdStyle = -1;
        changeStyle();
    }

    private void setupViews() {
        editText = findViewById(R.id.edit_text);
        stickersLayout = findViewById(R.id.stickers_layout);

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                CommonHelper.updateSpannableEditText(editText, textStyles.get(currentIdStyle));
            }
        });

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
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);

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
            changeStyle();
        } else if (view.equals(addStickerButton)) {
            createSticker();
        } else if (view.equals(saveButton)) {
            tryToSaveImageFromView(stickersLayout);
        }
    }

    private void changeStyle() {
        currentIdStyle = (currentIdStyle + 1) % textStyles.size();
        editText.setTextAppearance(getApplicationContext(), textStyles.get(currentIdStyle).styleId);
        CommonHelper.updateSpannableEditText(editText, textStyles.get(currentIdStyle));
    }

    private void createSticker() {
        StickerView stickerView = new StickerView(this, R.drawable.sticker_1);

        if (views.size() > 0) views.get(views.size() - 1).setActive(false);
        stickerView.setActive(true);

        views.add(stickerView);
        stickersLayout.addView(stickerView);
    }


}
