package com.app.whoot.util;

import android.content.Context;
import android.os.SystemClock;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.app.Constant;

//一次性Toast
public class ToastUtil {

    /*cannot be instantiated*/
    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /*
     * 短时间显示Toast
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (Constant.DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /*
     * 短时间显示Toast
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (Constant.DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /*
     * 长时间显示Toast
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (Constant.DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /*
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (Constant.DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示居中位置的Toast
     */
    public static void showgravity(Context context, String message) {

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
