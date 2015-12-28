package co.lujun.shuzhi.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import co.lujun.shuzhi.App;

/**
 * Created by lujun on 2015/7/6.
 */
public class CacheFileUtils {


    /**
     * write a Serlilizable Object into cache file
     * @param ser
     * @param file
     * @return
     */
    public static boolean saveObject(Serializable ser, String file){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = App.getContext().openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            try {
                oos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * read the cache file as a Serializable Object
     * @param file
     * @return
     */
    public static Serializable readObject(String file){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = App.getContext().openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                ois.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                fis.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * delete file or directory
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }
}
