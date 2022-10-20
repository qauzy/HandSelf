package cn.iyunbei.handself.service;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class MynahAI {
    public abstract void onScanResult(int cnt);
    public native boolean loadModel(String facing);
    public static String copyFromAssetsToCache(String modelPath, Context context) {
        String newPath = context.getCacheDir() + "/" + modelPath;
        File desDir = new File(newPath);
        try {
            InputStream stream = context.getAssets().open(modelPath);
            OutputStream output = new BufferedOutputStream(new FileOutputStream(newPath));

            byte data[] = new byte[1024];
            int count;

            while ((count = stream.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return desDir.getPath();
    }

    static {
        System.loadLibrary("mynahai");
    }
}
