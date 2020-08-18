package com.lzp.commonlibdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.lzp.commonlibdemo.R;

import java.util.Objects;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;


/**
 * 圆形图片
 */
public class RoundImageView extends AppCompatImageView {

    //图片
    private Bitmap bitmap, srcBitmap;
    //圆角角度
    private float filletSize;
    //圆形中空白处的颜色，设置CROP_CENTER才起作用
    private int cropBgColor;

    public RoundImageView(Context context) {
        super(context);
        initFromAttribute(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttribute(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFromAttribute(context, attrs);
    }

    private void initFromAttribute(Context context, AttributeSet attrs) {
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        filletSize = ta.getDimension(R.styleable.RoundImageView_filletSize, -1);
        cropBgColor = ta.getColor(R.styleable.RoundImageView_cropBgColor, Color.WHITE);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap == null && srcBitmap != null && getWidth() > 0 && getHeight() > 0) {
            bitmap = createCircleImage(srcBitmap, getWidth(), getHeight());
        }
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, getPaddingLeft(), getPaddingTop(), null);
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        srcBitmap = drawableToBitmap(Objects.requireNonNull(
                ContextCompat.getDrawable(getContext(), resId)));
        bitmap = null;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        srcBitmap = drawableToBitmap(drawable);
        bitmap = null;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        srcBitmap = bm;
        bitmap = null;
    }

    /**
     * Drawable转化为Bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 根据原图和变长绘制圆形图片
     */
    private Bitmap createCircleImage(Bitmap source, int w, int h) {
        int targetW = w - (getPaddingLeft() + getPaddingRight());
        int targetH = h - (getPaddingTop() + getPaddingBottom());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
        //产生一个同样大小的画布
        Canvas canvas = new Canvas(target);
        //首先绘制圆形
        if (filletSize < 0) {//<0说明没有设置圆角大小属性，默认是圆形
            filletSize = (targetW > targetH ? targetW : targetH) / 2;
        }
        paint.setColor(cropBgColor);
        canvas.drawRoundRect(new RectF(0, 0, targetW, targetH), filletSize, filletSize, paint);
        //使用SRC_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制图片
        float scale;
        int sl;
        int st;
        float targetRadio = targetW * 1f / targetH;
        float sourceRadio = source.getWidth() * 1f / source.getHeight();
        if (getScaleType() == ScaleType.CENTER_CROP) {
            if (targetRadio > sourceRadio) {
                scale = targetW * 1f / source.getWidth();
                sl = 0;
                st = (int) ((targetH - source.getHeight() * scale) / 2);
            } else {
                scale = targetH * 1f / source.getHeight();
                sl = (int) ((targetW - source.getWidth() * scale) / 2);
                st = 0;
            }
            source = Bitmap.createScaledBitmap(source, (int) (source.getWidth() * scale), (int) (source.getHeight() * scale), true);
        } else if (getScaleType() == ScaleType.FIT_XY) {
            sl = 0;
            st = 0;
            source = Bitmap.createScaledBitmap(source, targetW, targetH, true);
        } else {//其他默认就FIT_CENTER吧，需要其他的以后再写
            if (targetRadio > sourceRadio) {
                scale = targetH * 1f / source.getHeight();
                sl = (int) ((targetW - source.getWidth() * scale) / 2);
                st = 0;
            } else {
                scale = targetW * 1f / source.getWidth();
                sl = 0;
                st = (int) ((targetH - source.getHeight() * scale) / 2);
            }
            source = Bitmap.createScaledBitmap(source, (int) (source.getWidth() * scale), (int) (source.getHeight() * scale), true);
        }
        canvas.drawBitmap(source, sl, st, paint);
        return target;
    }
}