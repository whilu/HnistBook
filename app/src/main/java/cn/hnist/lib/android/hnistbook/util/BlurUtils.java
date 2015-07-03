package cn.hnist.lib.android.hnistbook.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import cn.hnist.lib.android.hnistbook.GlApplication;

/**
 * Created by lujun on 2015/7/1.
 */
public class BlurUtils {

    public static void blur(Bitmap bkg, View view, float sx, float sy) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        float radius = 2;

        Matrix matrix = new Matrix();
        matrix.postScale(sx,sy); //x和y放大缩小的比例
        bkg = Bitmap.createBitmap(bkg, 0, 0, bkg.getWidth(), bkg.getHeight(), matrix, true);

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        view.setBackground(new BitmapDrawable(GlApplication.getContext().getResources(), overlay));
        Log.d("cost", "cost " + (System.currentTimeMillis() - startMs) + "ms");
    }
}
