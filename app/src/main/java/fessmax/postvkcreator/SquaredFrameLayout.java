package fessmax.postvkcreator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SquaredFrameLayout extends FrameLayout {
    public SquaredFrameLayout(@NonNull Context context) {
        super(context);
    }

    public SquaredFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int squareSize = getMeasuredWidth(); // square size
        int measureSpec = MeasureSpec.makeMeasureSpec(squareSize, MeasureSpec.EXACTLY);
        super.onMeasure(measureSpec, measureSpec);
    }
}
