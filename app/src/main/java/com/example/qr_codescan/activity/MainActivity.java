package com.example.qr_codescan.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.qr_codescan.R;
import com.smartdevicesdk.device.DeviceInfo;
import com.smartdevicesdk.device.DeviceManage;

public class MainActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;

	// PC700 PDA3501
	public static DeviceInfo devInfo = DeviceManage.getDevInfo("PDA3501");

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		Button scanBtn = (Button) findViewById(R.id.scanner);
		final Button nfcBtn = (Button) findViewById(R.id.nfc);
		scanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		nfcBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent nfcIntent=new Intent(MainActivity.this,NFCActivity.class);
				nfcIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(nfcIntent);
			}
		});
	}


//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) {
//			case SCANNIN_GREQUEST_CODE:
//				if(resultCode == RESULT_OK){
//					Bundle bundle = data.getExtras();
//					//显示扫描到的内容
//					String href = bundle.getString("result");
//					if(href != null) {
////						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(href));
//						Intent browserIntent=new Intent(this,WebviewActivity.class);
//						browserIntent.putExtra(WebviewActivity.STRING_KEY,href);
//						browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(browserIntent);
//					}
////					mTextView.setText(href);
////					mTextView.setOnClickListener(new OnClickListener() {
////						@Override
////						public void onClick(View v) {
////
////						}
////					});
////
////					//显示
////					mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
//				}
//				break;
//		}
//	}

}