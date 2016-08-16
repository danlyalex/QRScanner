package com.example.qr_codescan.web;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by cplu on 2015/7/20.
 */
public class RotationAnimatorManager {
	private ObjectAnimator m_animator;
	private static final int LOWEST_API = 11;
	private int m_currentApiLevel;
	private static final int DURATION_DEFAULT = 2000;
	private static final int ROTATION_COUNT_DEFAULT = -1;       /// -1 is the same as ObjectAnimator.INFINITE

	public RotationAnimatorManager(View target){
		this(target, 0.0f, 360.0f, DURATION_DEFAULT, ROTATION_COUNT_DEFAULT);
	}

	public RotationAnimatorManager(View target, int duration){
		this(target, 0.0f, 360.0f, duration, ROTATION_COUNT_DEFAULT);
	}

	public RotationAnimatorManager(View target, int duration, int rotation_count){
		this(target, 0.0f, 360.0f, duration, rotation_count);
	}

	@TargetApi(LOWEST_API)
	public RotationAnimatorManager(View target, float start, float end, int duration, int rotation_count){
		m_currentApiLevel = Build.VERSION.SDK_INT;
		if(m_currentApiLevel >= LOWEST_API) {
			m_animator = ObjectAnimator.ofFloat(target, "rotation", start, end);
			m_animator.setDuration(duration);
			m_animator.setInterpolator(new LinearInterpolator());
			m_animator.setRepeatCount(rotation_count);
			m_animator.setRepeatMode(ObjectAnimator.RESTART);
		}
	}

	@TargetApi(LOWEST_API)
	public void start(){
		if(m_currentApiLevel >= LOWEST_API) {
			m_animator.start();
		}
		else{
			/// rollback
		}
	}

	@TargetApi(LOWEST_API)
	public void cancel(){
		if(m_currentApiLevel >= LOWEST_API) {
			m_animator.cancel();
		}
		else{
			/// rollback
		}
	}
}
