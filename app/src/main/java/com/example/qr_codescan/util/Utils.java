package com.example.qr_codescan.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.qr_codescan.web.StringRes;

/**
 * Created by py on 2016/8/11.
 */
public class Utils {

    public static final String DEFAULT_NETWORK_ENCODING = "UTF-8";
    private static Toast m_toast;
    public static boolean inForeground = true;
    private static InputMethodManager s_inputMethod;

    public static void showToast(Context ctx, String content) {
        if (ctx == null || !inForeground) {
            return;
        }
        if (m_toast == null) {
            m_toast = Toast.makeText(ctx, content, Toast.LENGTH_LONG);
        } else {
            //m_toast.cancel();
            m_toast.setText(content);
        }
        m_toast.show();
    }

    public static void showToast(Context ctx, @StringRes int string_id) {
        if (ctx != null) {
            showToast(ctx, ctx.getString(string_id));
        }
    }


    public static void hideInputMethod(Activity activity) {
        try {
            if (s_inputMethod == null) {
                s_inputMethod = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            if (s_inputMethod != null && s_inputMethod.isActive() && activity.getCurrentFocus() != null) {
                s_inputMethod.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            Log.e("tag",e.toString());
        }
    }
}
