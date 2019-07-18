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
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.util.Constants;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.app.whoot.util.Constants.SP_KEY_KOL_JOIN_FROM;

/**
 * Created by Sunrise on 8/13/2018.
 */

public class LoginCoupDialog extends Dialog {
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
    public LoginCoupDialog(Activity context) {
        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        this.context = context;
    }

    /**
     *
     * @param context
     * @param closeIvListener 自定义关闭按钮事件
     */
    public LoginCoupDialog(View.OnClickListener closeIvListener,Activity context) {
        //使用自定义Dialog样式
        super(context, R.style.box_dialog);
        this.context = context;
        this.mCloseIvListener=closeIvListener;
    }

    public LoginCoupDialog(Activity context,View.OnClickListener clickListener) {
        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        this.context = context;
        this.mClickListener = clickListener;
    }
    public LoginCoupDialog(Activity context, int theme, View.OnClickListener clickListener) {
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

        int my_tong = (int) SPUtil.get(context, "My_tong", 0);
        int My_yin = (int) SPUtil.get(context, "My_yin", 0);
        int My_jin = (int) SPUtil.get(context, "My_jin", 0);
        int My_zuan = (int) SPUtil.get(context, "My_zuan", 0);
        String brozenDesc=(String)SPUtil.get(context, "brozen_desc", "");
        String silverDesc=(String)SPUtil.get(context, "silver_desc", "");
        String goldDesc=(String)SPUtil.get(context, "gold_desc", "");
        String diamondDesc=(String)SPUtil.get(context, "diamond_desc", "");


        mShowEvent= Util.isEventStarted(ConfigureInfoManager.getInstance().getLastServerTime());
        if(mShowEvent){
            if (My_yin==0&&My_jin==0&&My_zuan==0){
                mJoinEventTv.setVisibility(View.GONE);
                login_box_ly.setBackgroundResource(R.drawable.bg_red_solid_radius_20);
                login_box_ly.setTextColor(getContext().getResources().getColor(R.color.white));
            }else {
                mJoinEventTv.setVisibility(View.VISIBLE);
                login_box_ly.setBackgroundResource(R.drawable.bg_red_radius_20);
                login_box_ly.setTextColor(getContext().getResources().getColor(R.color.color_fc6036));
            }
        }else{
            mJoinEventTv.setVisibility(View.GONE);
            login_box_ly.setBackgroundResource(R.drawable.bg_red_solid_radius_20);
            login_box_ly.setTextColor(getContext().getResources().getColor(R.color.white));
        }
        if (my_tong==0){
            login_coup_o.setVisibility(View.GONE);
        }else {
            login_coup_o.setVisibility(View.VISIBLE);
            login_coup_txt.setText("+"+my_tong);
            brozenDescTv.setText(brozenDesc);
        }
        if (My_yin==0){
            login_type_ly_one.setVisibility(View.GONE);
        }else {
            login_type_ly_one.setVisibility(View.VISIBLE);
            login_coup_txt_one.setText("+"+My_yin);
            silverDescTv.setText(silverDesc);
//            login_coup_txt_one.setText(Html.fromHtml(context.getResources().getString(R.string.Silver)+"<font color='#FC4A1A'> x"+My_yin+"</font>"));
        }
        if (My_jin==0){
            login_type_ly_twe.setVisibility(View.GONE);
        }else {
            login_type_ly_twe.setVisibility(View.VISIBLE);
            login_coup_txt_twe.setText("+"+My_jin);
            goldDescTv.setText(goldDesc);
//            login_coup_txt_twe.setText(Html.fromHtml(context.getResources().getString(R.string.Gold)+"<font color='#FC4A1A'> x"+My_jin+"</font>"));
        }
     /*   if(my_tong!=0&&My_jin!=0){
            login_coup_txt.setBackgroundResource(R.drawable.bg_red_radius_20);
            login_coup_txt.setTextColor(getContext().getResources().getColor(R.color.color_fc6036));
        }else if(my_tong!=0){
            login_coup_txt.setBackgroundResource(R.drawable.boxc_shape);
            login_coup_txt.setTextColor(getContext().getResources().getColor(R.color.white));
        }*/
        if (My_zuan==0){
            login_type_ly_three.setVisibility(View.GONE);
        }else {
            login_type_ly_three.setVisibility(View.VISIBLE);
            login_coup_txt_three.setText("+"+My_zuan);
            diamondDescTv.setText(diamondDesc);
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
                if(my_tong!=0){
                    list.add(Constants.TYPE_COUPON_GAIN_BROZEN);
                }
                if(My_yin!=0){
                    list.add(Constants.TYPE_COUPON_GAIN_SILVER);
                }
                if(My_jin!=0){
                    list.add(Constants.TYPE_COUPON_GAIN_GOLD);
                }
                if(My_zuan!=0){
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
                Bundle b=new Bundle();
                b.putString("type","POP_ACTIVITY");
                b.putString("activity","KOL");
                String kolJoinFrom=(String)SPUtil.get(context, SP_KEY_KOL_JOIN_FROM, "");
                b.putString("from",kolJoinFrom);
                FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.KOL_KOL_POPUP_JOIN,b);
            }
        });
       // this.setCancelable(true);
    }
}
