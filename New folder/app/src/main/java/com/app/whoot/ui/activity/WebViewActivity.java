package com.app.whoot.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.util.Constants;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 8/9/2018.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    private int html;
    private static final String TAG = "WebViewActivity";
    @Override
    public int getLayoutId() {
        return R.layout.webview_activi;
    }

    @Override
    public void onCreateInit() {

        Intent intent = getIntent();
        html = intent.getIntExtra("html",0);
        String  luchang= (String) SPUtil.get(this, "luchang", "");
        boolean zh = TimeUtil.isZh(this);
        if (luchang.equals("0")){
            if (zh){
                luchang="2";
            }else {
                luchang="3";
            }
        }
        Log.d(TAG, "onCreateInit: luchang "+luchang);
        if (html==1){
            webView.loadUrl(UrlUtil.Policy);//加载url
            titleBackTitle.setText(getResources().getString(R.string.yinsi));
        }else if (html==2){
            webView.loadUrl(UrlUtil.Terms);//加载url
            titleBackTitle.setText(getResources().getString(R.string.fuwu));
        }else if (html==3){
            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                webView.loadUrl(UrlUtil.Token_HK);//加载url
            }else if (luchang.equals("3")){
                webView.loadUrl(UrlUtil.Token_US);//加载url
            }
            titleBackTitle.setText(getResources().getString(R.string.question));
        }else if (html==4){
            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                webView.loadUrl(UrlUtil.About_HK+"?version="+ MyApplication.getInstance().getVersionName());//加载url
            }else if (luchang.equals("3")){
                webView.loadUrl(UrlUtil.About_US+"?version="+ MyApplication.getInstance().getVersionName());//加载url
            }
            titleBackTitle.setText(getResources().getString(R.string.about));
        }else if (html==5){
            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                webView.loadUrl(UrlUtil.Faq_HK);//加载url
            }else if (luchang.equals("3")){
                webView.loadUrl(UrlUtil.Faq_US);//加载url
            }
            titleBackTitle.setText(getResources().getString(R.string.question));
        }else if(html==6){
            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                webView.loadUrl(UrlUtil.ACTIVITY_HK);//加载url
            }else if (luchang.equals("3")){
                webView.loadUrl(UrlUtil.ACTIVITY_US);//加载url
            }
            titleBackTitle.setMarqueeRepeatLimit(-1);
            titleBackTitle.setText(getResources().getString(R.string.kol_cn));
        }else if(html==7){
            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                webView.loadUrl(UrlUtil.KOL_EVENT_HK);
            }else if (luchang.equals("3")){
                webView.loadUrl(UrlUtil.KOL_EVENT_US);
            }
            titleBackTitle.setMarqueeRepeatLimit(-1);
            titleBackTitle.setText(getResources().getString(R.string.kol_cn));
        }

        webView.addJavascriptInterface(this, "android");//添加js监听 这样html就能调用客户端
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen", "拦截url:" + url);
            if (url.equals("http://www.google.com/")) {
                //Toast.makeText(WebViewActivity.this, "国内不能访问google,拦截该url", Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    private void reportSearchClick(){
        Bundle b=new Bundle();
        b.putString("type","CLICK_KOL_RULES");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.KOL_KOL_RULES,b);
    }

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            if(html==7){
                try {
                    JSONObject obj=new JSONObject(message);
                    String ope=obj.optString("touchPos");
                    if("shopName".equals(ope)){
                        String shopId=obj.optString("shopID");
                        int id=Integer.parseInt(shopId);
                        SPUtil.put(WebViewActivity.this, Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_KOL_ACT);
                        Intent intent = new Intent(WebViewActivity.this, StoreDetailActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }else if ("rules".equals(ope)){
                        reportPageShown();
                    }else if("redeemBtn".equals(ope)) {
                        SPUtil.put(WebViewActivity.this, "shopId_item", obj.optString("shopID"));
                        SPUtil.put(WebViewActivity.this, "couponId_item",  obj.optString("CouponId")); //优惠卷类型
                        SPUtil.put(WebViewActivity.this, Constants.SP_KEY_COUP_DETAIL_FROM, Constants.GO_COUP_DETAIL_FROM_KOL_ACT);
                        Intent coupDetail = new Intent(WebViewActivity.this, CoupDetaiActivity.class);
                        startActivity(coupDetail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
                localBuilder.setMessage(message).setPositiveButton("确定", null);
                localBuilder.setCancelable(false);
                localBuilder.create().show();
            }

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen", "网页标题:" + title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try{
                progressBar.setProgress(newProgress);
            }catch (Exception e){}



        }
    };

    private void reportPageShown() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_ACTIVITY_RULE");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.KOL_KOL_RULES,b);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen", "是否有上一个页面:" + webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * JS调用android的方法
     *
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void getClient(String str) {
        Log.i("ansen", "html调用客户端:" + str);
    }

    @Override
    protected void onDestroy() {
        //resolve window leaking in webview
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setBuiltInZoomControls(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
        //释放资源
        //webView.destroy();
        //  webView=null;
    }

    @Override
    public void finish() {
        //if set zoomController in webview,activity will crash when it's destroyed.Add this code to resolve it
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();

    }

    @OnClick(R.id.title_back_fl)
    public void onViewClicked() {

        finish();
    }
}
