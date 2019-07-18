package com.app.whoot.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.PermissonCode;
import com.app.whoot.ui.view.custom.RoundView;
import com.app.whoot.ui.view.custom.ShapeImageView;
import com.app.whoot.ui.view.dialog.ClearlDialog;
import com.app.whoot.ui.view.dialog.CreaDialog;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.ui.view.popup.PhotoPopupWindow;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.CommonUtil;
import com.app.whoot.util.CompressImage;
import com.app.whoot.util.PhotoUtils;
import com.app.whoot.util.PopupWindowUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Sunrise on 4/18/2018.
 * 个人信息
 */

public class ProfileActivity extends BaseActivity {
    private static final int SUCCESS_SEND = 0x1111111;
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.pro_name)
    EditText proName;
    @BindView(R.id.pro_email)
    TextView proEmail;
    @BindView(R.id.pro_gender_mly)
    LinearLayout proGenderMly;
    @BindView(R.id.pro_female_fly)
    LinearLayout proFemaleFly;
    @BindView(R.id.pro_reg_txt)
    TextView proRegTxt;
    @BindView(R.id.pro_reg_ly)
    LinearLayout proRegLy;
    @BindView(R.id.pro_oca)
    EditText proOca;
    @BindView(R.id.pro_gender_img)
    ImageView proGenderImg;
    @BindView(R.id.pro_gender_img_one)
    ImageView proGenderImgOne;
    @BindView(R.id.pro_gender_no_one)
    ImageView proGenderNoOne;
    @BindView(R.id.pro_gender_no_twe)
    ImageView proGenderNoTwe;
    @BindView(R.id.pr_name_ly)
    LinearLayout pr_name_ly;
    @BindView(R.id.pro_round_too)
    ShapeImageView pro_round_too;
    @BindView(R.id.pro_round_too_ph)
    ImageView pro_round_too_ph;
    @BindView(R.id.checkbox_gender)
    CheckBox checkbox_gender;
    @BindView(R.id.checkbox_gen)
    CheckBox checkbox_gen;
    @BindView(R.id.radioGroupID)
    RadioGroup radioGroupID;

    private int flag = 0;   //0 男
    private int flag_one = 0;
    private PhotoPopupWindow popup;
    private File camerf;
    private static final int REQUESTCODE_PIC = 1;//相册
    private static final int REQUESTCODE_CAM = 2;//相机
    private static final int REQUESTCODE_CUT = 3;//图片裁剪
    private Uri uritempFile;
    private Bitmap photo = null;
    private File file;
    private String gender = "";
    private OkHttpClient okHttpClient;
    private String imgurl = "";
    private String name_one;
    private String gen;


    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private Uri imageUri;
    private Uri cropImageUri;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private String imgURL;
    private String imgBucket;
    private String urlUtil;

    private String flag_gen;
    private String flag_photo1;
    private String flag_email;
    private String flag_name;
    private String photo1;
    private String email;
    private static final String TAG = "ProfileActivity";
    @Override
    public int getLayoutId() {
        return R.layout.activi_pro;
    }

    @Override
    public void onCreateInit() {
        okHttpClient = new OkHttpClient();

        checkbox_gender.setChecked(true);
        urlUtil = (String) SPUtil.get(ProfileActivity.this, "URL", "");


        gen = (String) SPUtil.get(ProfileActivity.this, "gen", "");
        name_one = (String) SPUtil.get(ProfileActivity.this, "name_one", "");

        photo1 = (String) SPUtil.get(ProfileActivity.this, "photo", "");
        email = (String) SPUtil.get(ProfileActivity.this, "email", "");
        proName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        flag_gen=gen;
        flag_photo1=photo1;
        flag_email=email;
        flag_name=name_one;
        radioGroupID.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String tip = checkedId == R.id.femaleGroupID ? "你选择的是男人 " : "你选择的是女人";
                Log.d("dsgdffhgf",tip);
            }
        });
        String versionName = AppUtil.getVersionName(this);
        /**
         * 获取图片上传地址
         * */
        Http.OkHttpGet(this, UrlUtil.Release, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");

                    JSONArray versions = data.getJSONArray("Versions");
                    for (int i = 0; i < versions.length(); i++) {
                        JSONObject jsonObject1 = versions.getJSONObject(i);
                        String version = jsonObject1.getString("Version");
                        if (versionName.equals(version)) {
                            imgURL = jsonObject1.getString("ImgURL");
                            imgBucket = jsonObject1.getString("ImgBucket");
                            break;
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
        proEmail.setText(email);
        if (gen.equals("male")) {
            proGenderImg.setVisibility(View.GONE);
            proGenderImgOne.setVisibility(View.VISIBLE);
            flag = 1;
            flag_one=0;

        } else if (gen.equals("female")) {
            proGenderNoOne.setVisibility(View.GONE);
            proGenderNoTwe.setVisibility(View.VISIBLE);
            flag = 0;
            flag_one=1;
        }else {
            flag_one=3;
        }
        if (photo1.length()==0){
            pro_round_too_ph.setVisibility(View.VISIBLE);
            pro_round_too.setVisibility(View.GONE);

        }else {
            pro_round_too_ph.setVisibility(View.GONE);
            pro_round_too.setVisibility(View.VISIBLE);
            Glide.with(ProfileActivity.this)
                    .load(photo1)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .error(R.drawable.profile_photo_owner)
                    .into(pro_round_too);
        }
        proEmail.setText(email.toCharArray(), 0, email.length());
        proName.setText(name_one.toCharArray(), 0, name_one.length());
        popup = new PhotoPopupWindow(this);
        titleBackTitle.setText(R.string.profile);
        titleBackTxt.setVisibility(View.VISIBLE);



        popup.setOnItemClickListener(new PhotoPopupWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()) {
                    case R.id.photo_txt_p:  //相机拍照
                      //  openCamer();
                       /* String[] camera = new String[]{Manifest.permission.CAMERA};
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (checkSelfPermission(camera[0]) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(camera, PermissonCode.REQUEST_CODE_CAMERA);
                            } else {
                                openCamer();
                            }
                        } else {
                            openCamer();
                        }*/
                        autoObtainCameraPermission();
                        break;
                    case R.id.photo_txt_car:    //相册
                        String[] permission = new String[]{
                                //读写SD卡
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        };
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (checkSelfPermission(permission[0]) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(permission, PermissonCode.REQUEST_CODE_WRITE);
                            } else {
                                openPicture();
                            }
                        } else {
                            openPicture();
                        }

                        break;
                    case R.id.photo_txt_no:
                        popup.dismiss();

                        break;
                }
            }
        });


        //监听软键盘是否显示或隐藏
        pr_name_ly.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        pr_name_ly.getWindowVisibleDisplayFrame(r);
                        int screenHeight = pr_name_ly.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);
                        if (heightDifference > 200) {
                            //软键盘显示
                            // changeKeyboardHeight(heightDifference);

                        } else {
                            //软键盘隐藏

                            proName.clearFocus();
                        }
                    }

                });

    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                //ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);


                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(ProfileActivity.this, "com.app.whoot.fileprovider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                //ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0xAAAAAAAA) {
                // photoPath(msg.obj.toString());
            }else if (msg.what==SUCCESS_SEND){
                pro_round_too.setVisibility(View.VISIBLE);
                pro_round_too_ph.setVisibility(View.GONE);
                String photo_img = msg.getData().getString("photo_img");
                Glide.with(ProfileActivity.this)
                        .load(photo_img)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(pro_round_too);
            }

        }
    };
    private final String IMAGE_DIR = Environment.getExternalStorageDirectory() + "/gridview/";
    /**
     * 上传图片
     *
     * @param path
     */
    private void uploadImage(final String path) {

        new Thread() {

            private String saveBitmap;

            @Override
            public void run() {
                if (new File(path).exists()) {
                    Log.d("images", "源文件存在" + path);
                } else {
                    Log.d("images", "源文件不存在" + path);
                }

                File dir = new File(IMAGE_DIR);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                final File file = new File(dir + "/temp_photo" + System.currentTimeMillis() + ".jpg");


                try {
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(path).asFile().withOptions(options).compress(new FileCallback() {
                        @Override
                        public void callback(boolean isSuccess, String outfile) {
                            Log.d("images",outfile);
                            File file = new File(outfile);
                            String fileMd5=getFileMd5(file);
                            uploadAvatar(fileMd5,file);
                        }
                    });
                } catch (Exception e) {

                }


                Message message = new Message();
                message.what = 0xAAAAAAAA;
                message.obj = "";
                handler.sendMessage(message);

            }
        }.start();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(ProfileActivity.this, "com.app.whoot.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        //ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    //ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    //ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }




    //提示框
    public void Promptbox() {
        final CustomDialog.Builder builder = new CustomDialog.Builder(ProfileActivity.this);
        builder.setTitle(String.format(getString(R.string.md_title)));
        builder.setMessage(String.format(getString(R.string.pre)) );//content_c
        //R.string.login_o
        builder.setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
               // finish();
            }
        });
        //R.string.pro_save
        builder.setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //Save();
                finish();
            }
        });
        builder.create().show();
    }

    public void openPicture() {
        popup.dismiss();
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(picIntent, REQUESTCODE_PIC);
    }
    //保存
    public void Save(){
        SPUtil.put(ProfileActivity.this,"proDia","1");
        CreaDialog creaDialog = new CreaDialog(ProfileActivity.this, R.style.box_dialog);

        String name = proName.getText().toString().trim();
        if (name.length()<3||name.length()>30){

            ToastUtil.showgravity(ProfileActivity.this,getResources().getString(R.string.name_lo));
        }else if (name.equals("")) {

            ToastUtil.showgravity(ProfileActivity.this, getResources().getString(R.string.pro_twe));
        } /*else if (!proEmail.getText().toString().equals("")) {
            Toast.makeText(ProfileActivity.this, getResources().getString(R.string.pro_three), Toast.LENGTH_SHORT).show();
        }*/ else if (flag_one < 0) {

            ToastUtil.showgravity(ProfileActivity.this, getResources().getString(R.string.pro_foru));
        } else {
            creaDialog.show();
            /**
             * 敏感字
             * */
            Http.OkHttpGet(ProfileActivity.this, urlUtil+ UrlUtil.check + "?userName=" + name, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String code = object.getString("code");
                        if (code.equals("0")) {
                            boolean data = object.getBoolean("data");
                            if (data) {
                                //Toast.makeText(ProfileActivity.this, getResources().getString(R.string.pro_one), Toast.LENGTH_SHORT).show();
                                ToastUtil.showgravity(ProfileActivity.this, getResources().getString(R.string.pro_one));
                                creaDialog.dismiss();
                            } else {
                                String email = proEmail.getText().toString();
                                if (flag_one == 0) {
                                    gender = "male";
                                } else {
                                    gender = "female";
                                }
                                if (imgurl.equals("")){
                                    String photo = (String) SPUtil.get(ProfileActivity.this, "photo", "");
                                    if (photo.equals("")){
                                        imgurl="https://qc.whoot.com/portal/profile_photo_owner.png";
                                    }else {
                                        imgurl=photo;
                                    }
                                    Log.d("profile", "save imgurl: "+imgurl);
                                }
                                FormBody body = new FormBody.Builder()
                                        .add("name", name)
                                        .add("photo", imgurl)
                                        .add("gender", gender)
                                        .build();
                                Http.OkHttpPost(ProfileActivity.this, urlUtil+ UrlUtil.profile, body, new Http.OnDataFinish() {
                                    @Override
                                    public void OnSuccess(String result) {
                                        try {
                                            JSONObject object = new JSONObject(result);
                                            Log.d("dfddfd",result);
                                            String code = object.getString("code");
                                            if (code.equals("0")) {
                                                JSONObject data1 = object.getJSONObject("data");

                                               // String email1 = data1.getString("email");
                                                String gender = data1.getString("gender");
                                                String name1 = data1.getString("name");
                                                String photo = data1.getString("photo");
                                              //  SPUtil.put(ProfileActivity.this, "email", email1);
                                                SPUtil.put(ProfileActivity.this, "gen", gender);
                                                SPUtil.put(ProfileActivity.this, "name_one", name1);
                                                SPUtil.put(ProfileActivity.this, "photo", photo);
                                                SPUtil.put(ProfileActivity.this,"creaDia","1");
                                                creaDialog.dismiss();
                                                ClearlDialog creaDia = new ClearlDialog(ProfileActivity.this, R.style.box_dialog);
                                                creaDia.show();
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        super.run();

                                                        Looper.prepare();
                                                        try {

                                                            Thread.sleep(2000);
                                                            creaDia.dismiss();
                                                            finish();
                                                            SPUtil.remove(ProfileActivity.this,"creaDia");

                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Looper.loop();
                                                    }
                                                }.start();

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            creaDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        creaDialog.dismiss();
                                        ToastUtil.showgravity(ProfileActivity.this, getResources().getString(R.string.fail_no));
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        creaDialog.dismiss();
                    }
                }

                @Override
                public void onError(Exception e) {
                    creaDialog.dismiss();
                    ToastUtil.showgravity(ProfileActivity.this, getResources().getString(R.string.fail_no));
                }
            });

        }
    }
    @OnClick({R.id.title_back_fl, R.id.title_back_txt, R.id.pro_round_too,R.id.pro_round_too_ph, R.id.pro_gender_mly, R.id.pro_female_fly, R.id.pro_reg_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                String name_ = proName.getText().toString().trim();
                if (flag_one == 0) {
                    gender = "male";
                } else if (flag_one==1){
                    gender = "female";
                }else if (flag_one==3){
                    gender = "";
                }
                //判断是否有更改
                 if (!(name_one.equals(name_))){
                     Promptbox();
                 }else if (imgurl.length()>0&&!(imgurl.equals(flag_photo1))){
                     Promptbox();
                 }else if (!(gen.equals(gender))){
                     Promptbox();
                 }else {
                     finish();
                 }

                break;
            case R.id.title_back_txt: //保存
                Save();
                break;
            case R.id.pro_round_too: //换头像

                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtil.setWindowTransparentValue(ProfileActivity.this, 1.0f);
                    }
                });
                PopupWindowUtil.setWindowTransparentValue(ProfileActivity.this, 0.5f);
                //设置PopupWindow中的位置
                popup.showAtLocation(ProfileActivity.this.findViewById(R.id.pro_fy), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


                break;
            case R.id.pro_round_too_ph:
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtil.setWindowTransparentValue(ProfileActivity.this, 1.0f);
                    }
                });
                PopupWindowUtil.setWindowTransparentValue(ProfileActivity.this, 0.5f);
                //设置PopupWindow中的位置
                popup.showAtLocation(ProfileActivity.this.findViewById(R.id.pro_fy), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.pro_gender_mly:
                if (flag == 0) {
                    proGenderImg.setVisibility(View.GONE);
                    proGenderImgOne.setVisibility(View.VISIBLE);
                    proGenderNoOne.setVisibility(View.VISIBLE);
                    proGenderNoTwe.setVisibility(View.GONE);
                    flag_one = 0;
                    flag = 1;
                }

                break;
            case R.id.pro_female_fly:

                proGenderNoOne.setVisibility(View.GONE);
                proGenderNoTwe.setVisibility(View.VISIBLE);
                proGenderImg.setVisibility(View.VISIBLE);
                proGenderImgOne.setVisibility(View.GONE);
                flag_one = 1;
                flag = 0;

                break;
            case R.id.pro_reg_ly: //选择地区

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUploadAvatrtTh!=null&&!mUploadAvatrtTh.isInterrupted()){
            mUploadAvatrtTh.interrupt();
        }
    }

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;
    private void uploadAvatar(String fileMd5,File file){
        RequestBody filebody1 = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody1 = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileMd5+".png", filebody1)
                .addFormDataPart("thum_w", String.valueOf(150))
                .addFormDataPart("bucket", imgBucket)  //whoot-1251007673   ingcreations-whoot-sg   imgBucket
                .addFormDataPart("thum_h", String.valueOf(60))
                .addFormDataPart("md5", fileMd5)
                .addFormDataPart("region","ap-guangzhou")
                .build();
        Request build1 = new Request.Builder()
                .url(imgURL)
                .post(requestBody1)
                .build();
        okHttpClient.newCall(build1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String s = e.toString();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.d("kjhhg",string);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        imgurl = jsonObject.getString("data");
                        Log.d("profile","uploadAvatar "+imgurl);
                        Message message = new Message();
                        message.what = SUCCESS_SEND;
                        Bundle bundle = new Bundle();
                        bundle.putString("photo_img",imgurl);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Thread mUploadAvatrtTh;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case CODE_RESULT_REQUEST:
                    Bitmap bitmap5 = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap5 != null) {
                        pro_round_too.setVisibility(View.VISIBLE);
                        pro_round_too_ph.setVisibility(View.GONE);
                        pro_round_too.setImageBitmap(bitmap5);
                    }

                    mUploadAvatrtTh =new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            File file = savePhoto(bitmap5);
                            String fileMd5=getFileMd5(file);
                            uploadImage(Uri.fromFile(file).toString());
                          //  uploadAvatar(fileMd5,file);
                        }
                    };
                    mUploadAvatrtTh.start();

                    break;
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                  //  cropImageUri = Uri.fromFile(fileCropUri);
                    cropImageUri = Uri.fromFile(fileUri);
                 //
                    //  PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    uploadImage(cropImageUri.toString().replace("file://",""));
                    popup.dismiss();
                    break;
                case REQUESTCODE_CAM:
                    if (Build.VERSION.SDK_INT >= 24) {
                        Uri inputUri = FileProvider.getUriForFile(this, "com.app.whoot.fileprovider", camerf);
                        startPhotoZoom(inputUri);
                    } else {
                       // String filepath = PhotoBitmapUtils.amendRotatePhoto(String.valueOf(camerf), this);

                        int bitmapDegree = getBitmapDegree(camerf.getAbsolutePath());
                        String absolutePath = camerf.getAbsolutePath();
                        String pach = CommonUtil.amendRotatePhoto(absolutePath, ProfileActivity.this);
                       // Bitmap asciiPic = CommonUtil.createAsciiPic(pach, ProfileActivity.this);
                        //pro_round_too.setImageBitmap(asciiPic);
                        startPhotoZoom(Uri.fromFile(camerf));
                       try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(camerf));
                            Bitmap bitmap1 = rotateBitmapByDegree(bitmap, bitmapDegree);
                            startPhotoZoom(Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, null,null)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                    }
                    break;
                case REQUESTCODE_PIC:
                    if (data == null || data.getData() == null) {
                        return;
                    }
                    startPhotoZoom(data.getData());
                    break;
                case REQUESTCODE_CUT:
                    if (data != null) {
                        photo = CompressImage.decodeUri(ProfileActivity.this, uritempFile, 800, 800);


                        pro_round_too.setImageBitmap(photo);
                        this.file = savePhoto(photo);

                        String fileMd5 = getFileMd5(this.file);
                        RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), this.file);
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", fileMd5+".png", filebody)
                                .addFormDataPart("thum_w", String.valueOf(150))
                                .addFormDataPart("bucket", imgBucket)  //whoot-1251007673
                                .addFormDataPart("thum_h", String.valueOf(60))
                                .addFormDataPart("md5", fileMd5)
                                .addFormDataPart("region","ap-guangzhou")
                                .build();
                        Request build = new Request.Builder()
                                .url(imgURL)
                                .post(requestBody)
                                .build();
                        okHttpClient.newCall(build).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                String s = e.toString();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(string);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("0")) {
                                        imgurl = jsonObject.getString("data");
                                        Message message = new Message();
                                        message.what = SUCCESS_SEND;
                                        Bundle bundle = new Bundle();
                                        bundle.putString("photo_img",imgurl);
                                        message.setData(bundle);
                                        handler.sendMessage(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    break;
            }
        }
    }

    //编辑图片
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        //找到指定URI对应的资源图片
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("return-data", false);

        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "crop.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        //Bitmap bitmap = CompressImage.decodeUri(ProfileActivity.this, uritempFile, 800, 800);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        //进入系统裁剪图片的界面
        startActivityForResult(intent, REQUESTCODE_CUT);
    }

    public File savePhoto(Bitmap photos) {
        //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
        File nf = new File(Environment.getExternalStorageDirectory() + "/Licheepay");
        if (!nf.exists()) {
            nf.mkdir();
        }
        //在根目录下面的Licheepay文件夹下 创建head_img.png文件
        File mFile = new File(Environment.getExternalStorageDirectory() + "/Licheepay", System.currentTimeMillis()+".png");
        if (mFile.exists()) {
            mFile.delete();
        }
        FileOutputStream out = null;
        try {//打开输出流 将图片数据填入文件中
            out = new FileOutputStream(mFile);
            photos.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return mFile;
    }

    /**
     * 获取文件的MD5值
     *
     * @param file 文件路径
     * @return md5
     */
    public static String getFileMd5(File file) {
        MessageDigest messageDigest;
        //MappedByteBuffer byteBuffer = null;
        FileInputStream fis = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            if (file == null) {
                return "";
            }
            if (!file.exists()) {
                return "";
            }
            int len = 0;
            fis = new FileInputStream(file);
            //普通流读取方式
            byte[] buffer = new byte[1024 * 1024 * 10];
            while ((len = fis.read(buffer)) > 0) {
                //该对象通过使用 update（）方法处理数据
                messageDigest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            String md5 = bigInt.toString(16);
            while (md5.length() < 32) {
                md5 = "0" + md5;
            }
            return md5;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 将图片的旋转角度置为0  ，此方法可以解决某些机型拍照后图像，出现了旋转情况
     *
     * @Title: setPictureDegreeZero
     * @param path
     * @return void
     * @date
     */
    private void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度   /sdcard/Pictures/he_img.png
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
    /**
     * 通过Uri获取文件
     * @param ac
     * @param uri
     * @return
     */
    public static File getFileFromMediaUri(Context ac, Uri uri) {
        if(uri.getScheme().toString().compareTo("content") == 0){
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        }else if(uri.getScheme().toString().compareTo("file") == 0){
            return new File(uri.toString().replace("file://",""));
        }
        return null;
    }
    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }
    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
