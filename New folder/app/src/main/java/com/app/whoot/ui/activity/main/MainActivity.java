package com.app.whoot.ui.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.adapter.TabFragmentAdapter;
import com.app.whoot.app.MyApplication;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.base.activity.mvp.BaseMvpActivity;
import com.app.whoot.bean.FcmTokenBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.modle.helper.ExceptionHelper;
import com.app.whoot.modle.http.Http;
import com.app.whoot.modle.http.exception.AppException;
import com.app.whoot.ui.activity.LoginMeActivity;
import com.app.whoot.ui.activity.LuchangeActivity;
import com.app.whoot.ui.activity.MapsActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.fragment.CardFragment;
import com.app.whoot.ui.fragment.FindFragment;
import com.app.whoot.ui.fragment.MyFragment;
import com.app.whoot.ui.view.custom.CustomToast;
import com.app.whoot.ui.view.custom.NoScrollViewPager;
import com.app.whoot.ui.view.dialog.BoxDialog;
import com.app.whoot.ui.view.dialog.DailyDialog;
import com.app.whoot.ui.view.dialog.FcmCoupDialog;
import com.app.whoot.ui.view.dialog.LoginCoupDialog;
import com.app.whoot.ui.view.dialog.WriteDialog;
import com.app.whoot.ui.view.recycler.adapter.StandardAdapter;
import com.app.whoot.ui.view.recycler.decoration.ColorItemDecoration;
import com.app.whoot.util.AppLanguageUtils;
import com.app.whoot.util.Constants;
import com.app.whoot.util.LanguageConfig;
import com.app.whoot.util.LoadCfgUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.firebase.iid.FirebaseInstanceId;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import okhttp3.FormBody;

import static com.app.whoot.util.Constants.ACTION_START_MAIN_ACTIVITY_TO_MAP;
import static com.app.whoot.util.Constants.ACTION_START_MAIN_ACTIVITY_TO_USED_TOKEN_PAGE;
import static com.app.whoot.util.Constants.EVENT_TYPE_FCM;
import static com.app.whoot.util.Constants.EVENT_TYPE_GAIN_TOKEN;
import static com.app.whoot.util.Constants.EVENT_TYPE_MESSAGING;
import static com.app.whoot.util.Constants.EVENT_TYPE_SHOW_FIND_FRAG;
import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;
import static com.app.whoot.util.Constants.EVENT_TYPE_TOKEN_ANIM;
import static com.app.whoot.util.Constants.EVENT_TYPE_TOKEN_EXCHANGE;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.m_pager)
    NoScrollViewPager mPager;
    @BindView(R.id.m_tab)
    TabLayout mTab;
    @BindView(R.id.m_tologin)
    View mTologin;
    @BindView(R.id.rl_bottom)
    View mBottomRl;

    private Disposable mPermissionDisposable;

    //高德地图应用包名
    public static final String AMAP_PACKAGENAME = "com.autonavi.minimap";
    //百度地图应用包名
    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";
    //google地图应用包名
    public static final String GOOGLE_PACKAGENAME = "com.google.android.apps.maps";
    private int flag_map;
    private int flag=0;
    private boolean flagGaoDe, flagBAidu, flagGOOGLE;
    private boolean isGaoDe, isBAidu, isGOOGLE;


    List<String> newlist=new ArrayList<>();
    LoadCfgHandler mHandler;
    private static final int[] tabRes = {R.drawable.home_footer_tab_find_bg, R.drawable.home_footer_tab_card_bg,R.drawable.home_footer_tab_my_bg};
    public static final int TAB_INDEX_FIND=0;
    public static final int TAB_INDEX_CARD=1;
//    public static final int TAB_INDEX_GIFT=2;
    public static final int TAB_INDEX_MINE=2;
    private static final int MSG_LOAD_CFG=0x12;
    private static final String TAG = "MainActivity";
    private String url;
    private WriteDialog dailyDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityInject() {
        getActivityComponent().inject(this);
    }

    private static class LoadCfgHandler extends Handler {
        private WeakReference<MainActivity> actRef;
        public LoadCfgHandler(WeakReference<MainActivity> actRef){
            this.actRef=actRef;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_LOAD_CFG:
                    if(actRef!=null&&actRef.get()!=null){
                        actRef.get().loadCfg();
                    }
                    
                    break;
            }
        }
    }
    int a=0;
    private FcmCoupDialog loginCoupDialog;
    @Override
    protected void onCreateInitPageAndData() {
        transparentStatusBar();
        url = (String) SPUtil.get(this, "URL", "");
        mHandler=new LoadCfgHandler(new WeakReference<>(this));
        reloadCfgIfNeed();
        newlist.add(getResources().getString(R.string.find));
        newlist.add(getResources().getString(R.string.copp));
        newlist.add(getResources().getString(R.string.myme));
        initFooterTabs();
        addListener();
        mPager.setOffscreenPageLimit(3);
        //mPresenter.getData();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("dsfdgdfgdf","refreshedToken "+refreshedToken);
        EventBus.getDefault().register(this);
        getMaps();


    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_box_ly:
                    if(loginCoupDialog!=null) {
                        loginCoupDialog.dismiss();
                    }
                    changeFragment(TAB_INDEX_CARD);
                    break;

            }
        }
    };

    private void getMaps(){
        isGaoDe = isApp(MainActivity.this, AMAP_PACKAGENAME);
        isBAidu = isApp(MainActivity.this, BAIDUMAP_PACKAGENAME);
        isGOOGLE = isApp(MainActivity.this, GOOGLE_PACKAGENAME);
        if (isGaoDe){
            flag++;
            flagGaoDe=true;
        }
        if (isBAidu){

            flag++;
            flagBAidu=true;
        }
        if (isGOOGLE){
            flag++;
            flagGOOGLE=true;
        }
        flag_map = (int) SPUtil.get(MainActivity.this, "flag_map", 0);
        if (flag==1){
            if (flagGaoDe){
                SPUtil.put(MainActivity.this,"flag_map",3);
            }else if (flagGOOGLE){
                SPUtil.put(MainActivity.this,"flag_map",2);
            }else if (flagBAidu){
                SPUtil.put(MainActivity.this,"flag_map",1);
            }
        }
    }
    private void reloadCfgIfNeed(){
//        String cfgURL = (String) SPUtil.get(this, "cfgURL", "");
//        String prefixUrl = (String) SPUtil.get(this, "URL", "");
//        if(TextUtils.isEmpty(cfgURL)||TextUtils.isEmpty(prefixUrl)){
            loadCfg();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null)
        {
            return;
        }
        // 查找在Fragment中onRequestPermissionsResult方法并调用
        for (Fragment fragment : fragments)
        {
            if (fragment != null)
            {
                // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    private void getDailyDialog(String num){
        DailyDialog dailyDialog = new DailyDialog(this,num, R.style.token_dialog);
        dailyDialog.show();
        Thread t = new Thread(new Runnable() {

            @Override

            public void run() {

                try {

                    Thread.sleep(2500);

                } catch (InterruptedException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                }

                dailyDialog.dismiss();

            }
        });
        t.start();
    }
    //每日签到
    private void getDailySign(){
        String Token = (String) SPUtil.get(this, "Token", "");
        if (Token.length()>0){
            FormBody body = new FormBody.Builder()
                    .add("notifyToken",FirebaseInstanceId.getInstance().getToken()==null?"":FirebaseInstanceId.getInstance().getToken())
                    .build();
            Http.OkHttpPost(this, url + UrlUtil.sign, body, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Log.d("sign_ly",result+"=="+url + UrlUtil.sign);
                        int code = jsonObject.getInt("code");
                        if (code==0){
                            String data = jsonObject.getString("data");
                            //data="{\"1\":1}";
                            if (!data.equals("{}")){
                                String data_replace = data.replace("\"", "");
                                String replace1 = data_replace.replace("{", "").replace("}", "").replace("\"", "");
                                String[] split = replace1.split(",");
                                String regain = split[0];
                                String[] split1 = regain.split(":");
                                String s = split1[0];
                                String s1 = split1[1];
                                getDailyDialog(s1);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        String action=intent.getStringExtra(Constants.EXTRA_KEY_START_MAIN_ACTIVITY);
        if(action!=null&&action.equals(ACTION_START_MAIN_ACTIVITY_TO_MAP)){
            int shopId=intent.getIntExtra("shopId",0);
            Log.d(TAG, "onNewIntent: shopId "+shopId);
            findFrag.openStoreInMap(shopId);
        }else if(action!=null&&action.equals(ACTION_START_MAIN_ACTIVITY_TO_USED_TOKEN_PAGE)){
            changeFragment(TAB_INDEX_CARD);
            cardFragment.setTabIndex(CardFragment.TAB_INDEX.USED);
        }

    }
    private String Tokensplit;
    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventBusCarrier carrier) {
        String eventType=carrier.getEventType();
        if(EVENT_TYPE_TOKEN_EXCHANGE.equals(eventType)){
            TokenExchangeBean bean = (TokenExchangeBean) carrier.getObject();
            Log.d(TAG, "handleEvent: EVENT_TYPE_TOKEN_EXCHANGE ");
            if(bean==null){
                return;
            }
            int su_token = bean.getSuToken();
            int flag_token = bean.getFlagToken();
            int flag_coupon = bean.getFlagCoupn();
            Log.d(TAG, "onResume: "+flag_coupon+"=="+flag_token);
            if (su_token==1){
                changeFragment(TAB_INDEX_CARD);
                cardFragment.setTabIndex(CardFragment.TAB_INDEX.UNUSED);
//                if (flag_token!=1) {
//                    flag_coupon=0;//1、2、3代表执行动画,其他代表不执行动画
//                }
                Log.d("Token_D",flag_coupon+"=="+flag_token);
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType(Constants.EVENT_TYPE_TOKEN_ANIM);
                eventBusCarrier.setObject(bean);
                EventBus.getDefault().post(eventBusCarrier); //普通事件发布

            }
        }else if(EVENT_TYPE_GAIN_TOKEN.equals(eventType)){
            Log.d(TAG, "handleEvent: EVENT_TYPE_GAIN_TOKEN");
            changeFragment(TAB_INDEX_CARD);
            cardFragment.setTabIndex(CardFragment.TAB_INDEX.UNUSED);
        }else if (EVENT_TYPE_SIGN.equals(eventType)){
            Log.d(TAG, "handleEvent: EVENT_TYPE_SIGN");
            TokenExchangeBean object = (TokenExchangeBean) carrier.getObject();
            int suToken = object.getSuToken();
            if (suToken==1){
//                mPager.setCurrentItem(mCurrentIndex);
//                View v =mTab.getTabAt(mCurrentIndex).getCustomView();
//                ((TextView) v.findViewById(R.id.footer_text)).setSelected(true);
//                ((ImageView) v.findViewById(R.id.footer_img)).setSelected(true);
//                View v1 =mTab.getTabAt(0).getCustomView();
//                ((TextView) v1.findViewById(R.id.footer_text)).setSelected(false);
//                ((ImageView) v1.findViewById(R.id.footer_img)).setSelected(false);
                changeFragment(mCurrentIndex);
            }else if (suToken==4){
                changeFragment(TAB_INDEX_FIND);
            }else if (suToken==5){
                changeFragment(TAB_INDEX_MINE);
            }else if (suToken==2){
                changeFragment(TAB_INDEX_CARD);
            }
            getDailySign();

        }else if(EVENT_TYPE_SHOW_FIND_FRAG.equals(eventType)){
            changeFragment(TAB_INDEX_FIND);
        }else if (EVENT_TYPE_FCM.equals(eventType)){
            FcmTokenBean object = (FcmTokenBean) carrier.getObject();
            String fcmCoupn = object.getFcmCoupn();  //金币
            String fcmTken = object.getFcmTken();    //银币
            String fcmdiamond = object.getFcmdiamond();//钻石币
            String fcmname = object.getFcmname();
            String fcminviteCount = object.getFcminviteCount(); //人数 ,Tokensplit fcmname,fcminviteCount,
            Tokensplit=fcmTken+"="+fcmCoupn+"="+fcmdiamond;
            loginCoupDialog = new FcmCoupDialog(this,Tokensplit,fcmname,fcminviteCount, onClickListener);
            loginCoupDialog.show();


        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int su_token = intent.getIntExtra("su_token", 0);
        int flag_token = intent.getIntExtra("flag_token", 0);
        int flag_coupon = intent.getIntExtra("flag_coupon", 0);
        Log.d(TAG, "onResume: "+flag_coupon+"=="+flag_token);
        if (su_token==1){
            intent.putExtra("su_token",0);
            changeFragment(TAB_INDEX_CARD);
            if (flag_token!=1) {
                flag_coupon=0;//1、2、3代表执行动画,其他代表不执行动画
            }
            Log.d("Token_D",flag_coupon+"=="+flag_token);
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(Constants.EVENT_TYPE_TOKEN_ANIM);
            eventBusCarrier.setObject(flag_coupon);
            EventBus.getDefault().post(eventBusCarrier); //普通事件发布

        }
        String token = (String) SPUtil.get(this, "Token", "");
        if (token.length()!=0){
            getDailySign();
            if (dailyDialog==null){
                dailyDialog = new WriteDialog(this,R.style.token_dialog);
               // dailyDialog.show();
            }


        }

        if (mPager.getCurrentItem() == 0) {
            transparentStatusBar();
        } else {
            clearTransparentStatusBar();
        }



//        updateLoginTipView();
    }
//    public void updateLoginTipView(){
//        String token = (String) SPUtil.get(this, "Token", "");
//        if(frag_lo!=null) {
//            if (token != null && token.length() == 0) {
//                if(mCurrentIndex==0) {
//                    frag_lo.getBackground().setAlpha(170);
//                    frag_lo.setVisibility(View.VISIBLE);
//                }else{
//                    frag_lo.getBackground().setAlpha(0);
//                    frag_lo.setVisibility(View.GONE);
//                }
//            } else {
//                frag_lo.getBackground().setAlpha(0);
//                frag_lo.setVisibility(View.GONE);
//            }
//        }
//    }
//    public void setLoginTipVisibile(int visibile){
//        frag_lo.setVisibility(visibile);
//    }
    private void loadCfg(){
        Log.d(TAG, "loadCfg: ");
        LoadCfgUtils.loadCfg(this, new LoadCfgUtils.LoadCfgCallback() {
            @Override
            public void loadCfgSuccess() {
                Log.d(TAG, "loadCfgSuccess: ");
                onResume();
            }

            @Override
            public void loadCfgFail() {
                Log.d(TAG, "loadCfgFail: ");
                mHandler.sendEmptyMessageDelayed(MSG_LOAD_CFG,1000);
            }
        });
    }
    
    public void changeFragment(int index){
        TabLayout.Tab tabItem=mTab.getTabAt(index);
        if(tabItem!=null){
            View v = tabItem.getCustomView();
            ((TextView) v.findViewById(R.id.footer_text)).setSelected(true);
            ((ImageView) v.findViewById(R.id.footer_img)).setSelected(true);
            mPager.setCurrentItem(index);
        }
        for(int i=0;i<mTab.getTabCount();i++){
            if(i!=index) {
                TabLayout.Tab tab=mTab.getTabAt(i);
                ((TextView) tab.getCustomView().findViewById(R.id.footer_text)).setSelected(false);
                ((ImageView) tab.getCustomView().findViewById(R.id.footer_img)).setSelected(false);
            }
        }
        if(index==TAB_INDEX_FIND&&findFrag.getContentFlag()==1){
            setTabLayoutVisibility(View.GONE);
        }else {
            setTabLayoutVisibility(View.VISIBLE);
        }
    }

    private int mCurrentIndex;
    private void addListener() {
        for(int i=0;i<mTab.getTabCount();i++){
            TabLayout.Tab tab=mTab.getTabAt(i);
            View customView=tab.getCustomView();
            if(customView!=null) {
                customView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View customView = tab.getCustomView();
                        ((TextView) customView.findViewById(R.id.footer_text)).setSelected(true);
                        ((ImageView) customView.findViewById(R.id.footer_img)).setSelected(true);
                        mCurrentIndex = tab.getPosition();
                        for(int j=0;j<mTab.getTabCount();j++){
                            if(j!=mCurrentIndex) {
                                TabLayout.Tab other=mTab.getTabAt(j);
                                ((TextView) other.getCustomView().findViewById(R.id.footer_text)).setSelected(false);
                                ((ImageView) other.getCustomView().findViewById(R.id.footer_img)).setSelected(false);
                            }
                        }
                        Log.d(TAG, "onTabSelected: mCurrentIndex " + mCurrentIndex);

                        String token = (String) SPUtil.get(MainActivity.this, "Token", "");
                        if (token.length() == 0) {
                            if (mCurrentIndex == 1) {
                                Intent intent = new Intent(MainActivity.this, LoginMeActivity.class);
                                intent.putExtra("card_flag",1);
                                startActivity(intent);
                                View v = tab.getCustomView();
                                ((TextView) v.findViewById(R.id.footer_text)).setSelected(false);
                                ((ImageView) v.findViewById(R.id.footer_img)).setSelected(false);
                                View v1 = mTab.getTabAt(0).getCustomView();

                                ((TextView) v1.findViewById(R.id.footer_text)).setSelected(true);
                                ((ImageView) v1.findViewById(R.id.footer_img)).setSelected(true);
                            } else if (mCurrentIndex == 2) {
                                Intent intent = new Intent(MainActivity.this, LoginMeActivity.class);
                                intent.putExtra("my_flag",2);
                                startActivity(intent);
                                View v = tab.getCustomView();
                                ((TextView) v.findViewById(R.id.footer_text)).setSelected(false);
                                ((ImageView) v.findViewById(R.id.footer_img)).setSelected(false);
                                View v1 = mTab.getTabAt(0).getCustomView();
                                ((TextView) v1.findViewById(R.id.footer_text)).setSelected(true);
                                ((ImageView) v1.findViewById(R.id.footer_img)).setSelected(true);
                            } else {
                                mPager.setCurrentItem(tab.getPosition());

                            }
                        } else {
                            mPager.setCurrentItem(tab.getPosition());
                        }
                    }
                });
            }
        }
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private FindFragment findFrag;
    private CardFragment cardFragment;
    private MyFragment myFrag;
    /**
     * 初始化底部的三个tab
     */
    private void initFooterTabs() {
        for (int i = 0; i < newlist.size(); i++) {
            TabLayout.Tab tab = mTab.newTab();
            View v = LayoutInflater.from(this).inflate(R.layout.main_footer_tab_item, null);
            ((TextView) v.findViewById(R.id.footer_text)).setText(newlist.get(i));
            ((ImageView) v.findViewById(R.id.footer_img)).setImageResource(tabRes[i]);
            //为了防止tab的CustomView不充满父容器，写死一个高度
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(this,R.dimen.home_footer_height)));
            tab.setCustomView(v);
            mTab.addTab(tab);
        }
        findFrag=new FindFragment();
        cardFragment=new CardFragment();
        myFrag=new MyFragment();
        ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
            add(findFrag);
            add(cardFragment);
            add(myFrag);
        }};
        mPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), fragments));
    }

    //展示网络获取成功后的数据
    @Override
    public void showData() {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    //展示网络获取失败的错误信息
    @Override
    public void showErrorMsg(AppException e) {
        CustomToast.create(this).longShow(e.getMsg());
    }
    // 第一次按下返回键的事件
    private long firstTime;
    @Override
    public void onBackPressed() {
        /*if (isTaskRoot()) {//判断是否是根
            moveTaskToBack(false);//将app 移动到后台  不关闭主页面，等再次打开app,实现暖启动
        } else {
            super.onBackPressed();
        }*/
        if(findFrag!=null&&findFrag.isFromStoreDetailToOpenMap()){
            findFrag.setFromStoreDetailToOpenMap(false);
            findFrag.resetUIOnExitShowingOneStoreInMap();
            return;
        }
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 3000) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.exit), Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }

    }
    public void getCardFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardFragment cardFragment = new CardFragment();
        fragmentTransaction.replace(R.id.m_pager,cardFragment);
        fragmentTransaction.commit();
    }

    public void setTabLayoutVisibility(int visibility){
        mBottomRl.setVisibility(visibility);
    }


    private boolean isApp(Context context, String packname) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return true;
        } else {
            return false;
        }
    }
}
