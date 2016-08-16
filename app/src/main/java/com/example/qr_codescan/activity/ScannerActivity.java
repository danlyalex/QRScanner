/**
 * @author xuxl
 * @email leoxuxl@163.com
 * @version 1.0 2015年12月23日 下午5:50:17
 */
package com.example.qr_codescan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.qr_codescan.R;
import com.example.qr_codescan.cantrowitz.rxbroadcast.RxBroadcast;
import com.example.qr_codescan.media.TipSound;
import com.smartdevicesdk.scanner.ScannerHelper;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * This class is used for :
 *  Scan the QR code
 *
 * @author py.xie
 *
 */
public class ScannerActivity extends Activity {
    private static final String TAG = "ScannerActivity";
    ScannerHelper scanner = null;
    Button btnScan;
    TextView textView;
    private ScanBroadcastReceiver m_scanBroadcastReceiver;
    private Subscription m_subscription;
    private String SCANNER_CODE = "com.zkc.scancode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				textView.setText("please on the bar code");
                scanner.scan();
            }
        });

        textView = (TextView) findViewById(R.id.textView_scan);

        Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
//				if(msg.what== HandlerMessage.SCANNER_DATA_MSG)
//				{
//					TipSound.playScanSound(ScannerActivity.this);
//					String href = msg.obj.toString();
//					textView.setText(href);
//					if(href != null) {
////						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(href));
//						Intent browserIntent=new Intent(ScannerActivity.this,WebviewActivity.class);
//						browserIntent.putExtra(WebviewActivity.STRING_KEY,href);
//						browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(browserIntent);
//					}
//				}
            }

            ;
        };

        String device = MainActivity.devInfo.getScannerSerialport();
        int baudrate = MainActivity.devInfo.getScannerBaudrate();
        scanner = new ScannerHelper(this, device, baudrate, handler);


//		if(m_scanBroadcastReceiver ==null)
//		{
//			m_scanBroadcastReceiver = new ScanBroadcastReceiver();
//			IntentFilter intentFilter = new IntentFilter();
//			intentFilter.addAction("com.zkc.scancode");
//			Observable.just(intentFilter).distinct().subscribe(new Action1<IntentFilter>() {
//				@Override
//				public void call(IntentFilter intentFilter) {
//					ScannerActivity.this.registerReceiver(m_scanBroadcastReceiver, intentFilter);
//				}
//			});
//		}


    }


    @Override
    protected void onStart() {
        super.onStart();
        m_subscription = getBroadCastObservable().distinct().subscribe(new Action1<String>() {
            @Override
            public void call(String href) {
                Log.d("ScannerActivity", "ScanBroadcastReceiver code:" + href);
                if (href != null) {
                    TipSound.playScanSound(ScannerActivity.this);
//						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(href));
                    if (href.startsWith("http") || href.startsWith("https")) {
                        Intent browserIntent = new Intent(ScannerActivity.this, WebviewActivity.class);
                        browserIntent.putExtra(WebviewActivity.STRING_KEY, href);
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(browserIntent);
                    }
                    textView.setText(href);z
                }
            }
        });
    }

    /**
     * get broadcast observable
     * @return
     */
    private Observable<String> getBroadCastObservable() {
        return RxBroadcast.fromBroadcast(ScannerActivity.this, new IntentFilter(SCANNER_CODE)).map(new Func1<Intent, String>() {
            @Override
            public String call(Intent intent) {
                return intent.getExtras().getString("code");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "key press code is " + keyCode);
        if (keyCode == 135) {
            textView.setText("please on the bar code");
            scanner.scan();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (m_subscription != null) {
            m_subscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanner.Close();
//		if (m_scanBroadcastReceiver!=null) {
//			this.unregisterReceiver(m_scanBroadcastReceiver);
//			m_scanBroadcastReceiver=null;
//		}
    }


    class ScanBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String href = intent.getExtras().getString("code");
            Log.d("222222222222r", "ScanBroadcastReceiver code:" + href);
            if (href != null) {
                TipSound.playScanSound(ScannerActivity.this);
                textView.setText(href);
//						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(href));
                if (href.startsWith("http") || href.startsWith("https")) {
                    Intent browserIntent = new Intent(ScannerActivity.this, WebviewActivity.class);
                    browserIntent.putExtra(WebviewActivity.STRING_KEY, href);
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(browserIntent);
                }
            }
        }
    }
}
