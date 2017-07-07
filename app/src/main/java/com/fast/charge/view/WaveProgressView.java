package com.fast.charge.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class WaveProgressView extends View {
    private int f374a;
    private int f375b;
    private Bitmap bitmap;
    private Path path;
    private Paint paint;
    private float f379f;
    private float f380g;
    private String f381h;
    private int f382i;
    private Paint textPaint;
    private String f384k;
    private String f385l;
    private int f386m;
    private int f387n;
    private int f388o;
    private float f389p;
    private float f390q;
    private int f391r;
    private Handler handler;

    class MyHandler extends Handler {
        /* synthetic */ WaveProgressView waveProgressView;

        MyHandler(WaveProgressView waveProgressView) {
            this.waveProgressView = waveProgressView;
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1911:
                    this.waveProgressView.invalidate();
                    sendEmptyMessageDelayed(1911, (long) this.waveProgressView.f391r);
                    return;
                default:
                    return;
            }
        }
    }

    public WaveProgressView(Context context) {
        this(context, null, 0);
    }

    public WaveProgressView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WaveProgressView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f379f = 20.0f;
        this.f380g = 100.0f;
        this.f381h = "#5be4ef";
        this.f382i = 30;
        this.f384k = "";
        this.f385l = "#FFFFFF";
        this.f386m = 41;
        this.f387n = 100;
        this.f388o = 0;
        this.f390q = 0.0f;
        this.f391r = 10;
        this.handler = new MyHandler(this);
        initPaint();
    }

    private Bitmap getBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return createBitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void initPaint() {
        if (getBackground() == null) {
            throw new IllegalArgumentException(String.format("background is null.", new Object[0]));
        }
        this.bitmap = getBitmap(getBackground());
        this.path = new Path();
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Style.FILL);
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextAlign(Align.CENTER);
        this.handler.sendEmptyMessageDelayed(1911, 100);
    }

    private Bitmap generateBitmap() {
        int i;
        this.paint.setColor(Color.parseColor(this.f381h));
        this.textPaint.setColor(Color.parseColor(this.f385l));
        this.textPaint.setTextSize((float) this.f386m);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap createBitmap = Bitmap.createBitmap(this.f374a, this.f375b, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float f = (float) ((this.f375b * (this.f387n - this.f388o)) / this.f387n);
        if (this.f389p > f) {
            this.f389p -= (this.f389p - f) / 10.0f;
        }
        this.path.reset();
        this.path.moveTo(0.0f - this.f390q, this.f389p);
        int i2 = (this.f374a / (((int) this.f380g) * 4)) + 1;
        int i3 = 0;
        for (i = 0; i < i2 * 3; i++) {
            this.path.quadTo((this.f380g * ((float) (i3 + 1))) - this.f390q, this.f389p - this.f379f, (this.f380g * ((float) (i3 + 2))) - this.f390q, this.f389p);
            this.path.quadTo((this.f380g * ((float) (i3 + 3))) - this.f390q, this.f389p + this.f379f, (this.f380g * ((float) (i3 + 4))) - this.f390q, this.f389p);
            i3 += 4;
        }
        this.f390q += this.f380g / ((float) this.f382i);
        this.f390q %= this.f380g * 4.0f;
        this.path.lineTo((float) this.f374a, (float) this.f375b);
        this.path.lineTo(0.0f, (float) this.f375b);
        this.path.close();
        canvas.drawPath(this.path, this.paint);
        i = Math.min(this.f374a, this.f375b);
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, i, i, false);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_ATOP));
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, paint);
        canvas.drawText(this.f384k, (float) (this.f374a / 2), (float) (this.f375b / 2), this.textPaint);
        return createBitmap;
    }

    public void m542a(float f, float f2) {
        this.f379f = f;
        this.f380g = f2 / 2.0f;
    }

    public void m543a(int i, String str) {
        this.f388o = i;
        this.f384k = str;
    }

    public void m544a(String str, int i) {
        this.f385l = str;
        this.f386m = i;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.bitmap != null) {
            canvas.drawBitmap(generateBitmap(), 0.0f, 0.0f, null);
        }
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.f374a = MeasureSpec.getSize(i);
        int size = MeasureSpec.getSize(i2);
        this.f375b = size;
        this.f389p = (float) size;
    }

    public void setMaxProgress(int i) {
        this.f387n = i;
    }

    public void setWaveColor(String str) {
        this.f381h = str;
    }

    public void setmWaveSpeed(int i) {
        this.f382i = i;
    }
}