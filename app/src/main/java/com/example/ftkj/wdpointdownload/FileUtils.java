package com.example.ftkj.wdpointdownload;

import android.content.Context;
import android.os.Environment;

import java.io.File;
/**
 * Created by 天哥哥 at 2017/5/4 14:00
 */
public class FileUtils {

    public static boolean isSDCardValue() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = isSDCardValue() ? getExternalCacheDir().getAbsolutePath() : context.getCacheDir().getAbsolutePath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public static File getExternalCacheDir() {
        return new File(Environment.getExternalStorageDirectory().getPath());
    }

}
