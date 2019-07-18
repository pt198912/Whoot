package com.app.whoot.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.bean.TouristsBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * Created by Sunrise on 4/25/2019.
 */

public class WindowActivity extends Activity {


    @BindView(R.id.title_window)
    TextView titleWindow;
    @BindView(R.id.message_window)
    TextView messageWindow;
    @BindView(R.id.content_window)
    LinearLayout contentWindow;
    @BindView(R.id.windowButton_window)
    Button negativeButtonWindow;
    private List<TouristsBean> list = new ArrayList<>();
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_dialog);
        ButterKnife.bind(this);
        url = (String) SPUtil.get(this, "URL", "");
        SPUtil.remove(this,"Token");
        SPUtil.remove(this,"accToken");
    }

  /*  @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }*/
    @Override
    public void onBackPressed() {
        SPUtil.remove(WindowActivity.this,"accToken");
        SPUtil.remove(WindowActivity.this,"Token");

    }
    @OnClick({R.id.windowButton_window})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.windowButton_window:
                Intent intent1 = new Intent(this, LoginMeActivity.class);
                intent1.putExtra("wind_flag",1);
                SPUtil.remove(WindowActivity.this,"accToken");
                SPUtil.remove(WindowActivity.this,"Token");
                startActivity(intent1);
                finish();
                break;
        }
    }
}
