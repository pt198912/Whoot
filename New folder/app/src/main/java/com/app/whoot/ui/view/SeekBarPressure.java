package com.app.whoot.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.util.ScreenUtils;

import java.math.BigDecimal;

/**
 * pengtao
 */
public class SeekBarPressure extends View {
    private static final String TAG = "SeekBarPressure";
    private static final int CLICK_ON_LOW = 1;      //点击在前滑块上
    private static final int CLICK_ON_HIGH = 2;     //点击在后滑块上
    private static final int CLICK_IN_LOW_AREA = 3;
    private static final int CLICK_IN_HIGH_AREA = 4;
    private static final int CLICK_OUT_AREA = 5;
    private static final int CLICK_INVAILD = 0;
    /*
     * private static final int[] PRESSED_STATE_SET = {
     * android.R.attr.state_focused, android.R.attr.state_pressed,
     * android.R.attr.state_selected, android.R.attr.state_window_focused, };
     */
    private static final int[] STATE_NORMAL = {};
    private static final int[] STATE_PRESSED = {
            android.R.attr.state_pressed, android.R.attr.state_window_focused,
    };
    private Drawable hasScrollBarBg;        //滑动条滑动后背景图
    private Drawable notScrollBarBg;        //滑动条未滑动背景图
    private Drawable mThumbLow;         //前滑块
    private Drawable mThumbHigh;        //后滑块

    private int mScollBarWidth;     //控件宽度=滑动条宽度+滑动块宽度
    private int mScollBarHeight;    //滑动条高度

    private int mThumbWidth;        //滑动块宽度
    private int mThumbHeight;       //滑动块高度

    private double mOffsetLow = 0;     //前滑块中心坐标
    private double mOffsetHigh = 0;    //后滑块中心坐标
    private int mDistance = 0;      //总刻度是固定距离 两边各去掉半个滑块距离

    private int mThumbMarginTop ;   //滑动块顶部距离上边框距离，也就是距离字体顶部的距离

    private int mFlag = CLICK_INVAILD;
    private OnSeekBarChangeListener mBarChangeListener;


    private double defaultScreenLow = 0;    //默认前滑块位置百分比
    private double defaultScreenHigh = 100;  //默认后滑块位置百分比

    private boolean isEdit = false;     //输入框是否正在输入

    public SeekBarPressure(Context context) {
        this(context, null);
    }

    public SeekBarPressure(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarPressure(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        this.setBackgroundColor(Color.BLACK);

        Resources resources = getResources();
        notScrollBarBg = resources.getDrawable(R.drawable.seekbarpressure_bg_normal);
        hasScrollBarBg = resources.getDrawable(R.drawable.seekbarpressure_bg_progress);
        mThumbLow = resources.getDrawable(R.drawable.seekbarpressure_thumb);
        mThumbHigh = resources.getDrawable(R.drawable.seekbarpressure_thumb);
        mThumbMarginTop=ScreenUtils.dp2px(getContext(),20);
        mThumbLow.setState(STATE_NORMAL);
        mThumbHigh.setState(STATE_NORMAL);
        mScollBarWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        mScollBarHeight = ScreenUtils.dp2px(getContext(),2);

        mThumbWidth = ScreenUtils.dp2px(getContext(),25);
        mThumbHeight = ScreenUtils.dp2px(getContext(),25);
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
    }

    //默认执行，计算view的宽高,在onDraw()之前
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        mScollBarWidth = width;
        mOffsetHigh = width - mThumbWidth / 2;
        mOffsetLow = mThumbWidth / 2;
        mDistance = width - mThumbWidth;

        mOffsetLow = formatDouble(defaultScreenLow / 100 * (mDistance ))+ mThumbWidth / 2;
        mOffsetHigh = formatDouble(defaultScreenHigh / 100 * (mDistance)) + mThumbWidth / 2;
        setMeasuredDimension(width, height);
    }


    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {
        }

        return specSize;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int defaultHeight = ScreenUtils.dp2px(getContext(),70);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {
            defaultHeight = specSize;
        }

        return defaultHeight;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
    public static final int MAX_PART=6;
    public static final int MAX_VALUE=310;
    public static final int MIN_VALUE=0;
    public static final int EXTRA_END=ScreenUtils.dp2px(MyApplication.getInstance(),40);
    public static final int EXTRA_VALUE=10;
    Paint text_Paint = new Paint();
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        text_Paint.setTextAlign(Paint.Align.CENTER);
        text_Paint.setColor(getResources().getColor(R.color.main_gold));
        text_Paint.setTextSize(ScreenUtils.dp2px(getContext(),12));
        text_Paint.setAntiAlias(true);
//        text_Paint.setMaskFilter(null);
        int aaa = mThumbMarginTop + mThumbHeight / 2 - mScollBarHeight / 2;
        int bbb = aaa + mScollBarHeight;

        //白色，不会动
        notScrollBarBg.setBounds(ScreenUtils.dp2px(getContext(),6), aaa, mScollBarWidth-ScreenUtils.dp2px(getContext(),6) , bbb);
        notScrollBarBg.draw(canvas);
        int divider=(mScollBarWidth-EXTRA_END)/MAX_PART;
        text_Paint.setColor(getResources().getColor(R.color.filter_pop));
        canvas.drawCircle(ScreenUtils.dp2px(getContext(),6), aaa + ScreenUtils.dp2px(getContext(), 1), ScreenUtils.dp2px(getContext(), 3), text_Paint);
        for(int i=0;i<MAX_PART-1;i++){
            if((i+1)*divider<mOffsetLow - mThumbWidth / 2||(i+1)*divider>mOffsetHigh - mThumbWidth / 2){
                text_Paint.setColor(getResources().getColor(R.color.filter_pop));
            }else {
                text_Paint.setColor(getResources().getColor(R.color.main_gold));
            }
            canvas.drawCircle((i + 1) * divider, aaa + ScreenUtils.dp2px(getContext(), 1), ScreenUtils.dp2px(getContext(), 3), text_Paint);
        }
        int j=MAX_PART-1;
        canvas.drawCircle((j + 1) * divider, aaa + ScreenUtils.dp2px(getContext(), 1), ScreenUtils.dp2px(getContext(), 3), text_Paint);
        text_Paint.setColor(getResources().getColor(R.color.main_tab_defaule));
        for(int i=0;i<MAX_PART+1;i++){
            if(i==0){
                canvas.drawText(i*((MAX_VALUE-EXTRA_VALUE)/MAX_PART)+"", i*divider+ScreenUtils.dp2px(getContext(),6), mThumbMarginTop+mThumbHeight+ScreenUtils.dp2px(getContext(),17), text_Paint);
            }else if(i==MAX_PART){
                canvas.drawText(i*((MAX_VALUE-EXTRA_VALUE)/MAX_PART)+"+", i*divider-ScreenUtils.dp2px(getContext(),5), mThumbMarginTop+mThumbHeight+ScreenUtils.dp2px(getContext(),17), text_Paint);
            }else {
                canvas.drawText(i*((MAX_VALUE-EXTRA_VALUE)/MAX_PART)+"", i*divider,mThumbMarginTop+mThumbHeight+ScreenUtils.dp2px(getContext(),17), text_Paint);
            }

        }
        //蓝色，中间部分会动
        hasScrollBarBg.setBounds((int)mOffsetLow, aaa, (int)mOffsetHigh, bbb);
        hasScrollBarBg.draw(canvas);

//        BlurMaskFilter maskFilter = new BlurMaskFilter(ScreenUtils.dp2px(getContext(),5), BlurMaskFilter.Blur.SOLID);
//        text_Paint.setMaskFilter(maskFilter);
//        text_Paint.setShadowLayer(mThumbWidth/2+ScreenUtils.dp2px(getContext(),4), (int)(mOffsetLow - mThumbWidth / 2), mThumbMarginTop, Color.GREEN);
        //前滑块
        mThumbLow.setBounds((int)(mOffsetLow - mThumbWidth / 2), mThumbMarginTop, (int)(mOffsetLow + mThumbWidth / 2), mThumbHeight + mThumbMarginTop);
        mThumbLow.draw(canvas);

        Log.d(TAG, "onDraw: mOffsetHigh "+mOffsetHigh);
        //后滑块
        mThumbHigh.setBounds((int)(mOffsetHigh - mThumbWidth / 2), mThumbMarginTop, (int)(mOffsetHigh + mThumbWidth / 2), mThumbHeight + mThumbMarginTop);
        mThumbHigh.draw(canvas);

        double progressLow = formatDouble((mOffsetLow - mThumbWidth / 2) * 100 / mDistance);
        double progressHigh = formatDouble((mOffsetHigh - mThumbWidth / 2) * 100 / mDistance);
//            Log.d(TAG, "onDraw-->mOffsetLow: " + mOffsetLow + "  mOffsetHigh: " + mOffsetHigh   + "  progressLow: " + progressLow + "  progressHigh: " + progressHigh);
//        canvas.drawText((int) progressLow + "", (int)mOffsetLow - 2 - 2, 15, text_Paint);
//        canvas.drawText((int) progressHigh + "", (int)mOffsetHigh - 2, 15, text_Paint);
//        if(Math.abs(mProgressLow-progressLow)>=MIN_UNIT*100/MAX_VALUE||Math.abs(mProgressHigh-progressHigh)>=MIN_UNIT*100/MAX_VALUE){
//            mProgressHigh=progressHigh;
//            mProgressLow=progressLow;
//        }else{
//            progressHigh=mProgressHigh;
//            progressLow=mProgressLow;
//        }
        mProgressHigh=progressHigh;
        mProgressLow=progressLow;
        if (mBarChangeListener != null) {
            if (!isEdit) {
                mBarChangeListener.onProgressChanged(this, progressLow, progressHigh);
            }

        }
    }
    private double mProgressLow=0;
    private double mProgressHigh=100;
    private static final int MIN_UNIT=10;
    public int getAverageUnit(){
        return MAX_VALUE/MAX_PART;
    }
    public int getMinUnit() {
        return MIN_UNIT;
    }

    public double getProgressLow() {
        return mProgressLow;
    }
    public double getProgressHigh(){
        return mProgressHigh;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //按下
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (mBarChangeListener != null) {
                mBarChangeListener.onProgressBefore();
                isEdit = false;
            }
            mFlag = getAreaFlag(e);
//            Log.d(TAG, "e.getX: " + e.getX() + "mFlag: " + mFlag);
//            Log.d("ACTION_DOWN", "------------------");
            if (mFlag == CLICK_ON_LOW) {
                mThumbLow.setState(STATE_PRESSED);
            } else if (mFlag == CLICK_ON_HIGH) {
                mThumbHigh.setState(STATE_PRESSED);
            } else if (mFlag == CLICK_IN_LOW_AREA) {
                mThumbLow.setState(STATE_PRESSED);
                //如果点击0-mThumbWidth/2坐标
                if (e.getX() < 0 || e.getX() <= mThumbWidth/2) {
                    mOffsetLow = mThumbWidth/2;
                } else if (e.getX() > mScollBarWidth - mThumbWidth/2) {
//                    mOffsetLow = mDistance - mDuration;
                    mOffsetLow = mThumbWidth/2 + mDistance;
                } else {
                    mOffsetLow = formatDouble(e.getX());
//                    if (mOffsetHigh<= mOffsetLow) {
//                        mOffsetHigh = (mOffsetLow + mDuration <= mDistance) ? (mOffsetLow + mDuration)
//                                : mDistance;
//                        mOffsetLow = mOffsetHigh - mDuration;
//                    }
                }
            } else if (mFlag == CLICK_IN_HIGH_AREA) {
                mThumbHigh.setState(STATE_PRESSED);
//                if (e.getX() < mDuration) {
//                    mOffsetHigh = mDuration;
//                    mOffsetLow = mOffsetHigh - mDuration;
//                } else if (e.getX() >= mScollBarWidth - mThumbWidth/2) {
//                    mOffsetHigh = mDistance + mThumbWidth/2;
                if(e.getX() >= mScollBarWidth - mThumbWidth/2) {
                    mOffsetHigh = mDistance + mThumbWidth/2;
                } else {
                    mOffsetHigh = formatDouble(e.getX());
//                    if (mOffsetHigh <= mOffsetLow) {
//                        mOffsetLow = (mOffsetHigh - mDuration >= 0) ? (mOffsetHigh - mDuration) : 0;
//                        mOffsetHigh = mOffsetLow + mDuration;
//                    }
                }
            }
            //设置进度条
            refresh();

            //移动move
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
//            Log.d("ACTION_MOVE", "------------------");
//            int count=MAX_VALUE/MIN_UNIT;
//            double distanceAverage=(double)mDistance/count;
            if (mFlag == CLICK_ON_LOW) {
                if (e.getX() < 0 || e.getX() <= mThumbWidth/2) {
                    mOffsetLow = mThumbWidth/2;
                } else if (e.getX() >= mScollBarWidth - mThumbWidth/2) {
                    mOffsetLow = mThumbWidth/2 + mDistance;
                    mOffsetHigh = mOffsetLow;
                } else {
                    mOffsetLow = formatDouble(e.getX());
                    if (mOffsetHigh - mOffsetLow <= 0) {
                        mOffsetHigh = (mOffsetLow <= mDistance+mThumbWidth/2) ? (mOffsetLow) : (mDistance+mThumbWidth/2);
                    }
                }
            } else if (mFlag == CLICK_ON_HIGH) {
                if (e.getX() <  mThumbWidth/2) {
                    mOffsetHigh = mThumbWidth/2;
                    mOffsetLow = mThumbWidth/2;
                } else if (e.getX() > mScollBarWidth - mThumbWidth/2) {
                    mOffsetHigh = mThumbWidth/2 + mDistance;
                } else {
                    mOffsetHigh = formatDouble(e.getX());
                    if (mOffsetHigh - mOffsetLow <= 0) {
                        mOffsetLow = (mOffsetHigh >= mThumbWidth/2) ? (mOffsetHigh) : mThumbWidth/2;
                    }
                }
            }
            //设置进度条
            refresh();
            //抬起
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
//            Log.d("ACTION_UP", "------------------");
            mThumbLow.setState(STATE_NORMAL);
            mThumbHigh.setState(STATE_NORMAL);

            if (mBarChangeListener != null) {
                mBarChangeListener.onProgressAfter();
            }
            //这两个for循环 是用来自动对齐刻度的，注释后，就可以自由滑动到任意位置
//            for (int i = 0; i < money.length; i++) {
//                 if(Math.abs(mOffsetLow-i* ((mScollBarWidth-mThumbWidth)/ (money.length-1)))<=(mScollBarWidth-mThumbWidth)/(money.length-1)/2){
//                     mprogressLow=i;
//                     mOffsetLow =i* ((mScollBarWidth-mThumbWidth)/(money.length-1));
//                     invalidate();
//                     break;
//                }
//            }
//
//            for (int i = 0; i < money.length; i++) {
//                  if(Math.abs(mOffsetHigh-i* ((mScollBarWidth-mThumbWidth)/(money.length-1) ))<(mScollBarWidth-mThumbWidth)/(money.length-1)/2){
//                      mprogressHigh=i;
//                       mOffsetHigh =i* ((mScollBarWidth-mThumbWidth)/(money.length-1));
//                       invalidate();
//                       break;
//                }
//            }
        }
        return true;
    }

    public int getAreaFlag(MotionEvent e) {

        int top = mThumbMarginTop;
        int bottom = mThumbHeight + mThumbMarginTop;
        if (e.getY() >= top && e.getY() <= bottom && e.getX() >= (mOffsetLow - mThumbWidth / 2) && e.getX() <= mOffsetLow + mThumbWidth / 2) {
            return CLICK_ON_LOW;
        } else if (e.getY() >= top && e.getY() <= bottom && e.getX() >= (mOffsetHigh - mThumbWidth / 2) && e.getX() <= (mOffsetHigh + mThumbWidth / 2)) {
            return CLICK_ON_HIGH;
        } else if (e.getY() >= top
                && e.getY() <= bottom
                && ((e.getX() >= 0 && e.getX() < (mOffsetLow - mThumbWidth / 2)) || ((e.getX() > (mOffsetLow + mThumbWidth / 2))
                && e.getX() <= ((double) mOffsetHigh + mOffsetLow) / 2))) {
            return CLICK_IN_LOW_AREA;
        } else if (e.getY() >= top
                && e.getY() <= bottom
                && (((e.getX() > ((double) mOffsetHigh + mOffsetLow) / 2) && e.getX() < (mOffsetHigh - mThumbWidth / 2)) || (e
                .getX() > (mOffsetHigh + mThumbWidth/2) && e.getX() <= mScollBarWidth))) {
            return CLICK_IN_HIGH_AREA;
        } else if (!(e.getX() >= 0 && e.getX() <= mScollBarWidth && e.getY() >= top && e.getY() <= bottom)) {
            return CLICK_OUT_AREA;
        } else {
            return CLICK_INVAILD;
        }
    }

    //更新滑块
    private void refresh() {
        invalidate();
    }
    //设置前滑块的值    
    public void setProgressLow(double  progressLow) {
        this.defaultScreenLow = progressLow;
        mOffsetLow = formatDouble(progressLow / 100 * (mDistance ))+ mThumbWidth / 2;
        isEdit = true;
        refresh();
    }

    //设置后滑块的值
    public void setProgressHigh(double  progressHigh) {
        this.defaultScreenHigh = progressHigh;
        mOffsetHigh = formatDouble(progressHigh / 100 * (mDistance)) + mThumbWidth / 2;
        isEdit = true;
        refresh();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
        this.mBarChangeListener = mListener;
    }

    //回调函数，在滑动时实时调用，改变输入框的值
    public interface OnSeekBarChangeListener {
        //滑动前
        public void onProgressBefore();

        //滑动时
        public void onProgressChanged(SeekBarPressure seekBar, double progressLow,
                                      double progressHigh);

        //滑动后
        public void onProgressAfter();
    }

/*    private int formatInt(double value) {
        BigDecimal bd = new BigDecimal(value);
        BigDecimal bd1 = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return bd1.intValue();
    }*/

    public static double formatDouble(double pDouble) {
        BigDecimal bd = new BigDecimal(pDouble);
        BigDecimal bd1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        pDouble = bd1.doubleValue();
        return pDouble;
    }

}