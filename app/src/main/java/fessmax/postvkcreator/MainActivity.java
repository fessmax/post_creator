package fessmax.postvkcreator;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private ViewGroup stickersLayout;
    private ArrayList<StickerView> views = new ArrayList<>();

    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();

        this.editText = findViewById(R.id.edit_text);
        this.stickersLayout = findViewById(R.id.stickers_layout);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToSaveImageFromView(stickersLayout);
            }
        });

        Button keyboardButton = findViewById(R.id.keyboard_button);
        keyboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });


/*        String myString = "";
        Spannable spanna = new SpannableString(myString);
        spanna.setSpan(new BackgroundColorSpan(0xFFCCCCCC), 0, myString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(spanna);*/

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                updateSpannableEditText(editable);
            }
        });

    }

    Spannable span;
    void updateSpannableEditText(CharSequence charSequence){

        int color = flag ? 0xFFCCCCCC : 0xFFCC00CC;
        span = (Spannable) charSequence;
        span.setSpan(new BackgroundColorSpan(color), 0, editText.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public boolean flag = true;

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);

            View actionBarView = getSupportActionBar().getCustomView();
            ImageButton addStickerButton = actionBarView.findViewById(R.id.stickers_button);
            addStickerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createSticker();
                }
            });

            ImageButton changeStyle = findViewById(R.id.change_style_button);
            changeStyle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag) {
                        editText.setTextAppearance(getApplicationContext(), R.style.TextViewStyle1);
                    } else {
                        editText.setTextAppearance(getApplicationContext(), R.style.TextViewStyle2);
                    }
                    flag = !flag;
                    updateSpannableEditText(editText.getText());
                }
            });
        }
    }

    private void createSticker() {
        StickerView stickerView = new StickerView(this, R.drawable.sticker_1);

        if (views.size() > 0) views.get(views.size() - 1).setActive(false);
        stickerView.setActive(true);

        views.add(stickerView);
        stickersLayout.addView(stickerView);
        Log.e("createSticker", stickersLayout.getWidth() + " - " + stickersLayout.getHeight());
    }

    private View viewForSave;

    private void tryToSaveImageFromView(View view) {
        viewForSave = view;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return;
        }
        saveImageFromView(view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImageFromView(viewForSave);
                } else {
                    Toast.makeText(this, "Please grant permission", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private String saveImageFromView(View view) {
        if (view == null) return "";
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        String fileName = "New_post_" + System.currentTimeMillis();
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                returnedBitmap,
                fileName,
                "new post"
        );
        Log.e("saveImageFromView", String.format("fileName: %s, filePath: %s", fileName, savedImageURL));
        return savedImageURL;
    }

}
