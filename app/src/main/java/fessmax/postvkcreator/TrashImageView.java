package fessmax.postvkcreator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class TrashImageView extends ImageView implements StickerView.OnStickerMoveListener {
    public interface OnTrashView {
        void trashView(View view);
    }

    public TrashImageView(Context context) {
        super(context);
    }

    public TrashImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TrashImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnTrashView listener;

    public void setOnTrashViewListener(OnTrashView listener) {
        this.listener = listener;
    }

    @Override
    public void onStickerMove(MotionEvent event, View view) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                if (checkIntersect(event)) {
                    if (listener != null) listener.trashView(view);
                }
                setVisibility(INVISIBLE);
                break;
            default:
                this.setSelected(checkIntersect(event));
                setVisibility(VISIBLE);
                break;
        }
        dispatchTouchEvent(event);
    }

    boolean checkIntersect(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();

        float sX = this.getX();
        float sY = this.getY();

        float fX = this.getX() + this.getMeasuredWidth();
        float fY = this.getY() + this.getMeasuredHeight();

        return (sX <= eX && eX <= fX) && (sY <= eY && eY <= fY);
    }
}
