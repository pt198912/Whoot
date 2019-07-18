package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.MeCommAdapter;
import com.app.whoot.adapter.TestGridViewAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.MeCommBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.custom.MultiImageView;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.custom.ScaleImageView;
import com.app.whoot.util.Constants;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.SysUtils;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.jchou.imagereview.ui.ImagePagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 6/19/2018.
 * 评论详情
 */

public class MeCommentActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.mecomm_text)
    TextView mecommText;
    @BindView(R.id.mecomm_title)
    TextView mecommTitle;
    @BindView(R.id.mecomm_grid)
    MultiImageView mecommGrid;
    @BindView(R.id.mecomm_img)
    ImageView mecommImg;
    @BindView(R.id.mecomm_tet)
    TextView mecommTet;
    @BindView(R.id.mecomm_z)
    TextView mecommZ;
    @BindView(R.id.mecomm_zx)
    TextView mecommZx;
    @BindView(R.id.mecomm_tz)
    TextView mecommTz;
    @BindView(R.id.mecomm_ly)
    LinearLayout mecommLy;
    @BindView(R.id.edit_img)
    ImageView edit_img;
    @BindView(R.id.edit_ex_0)
    ImageView edit_ex_0;
    @BindView(R.id.edit_ex_1)
    ImageView edit_ex_1;
    @BindView(R.id.edit_ex_2)
    ImageView edit_ex_2;
    @BindView(R.id.edit_ex_3)
    ImageView edit_ex_3;
    @BindView(R.id.edit_ex_4)
    ImageView edit_ex_4;
    @BindView(R.id.edit_name)
    TextView edit_name;
    @BindView(R.id.edit_cai)
    TextView edit_cai;
    @BindView(R.id.edit_ping)
    TextView edit_ping;
    @BindView(R.id.edit_dele)
    ImageView edit_dele;
    @BindView(R.id.edit_num)
    TextView edit_num;
    @BindView(R.id.title_review_img)
    ImageView title_review_img;
    @BindView(R.id.mecomm_recycler)
    RecyclerView mecomm_recycler;

    private List<MeCommBean> list=new ArrayList<MeCommBean>();
    private ArrayList<String> imglist=new ArrayList<>();
    private TestGridViewAdapter nearByInfoImgsAdapter;
    private int wh;
    private String[] split;
    private String urlUtil;
    private String commentId;
    private String useCount;
    private String gift_flag;
    private String TAG="MeCommentActivity.class";
    private MeCommAdapter mainAdapter;
    //存放返回时当前页码
    private Bundle bundle;

    @Override
    public int getLayoutId() {
        return R.layout.me_comment;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(getResources().getString(R.string.me_comm));


        wh = (SysUtils.getScreenWidth(this)- SysUtils.Dp2Px(this, 99)) / 3;

        urlUtil = (String) SPUtil.get(MeCommentActivity.this, "URL", "");

        Intent intent = getIntent();
        commentId = intent.getStringExtra("commentId");
        useCount = intent.getStringExtra("useCount");
        gift_flag = intent.getStringExtra("gift_flag");
        int flag_cou = intent.getIntExtra("flag_cou", 0);
       /* if (flag_cou!=1){
            title_review_img.setVisibility(View.VISIBLE);
        }*/
        //getMecomment();

    }
    private int img=0;
    private void getMecomment(){
        Http.OkHttpGet(MeCommentActivity.this, urlUtil+ UrlUtil.comment + "/"+ commentId, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {

                Log.d(TAG,result+urlUtil+ UrlUtil.comment + "/"+ commentId);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")){

                        if (list.size()>0){
                            list.clear();
                        }
                        Gson gson = new Gson();
                        MeCommBean meCommBean = gson.fromJson(result, MeCommBean.class);
                        list.add(meCommBean);
                        if (img==0){
                            img=1;
                            Glide.with(MeCommentActivity.this)
                                    .load(list.get(0).getData().getShopImg())
                                    .centerCrop()
                                    .skipMemoryCache(true)
                                    .thumbnail(0.1f)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存
                                    .error(R.drawable.review_pic_blank)
                                    .into(new GlideDrawableImageViewTarget(edit_img, 1));
                        }

                        edit_name.setText(list.get(0).getData().getShopName());
                        edit_cai.setText(list.get(0).getData().getCommodityName());
                        String timedateone = TimeUtil.timedateone(list.get(0).getData().getUpdateTm());
                     //   edit_ping.setText(getResources().getString(R.string.redemp)+timedateone);
                        double shopGrade = list.get(0).getData().getGrade();
                        String substring = String.valueOf(shopGrade).substring(0, 1);
                        String s_xing = DateUtil.formateRate(String.valueOf(shopGrade));
                        if (substring.equals("1")){
                            edit_ex_0.setBackgroundResource(R.drawable.icon_star_10);

                            if (s_xing.equals("1")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("2")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("3")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("4")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("5")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_5);
                            }else if (s_xing.equals("6")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("7")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("8")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("9")){
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                            }else {
                                edit_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                            }
                            edit_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                            edit_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                            edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                        }else if (substring.equals("2")){
                            edit_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                            if (s_xing.equals("1")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("2")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("3")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("4")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("5")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_5);
                            }else if (s_xing.equals("6")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("7")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("8")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("9")){
                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                            }else {

                                edit_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                            }
                            edit_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                            edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                        }else if (substring.equals("3")){
                            edit_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                            if (s_xing.equals("1")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("2")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("3")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("4")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("5")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_5);
                            }else if (s_xing.equals("6")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("7")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("8")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("9")){
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                            }else {
                                edit_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                            }

                            edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                        }else if (substring.equals("4")){
                            edit_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                            if (s_xing.equals("1")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("2")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("3")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("4")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                            }else if (s_xing.equals("5")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_5);
                            }else if (s_xing.equals("6")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("7")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("8")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                            }else if (s_xing.equals("9")){
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                            }else {
                                edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                            }
                        }else if (substring.equals("5")){
                            edit_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                            edit_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                        }else {
                            edit_ex_0.setBackgroundResource(R.drawable.icon_star_0);
                            edit_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                            edit_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                            edit_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                            edit_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                        }

                        mecommTitle.setText(list.get(0).getData().getContent());

                        int couponId = list.get(0).getData().getCouponId();
                        if (gift_flag==null){
                            edit_dele.setVisibility(View.VISIBLE);
                            edit_num.setVisibility(View.VISIBLE);
                            if (couponId==1){
                                edit_dele.setBackgroundResource(R.drawable.token_icon_bronze);
                            }else if (couponId==2){
                                edit_dele.setBackgroundResource(R.drawable.token_icon_silver);
                            }else if (couponId==3){
                                edit_dele.setBackgroundResource(R.drawable.token_icon_gold);
                            }else if (couponId==4){
                                edit_dele.setBackgroundResource(R.drawable.token_icon_diamond);
                            }else if (couponId==0){
                                edit_dele.setBackgroundResource(R.drawable.gifts_tab_icon_gift);

                            }
                            if (!useCount.equals("0")){
                                edit_num.setText("X"+useCount);
                            }

                        }else {
                            edit_dele.setVisibility(View.INVISIBLE);
                            edit_num.setVisibility(View.INVISIBLE);
                        }

                        long updateTm = list.get(0).getData().getUpdateTm();
                        String timeFormatText = TimeUtil.timedaNo(updateTm);
                        mecommText.setText(timeFormatText);

                        long replyTm = list.get(0).getData().getReplyTm();
                        if (replyTm==0){
                            mecommLy.setVisibility(View.GONE);
                        }else {
                            mecommLy.setVisibility(View.VISIBLE);
                        }
                        String timedate = TimeUtil.timedaNo(replyTm);
                        mecommZx.setText(timedate);
                        String reply = list.get(0).getData().getReply();
                        mecommTz.setText(reply);
                        try {
                            String imgUrl = list.get(0).getData().getImgUrl();
                            if (imgUrl.equals("")){
                                mecommGrid.setVisibility(View.GONE);
                            }else {
                                mecommGrid.setVisibility(View.GONE);
                                if (imglist.size()!=0){
                                    imglist.clear();
                                }
                                if (imgUrl.indexOf(",")!=-1){
                                    split = imgUrl.split(",");

                                    for (int i = 0; i < split.length ; i++) {
                                        Log.d("zheshizenm", split[i].trim());
                                        String s = split[i];

                                        imglist.add(s.trim());
                                    }
                                }else {
                                    imglist.add(imgUrl.trim());
                                }
                                mecomm_recycler.setLayoutManager(new GridLayoutManager(MeCommentActivity.this, 3));
                                mainAdapter = new MeCommAdapter(imglist);
                                mainAdapter.setOnItemClickListener(new MeCommAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int pos) {
                                        ImagePagerActivity.startImagePage(MeCommentActivity.this,
                                                imglist,pos,mecomm_recycler.getLayoutManager().findViewByPosition(pos));
                                    }
                                });
                                mecomm_recycler.setAdapter(mainAdapter);

                                //设置转场动画的共享元素，因为跳转和返回都会调用，需要判断bundle是否为空
                                setExitSharedElementCallback(new SharedElementCallback() {
                                    @Override
                                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                                        if (bundle!=null){
                                            int index = bundle.getInt(ImagePagerActivity.STATE_POSITION,0);
                                            sharedElements.clear();
                                            sharedElements.put("img", mecomm_recycler.getLayoutManager().findViewByPosition(index));
                                            bundle=null;
                                        }
                                    }
                                });

                                mecommGrid.setList(imglist);
                                mecommGrid.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        ScaleImageView scaleImageView = new ScaleImageView(MeCommentActivity.this);
                                        scaleImageView.setUrls(imglist, position);
                                        scaleImageView.create();

                                    }
                                });
                            }

                        }catch (Exception e){

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
    //返回的时候获取数据
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        bundle = data.getExtras();
        int currentPosition = bundle.getInt(ImagePagerActivity.STATE_POSITION,0);
        //做相应的滚动
        mecomm_recycler.scrollToPosition(currentPosition);
        //暂时延迟 Transition 的使用，直到我们确定了共享元素的确切大小和位置才使用
        //postponeEnterTransition后不要忘记调用startPostponedEnterTransition
        ActivityCompat.postponeEnterTransition(this);
        mecomm_recycler.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mecomm_recycler.getViewTreeObserver().removeOnPreDrawListener(this);
                // TODO: figure out why it is necessary to request layout here in order to get a smooth transition.
                mecomm_recycler.requestLayout();
                //共享元素准备好后调用startPostponedEnterTransition来恢复过渡效果
                ActivityCompat.startPostponedEnterTransition(MeCommentActivity.this);
                return true;
            }
        });
    }

    private void initInfoImages(MyGridView mecommGrid, List<String> imglist) {
        int w = 0;
        switch (imglist.size()) {
            case 1:
                w=wh;
                mecommGrid.setNumColumns(1);
                break;
            case 2:
            case 4:
                w=2*wh+ SysUtils.Dp2Px(this, 2);
                mecommGrid.setNumColumns(2);
                break;
            case 3:
            case 5:
            case 6:
                w=wh*3+SysUtils.Dp2Px(this, 2)*2;
                mecommGrid.setNumColumns(3);
                break;
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, LinearLayout.LayoutParams.WRAP_CONTENT);
        mecommGrid.setLayoutParams(params);
        nearByInfoImgsAdapter=new TestGridViewAdapter(this, imglist);
        mecommGrid.setAdapter(nearByInfoImgsAdapter);
    }


    @OnClick({R.id.title_back_fl, R.id.title_review_img,R.id.edit_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_review_img:  //修改评论
                Intent intent1 = new Intent(MeCommentActivity.this, WriteEditActivity.class);
                intent1.putExtra("commentId",commentId);
                startActivity(intent1);
                break;
            case R.id.edit_name:
                SPUtil.put(MeCommentActivity.this, Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_MY_REVIEW);
                Intent intent = new Intent(MeCommentActivity.this, StoreDetailActivity.class);
                int shopId = list.get(0).getData().getShopId();
                intent.putExtra("id",shopId);
                intent.putExtra("comment",1);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getMecomment();
    }
}
