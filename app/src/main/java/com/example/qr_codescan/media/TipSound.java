/** 
 *  
 * @author	xuxl
 * @email	leoxuxl@163.com
 * @version  
 *     1.0 2015年12月24日 下午2:23:26 
 */ 
package com.example.qr_codescan.media;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.example.qr_codescan.R;


/**
 * This class is used for : 
 *  
 * @author	xuxl
 * @email	leoxuxl@163.com
 * @version  
 *     1.0 2015年12月24日 下午2:23:26 
 */
public class TipSound implements OnCompletionListener {
	static MediaPlayer mPlayer;
	private static void play(Context context, int rid){
		mPlayer = MediaPlayer.create(context,rid);
		mPlayer.setLooping(false);
		mPlayer.start();
	}
	
	/**
	 * 播放扫描声音<br/>
	 * play scan sound
	 * @param context
	 */
	public static void playScanSound(Context context){
		play(context, R.raw.scan);
	}

	@Override
	public void onCompletion(MediaPlayer mp){
		mPlayer.release();
		
	}

}
