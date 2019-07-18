package com.app.whoot.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.Pickers;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.custom.ClearEditText;
import com.app.whoot.ui.view.custom.PickerScrollView;
import com.app.whoot.ui.view.popup.KeyboardPopupWindow;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.KeyboardUtils;
import com.app.whoot.util.NoFastClickUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.app.whoot.ui.view.custom.PickerScrollView.onSelectListener;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.title_back_shou)
    ImageView titleBackShou;
    @BindView(R.id.title_back_no)
    ImageView titleBackNo;
    @BindView(R.id.title_back_img)
    ImageView titleBackImg;
    @BindView(R.id.title_del_img)
    ImageView titleDelImg;
    @BindView(R.id.title_review_img)
    ImageView titleReviewImg;
    @BindView(R.id.regist_code)
    TextView registCode;
    @BindView(R.id.regist_number)
    ClearEditText registNumber;
    @BindView(R.id.regist_next)
    TextView registNext;
    @BindView(R.id.regist_emil)
    TextView registEmil;

    private boolean delete = false;
    private static int sLastLength;
    private int forget;
    private String url;
    private String languageType;
    private PickerScrollView pickerscrlllview; // 滚动选择器
    private RelativeLayout picker_rel; // 选择器布局
    private TextView picker_yes;
    private List<Pickers> list; // 滚动选择器数据
    private String[] id;
    private String[] name;
    private String Code_number;
    public static RegisterActivity instance;
    private TextView picker_no;
    private boolean isUiCreated = false;
    private KeyboardPopupWindow window;
    private int card_flag;
    private int my_flag;

    @Override
    public int getLayoutId() {
        return R.layout.regist_activi;
    }

    @Override
    public void onCreateInit() {
        instance=this;
        registNext.getBackground().setAlpha(100);
        url = (String) SPUtil.get(this, "URL", "");
        Intent intent = getIntent();
        forget = intent.getIntExtra("Forget", 0);
        card_flag = intent.getIntExtra("card_flag", 0);
        my_flag = intent.getIntExtra("my_flag", 0);
        if (forget ==1){
            titleBackTitle.setText(getResources().getString(R.string.forget_pas));
            registEmil.setText(getResources().getString(R.string.use_email));
        }else if (forget ==2){
            titleBackTitle.setText(getResources().getString(R.string.sign));
            registEmil.setText(getResources().getString(R.string.email_use));
        }else if (forget==3){
            titleBackTitle.setText(getResources().getString(R.string.phone_number));
            registEmil.setVisibility(View.INVISIBLE);
        }else if (forget==4){
            titleBackTitle.setText(getResources().getString(R.string.phone_number));
            registEmil.setVisibility(View.INVISIBLE);
        }
        picker_rel = (RelativeLayout) findViewById(R.id.picker_rel);
        pickerscrlllview = (PickerScrollView) findViewById(R.id.pickerscrlllview);
        picker_yes = findViewById(R.id.picker_yes);
        picker_no = findViewById(R.id.picker_no);
        initLinstener();
        initData();

        String  luchang= (String) SPUtil.get(this, "luchang", "");
        boolean zh = TimeUtil.isZh(this);
        if (luchang.equals("0")){
            if (zh){
                luchang="3";
            }else {
                luchang="2";
            }
        }
        if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
            languageType="1";
        }else if (luchang.equals("3")){
            languageType="0";
        }
        if (registNumber.getText().toString().trim().length()>0){
            registNext.setClickable(true);
            registNext.getBackground().setAlpha(255);
        }else {
            registNext.setClickable(false);
            registNext.getBackground().setAlpha(100);
        }
        window = new KeyboardPopupWindow(RegisterActivity.this, getWindow().getDecorView(), registNumber, false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (window !=null){
                    window.show();
                }
            }
        },500);
        registNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picker_rel!=null&&picker_rel.isShown()){
                    picker_rel.setVisibility(View.GONE);
                }
                if (window !=null){
                    window.show();
                }
            }
        });
        registNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (window != null && isUiCreated) {//很重要，Unable to add window -- token null is not valid; is your activity running?
                    window.refreshKeyboardOutSideTouchable(!hasFocus);// 需要等待页面创建完成后焦点变化才去显示自定义键盘
                }

                if (hasFocus) {//隐藏系统软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(registNumber.getWindowToken(), 0);
                }
            }
        });

        registNumber.addTextChangedListener(new TextWatcher() {

            private int oldLength = 0;
            private boolean isChange = true;
            private int curLength = 0;
            private int emptyNumB = 0;  //初始空格数
            private int emptyNumA = 0;  //遍历添加空格后的空格数

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldLength = s.length();
                Log.i(TAG, "未改变长度: " + oldLength);
                emptyNumB = 0;
                for (int i = 0; i < s.toString().length(); i++) {
                    if (s.charAt(i) == ' ') emptyNumB++;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d(TAG,"一次性输入的字符"+count);
                curLength = s.length();
                Log.i(TAG, "当前长度: " + curLength);
                if (Code_number.equals("852")){
                    if (curLength==9){
                        registNext.setClickable(true);
                        registNext.getBackground().setAlpha(255);
                    }else {
                        registNext.setClickable(false);
                        registNext.getBackground().setAlpha(100);
                    }
                    if (curLength>9){
                        registNumber.setText(s.subSequence(0,9));
                        registNumber.setSelection(9);
                    }
                }else if (Code_number.equals("86")){
                    if (curLength==13){
                        registNext.setClickable(true);
                        registNext.getBackground().setAlpha(255);
                    }else {
                        registNext.setClickable(false);
                        registNext.getBackground().setAlpha(100);
                    }
                    if (curLength>13){
                        registNumber.setText(s.subSequence(0,13));
                        registNumber.setSelection(13);
                    }
                }

                //优化处理,如果长度未改变或则改变后长度小于3就不需要添加空格
                if (curLength == oldLength || curLength <= 3) {
                    Log.d(TAG,"一次性输入的字符"+curLength);
                    isChange = false;
                } else {
                    isChange = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChange) {
                    if (curLength - sLastLength < 0) {  //判断是editext中的字符串是在减少 还是在增加
                        delete = true;
                    } else {
                        delete = false;
                    }
                    sLastLength = curLength;
                    int selectIndex = registNumber.getSelectionEnd();//获取光标位置
                    String content = s.toString().replaceAll(" ", "");
                    Log.i(TAG, "content:" + content);
                    StringBuffer sb = new StringBuffer(content);
                    //遍历加空格
                    int index = 1;
                    emptyNumA = 0;
                    if (Code_number.equals("852")){
                        for (int i = 0; i < content.length(); i++) {
                            if ((i + 1) % 4 == 0) {
                                sb.insert(i + index, " ");
                                index++;
                                emptyNumA++;
                            }
                        }
                    }else if (Code_number.equals("86")){
                        for (int i = 0; i < content.length(); i++) {
                            if (i == 2) {

                                sb.insert(i + index, " ");
                                index++;
                                emptyNumA++;
                            } else if (i == 6) {
                                sb.insert(i + index, " ");
                                index++;
                                emptyNumA++;
                            }
                        }
                    }
                    Log.i(TAG, "result content:" + sb.toString());
                    String result = sb.toString();
                    //遍历加空格后 如果发现最后一位是空格 就把这个空格去掉
                    if (result.endsWith(" ")) {
                        result = result.substring(0, result.length() - 1);
                        emptyNumA--;
                    }
                    /**
                     * 用遍历添加空格后的字符串 来替换editext变化后的字符串
                     */
                    s.replace(0, s.length(), result);

                    //处理光标位置
                    if (emptyNumA > emptyNumB) {
                        selectIndex = selectIndex + (emptyNumA - emptyNumB);
                    }
                    if (selectIndex > s.length()) {
                        selectIndex = s.length();
                    } else if (selectIndex < 0) {
                        selectIndex = 0;
                    }
                    // 例如"123 45"且光标在4后面 这时需要删除4 光标的处理
                    if (selectIndex > 1 && s.charAt(selectIndex - 1) == ' ') {
                        if (delete) {
                            selectIndex--;
                        } else {
                            selectIndex++;
                        }
                    }
                    registNumber.setSelection(selectIndex);
                    isChange = false;
                }
            }
        });

        getFirebaseEvent();

    }
    public void getFirebaseEvent(){
        //埋点
        Bundle bundle = new Bundle();
        bundle.putString("type","SHOW_RMOBILE");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.SHOW_REGISTER_CELLPHONE,bundle);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (window != null && window.isShowing()) {

            window.dismiss();
        }
        return super.onTouchEvent(event);
    }

    private void initData() {
        list = new ArrayList<Pickers>();
        id = new String[] { "1", "2"};
        name = new String[] { "香港 + 852", "中國 + 86"};
        for (int i = 0; i < name.length; i++) {
            list.add(new Pickers(name[i], id[i]));
        }
        // 设置数据，默认选择第一条
        pickerscrlllview.setData(list);
        pickerscrlllview.setSelected(0);
        Code_number="852";

}
    // 滚动选择器选中事件
    onSelectListener pickerListener = new onSelectListener() {

        @Override
        public void onSelect(Pickers pickers) {
            String showConetnt = pickers.getShowConetnt();
            if (showConetnt.indexOf("香港")!=-1){
                registCode.setText("+ 852");
                Code_number="852";
            }else if (showConetnt.indexOf("中國")!=-1)
                registCode.setText("+ 86");
                Code_number="86";
        }
    };
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isUiCreated = true;
    }

    private void initLinstener() {
        pickerscrlllview.setOnSelectListener(pickerListener);
    }

    @OnClick({R.id.title_back_fl, R.id.regist_code, R.id.regist_next, R.id.regist_emil,R.id.picker_yes,R.id.picker_no})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.regist_code:
                picker_rel.setVisibility(View.VISIBLE);
                if (window!=null&&window.isShowing()){
                    window.dismiss();
                }
                break;
            case R.id.picker_yes:
                picker_rel.setVisibility(View.GONE);
                break;
            case R.id.picker_no:
                picker_rel.setVisibility(View.GONE);
                break;
            case R.id.regist_next:
                if(!NoFastClickUtils.isFastClick()){
                String Numbertrim = registNumber.getText().toString().trim().replace(" ","");
                if (Code_number.equals("86")){
                    if (Numbertrim.length()!=11){
                        ToastUtil.showgravity(this,getResources().getString(R.string.phone_should));
                        return;
                    }else{
                        boolean containAll = AppUtil.isPhoneNumber(Numbertrim);
                        if (!containAll){
                            ToastUtil.showgravity(this,getResources().getString(R.string.phone_should_lo));
                            return;
                        }
                    }
                }else if (Code_number.equals("852")){
                    if (Numbertrim.length()!=8){
                        ToastUtil.showgravity(this,getResources().getString(R.string.phone_should_lo));
                        return;
                    }
                }
                loading(getResources().getString(R.string.listview_loading));
                Http.OkHttpGet(this, url + UrlUtil.IsMobileReg + "?mobile=" + Numbertrim, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        Log.d("sdfhdsjkg",url + UrlUtil.IsMobileReg + "?mobile=" + Numbertrim+"=="+result);


                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getString("code").equals("0")){
                                dissLoad();
                                if (object.getBoolean("data")){
                                    if (forget==1){
                                        Http.OkHttpGet(RegisterActivity.this, url + UrlUtil.Mobile_resetPwdCode+"?mobile="+Numbertrim+"&nationCode="+Code_number+"&languageType="+languageType, new Http.OnDataFinish() {
                                            @Override
                                            public void OnSuccess(String result) {
                                                dissLoad();
                                                Log.d("languageType1",url + UrlUtil.Mobile_resetPwdCode+"?mobile="+Numbertrim+"&nationCode="+Code_number+"&languageType="+languageType+"=="+result);
                                                try {
                                                    JSONObject object = new JSONObject(result);
                                                    if (object.getString("code").equals("0")){
                                                        Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                                                        intent.putExtra("Forget",forget);
                                                        intent.putExtra("Number",Numbertrim);
                                                        intent.putExtra("nationCode",Code_number);
                                                        intent.putExtra("languageType",languageType);
                                                        intent.putExtra("card_flag",card_flag);
                                                        intent.putExtra("my_flag",my_flag);
                                                        startActivity(intent);
                                                    }else if (object.getString("code").equals("1025")){
                                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.code_xian));
                                                    }else if (object.getString("code").equals("5057")){
                                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_no_login));
                                                    }else if (object.getString("code").equals("1016")){
                                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_should_lo));
                                                    }else if (object.getString("code").equals("5103")){
                                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.failed));
                                                    }else {
                                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.fan));
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                            }
                                        });
                                    }else if (forget==2){
                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_now));
                                        return;
                                    }else if (forget==3){
                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.this_number));
                                        return;
                                    }else if (forget==4){
                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_lo));
                                        return;
                                    }
                                }else if (forget==1){
                                    if (!object.getBoolean("data")){
                                        ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_no_login));
                                        return;
                                    }

                                }else if (forget==2){ //注册
                                    Http.OkHttpGet(RegisterActivity.this, url + UrlUtil.Mobile_code+"?mobile="+Numbertrim+"&nationCode="+Code_number+"&languageType="+languageType, new Http.OnDataFinish() {
                                        @Override
                                        public void OnSuccess(String result) {

                                            Log.d("languageType2",url + UrlUtil.Mobile_code+"?mobile="+Numbertrim+"&nationCode="+Code_number+"&languageType="+languageType+"=="+result);
                                            try {
                                                JSONObject object = new JSONObject(result);
                                                if (object.getString("code").equals("0")){
                                                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                                                    intent.putExtra("Forget",forget);
                                                    intent.putExtra("Number",Numbertrim);
                                                    intent.putExtra("nationCode",Code_number);
                                                    intent.putExtra("languageType",languageType);
                                                    startActivity(intent);
                                                }else if (object.getString("code").equals("1025")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.code_xian));
                                                }else if (object.getString("code").equals("5057")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_no_login));
                                                }else if (object.getString("code").equals("1016")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_should_lo));
                                                }else if (object.getString("code").equals("5103")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.failed));
                                                }else {
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.fan));
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                                }else if (forget==3){//更改手机号
                                    Http.OkHttpGet(RegisterActivity.this, url + UrlUtil.Mobile_code+"?mobile="+Numbertrim+"&nationCode="+Code_number+"&languageType="+languageType, new Http.OnDataFinish() {
                                        @Override
                                        public void OnSuccess(String result) {

                                            Log.d("languageType3",url + UrlUtil.Mobile_code+"?mobile="+Numbertrim+"&nationCode="+Code_number+"&languageType="+languageType+"=="+result);
                                            try {
                                                JSONObject object = new JSONObject(result);
                                                if (object.getString("code").equals("0")){
                                                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                                                    intent.putExtra("Forget",forget);
                                                    intent.putExtra("Number",Numbertrim);
                                                    intent.putExtra("nationCode",Code_number);
                                                    intent.putExtra("languageType",languageType);
                                                    startActivity(intent);
                                                }else if (object.getString("code").equals("1025")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.code_xian));
                                                }else if (object.getString("code").equals("5057")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_no_login));
                                                }else if (object.getString("code").equals("1016")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_should_lo));
                                                }else if (object.getString("code").equals("5103")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.failed));
                                                }else {
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.fan));
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                                }else if (forget==4){
                                    Http.OkHttpGet(RegisterActivity.this, url + UrlUtil.Mobile_code+"?mobile="+Numbertrim+"&nationCode="+Code_number+"&languageType="+languageType, new Http.OnDataFinish() {
                                        @Override
                                        public void OnSuccess(String result) {

                                            Log.d("languageType4",url + UrlUtil.Mobile_code+"?mobile="+Numbertrim+"&nationCode="+Code_number+"&languageType="+languageType+"=="+result);
                                            try {
                                                JSONObject object = new JSONObject(result);
                                                if (object.getString("code").equals("0")){
                                                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                                                    intent.putExtra("Forget",forget);
                                                    intent.putExtra("Number",Numbertrim);
                                                    intent.putExtra("nationCode",Code_number);
                                                    intent.putExtra("languageType",languageType);
                                                    startActivity(intent);
                                                }else if (object.getString("code").equals("1025")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.code_xian));
                                                }else if (object.getString("code").equals("5057")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_no_login));
                                                }else if (object.getString("code").equals("1016")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.phone_should_lo));
                                                }else if (object.getString("code").equals("5103")){
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.failed));
                                                }else {
                                                    ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.fan));
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
                            }else {
                                dissLoad();
                                ToastUtil.showgravity(RegisterActivity.this,getResources().getString(R.string.this_number));
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
                break;
            case R.id.regist_emil:
                if (forget==1){
                    Intent intent1 = new Intent(RegisterActivity.this, ForgetActivity.class);
                    startActivity(intent1);
                }else if (forget==2){
                    Intent intent1 = new Intent(RegisterActivity.this, SignEmailActivity.class);
                    intent1.putExtra("card_flag",card_flag);
                    intent1.putExtra("my_flag",my_flag);
                    startActivity(intent1);
                }
                reportPageShown();
                break;
        }
    }
    //埋点
    private void reportPageShown() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_MAIL");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.REGISTER_BY_EMAIL,b);
    }


}
