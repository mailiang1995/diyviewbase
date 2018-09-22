package com.mailiang.diyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CustomView extends View{

    private String text;
    private int color;
    private int background;
    private int size;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect rect;
    private Paint paint;

    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 获取自定义属性样式
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取样式数组
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomView,defStyleAttr,0);
        int n = typedArray.getIndexCount();
        for (int i=0;i<n;i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.CustomView_text:
                    text = typedArray.getString(attr);
                    break;
                case R.styleable.CustomView_color:
                    color = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomView_backcolor:
                    background = typedArray.getColor(attr,Color.GRAY);
                    break;
                case R.styleable.CustomView_size:
                    size = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();

        //获取绘制文本的宽与高
        paint = new Paint();
        paint.setTextSize(size);

        rect = new Rect();
        paint.getTextBounds(text,0,text.length(),rect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY){//明确值或match_parent
            width = widthSize;
        }
        else {//wrap_content
            paint.setTextSize(size);
            paint.getTextBounds(text,0,text.length(),rect);
            float textWidth = rect.width();
            int desired = (int)(getPaddingLeft()+textWidth+getPaddingRight());
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY){//明确值或match_parent
            height = heightSize;
        }
        else {//wrap_content
            paint.setTextSize(size);
            paint.getTextBounds(text,0,text.length(),rect);
            float textHeight = rect.height();
            int desired = (int)(getPaddingTop()+textHeight+getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(background);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);
        paint.setColor(color);
        canvas.drawText(text,getWidth()/2-rect.width()/2,getHeight()/2+rect.height()/2,paint);
    }
}
