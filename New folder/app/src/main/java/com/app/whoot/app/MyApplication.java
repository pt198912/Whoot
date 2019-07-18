package com.app.whoot.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.app.whoot.BuildConfig;
import com.app.whoot.R;
import com.app.whoot.base.BaseApplication;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.PriceScopeBean;
import com.app.whoot.modle.dagger2.component.AppComponent;
import com.app.whoot.modle.dagger2.component.DaggerAppComponent;
import com.app.whoot.modle.dagger2.module.AppModule;
import com.app.whoot.modle.dagger2.module.HttpModule;
import com.app.whoot.ui.view.CustomRefreshHeader;
import com.app.whoot.ui.view.auto.config.AutoLayoutConfig;
import com.app.whoot.ui.view.dialog.LoginCoupDialog;
import com.app.whoot.util.KqwException;
import com.app.whoot.util.PreferencesHelper;
import com.app.whoot.util.ScreenUtils;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.FacebookSdk;
//import com.squareup.leakcanary.LeakCanary;
import com.google.android.gms.maps.model.LatLng;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;


//  Created by ruibing.han on 2018/3/29.

public class MyApplication extends BaseApplication {
    private static MyApplication instance;
    private WeakReference<BaseActivity> mCurrentAct;
    private static AppComponent mAppComponent;
    public static Locale mLanguage;
    public static String locale;
    private PreferencesHelper mLanguageHelper;
    private Map<String, PriceScopeBean> priceScopeBeanMap=new HashMap<>();
    private Map<Integer,Map<Integer,String>> mChineseLanDishMap=new HashMap<>();
    private Map<Integer,Map<Integer,String>> mEnglishLanDishMap=new HashMap<>();
    private LatLng mLastLocation;
    private static final String TAG = "MyApplication";

    public Map<Integer, Map<Integer, String>> getChineseLanDishMap() {
        return mChineseLanDishMap;
    }

    public Map<Integer, Map<Integer, String>> getEnglishLanDishMap() {
        return mEnglishLanDishMap;
    }

    public void setLastLocation(LatLng lastLocation) {
        this.mLastLocation = lastLocation;
    }

    public LatLng getLastLocation() {
        return mLastLocation;
    }

    // private FirebaseAnalytics mFirebaseAnalytics;
    public static AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(getInstance()))
                    .httpModule(new HttpModule())
                    .build();
        }
        return mAppComponent;
    }

    public Map<String, PriceScopeBean> getPriceScopeBeanMap() {
        return priceScopeBeanMap;
    }

    public PreferencesHelper getLanguageHelper() {
        return mLanguageHelper;
    }
    public Locale getLanguage() {
        return mLanguage;
    }

    public void setCurrentAct(WeakReference<BaseActivity> currentAct) {
        this.mCurrentAct = currentAct;
    }

    public WeakReference<BaseActivity> getCurrentAct() {
        return mCurrentAct;
    }

    //获取全局的上下文
    public static MyApplication getInstance() {
        return instance;
    }
    public void setLanguage(Locale language) {
        this.mLanguage = language;
        this.locale=language.toString();
    }

    static{
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new CustomRefreshHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    private String mVersionName;

    public String getVersionName() {
        return mVersionName;
    }

    public String initVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    @Override
    public void onCreateInit() {
        instance = this;
        Log.d(TAG, "onCreateInit: Locale.getDefault "+Locale.getDefault());
        mVersionName=initVersionName(this);
        mLanguageHelper=new PreferencesHelper(this,"languageHelper");
        AutoLayoutConfig.getInstance().useDeviceSize().init(this);//初始化屏幕适配的大小信息
        //facebook登录初始化
        FacebookSdk.sdkInitialize( getApplicationContext() );

        // Obtain the FirebaseAnalytics instance.  Google Analytics初始化
       // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Bugly
        CrashReport.initCrashReport(getApplicationContext(), "b1a5cfe6a2", false);
        //全局异常捕获
        //KqwException handler = KqwException.getInstance(this);
        //Thread.setDefaultUncaughtExceptionHandler(handler);
        //fabric 初始化
        //Fabric.with(this,new Crashlytics());
        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);
//        initLeakCanary();
    }

//    private void initLeakCanary(){
//        //内存泄漏检测
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //activity生命周期回调监听
    @Override
    public void onInitActivityLifecycleCallbacks() {

    }

    //内存使用情况监听
    @Override
    public void onInitComponentStorageCallbacks() {

    }

    //耗时初始化
    @Override
    public void onInitIntentService() {
        //AppIntentService.initService(getInstance());
    }
}
