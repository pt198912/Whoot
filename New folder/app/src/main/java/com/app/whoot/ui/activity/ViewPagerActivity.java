
package com.app.whoot.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CommBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.demo.ImageLoader;
import com.app.whoot.ui.view.demo.ImageViewer;
import com.app.whoot.ui.view.demo.ViewData;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.fido.fido2.api.common.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerActivity extends BaseActivity {


    private String imgea;
    private List<CommBean> list = new ArrayList<>();
    private int flag_img;
    private List<String> imglist = new ArrayList<>();
    private String[] split;
    private List<ViewData> mViewDatas;
    private RequestOptions mOptions;
    private ImageViewer imageViewer;
    private String urlUtil;

    @Override
    public int getLayoutId() {
        return R.layout.activity_view_pager;
    }

    @Override
    public void onCreateInit() {

        urlUtil = (String) SPUtil.get(ViewPagerActivity.this, "URL", "");
        imageViewer = findViewById(R.id.view_pager);
        Intent intent = getIntent();
        imgea = intent.getStringExtra("imgea");  //条目的position
        flag_img = intent.getIntExtra("flag_img", 0);  //图片的position
        mViewDatas = new ArrayList<>();


        getData();

    }


    public void getData() {

        Http.OkHttpGet(ViewPagerActivity.this, urlUtil+ UrlUtil.comments, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        list.clear();
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length() == 0) {

                        } else {

                            for (int i = 0; i < data.length(); i++) {

                                CommBean bean = GSonUtil.parseGson(data.getString(i), CommBean.class);
                                list.add(bean);
                            }
                            String imgUrl = list.get(Integer.parseInt(imgea)).getImgUrl();
                            if (imgUrl.equals("")) {

                            } else {

                                if (imgUrl.indexOf(",") != -1) {
                                    split = imgUrl.split(",");
                                    if (imglist.size() != 0) {
                                        imglist.clear();
                                    }
                                    for (int i = 0; i < split.length; i++) {
                                        String s = split[i];

                                        imglist.add(s.trim());

                                        ViewData viewData = new ViewData();
                                        mViewDatas.add(viewData);
                                    }
                                } else {
                                    imglist.add(imgUrl.trim());

                                    ViewData viewData = new ViewData();
                                    mViewDatas.add(viewData);
                                }
                                // 图片浏览的起始位置
                                imageViewer.setStartPosition(flag_img);
                                // 图片的数据源
                                imageViewer.setImageData(imglist);
                                // 外部 View 的位置以及尺寸等信息
                                imageViewer.setViewData(mViewDatas);
                                imageViewer.setImageLoader(new ImageLoader() {
                                    @Override
                                    public void displayImage(int position, Object src, ImageView view) {
                                        Glide.with(ViewPagerActivity.this)
                                                .load(src)
                                                .into(new SimpleTarget<GlideDrawable>() {

                                                    @Override
                                                    public void onLoadStarted(Drawable placeholder) {
                                                        super.onLoadStarted(placeholder);
                                                        view.setImageDrawable(placeholder);
                                                    }

                                                    @Override
                                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                        super.onLoadFailed(e, errorDrawable);
                                                        view.setImageDrawable(errorDrawable);
                                                    }

                                                    @Override
                                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                        view.setImageDrawable(resource);
                                                       // mImageList.set(position, resource);
                                                        mViewDatas.get(position).setImageWidth(resource.getIntrinsicWidth());
                                                        mViewDatas.get(position).setImageHeight(resource.getIntrinsicHeight());
                                                    }
                                                });
                                    }
                                });

                              /*  if (mViewDatas.get(flag_img).getWidth() == 0) {

                                    for (int i = 0; i < imglist.size(); i++) {
                                        int[] location = new int[2];
                                        // 获取在整个屏幕内的绝对坐标
                                        //autoGridView.getChildAt(i).getLocationOnScreen(location);
                                        ViewData viewData = mViewDatas.get(i);
                                        viewData.setX(location[0]);
                                        // 此处注意，获取 Y 轴坐标时，需要根据实际情况来处理《状态栏》的高度，判断是否需要计算进去
                                        viewData.setY(location[1]);
                                        //viewData.setWidth(autoGridView.getChildAt(i).getMeasuredWidth());
                                        //viewData.setHeight(autoGridView.getChildAt(i).getMeasuredHeight());
                                        mViewDatas.set(i, viewData);
                                    }
                                }
                                imageViewer.setStartPosition(flag_img);
                                imageViewer.setImageData(imglist);
                                imageViewer.setViewData(mViewDatas);
                                imageViewer.setImageLoader(new ImageLoader() {
                                    @Override
                                    public void displayImage(final int position, Object src, final ImageView view) {
                                        Glide.with(ViewPagerActivity.this)
                                                .load(src)
                                              //  .apply(mOptions)
                                                .into(new SimpleTarget<GlideDrawable>() {
                                                    @Override
                                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                        view.setImageDrawable(resource);
                                                        mViewDatas.get(position).setImageWidth(resource.getIntrinsicWidth());
                                                        mViewDatas.get(position).setImageHeight(resource.getIntrinsicHeight());
                                                    }
                                                });
                                    }
                                });
                                imageViewer.watch();*/

                            }

                        }

                    } else {

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
