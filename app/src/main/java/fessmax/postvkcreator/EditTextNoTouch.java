package fessmax.postvkcreator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class EditTextNoTouch extends EditText {
    public EditTextNoTouch(Context context) {
        super(context);
    }

    public EditTextNoTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextNoTouch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
//        return super.onTouchEvent(event);
    }
}
