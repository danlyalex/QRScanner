package com.example.qr_codescan.web;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.qr_codescan.R;

/**
 * Created by yu on 2015/7/13.
 */
public class AlphaProgressView extends View {

    private Paint m_paint;
    private LinearGradient m_linearGradient;
    private int m_viewWidth;
    private float m_drawWidth;
    private int m_progress;
    private Context m_ctx;

    public AlphaProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_ctx = context;
        m_paint = new Paint();
        m_paint.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.alpha_progress_height));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        m_viewWidth = w;
        m_linearGradient = new LinearGradient(0, 0, m_viewWidth, 0, getColor(R.color.transparent_white_26), getColor(R.color.default_white), Shader.TileMode.CLAMP);
        m_paint.setShader(m_linearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        m_drawWidth = m_viewWidth * 1.0f * m_progress / 100;
        canvas.drawLine(0, 0, m_drawWidth, 0, m_paint);
    }

    private int getColor(int color) {
        return m_ctx.getResources().getColor(color);
    }

    public void setProgress(int progress) {
        m_progress = progress;
        invalidate();
    }
}
