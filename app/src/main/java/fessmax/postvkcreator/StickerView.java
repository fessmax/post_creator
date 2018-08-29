package fessmax.postvkcreator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class StickerView extends ImageView implements View.OnTouchListener {

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOMROTATE = 2;
    private int mode = NONE;

    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float[] lastEvent = null;

    private boolean activeFlag = false;

    public StickerView(Context context, int resId) {
        super(context);
        setImageResource(resId);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setScaleType(ImageView.ScaleType.MATRIX);

        setOnTouchListener(this);
    }

    public void setActive(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    private boolean checkCorrectTouch(MotionEvent event){
        int[] position = new int[2];
        getLocationInWindow(position);
        Log.e("position in window", position[0] + " - " + position[1]);

        Log.e("event", event.getRawX() + " - " + event.getRawY());
        Log.e("matrix", matrix.toString());
        float[] values = new float[9];
        matrix.getValues(values);
        float globalX = values[Matrix.MTRANS_X];
        float globalY = values[Matrix.MTRANS_Y];
        float width = values[Matrix.MSCALE_X]*getDrawable().getIntrinsicWidth();
        float height = values[Matrix.MSCALE_Y]*getDrawable().getIntrinsicHeight();

        Log.e("values", globalX + ", " + globalY + "; " + width + " - " + height);

        return activeFlag;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (!checkCorrectTouch(event)) return false;
        ImageView view = (ImageView) v;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOMROTATE;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOMROTATE) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 2) {
                        float newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        matrix.postRotate(r,  mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

}
