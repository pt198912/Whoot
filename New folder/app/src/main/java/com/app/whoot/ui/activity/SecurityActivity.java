package com.app.whoot.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.ui.view.dialog.InviteDialog;
import com.app.whoot.ui.view.popup.LoginPopupWindow;
import com.app.whoot.ui.view.popup.UpdatePhonePopupWindow;
import com.app.whoot.util.PopupWindowUtil;
import com.app.whoot.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 账号安全
 * */
public class SecurityActivity extends BaseActivity implements UpdatePhonePopupWindow.OnItemClickListener {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.secu_bound)
    TextView secuBound;
    @BindView(R.id.secu_security)
    LinearLayout secuSecurity;
    @BindView(R.id.secu_exchange)
    LinearLayout secuExchange;
    @BindView(R.id.secu_pswa)
    TextView secu_pswa;
    @BindView(R.id.secu_iset)
    TextView secu_iset;
    private UpdatePhonePopupWindow popup;
    private String phone;
    private InviteDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.secur_activi;
    }

    @Override
    public void onCreateInit() {

        titleBackTitle.setText(getResources().getString(R.string.account_se));
    }


    @OnClick({R.id.title_back_fl, R.id.secu_security, R.id.secu_exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.secu_security:
                if (phone.length()>0){
                    popup = new UpdatePhonePopupWindow(SecurityActivity.this,phone);
                    popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            PopupWindowUtil.setWindowTransparentValue(SecurityActivity.this, 1.0f);
                        }
                    });
                    PopupWindowUtil.setWindowTransparentValue(SecurityActivity.this, 0.5f);
                    //设置PopupWindow中的位置
                    popup.showAtLocation(SecurityActivity.this.findViewById(R.id.secur_ly), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    popup.setOnItemClickListener(this);
                }else {
                    Intent intent = new Intent(SecurityActivity.this, RegisterActivity.class);
                    intent.putExtra("Forget",4);
                    startActivity(intent);
                }
                break;
            case R.id.secu_exchange:
                boolean bindpwd = (boolean) SPUtil.get(this, "bindpwd", false);
                if (bindpwd){
                    Intent intent = new Intent(SecurityActivity.this, ChangeActivity.class);
                    startActivity(intent);
                }else {
                    if (phone.length()>0){
                        Intent intent = new Intent(SecurityActivity.this, ChangeActivity.class);
                        intent.putExtra("set_password",1);
                        startActivity(intent);
                    }else { //未绑定手机号码
                        dialog = new InviteDialog(SecurityActivity.this,R.layout.invite_dingdiglog,"6000",onClickListener);
                        dialog.show();
                    }
                }

                break;
        }
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.invie_positiveButton:
                    dialog.dismiss();
                    break;
                case R.id.invie_negativeButton:
                    Intent intent = new Intent(SecurityActivity.this, RegisterActivity.class);
                    intent.putExtra("Forget",4);
                    startActivity(intent);
                    dialog.dismiss();
                    break;

            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        phone= (String) SPUtil.get(this,"you_tel","");
        if (phone.length()>0){
            secuBound.setVisibility(View.INVISIBLE);
        }else {
            secuBound.setVisibility(View.VISIBLE);
        }
        boolean bindpwd = (boolean) SPUtil.get(this, "bindpwd", false);
        if (bindpwd){
            secu_pswa.setText(getResources().getString(R.string.change));
            secu_iset.setText("");
        }else {
            secu_pswa.setText(getResources().getString(R.string.invite_log));
            secu_iset.setText(getResources().getString(R.string.invite_setin));
        }
    }

    @Override
    public void setOnItemClick(View v) {
                switch (v.getId()){
                    case R.id.update_pop_out:   //更改
                        Intent intent = new Intent(SecurityActivity.this, RegisterActivity.class);
                        intent.putExtra("Forget",3);
                        startActivity(intent);
                        popup.dismiss();
                        break;
                    case R.id.update_pop_cr:
                        popup.dismiss();
                        break;
                }
    }

}
