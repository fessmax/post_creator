package fessmax.postvkcreator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

import fessmax.postvkcreator.data.TextStyle;

public class CommonHelper {


    public static String saveImageFromView(Context context, View view) {
        try {
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
                    context.getContentResolver(),
                    returnedBitmap,
                    fileName,
                    "new post"
            );
            return savedImageURL;
        } catch (Exception e) {
            Log.e("saveImageFromView", e.getMessage(), e);
            return "";
        }
    }


    public static void setCursorColor(EditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception e) {
            Log.e("setCursorColor", e.getMessage(), e);
        }
    }


    public static void updateSpannableEditText(EditText view, TextStyle textStyle) {
        Spannable span = (Spannable) view.getText();
        span.setSpan(new BackgroundColorSpan(textStyle.spanColor), 0, view.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        CommonHelper.setCursorColor(view, textStyle.cursorColor);
    }

    public static Bitmap getRoundedCornerBitmap(Drawable drawable, float radius) {
        Bitmap bitmap = null;
        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas c = new Canvas(bitmap);
        drawable.setBounds(0, 0, c.getWidth(), c.getHeight());
        drawable.draw(c);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = radius;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
