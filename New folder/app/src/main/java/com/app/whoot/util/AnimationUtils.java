package com.app.whoot.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.ui.view.RoundProgressBar;

import java.util.Timer;
import java.util.TimerTask;


public class AnimationUtils {
    private static Timer mTimer;
    private static int mCirclePbProgress;
    private static final String TAG = "AnimationUtils";
    public interface OnCircleProgressAnimListener{
        void onAnimEnd();
        void onAnimStart();
    }
    @UiThread
    public static void startCircleProgressAnim( RoundProgressBar pb,OnCircleProgressAnimListener listener){
        if(pb==null){
            return;
        }
        stopCircleProgressAnim();
        mTimer=new Timer();
        Handler handler=new Handler();
        mCirclePbProgress=0;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "setProgress: ");
                        if(listener!=null){
                            listener.onAnimStart();
                        }
                        if(pb!=null) {
                            pb.setProgress(mCirclePbProgress);
                        }
                        if(mCirclePbProgress>100){
                            pb.setProgress(0);
                            stopCircleProgressAnim();
                            if(listener!=null){
                                listener.onAnimEnd();
                            }
                        }else {
                            mCirclePbProgress++;
                        }

                    }
                });
            }
        },0,5);

    }
    public static void stopCircleProgressAnim(){

        if(mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
    }
    public static void transArrowAnim(ImageView arrowIv,ImageView originCoinIv,ImageView destCoinIv,ImageView lightEffectView, View flBgView,final RoundProgressBar pb,final TextView progressTv, boolean halfDistance){
        if(arrowIv==null){
            return;
        }
        Animation anim=arrowIv.getAnimation();
        if(anim!=null){
            anim.cancel();
        }
        int halfArrowHeight=MyApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.half_arrow_height);
        ViewGroup.MarginLayoutParams lp=(ViewGroup.MarginLayoutParams)arrowIv.getLayoutParams();
        Log.d(TAG, "transArrowAnim: topMargin "+lp.topMargin);
        TranslateAnimation translateAnimation;
        if(halfDistance){
            progressTv.setText("1");
            translateAnimation=new TranslateAnimation(0,0,halfArrowHeight,0);

        }else{
            progressTv.setText("2");
            translateAnimation=new TranslateAnimation(0,0,0,-halfArrowHeight);

        }


        if(halfDistance) {
            translateAnimation.setDuration(500);//tempory
            translateAnimation.setFillAfter(false);
        }else{
            translateAnimation.setDuration(500);
            translateAnimation.setFillAfter(false);
        }
        translateAnimation.setRepeatCount(0);
        OnCircleProgressAnimListener listener=new OnCircleProgressAnimListener() {
            @Override
            public void onAnimEnd() {
                progressTv.setText("0");
                startMetalCoinAnim(originCoinIv,destCoinIv,lightEffectView,flBgView);
            }

            @Override
            public void onAnimStart() {

            }
        };
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                arrowIv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(!halfDistance) {
                    arrowIv.setVisibility(View.GONE);
                }else{
                    arrowIv.setVisibility(View.VISIBLE);
                }
                if(!halfDistance) {
                    startCircleProgressAnim(pb, listener);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        arrowIv.startAnimation(translateAnimation);
    }
    public static void startMetalCoinAnim(ImageView originCoin,ImageView metalCoin,ImageView lightEffectView,View flBgView){
        if(metalCoin==null){
            return;
        }
        Animation anim=metalCoin.getAnimation();
        if(anim!=null){
            anim.cancel();
        }

//        metalCoin.startAnimation(translateAnimation);
        flipAnimatorY(metalCoin,originCoin,lightEffectView,flBgView);
    }
    static TranslateAnimation translateAnimation;

    static ObjectAnimator animator2;
    public static void flipAnimatorY(final ImageView destIv, final ImageView originIv,ImageView lightEffectView,View flBgView) {

        Animation anim=destIv.getAnimation();
        if(anim!=null){
            anim.cancel();
        }
        PropertyValuesHolder rotationY = PropertyValuesHolder.ofFloat("rotationY", 720,0);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f,2.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f,2.0f);
        PropertyValuesHolder scaleX1 = PropertyValuesHolder.ofFloat("scaleX", 2.0f,1.0f);
        PropertyValuesHolder scaleY1 = PropertyValuesHolder.ofFloat("scaleY", 2.0f,1.0f);
        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(destIv, rotationY,scaleX,scaleY);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(lightEffectView,scaleX,scaleY);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(lightEffectView,scaleX1,scaleY1);
//        animator1.setInterpolator(new AccelerateInterpolator(2.0f));
        PropertyValuesHolder rotationY1 = PropertyValuesHolder.ofFloat("rotationY", 0,720);
//        ObjectAnimator animator3= ObjectAnimator.ofPropertyValuesHolder(destIv, rotationY1);
//        animator3.setInterpolator(new AccelerateInterpolator(3.0f));
//        animator3.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                                destIv.setVisibility(View.GONE);
//                                destIv.setX(x);
//                                destIv.setY(y);
//                                ScaleAnimation scaleAnimation1=new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                                scaleAnimation1.setDuration(500);
//                                originIv.startAnimation(scaleAnimation1);
//                animator2.setDuration(500).start();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//                destIv.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                flBgView.setVisibility(View.VISIBLE);
                lightEffectView.setImageResource(R.drawable.coin_light_effect);
                AnimationDrawable drawable=(AnimationDrawable)lightEffectView.getDrawable();
                drawable.start();
                flBgView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                        int[] location=new int[2];
                        int[] location1=new int[2];
                        originIv.getLocationOnScreen(location);
                        destIv.getLocationOnScreen(location1);
                        float toXDelta= location1[0]-location[0];
                        float toYDelta= location1[1]-location[1];
                        float x=destIv.getX();
                        float y=destIv.getY();
//                        Log.d(TAG, "onLayoutChange: "+(location1[1])+","+ScreenUtils.getScreenHeight(MyApplication.getInstance())/2);
//                        translateAnimation =new TranslateAnimation(0,-toXDelta+destIv.getWidth(),0,-toYDelta);
//                        translateAnimation.setInterpolator(new AccelerateInterpolator());
//                        translateAnimation.setDuration(200);
//                        translateAnimation.setFillAfter(false);
//                        ScaleAnimation scaleAnimation=new ScaleAnimation(1.0f,0.5f,1.0f,0.5f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                        scaleAnimation.setDuration(500);
//                        scaleAnimation.setFillAfter(true);
//                        set=new AnimationSet(false);
//                        set.addAnimation(translateAnimation);
//
//                        ScaleAnimation scaleAnimation1=new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                        scaleAnimation1.setDuration(500);
//
//                        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                destIv.setVisibility(View.GONE);
//                                originIv.startAnimation(scaleAnimation1);
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
                        Log.d(TAG, "onLayoutChange: toYDelta "+toYDelta+","+toXDelta);
                        PropertyValuesHolder transX= PropertyValuesHolder.ofFloat("translationX", 0f,-toXDelta);
                        PropertyValuesHolder transY = PropertyValuesHolder.ofFloat("translationY", 0f,-toYDelta);

                        animator2= ObjectAnimator.ofPropertyValuesHolder(destIv, scaleX1,scaleY1,transX,transY,rotationY1);
                        animator2.setInterpolator(new AccelerateInterpolator(2.0f));
                        animator2.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                flBgView.setVisibility(View.GONE);
                                destIv.setX(x);
                                destIv.setY(y);
                                ScaleAnimation scaleAnimation1=new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                scaleAnimation1.setDuration(500);
                                originIv.startAnimation(scaleAnimation1);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {
                                flBgView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onAnimationEnd(Animator animation) {


                Handler handler=new Handler();
                try {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AnimationDrawable drawable=(AnimationDrawable)lightEffectView.getDrawable();
                            if(drawable!=null) {
                                drawable.stop();
                            }
                            lightEffectView.setImageDrawable(null);
                            animator2.setDuration(500).start();
//                            animator3.setDuration(500).start();
                        }
                    }, 800);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                flBgView.setVisibility(View.GONE);
                AnimationDrawable drawable=(AnimationDrawable)lightEffectView.getDrawable();
                if(drawable!=null) {
                    drawable.stop();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });




//        animator.setDuration(500).start();
        animator1.setDuration(500).start();
    }

}
