package com.app.whoot.base.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.base.impl.ActivityImpl;
import com.app.whoot.interfacevoke.OnHttpInfoListener;
import com.app.whoot.ui.activity.LuchangeActivity;
import com.app.whoot.ui.view.dialog.RotateLoadingDialog;
import com.app.whoot.util.AppLanguageUtils;
import com.app.whoot.util.ConstantLanguages;
import com.app.whoot.util.LanguageConfig;
import com.app.whoot.util.SPUtil;
import com.gyf.barlibrary.ImmersionBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//无MVP的Activity基类
public abstract class BaseActivity extends AutoLayoutActivity implements ActivityImpl {

    public static final String BUNDLE_NAME = "afferentName";//activity页面传入的数据Bundle的name
    private Unbinder mUnBind;
    private RotateLoadingDialog mLoadingDlg;
    private Handler mHandler;
    private OnHttpInfoListener mHttpInfoListener;
    protected ImmersionBar mImmersionBar;
    protected boolean mAutoAdjustStatusBar=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnBind = ButterKnife.bind(this);
        mHandler=new Handler();
        initLanguageConfig();
//        setStatusTransacation();
        onCreateInit();//在onCreate方法初始化页面

        // 系统 6.0 以上 状态栏白底黑字的实现方法
//        this.getWindow()
//                .getDecorView()
//                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        mImmersionBar = ImmersionBar.with(this);
//        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.KITKAT){
//            mImmersionBar.statusBarDarkFont(true,0.2f);  //状态栏字体是深色，不写默认为亮色
//        }else{
//            mImmersionBar.statusBarDarkFont(true);
//        }
//        mImmersionBar.statusBarColor(R.color.white)
//                .navigationBarColor(R.color.white)
//                .flymeOSStatusBarFontColor(R.color.white)  //修改flyme OS状态栏字体颜色
//                .init();
        if(mAutoAdjustStatusBar) {
            clearTransparentStatusBar();
        }
    }
    public void transparentStatusBar(){
        if(null==mImmersionBar){
            mImmersionBar = ImmersionBar.with(this);
        }
        mImmersionBar.keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .transparentStatusBar().statusBarDarkFont(false)
                .init();
    }
    public void clearTransparentStatusBar(){
        if(null==mImmersionBar){
            mImmersionBar = ImmersionBar.with(this);
        }
        mImmersionBar.keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .transparentStatusBar().statusBarDarkFont(true,0.2f)
                .init();
    }
    private void initLanguageConfig(){
        String lan= MyApplication.getInstance().getLanguageHelper().getValue("lan");
        Log.d(TAG, "initLanguageConfig: "+lan);
        if(lan!=null){
            String[] tmp=lan.split("_");
            if(tmp.length>1){
                MyApplication.mLanguage=new Locale(tmp[0],tmp[1]);
            }else{
                MyApplication.mLanguage=new Locale(lan);
            }
            MyApplication.locale=lan;
            AppLanguageUtils.setLanguage(this,MyApplication.mLanguage,lan);
        }else {

            Locale locale=Locale.getDefault();
            Log.d(TAG, "initLanguageConfig: Locale.getDefault "+locale.getLanguage());
            if(locale.getLanguage().contains("en")){
                SPUtil.put(this,"confirm", 3);
                AppLanguageUtils.setLanguage(this,new Locale("en"), LanguageConfig.ID_EN);
            }else{
                SPUtil.put(this,"confirm", 2);
                AppLanguageUtils.setLanguage(this,new Locale("zh","TW"), LanguageConfig.ID_TAIWAN);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setCurrentAct(new WeakReference<>(this));
    }

    public void registerHttpInfoListener(OnHttpInfoListener listener){
        this.mHttpInfoListener=listener;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBind.unbind();
        dissLoad();
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
        if(mImmersionBar!=null){
            mImmersionBar.destroy();
        }
    }
    /**
     * title栏透明
     */
//    private void setStatusTransacation() {
//        if (Build.VERSION.SDK_INT >= 19) {
//            Window win = getWindow();
//            WindowManager.LayoutParams winParams = win.getAttributes();
//            final int bits = 0x6000000;
//            winParams.flags |= bits;
//            win.setAttributes(winParams);
//        }
//    }
    //基本的页面跳转 ---------------------startActivity----------------------
    @Override
    public void gotoActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    //基本的页面跳转 带参数
    @Override
    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(BUNDLE_NAME, bundle);
        startActivity(intent);
    }

    //基本的页面跳转 ---------------------startActivityForResult--------------------------
    @Override
    public void gotoActivityForResult(Class<? extends Activity> clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
    }

    @Override
    public void gotoActivityForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(BUNDLE_NAME, bundle);
        startActivityForResult(intent, requestCode);
    }

    private List<OnDispatchTouchListener> mDispacthTOuchListener=new ArrayList<>();
    public interface OnDispatchTouchListener{
        void dispatchTouchEvent(MotionEvent ev);
    }
    public void registerDispatchTouchEventListener(OnDispatchTouchListener listener){
        if(listener!=null){
            mDispacthTOuchListener.add(listener);
        }
    }
    public void unregisterDispatchTouchEventListener(OnDispatchTouchListener listener){
        if(listener!=null){
            mDispacthTOuchListener.remove(listener);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                Log.d(TAG, "hideSoftInput: ");
                hideSoftInput(v.getWindowToken());
            }else{
                Log.d(TAG, "notHideSoftInput: ");
            }
        }
        for(OnDispatchTouchListener listener:mDispacthTOuchListener){
            if(listener!=null){
                listener.dispatchTouchEvent(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationOnScreen(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, 0);
        }
    }

    private static final String TAG = "BaseActivity";
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public RotateLoadingDialog loading(String title) {
        if (null == mLoadingDlg) {
            mLoadingDlg = new RotateLoadingDialog(BaseActivity.this);
            mLoadingDlg.setCanceledOnTouchOutside(false);
        }
        if (!isFinishing() && !isDestroyed()) {
            mLoadingDlg.setTitle(title);
            if(mHandler!=null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDlg.show();
                    }
                }, 2000);
            }

        }
        return mLoadingDlg;
    }


    public void dissLoad() {
        if(mHandler!=null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mLoadingDlg != null && mLoadingDlg.isShowing()) {
            mLoadingDlg.dismiss();
        }
    }
}
