package com.example.qr_codescan.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qr_codescan.R;
import com.example.qr_codescan.web.RotationAnimatorManager;
import com.example.qr_codescan.util.Utils;
import com.example.qr_codescan.web.AlphaProgressView;
import com.example.qr_codescan.web.InternalWebView;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by py on 2016/8/11.
 */
public class WebviewActivity extends Activity {
    public static final String SEARCH_URL_PREFIX = "http://www.baidu.com/s?wd=";
    private static final int REFRESH_ROTATION_DURATION = 800;
    private static final int REFRESH_ROTATION_COUNT = 30;
    public static final String STRING_KEY = "qrscanner:url";
    public static Pattern URL_PATTERN = Pattern.compile("^((ht|f)tp(s?)\\://){1}[^\\.]+(\\.[^\\.]+)+$");
    public static Pattern SEARCH_KEYWORD_PATTERN = Pattern.compile("^[^\\.]+$");
    private InternalWebView m_internalWebview;
    private AlphaProgressView m_progressBar;
    private String m_url;
    private static final String URL_KEY = "URL_KEY";

    private RelativeLayout m_layout;
    private String m_titleStr;

    private ImageView m_backView;
    private EditText m_titleView;
    private TextView m_loadingUrlView;
    private ImageView m_pageBackView;
    private ImageView m_pageForwardView;
    private ImageView m_pageRefreshView;
    private RotationAnimatorManager m_refreshManager;
    private Handler m_handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        m_layout = (RelativeLayout) findViewById(R.id.webview_layout);
        m_progressBar = (AlphaProgressView) m_layout.findViewById(R.id.internal_webview_progressbar);
        //code
        View view = m_layout.findViewById(R.id.layout_title);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        m_backView = (ImageView) m_layout.findViewById(R.id.imageview_back_btn);
        m_titleView = (EditText) m_layout.findViewById(R.id.edittext_webview_title);
        m_loadingUrlView = (TextView) m_layout.findViewById(R.id.txtview_load_url);
        m_pageBackView = (ImageView) m_layout.findViewById(R.id.page_back);
        m_pageForwardView = (ImageView) m_layout.findViewById(R.id.page_forward);
        m_pageRefreshView = (ImageView)m_layout.findViewById(R.id.page_refresh);
        m_refreshManager = new RotationAnimatorManager(m_pageRefreshView, REFRESH_ROTATION_DURATION, REFRESH_ROTATION_COUNT);

        m_backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        m_titleView.setHint(R.string.loading_page);
        m_titleView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                load_input_page();
                return true;
            }
        });

        m_loadingUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_input_page();
            }
        });
        m_pageBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerBackkey();
            }
        });
        m_pageForwardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (m_internalWebview.canGoForward()) {
                    m_titleView.setText("");
                    m_titleView.setHint(R.string.loading_page);
                    m_internalWebview.goForward();
                }
            }
        });
        m_pageRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_refreshManager.start();
                m_internalWebview.reload();
            }
        });

        m_internalWebview = new InternalWebView(this); //m_layout.findViewById(R.id.internal_webview);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        param.setMargins(0, getActivity().getResources().getDimensionPixelSize(R.dimen.loading_progress_height), 0, 0);
        param.addRule(RelativeLayout.BELOW, R.id.layout_title);
        param.addRule(RelativeLayout.ABOVE, R.id.separator_line);
//        param.addRule(RelativeLayout.BELOW, R.id.internal_webview_progressbar);
        m_layout.addView(m_internalWebview, param);

        m_internalWebview.setWebChromeClient(new InternalWebChromeClient());
        m_internalWebview.setWebViewClient(new InternalWebClient());
        m_internalWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });

        String params = getStringParams();
        if (params != null) {
            m_url = params;
            m_titleStr = params;
        }

        if (savedInstanceState != null) {
            // Restore last state for m_bChecked position.
            m_url = savedInstanceState.getString(URL_KEY);
        }

        if (m_url != null) {
            m_internalWebview.loadUrl(m_url);
        }
        m_handler = new Handler();
    }

    public String getStringParams() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getStringExtra(WebviewActivity.STRING_KEY);
        }
        return null;
    }


    private void load_input_page() {
        String url = m_titleView.getText().toString().trim();
        if (url != null) {
            m_url = convert_url(url);
            if (m_url != null) {
                m_titleView.setText("");
                m_titleView.setHint(R.string.loading_page);
                m_internalWebview.loadUrl(m_url);
            } else if (is_search_keyword(url)) {
                try {
                    m_url = SEARCH_URL_PREFIX + URLEncoder.encode(url, Utils.DEFAULT_NETWORK_ENCODING);
                    m_titleView.setText("");
                    m_titleView.setHint(R.string.loading_page);
                    m_internalWebview.loadUrl(m_url);
                } catch (Exception e) {
                    m_url = null;
                }
            } else {
                Utils.showToast(this, R.string.error_url);
            }
        }

        Utils.hideInputMethod(this);
    }

    /**
     * check if url is a keyword to search
     *
     * @param url
     * @return
     */
    private boolean is_search_keyword(String url) {
        Matcher matcher = SEARCH_KEYWORD_PATTERN.matcher(url);
        return matcher.matches();
    }

    private String convert_url(String url) {
        String result;
        if (url == null) {
            result = null;
        } else if (url.startsWith("http://") || url.startsWith("https://")) {
            result = url;
        } else {
            result = "http://" + url;
        }
        Matcher matcher = URL_PATTERN.matcher(result);
        if (matcher.find()) {
            return result;
        } else {
            return null;
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onResume() {
        super.onResume();
//		m_callback.onWebviewResumed();
        if (m_internalWebview != null) {
            m_internalWebview.onResume();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPause() {
//        if (m_internalWebview != null) {
//            m_internalWebview.onPause();
//            m_internalWebview.loadUrl("about://blank");
//        }
//		m_callback.onWebviewPaused();
        if (m_internalWebview != null) {
            m_internalWebview.onPause();
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL_KEY, m_url);
    }

    @Override
    public void onDestroy() {
//        if (m_layout != null) {
//            ViewGroup parentViewGroup = (ViewGroup) m_layout.getParent();
//            if (parentViewGroup != null) {
//                parentViewGroup.removeView(m_layout);
//            }
//        }
        m_titleStr = null;
        Utils.hideInputMethod(this);
        super.onDestroy();
    }

//	public void prepare_url(String url, String title) {
//		m_url = url;
//		m_titleStr = title;
////        if(m_internalWebview != null){
////            m_internalWebview.clearHistory();
////        }
//	}

    public boolean triggerBackkey() {
        if (m_internalWebview != null) {
            if (m_internalWebview.canGoBack()) {
                m_titleView.setText("");
                m_titleView.setHint(R.string.loading_page);
                m_internalWebview.goBack();
                return true;
            }
        }
        return false;
    }



    private int virtual_progress = 0;
    private final static int MAX_VIRTUAL_PROGRESS = 40;
    private Runnable progress_runnable = new Runnable() {

        @Override
        public void run() {
            if (virtual_progress < MAX_VIRTUAL_PROGRESS) {
                virtual_progress += 1;
            }
            m_progressBar.setProgress(virtual_progress);
            m_handler.postDelayed(this, 300);
        }
    };

    private class InternalWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            m_handler.removeCallbacks(progress_runnable);
                m_progressBar.setVisibility(View.VISIBLE);
//                m_progressBar.setProgress(0);
                virtual_progress = 0;
                m_handler.post(progress_runnable);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            m_url = url;
                m_progressBar.setVisibility(View.GONE);
                m_titleView.setHint(m_titleStr == null ? view.getTitle() : m_titleStr);
                m_titleStr = null;
//                if (m_titleStr == null) {
//                    m_titleView.setHint(view.getTitle());
//                }
//                else {
//                    m_titleView.setHint(m_titleStr);
//                }
                m_refreshManager.cancel();
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            return;
        }
    }

    private class InternalWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
                m_titleView.setHint(m_titleStr == null ? title : m_titleStr);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
            if (progress > MAX_VIRTUAL_PROGRESS) {
                m_handler.removeCallbacks(progress_runnable);
                m_progressBar.setProgress(progress);
            }
//            if (progress == 100) {
//                m_progressBar.setVisibility(View.INVISIBLE);
//            }
//            else {
//                m_progressBar.setVisibility(View.VISIBLE);
//            }
        }
    }
}
