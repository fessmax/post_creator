package fessmax.postvkcreator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class EditTextNoTouch extends EditText {

    public interface OnTextChangeListener {
        void OnTextChanged(boolean isNotEmpty);
    }

    public EditTextNoTouch(Context context) {
        super(context);
        init();
    }

    public EditTextNoTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextNoTouch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ArrayList<TextStyle> textStyles;
    private int currentIdStyle;
    private OnTextChangeListener textChangeListener;

    void init() {
        textStyles = TextStyle.generateSyles();
        currentIdStyle = -1;
        changeStyle();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                CommonHelper.updateSpannableEditText(EditTextNoTouch.this, textStyles.get(currentIdStyle));
                if (textChangeListener != null)
                    textChangeListener.OnTextChanged(EditTextNoTouch.this.getText().length() > 0);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    public void changeStyle() {
        currentIdStyle = (currentIdStyle + 1) % textStyles.size();
        setTextAppearance(getContext(), textStyles.get(currentIdStyle).styleId);
        CommonHelper.updateSpannableEditText(this, textStyles.get(currentIdStyle));
    }

    public void addOnTextChangedListener(OnTextChangeListener listener) {
        this.textChangeListener = listener;
    }
}
