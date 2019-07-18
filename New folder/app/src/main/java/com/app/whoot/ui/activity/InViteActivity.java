package com.app.whoot.ui.activity;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.DemoAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.InviteBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.ui.view.dialog.InviteDialog;
import com.app.whoot.ui.view.dialog.ReferralDialog;
import com.app.whoot.util.CopyButtonLibrary;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.app.whoot.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;

/**
 * 邀请有礼
 */
public class InViteActivity extends BaseActivity {


    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTxt;
    @BindView(R.id.invite_help)
    ImageView inviteHelp;
    @BindView(R.id.invite_txt)
    TextView inviteTxt;
    @BindView(R.id.invite_img)
    ImageView inviteImg;
    @BindView(R.id.invite_my)
    TextView inviteMy;
    @BindView(R.id.invite_token)
    TextView inviteToken;
    @BindView(R.id.invite_ly)
    LinearLayout invite_ly;
    @BindView(R.id.invite_recyc)
    RecyclerView invite_recyc;
    @BindView(R.id.invite_men)
    TextView inviteMen;
    @BindView(R.id.invite_Diamond)
    TextView inviteDiamond;
    @BindView(R.id.invite_Gold)
    TextView inviteGold;
    @BindView(R.id.invite_Silver)
    TextView inviteSilver;
    @BindView(R.id.invite_to)
    ImageView invite_to;

    public static final String SETTINGS_ACTION =
            "android.settings.APPLICATION_DETAILS_SETTINGS";

    private ReferralDialog dialog;
    private String url;
    private String inviteCode;
    private String Luch = "";
    private List<InviteBean> mEntityList = new ArrayList();
    private InviteDialog dialogIn;

    @Override
    public int getLayoutId() {
        return R.layout.invite_activi;
    }

    @Override
    public void onCreateInit() {
        titleBackTxt.setText(getResources().getString(R.string.invite_li));
        url = (String) SPUtil.get(this, "URL", "");
        isHistory();
        String luchang = (String) SPUtil.get(this, "luchang", "");
        boolean zh = TimeUtil.isZh(this);
        if (luchang.equals("0")) {
            if (zh) {
                luchang = "3";
            } else {
                luchang = "2";
            }
        }
        if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
            Luch = "tc";
            invite_to.setBackgroundResource(R.drawable.invitation_form_cn);
        } else if (luchang.equals("3")) {
            Luch = "en";
            invite_to.setBackgroundResource(R.drawable.invitation_form_en);
        }
        // 定义一个线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this);
        // 设置布局管理器
        invite_recyc.setLayoutManager(manager);
    }
    private List<String> mName=new ArrayList<>();
    private List<String> mCode=new ArrayList<>();
    public boolean isPowerOfTwo(int n) {
        if(n <= 0)  return false;
        return (n &= (n-1)) == 0;
        //位运算，如果是2的n次方，转化成二进制是10000...的形式，所以它与上-1的数为0
    }

    @OnClick({R.id.invite_help, R.id.invite_txt, R.id.invite_img, R.id.invite_my, R.id.invite_token, R.id.title_back_fl, R.id.invite_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.invite_help:
                break;
            case R.id.invite_txt:
                ToastUtil.showgravity(this,getResources().getString(R.string.invite_copy));
                CopyButtonLibrary library = new CopyButtonLibrary(this, inviteTxt);
                library.init();
                boolean notificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled();
                if (notificationsEnabled == false) {
                    boolean inviteFlag = (boolean) SPUtil.get(InViteActivity.this, "Invite_flag", false);
                    if (inviteFlag) {
                        CopyButtonLibrary library1 = new CopyButtonLibrary(this, inviteTxt);
                        library1.init();

                    } else {
                        dialogIn = new InviteDialog(InViteActivity.this,R.layout.invite_dingdiglog,"7000",onClickListener);
                        dialogIn.show();
                    }
                }
                reportPageCopy();
                break;
            case R.id.invite_img:
                ToastUtil.showgravity(this,getResources().getString(R.string.invite_copy));
                CopyButtonLibrary library1 = new CopyButtonLibrary(this, inviteTxt);
                library1.init();
                boolean notifications = NotificationManagerCompat.from(this).areNotificationsEnabled();
                if (notifications == false) {
                    boolean inviteFlag = (boolean) SPUtil.get(InViteActivity.this, "Invite_flag", false);
                    if (inviteFlag) {

                    } else {
                        dialogIn = new InviteDialog(InViteActivity.this,R.layout.invite_dingdiglog,"7000",onClickListener);
                        dialogIn.show();
                    }
                }//d6nhyy
                reportPageCopy();
                break;
            case R.id.invite_my:
                boolean notificat = NotificationManagerCompat.from(this).areNotificationsEnabled();
                if (notificat == false) {
                    boolean inviteFlag = (boolean) SPUtil.get(InViteActivity.this, "Invite_flag", false);
                    if (inviteFlag) {

                    } else {
                        dialogIn = new InviteDialog(InViteActivity.this,R.layout.invite_dingdiglog,"7000",onClickListener);
                        dialogIn.show();
                    }
                }

                reportPageShare();
                andro();
                break;
            case R.id.invite_token:
                EventBusCarrier carrier = new EventBusCarrier();
                TokenExchangeBean bean = new TokenExchangeBean();
                bean.setSuToken(2);
                bean.setFlagToken(1);
                bean.setFlagCoupn(1);
                carrier.setEventType(EVENT_TYPE_SIGN);
                carrier.setObject(bean);
                EventBus.getDefault().post(carrier);
                finish();
                reportPageToken();
                break;
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.invite_ly:
                dialog = new ReferralDialog(InViteActivity.this, R.style.box_dialog, onClickListener);
                dialog.show();
                reportPageHelp();
                break;
        }
    }

    private void reportPageToken() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_MYTOKEN");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.REFER_MYTOKEN,b);
    }

    private void reportPageShare() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_INVITE_SHARE");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.REFER_SHARE,b);
    }

    private void reportPageCopy() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_INVITE_COPY");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.REFER_COPY,b);
    }

    //埋点
    private void reportPageHelp() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_INVITE_HELP");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.REFER_HELP,b);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.vox_finsh:  //确认
                    dialog.dismiss();
                    break;
                case R.id.invie_positiveButton:  //不在提醒
                    SPUtil.put(InViteActivity.this, "Invite_flag", true);
                    dialogIn.dismiss();
                    break;
                case R.id.invie_negativeButton:  //去设定
                    goSet();
                    dialogIn.dismiss();
                    break;
            }
        }
    };

    /**
     * 分享
     */
    public void andro() {


        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        for (int i = 0; i < 3; i++) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.invite_fen) +inviteCode+"  "+ getResources().getString(R.string.invite_lii) +
                    "\n" + Html.fromHtml(UrlUtil.INVITE) + Luch + "&code=" + inviteCode);
            intent.setType("text/plain");

            //intent.setType("image/jpeg");
            //intent.putExtra(Intent.EXTRA_TITLE, "标题");
            //intent.putExtra(Intent.EXTRA_SUBJECT, "内容");
            if (i == 0) {
                // intent.setPackage("com.whatsapp");
                intent.setClassName("com.whatsapp", "com.whatsapp.ContactPicker");
            } else if (i == 1) {
                intent.setPackage("com.facebook.orca");
            } else if (i == 2) {
                intent.setPackage("com.tencent.mm");
            }
            targetedShareIntents.add(intent);
        }
        Intent chooserIntent = null;
        try {
            chooserIntent = Intent.createChooser(Intent.getIntent(""), getResources().getString(R.string.invite_get));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (chooserIntent == null) {
            return;
        }
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

        try {
            startActivity(chooserIntent);
        } catch (ActivityNotFoundException ex) {
            // Toast.makeText(this, "Can't find share component to share", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 我的邀请码
     */
    private void isHistory() {
        Http.OkHttpGet(InViteActivity.this, url + UrlUtil.History, new Http.OnDataFinish() {

            private JSONArray userInfoVoList;
            private int invint = 0;
            private int length=0;

            @Override
            public void OnSuccess(String result) {
                Log.d("invite_code", result + url + UrlUtil.History);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("code").equals("0")) {

                        reportPageShown();

                        JSONObject data = object.getJSONObject("data");
                        inviteCode = data.getString("inviteCode");
                        inviteTxt.setText(inviteCode);
                        try {
                            userInfoVoList = data.getJSONArray("userInfoVoList");
                            length= userInfoVoList.length();
                        } catch (Exception e) {
                            length = 0;
                        }


                        if (length < 2) {
                            inviteDiamond.setText("0");
                            inviteGold.setText("0");
                            inviteSilver.setText("0");
                        } else if (length >= 2 && length < 4) {
                            inviteDiamond.setText("0");
                            inviteGold.setText("1");
                            inviteSilver.setText("0");
                        } else if (length >= 4 && length < 6) {
                            inviteDiamond.setText("0");
                            inviteGold.setText("2");
                            inviteSilver.setText("1");
                        } else if (length >= 6 && length < 8) {
                            inviteDiamond.setText("0");
                            inviteGold.setText("4");
                            inviteSilver.setText("1");
                        } else if (length >= 8 && length < 10) {
                            inviteDiamond.setText("0");
                            inviteGold.setText("6");
                            inviteSilver.setText("2");
                        } else if (length >= 10) {
                            inviteDiamond.setText("1");
                            inviteGold.setText("8");
                            inviteSilver.setText("2");
                        }
                        inviteMen.setText(length + "");
                        if (length!=0){
                            for (int i = 0; i < userInfoVoList.length(); i++) {
                                InviteBean gson = GSonUtil.parseGson(userInfoVoList.getString(i), InviteBean.class);
                                mEntityList.add(gson);
                            }

                            // 设置adapter
                            DemoAdapter adapter = new DemoAdapter(InViteActivity.this, mEntityList);
                            invite_recyc.setAdapter(adapter);
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

    private void reportPageShown() {
        Bundle b=new Bundle();
        b.putString("type","SHOW_INVITE");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.SHOW_ME_REFER,b);
    }

    /**
     * 进入设置界面
     */
    private void goToSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        }
    }

    /**
     * 应用设置界面
     */
    private void goSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            Intent intent = new Intent()
                    .setAction(SETTINGS_ACTION)
                    .setData(Uri.fromParts("package",
                            getApplicationContext().getPackageName(), null));
            startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent()
                    .setAction(SETTINGS_ACTION)
                    .setData(Uri.fromParts("package",
                            getApplicationContext().getPackageName(), null));
            startActivity(intent);
            return;
        }
    }
}
