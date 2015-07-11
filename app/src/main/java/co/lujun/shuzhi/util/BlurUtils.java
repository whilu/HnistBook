package co.lujun.shuzhi.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import co.lujun.shuzhi.GlApplication;

/**
 * Created by lujun on 2015/7/1.
 */
public class BlurUtils {

    public static void blur(Bitmap bkg, View view) {
//        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        float radius = 2;

        float[] scaleNum = calScaleNum(bkg, view);

        Matrix matrix = new Matrix();
        if (scaleNum.length == 2) {
            matrix.postScale(scaleNum[0], scaleNum[1]); //x和y放大缩小的比例
        }else {
            matrix.postScale(1.0f, 1.0f);
        }
        bkg = Bitmap.createBitmap(bkg, 0, 0, bkg.getWidth(), bkg.getHeight(), matrix, true);

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(GlApplication.getContext().getResources(), overlay));
//        Log.d("cost", "cost " + (System.currentTimeMillis() - startMs) + "ms");
    }

    private static float[] calScaleNum(Bitmap bitmap, View view){
        float sx = 1.0f;
        float sy = 1.0f;

        if (bitmap.getWidth() != 0 && bitmap.getHeight() != 0
                && view.getWidth()!= 0 && view.getHeight() != 0){
            if (bitmap.getWidth() < view.getWidth()){
                sx = (float) view.getWidth() / bitmap.getWidth();
            }else {
                sx = (float) bitmap.getWidth() / view.getWidth();
            }
            if (bitmap.getHeight() < view.getHeight()){
                sy = (float) view.getHeight() / bitmap.getHeight();
            }else {
                sy = (float) bitmap.getHeight() / view.getHeight();
            }
        }
        return new float[]{sx, sy};
    }
}
