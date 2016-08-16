package com.example.qr_codescan.web;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class InternalWebView extends WebView {

    public InternalWebView(Context context) {
        this(context,null);
    }

    public InternalWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            WebSettings settings = getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setAllowFileAccess(true);
            //        settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);
        }
        catch (Exception e){

        }
    }
}
