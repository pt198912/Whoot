package com.app.whoot.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.ui.activity.AboutActivity;
import com.app.whoot.ui.activity.HelpActivity;
import com.app.whoot.ui.activity.LuchangeActivity;
import com.app.whoot.ui.activity.SettingActivity;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.view.dialog.ClearlDialog;
import com.app.whoot.ui.view.dialog.CreaDialog;
import com.app.whoot.ui.view.popup.LoginPopupWindow;
import com.app.whoot.util.DataCleanManager;
import com.app.whoot.util.PopupWindowUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.UrlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sunrise on 1/2/2019.
 */

public class MoreFragment extends BaseFragment {

    @BindView(R.id.gift_ly)
    LinearLayout giftLy;
    @BindView(R.id.exchange)
    LinearLayout exchange;
    @BindView(R.id.set_help)
    LinearLayout setHelp;
    @BindView(R.id.set_about)
    LinearLayout setAbout;
    @BindView(R.id.set_fuwu)
    LinearLayout setFuwu;
    @BindView(R.id.set_yinsi)
    LinearLayout setYinsi;
    @BindView(R.id.set_clear)
    TextView setClear;
    @BindView(R.id.set_clear_ly)
    LinearLayout setClearLy;

    private ClearlDialog creaDia;
    private CreaDialog creaDialog;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int arg1 = msg.arg1;


            boolean destroyed = getActivity().isFinishing();
            if (!destroyed){
                setClear.setText("");
                creaDia = new ClearlDialog(getActivity(), R.style.box_dialog);
                creaDia.show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        Looper.prepare();
                        try {

                            Thread.sleep(1000);
                            creaDia.dismiss();


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }.start();
            }


        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragm_more;
    }

    @Override
    public void onViewCreatedInit() {

        try {
            String cacheSize = DataCleanManager.getTotalCacheSize(getContext());
            if (cacheSize.equals("0K")) {
                setClear.setText("");
            } else {
                setClear.setText(cacheSize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStartInit() {

    }

    @OnClick({R.id.exchange, R.id.set_help, R.id.set_about, R.id.set_clear_ly,R.id.set_fuwu,R.id.set_yinsi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exchange:
                Intent intent2 = new Intent(getActivity(), LuchangeActivity.class);
                startActivity(intent2);
                break;
            case R.id.set_help:
                Intent intent1 = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent1);
                break;
            case R.id.set_about:  //关于
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.set_clear_ly:

                creaDialog = new CreaDialog(getActivity(), R.style.box_dialog);
                creaDialog.show();
                DataCleanManager.clearAllCache(getActivity());
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Looper.prepare();
                        try {
                            Thread.sleep(3000);
                            creaDialog.dismiss();

                            Message message = handler.obtainMessage(3);
                            handler.sendMessage(message);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }.start();
                /*ClearlDialog creaDia= new ClearlDialog(this, R.style.box_dialog);
                creaDia.show();*/

                break;

            case R.id.set_fuwu:
                Intent intent3 = new Intent(getActivity(), WebViewActivity.class);
                intent3.putExtra("html", 2);

                startActivity(intent3);
                break;
            case R.id.set_yinsi:
                Intent intent4 = new Intent(getActivity(), WebViewActivity.class);
                intent4.putExtra("html", 1);
                startActivity(intent4);
                break;
        }
    }
}
