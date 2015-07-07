package co.lujun.shuzhi.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import co.lujun.shuzhi.GlApplication;

/**
 * Created by lujun on 2015/7/7.
 */
public class ScreenUtils {

    /**
     * get screen width and height with px
     * @return
     */
    public static int[] getScreenWidthXHeight_Px(){
        WindowManager wm = (WindowManager) GlApplication.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT < 13){
            return new int[]{display.getWidth(), display.getHeight()};
        }else {
            Point point = new Point();
            display.getSize(point);
            return new int[]{point.x, point.y};
        }
    }
}
