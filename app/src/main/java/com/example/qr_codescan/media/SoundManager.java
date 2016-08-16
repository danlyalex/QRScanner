package com.example.qr_codescan.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.qr_codescan.R;

import java.util.HashMap;
import java.util.Map;


public class SoundManager {

	private Context mContext ;
	private static SoundManager sInstance ;
	public static class Type{
		public final static int MUSIC_CLICK = 1 ;
		public final static int MUSIC_FOCUSED = 2 ;
	}
	private SoundPool mSp  ;
	private Map sSpMap ;
	@SuppressWarnings("unchecked")
	private SoundManager(Context context){
		mContext = context ;
		sSpMap = new HashMap<Integer, Integer>() ;
		mSp = new SoundPool(10 , AudioManager.STREAM_MUSIC ,100) ;
		sSpMap.put(Type.MUSIC_CLICK, mSp.load(mContext, R.raw.scan, 1)) ;
//		sSpMap.put(Type.MUSIC_FOCUSED, mSp.load(mContext, R.raw.focused, 1)) ;
	}
	
	public static SoundManager getInstance(Context context){
		if(sInstance == null)
			sInstance = new SoundManager(context) ;
		return sInstance ;
	}
	
	public void play(int type){
		if(sSpMap.get(type) == null) return ;
		mSp.play((Integer) sSpMap.get(type), 1, 1, 0, 0, 1) ;
	}
}
