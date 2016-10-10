package com.meitu.makeuppicker.util;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.meitu.makeuppicker.R;

/**
 * Created by Administrator on 2016/9/30 0030.
 */

public class MyDialog {
    private static final String TAG = "MyDialog";

    /**
     * 显示选择窗
     *
     * @param context
     * @param title 标题
     * @param items 选择项
     * @param onClickListener 点击选择项监听
     */
    public static void showSelectDialog(Context context, String title, String[] items, DialogInterface.OnClickListener onClickListener) {
        if (items.length == 0) {
            Log.e(TAG, "showSelectDialog error,the length of items is zero");
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, onClickListener);
        builder.show();
    }

    /**
     * 显示两个按钮的对话框
     * @param context
     * @param title 标题
     * @param msg 内容
     * @param onConfirmClick 点击确认监听
     */
    public static void showConfirmDialog(Context context, String title, String msg, DialogInterface.OnClickListener onConfirmClick) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ok, onConfirmClick);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 弹出一个确定按钮的提示框
     * @param context
     * @param title 标题
     * @param msg 内容
     */
    public static void showAlertDialog(Context context, String title, String msg){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
