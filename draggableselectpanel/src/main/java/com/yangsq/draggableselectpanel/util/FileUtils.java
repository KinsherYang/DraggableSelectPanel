package com.yangsq.draggableselectpanel.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

/**
 * @author ysq 2016/9/30.
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 独处文件对象缓存
     * @param context
     * @param fileName 文件名
     * @return
     */
    public static Object readObject(Context context, String fileName) {
        Object back = null;
        ObjectInputStream objIn = null;
        try {
            FileInputStream in = context.openFileInput(fileName);
            objIn = new ObjectInputStream(in);
            back = objIn.readObject();
            return back;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "readObject fail,file not found");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "readObject fail,c;ass not found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "readObject fail,io exception");
        } finally {
            if (objIn != null)
                try {
                    objIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return back;
        }
    }

    /**
     * 把对象写入文件缓存
     * @param context
     * @param fileName 文件名
     * @param obj 要写入的对象
     * @throws Exception
     */
    public static void writeObject(Context context, String fileName, Object obj) {
        ObjectOutputStream objOut = null;
        try {
            FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "readObject fail,file not found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "readObject fail,io exception");
        } finally {
            if (objOut != null)
                try {
                    objOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
