package cn.hnist.lib.android.hnistbook.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/7/2.
 */
public class ImageUtils {

    private class DownloadImage implements Runnable{

        private String mUrl;

        public DownloadImage(String url){
            this.mUrl = url;
        }

        @Override
        public void run() {

        }
    }

}
