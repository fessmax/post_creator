package fessmax.postvkcreator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

@SuppressLint("AppCompatCustomView")
public class StickerView extends ImageView implements View.OnTouchListener {

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private Matrix matrixNoRotate = new Matrix();
    private Matrix savedMatrixNoRotate = new Matrix();

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
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        setScaleType(ImageView.ScaleType.MATRIX);

        setOnTouchListener(this);
    }

    public void setActive(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    private boolean checkCorrectTouch(MotionEvent event) {

        Log.e("checkCorrectTouch", event.getPointerCount() + "");

        int[] position = new int[2];
        getLocationInWindow(position);
        float eventX = event.getRawX();
        float eventY = event.getRawY();

        float[] values = new float[9];
        matrixNoRotate.getValues(values);
        float globalX = values[Matrix.MTRANS_X];
        float globalY = values[Matrix.MTRANS_Y];
        float scaleX = values[Matrix.MSCALE_X];
        float scaleY = values[Matrix.MSCALE_Y];
        float width = scaleX * getDrawable().getIntrinsicWidth();
        float height = scaleY * getDrawable().getIntrinsicHeight();
        float skewX = values[Matrix.MSKEW_X];
        float skewY = values[Matrix.MSKEW_Y];

        float rScale = (float) Math.sqrt(scaleX * scaleX + skewY * skewY);
        float rwidth =  getDrawable().getIntrinsicWidth() * rScale;
        float rheight =  getDrawable().getIntrinsicHeight() * rScale;

        width = rwidth;
        height = rheight;

        Log.e("position in window", position[0] + " - " + position[1]);
        Log.e("event", event.getRawX() + " - " + event.getRawY());
//        Log.e("matrix", matrixNoRotate.toString());
        Log.e("values", globalX + ", " + globalY + "; " + width + " - " + height + "; " + rwidth + " - " + rheight);

        float lX = position[0] + globalX - 100;
        float rX = position[0] + globalX + width + 100;
        float lY = position[1] + globalY - 100;
        float rY = position[1] + globalY + height + 100;

        return (lX <= eventX && eventX <= rX && lY <= eventY && eventY <= rY);
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (!checkCorrectTouch(event)) return false;
        ImageView view = (ImageView) v;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                savedMatrixNoRotate.set(matrixNoRotate);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    savedMatrixNoRotate.set(matrixNoRotate);
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
                    matrixNoRotate.set(savedMatrixNoRotate);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                    matrixNoRotate.postTranslate(dx, dy);
                } else if (mode == ZOOMROTATE) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        matrixNoRotate.set(savedMatrixNoRotate);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                        matrixNoRotate.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 2) {
                        float newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        matrix.postRotate(r, mid.x, mid.y);
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
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }catch (Exception e){
            Log.e("spacing", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        try {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }catch (Exception e){
            Log.e("midPoint", e.getMessage(), e);
        }
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        try {
            double delta_x = (event.getX(0) - event.getX(1));
            double delta_y = (event.getY(0) - event.getY(1));
            double radians = Math.atan2(delta_y, delta_x);
            return (float) Math.toDegrees(radians);
        }catch (Exception e){
            Log.e("rotation", e.getMessage(), e);
            return 0;
        }
    }

}
