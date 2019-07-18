package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.GainTokenBean;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.util.Constants;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunrise on 8/13/2018.
 */

public class FcmCoupDialog extends Dialog {
    private String fcminviteCount;
    private String fcmname;
    private String fcmdiamond="0";
    private  String Tokensplit;
    /**
     * 上下文对象 *
     */
    Activity context;

    private TextView login_box_ly;
    private TextView mJoinEventTv;


    private View.OnClickListener mClickListener;
    private ImageButton vox_finsh;
    private ImageButton login_finsh;
    private boolean mShowEvent;
    private View.OnClickListener mCloseIvListener;
    private String silver="0";
    private String gold="0";

    public FcmCoupDialog(Activity context) {
        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        this.context = context;
    }

    /**
     *
     * @param context
     * @param closeIvListener 自定义关闭按钮事件
     */
    public FcmCoupDialog(View.OnClickListener closeIvListener, Activity context) {
        //使用自定义Dialog样式jxmhyy
        super(context, R.style.box_dialog);
        this.context = context;
        this.mCloseIvListener=closeIvListener;
    }

    public FcmCoupDialog(Activity context, String Tokensplit,String fcmname,String fcminviteCount,View.OnClickListener clickListener) {
        //使用自定义Dialog样式  String Tokensplit,String fcmname,String fcminviteCount,
        super(context, R.style.custom_dialog);
        this.context = context;
        this.mClickListener = clickListener;
        this.Tokensplit=Tokensplit;
        this.fcmname=fcmname;
        this.fcminviteCount=fcminviteCount;
    }
    public FcmCoupDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.login_t_dialog);


        login_box_ly = findViewById(R.id.login_box_ly);
        mJoinEventTv=findViewById(R.id.tv_join_event);
        login_finsh = findViewById(R.id.login_finsh);
        LinearLayout login_coup_o = findViewById(R.id.login_coup_o);
        ImageView login_coup_img = findViewById(R.id.login_coup_img);
        TextView login_coup_txt = findViewById(R.id.login_coup_txt);
        ImageView login_coup_img_one = findViewById(R.id.login_coup_img_one);
        LinearLayout login_type_ly_one = findViewById(R.id.login_type_ly_one);
        TextView login_coup_txt_one = findViewById(R.id.login_coup_txt_one);
        LinearLayout login_type_ly_twe = findViewById(R.id.login_type_ly_twe);
        ImageView login_coup_img_twe = findViewById(R.id.login_coup_img_twe);
        TextView login_coup_txt_twe = findViewById(R.id.login_coup_txt_twe);
        LinearLayout login_type_ly_three = findViewById(R.id.login_type_ly_three);
        ImageView login_coup_img_three = findViewById(R.id.login_coup_img_three);
        TextView login_coup_txt_three = findViewById(R.id.login_coup_txt_three);
        TextView brozenDescTv=findViewById(R.id.tv_brozen_desc);
        TextView silverDescTv=findViewById(R.id.tv_silver_desc);
        TextView goldDescTv=findViewById(R.id.tv_gold_desc);
        TextView diamondDescTv=findViewById(R.id.tv_diamond_desc);
        TextView login_type_text = findViewById(R.id.login_type_text);
        //Tokensplit="3=3=3";
        String[] split = Tokensplit.split("=");
        int length = split.length;
        if (length==1){
            silver = split[0];
        }
        if (length==2){
            silver = split[0];
            gold = split[1];

        }
        if (length==3){
            silver = split[0];
            gold = split[1];
            fcmdiamond = split[2];
        }
        if (silver.equals("")){
            silver="0";
        }
        if (gold.equals("")){
            gold="0";
        }
        if (fcmdiamond.equals("")){
            fcmdiamond="0";
        }

         login_type_text.setText(Html.fromHtml(context.getResources().getString(R.string.fcm_one)+" <font color='#FC4A1A'>"+fcminviteCount+" </font>"+context.getResources().getString(R.string.fcm_three)));


        mShowEvent= Util.isEventStarted(ConfigureInfoManager.getInstance().getLastServerTime());
        if(mShowEvent){
            mJoinEventTv.setVisibility(View.VISIBLE);
            login_box_ly.setBackgroundResource(R.drawable.bg_red_radius_20);
            login_box_ly.setTextColor(getContext().getResources().getColor(R.color.color_fc6036));
        }else{
            mJoinEventTv.setVisibility(View.GONE);
            login_box_ly.setBackgroundResource(R.drawable.bg_red_solid_radius_20);
            login_box_ly.setTextColor(getContext().getResources().getColor(R.color.white));
        }
     /*   if (my_tong==0){
            login_coup_o.setVisibility(View.GONE);
        }else {
            login_coup_o.setVisibility(View.VISIBLE);
            login_coup_txt.setText("+"+my_tong);
            brozenDescTv.setText(brozenDesc);
        }*/
        if (Integer.parseInt(silver)==0){
            login_type_ly_one.setVisibility(View.GONE);
        }else {
            login_type_ly_one.setVisibility(View.VISIBLE);
            login_coup_txt_one.setText("+"+silver);
          //  silverDescTv.setText(silverDesc);
//            login_coup_txt_one.setText(Html.fromHtml(context.getResources().getString(R.string.Silver)+"<font color='#FC4A1A'> x"+My_yin+"</font>"));
        }

        if (Integer.parseInt(gold)==0){
            login_type_ly_twe.setVisibility(View.GONE);
        }else {
            login_type_ly_twe.setVisibility(View.VISIBLE);
            login_coup_txt_twe.setText("+"+gold);
           // goldDescTv.setText(goldDesc);
//            login_coup_txt_twe.setText(Html.fromHtml(context.getResources().getString(R.string.Gold)+"<font color='#FC4A1A'> x"+My_jin+"</font>"));
        }

        if (Integer.parseInt(fcmdiamond)==0){
            login_type_ly_three.setVisibility(View.GONE);
        }else {
            login_type_ly_three.setVisibility(View.VISIBLE);
            login_coup_txt_three.setText("+"+fcmdiamond);
           // diamondDescTv.setText(diamondDesc);
//            login_coup_txt_three.setText(Html.fromHtml(context.getResources().getString(R.string.Diamond)+"<font color='#FC4A1A'> x"+My_zuan+"</font>"));
        }
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        login_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mCloseIvListener!=null){
                    mCloseIvListener.onClick(v);
                }
            }
        });
       /* login_box_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });*/
        // 为按钮绑定点击事件监听器

        login_box_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainPage=new Intent(getContext(),MainActivity.class);
                mainPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(mainPage);
                GainTokenBean bean = new GainTokenBean();
                List<Integer> list=new ArrayList<>();
               /* if(my_tong!=0){
                    list.add(Constants.TYPE_COUPON_GAIN_BROZEN);
                }*/
                if(Integer.parseInt(silver)!=0){
                    list.add(Constants.TYPE_COUPON_GAIN_SILVER);
                }
                if(Integer.parseInt(gold)!=0){
                    list.add(Constants.TYPE_COUPON_GAIN_GOLD);
                }
                if(Integer.parseInt(fcmdiamond)!=0){
                    list.add(Constants.TYPE_COUPON_GAIN_DIAMOND);
                }
                bean.setAnimTypeList(list);
                EventBusCarrier carrier=new EventBusCarrier();
                carrier.setEventType(Constants.EVENT_TYPE_GAIN_TOKEN);
                carrier.setObject(bean);
                EventBus.getDefault().post(carrier);
                dismiss();
            }
        });
        mJoinEventTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webViewIntent=new Intent(getContext(), WebViewActivity.class);
                webViewIntent.putExtra("html",7);
                getContext().startActivity(webViewIntent);
            }
        });
       // this.setCancelable(true);
    }
}
