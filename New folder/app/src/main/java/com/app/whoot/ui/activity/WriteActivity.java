package com.app.whoot.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.adapter.GridViewAddImgesAdpter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.ConfiguraBean;
import com.app.whoot.bean.CouponBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.PermissonCode;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.custom.StarRatingView;
import com.app.whoot.ui.view.dialog.ClearlDialog;
import com.app.whoot.ui.view.dialog.CreaDialog;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.ui.view.dialog.DailyDialog;
import com.app.whoot.ui.view.popup.PhotoPopupWindow;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.BitmapUtil;
import com.app.whoot.util.CommonUtil;
import com.app.whoot.util.CompressImage;
import com.app.whoot.util.Constants;
import com.app.whoot.util.PhotoUtils;
import com.app.whoot.util.PopupWindowUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.google.gson.Gson;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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

import static com.app.whoot.ui.activity.ProfileActivity.rotateBitmapByDegree;

/**
 * Created by Sunrise on 5/3/2018.
 * 评论
 */

public class WriteActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.write_edt)
    EditText writeEdt;
    @BindView(R.id.write_grid)
    MyGridView writeGrid;
    @BindView(R.id.title_back_img)
    ImageView title_back_img;


    @BindView(R.id.write_star)
    StarRatingView writeStar;
    private static final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片

    private SimpleAdapter simpleAdapter;     //适配器
    private ArrayList<HashMap<String, Object>> imageItem;

    private String contract_name;
    private String contract_mobile;
    private String mark;
    private String m_nickname;
    private String m_mobile;
    private String m_address;
    private String img_st = "";

    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final String IMAGE_DIR = Environment.getExternalStorageDirectory() + "/gridview/";

    private OkHttpClient okHttpClient;
    private String imgurl;

    private int flag_0 = 0;
    private int flag_1 = 0;
    private int flag_2 = 0;
    private int flag_3 = 0;
    private int flag_4 = 0;
    private int flag = 0;
    private String shopid;

    private List<String> img_list = new ArrayList<>();
    private String couponId;
    private String commodityId;
    private String shopName;
    private String commodityName;
    private String couponHistoryId;
    private int i = 0;
    private String useCount;
    private String gift_flag;
    private String imgURL;
    private String imgBucket;
    private String urlUtil;
    private int xing;
    private int excoupen;
    private DailyDialog dailyDialog;
    private List<CouponBean> couponlist = new ArrayList<CouponBean>();
    private PhotoPopupWindow popup;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;
    private static final int REQUESTCODE_PIC = 1;//相機
    private static final int REQUESTCODE_CAM = 2;//相冊
    private static final int REQUESTCODE_CUT = 3;//图片裁剪
    private File camerf;
    private Uri uritempFile;
    private File file;
    private Bitmap photo = null;
    private boolean mFromSuccessTokenPage;

    @Override
    public int getLayoutId() {
        return R.layout.write_activi;
    }

    @Override
    public void onCreateInit() {

        mFromSuccessTokenPage=getIntent().getBooleanExtra("from_success_token_page",false);
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
        popup = new PhotoPopupWindow(this);
        urlUtil = (String) SPUtil.get(WriteActivity.this, "URL", "");
        okHttpClient = new OkHttpClient();
        titleBackTitle.setText(getResources().getString(R.string.review));
        title_back_img.setVisibility(View.GONE);
        Intent intent = getIntent();
        shopid = intent.getStringExtra("shopid");
        couponId = intent.getStringExtra("couponId");
        commodityId = intent.getStringExtra("commodityId");
        shopName = intent.getStringExtra("shopName");
        commodityName = intent.getStringExtra("commodityName");
        couponHistoryId = intent.getStringExtra("couponHistoryId");
        excoupen = intent.getIntExtra("Excoupen",0);
        useCount = intent.getStringExtra("useCount");
        gift_flag = intent.getStringExtra("gift_flag");
        titleBackTxt.setVisibility(View.VISIBLE);
        titleBackTxt.setText(getResources().getString(R.string.sub));
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        writeGrid.setAdapter(gridViewAddImgesAdpter);

        xing = intent.getIntExtra("xing", 0);

        if (xing == 0) {
            writeStar.setRate(10);
        } else {
            writeStar.setRate(xing);
        }


        writeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtil.setWindowTransparentValue(WriteActivity.this, 1.0f);
                    }
                });
                PopupWindowUtil.setWindowTransparentValue(WriteActivity.this, 0.5f);
                //设置PopupWindow中的位置
                popup.showAtLocation(WriteActivity.this.findViewById(R.id.write_ly), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


             /*   //获取权限
                String[] permission = new String[]{
                        //读写SD卡
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                };
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(permission[0]) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(permission, PermissonCode.REQUEST_CODE_WRITE);

                    } else {
                        //选择图片
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, IMAGE_OPEN);
                    }
                } else {
                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                }*/

            }
        });

        writeStar.setOnRateChangeListener(new StarRatingView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
                if (rate == 1) {
                    i = rate;
                } else {
                    i = rate / 2;
                }
                Log.d("xingxing", String.valueOf(rate));
            }
        });
        getTokenNumer();

        popup.setOnItemClickListener(new PhotoPopupWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()) {
                    case R.id.photo_txt_p:  //相机拍照
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
                                //选择图片
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, REQUESTCODE_CAM);
                            }
                        } else {
                            //选择图片
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUESTCODE_CAM);
                        }

                        break;
                    case R.id.photo_txt_no:
                        popup.dismiss();

                        break;
                }
            }
        });
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
                    imageUri = FileProvider.getUriForFile(WriteActivity.this, "com.app.whoot.fileprovider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                //ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
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
                            imageUri = FileProvider.getUriForFile(WriteActivity.this, "com.app.whoot.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
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
                    }

                    mUploadAvatrtTh =new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            File file = savePhoto(bitmap5);
                            String fileMd5=getFileMd5(file);
                            uploadAvatar(fileMd5,file);
                        }
                    };
                    mUploadAvatrtTh.start();
                    
                    break;
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileUri);
                    popup.dismiss();
                    uploadImage(cropImageUri.toString().replace("file://",""));

                    break;
                case REQUESTCODE_CAM:   //相機

                    popup.dismiss();
                    // 从相册返回的数据
                    if (data != null) {
                        // 得到图片的全路径
                        Uri uri = data.getData();
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = managedQuery(uri, proj, null, null, null);

                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);


                        uploadImage(path);
                    }
                    break;
                case REQUESTCODE_PIC:
                    if (data == null || data.getData() == null) {
                        return;
                    }
                    //startPhotoZoom(data.getData());
                    break;
                case REQUESTCODE_CUT:
                    if (data != null) {
                        photo = CompressImage.decodeUri(WriteActivity.this, uritempFile, 800, 800);


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


    List<CouponBean.DataBean.CouponsBean> brozens = new ArrayList<>();
    private void getTokenNumer() {

        Http.OkHttpGet(this, urlUtil + UrlUtil.coupon, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")){
                        Gson gson = new Gson();
                        CouponBean couponBean = gson.fromJson(result, CouponBean.class);
                        couponlist.add(couponBean);
                        List<CouponBean.DataBean.CouponsBean> coupons = couponlist.get(0).getData().getCoupons();
                        for (int j = 0; j <coupons.size() ; j++) {
                            CouponBean.DataBean.CouponsBean bean=coupons.get(i);
                            if (coupons.get(j).getCouponId() == 1) { //铜
                                brozens.add(bean);
                            }
                        }
                        SPUtil.put(WriteActivity.this,"brozens_num",brozens.size());
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

                try {
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(path).asFile().withOptions(options).compress(new FileCallback() {
                        @Override
                        public void callback(boolean isSuccess, String outfile) {
                            Log.d("images","outfile: "+outfile);
                            photoPath(outfile);
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0xAAAAAAAA) {
               // photoPath(msg.obj.toString());
            }

        }
    };

    public void photoPath(String path) {

       /* Uri uritempFile = Uri.parse("file://" + "/" + path);
        Log.d("tupian",uritempFile+"");
        Bitmap bitmap = CompressImage.decodeUri(WriteActivity.this, uritempFile, 800, 800);
        File file1 = savePhoto(bitmap);*/


        File file = new File(path);
        String fileMd5 = getFileMd5(file);
        Log.d("eee", fileMd5);
        RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileMd5 + ".png", filebody)
                .addFormDataPart("thum_w", String.valueOf(150))
                .addFormDataPart("bucket", imgBucket)
                .addFormDataPart("thum_h", String.valueOf(150))
                .addFormDataPart("md5", fileMd5)
                .addFormDataPart("region", "ap-guangzhou")
                .build();
        Request build = new Request.Builder()
                .url(imgURL)
                .post(requestBody)
                .build();
        okHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        imgurl = jsonObject.getString("data");
                        Log.d("tupianlianjie", path);
                        Log.d("tupianlianjie", imgurl);
                        if (img_list.size()<=6){
                            img_list.add(imgurl);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        datas.add(map);
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }

    //提示框
    public void Promptbox() {
        final CustomDialog.Builder builder = new CustomDialog.Builder(WriteActivity.this);
        builder.setTitle(String.format(getString(R.string.md_title)));
        builder.setMessage(String.format(getString(R.string.mush)));//content_c
        //R.string.login_o
        builder.setPositiveButton(getResources().getString(R.string.mush_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                 finish();
                 if(mFromSuccessTokenPage){
                     setResult(RESULT_CANCELED);
                 }
            }
        });
        //R.string.pro_save
        builder.setNegativeButton(getResources().getString(R.string.mush_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


            }
        });
        builder.create().show();
    }

    @OnClick({R.id.title_back_txt, R.id.title_back_fl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                Promptbox();
            /*    String trim = writeEdt.getText().toString().trim();
                if (img_list.size() > 0) {
                    String s = img_list.toString();
                    String replace = s.replace("[", "");
                    String replace1 = replace.replace("]", "");
                    img_st = replace1;
                } else {
                    img_st = "";
                }
                if (excoupen==1){
                  //  finish();
                }else if (trim.equals("")) {
                    Intent intent = new Intent(WriteActivity.this, StoreDetailActivity.class);
                    intent.putExtra("id", Integer.parseInt(shopid));
                    startActivity(intent);
                   // finish();
                } else {
                    Promptbox();
                }*/
                break;
            case R.id.title_back_txt: //评论
                Write();

                break;
        }
    }

    private void Write() {
        SPUtil.put(WriteActivity.this, "proDia", "2");
        CreaDialog creaDialog = new CreaDialog(WriteActivity.this, R.style.box_dialog);
        creaDialog.show();

        if (img_list.size() > 0) {
            String s = img_list.toString();
            String replace = s.replace("[", "");
            String replace1 = replace.replace("]", "");
            img_st = replace1;
        } else {
            img_st = "";
        }
        if (i == 0 && xing == 0) {
            i = 5;
        } else {
            if (i==0) {
                i = xing / 2;
            }
        }

        String trim = writeEdt.getText().toString().trim();
        if (trim.length()>0&&trim.length() <= 200) {
            if (gift_flag == null) {
                FormBody body = new FormBody.Builder()
                        .add("grade", String.valueOf(i))
                        .add("shopId", shopid)
                        .add("content", trim)
                        .add("imgUrl", img_st)
                        .add("couponId", couponId)
                        .add("commodityId", commodityId)
                        .add("locationId", "1")
                        .add("shopName", shopName)
                        .add("commodityName", commodityName)
                        .add("couponHistoryId", couponHistoryId)
                        .add("useCount", useCount)
                        .build();
                Log.d("ooioio", shopid + "=" + couponHistoryId + "=" + couponId + "=" + commodityName+"="+i);
                Http.OkHttpPost(WriteActivity.this, urlUtil + UrlUtil.comment, body, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        try {
                            Log.d("pinglunlibao", result);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")) {
                                creaDialog.dismiss();

                                SPUtil.put(WriteActivity.this, "pinglun", 1);

                                    getDailyDialog("write");



                            } else if (jsonObject.getString("code").equals("5700")) {
                                creaDialog.dismiss();
                                //Toast.makeText(WriteActivity.this, getResources().getString(R.string.bu_ya), Toast.LENGTH_SHORT).show();
                                ToastUtil.showgravity(WriteActivity.this, getResources().getString(R.string.bu_ya));
                            } else {
                                creaDialog.dismiss();
                                //Toast.makeText(WriteActivity.this, getResources().getString(R.string.lun), Toast.LENGTH_SHORT).show();
                                ToastUtil.showgravity(WriteActivity.this, getResources().getString(R.string.lun));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            creaDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        creaDialog.dismiss();
                        ToastUtil.showgravity(WriteActivity.this, getResources().getString(R.string.fail_no));
                    }
                });
            } else {
                FormBody body = new FormBody.Builder()
                        .add("grade", String.valueOf(i))
                        .add("shopId", shopid)
                        .add("content", trim)
                        .add("imgUrl", img_st)
                        .add("commodityId", commodityId)
                        .add("locationId", "1")
                        .add("shopName", shopName)
                        .add("commodityName", commodityName)
                        .add("giftRecordId", couponHistoryId)
                        .build();

                Http.OkHttpPost(WriteActivity.this, urlUtil + UrlUtil.comment, body, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        creaDialog.dismiss();
                        try {
                            Log.d("pinglunlibao", result);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")) {

                                //Toast.makeText(WriteActivity.this, getResources().getString(R.string.write_twe), Toast.LENGTH_SHORT).show();
                                ToastUtil.showgravity(WriteActivity.this, getResources().getString(R.string.write_twe));
                                SPUtil.put(WriteActivity.this, "pinglun", 1);
                                finish();
                                if(mFromSuccessTokenPage){
                                    setResult(RESULT_OK);
                                }
                            } else if (jsonObject.getString("code").equals("5700")) {
                                //Toast.makeText(WriteActivity.this, getResources().getString(R.string.bu_ya), Toast.LENGTH_SHORT).show();
                                ToastUtil.showgravity(WriteActivity.this, getResources().getString(R.string.bu_ya));
                            } else {
                                //Toast.makeText(WriteActivity.this, getResources().getString(R.string.lun), Toast.LENGTH_SHORT).show();
                                ToastUtil.showgravity(WriteActivity.this, getResources().getString(R.string.lun));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        creaDialog.dismiss();
                    }
                });
            }


        } else {
            creaDialog.dismiss();
            Toast.makeText(WriteActivity.this, getResources().getString(R.string.pingjia), Toast.LENGTH_SHORT).show();
        }

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
    private void getDailyDialog(String write){

        Handler mainHandler = new Handler(Looper.getMainLooper());

       mainHandler.post(new Runnable() {
           @Override
           public void run() {
               dailyDialog = new DailyDialog(WriteActivity.this,write, R.style.token_dialog);
               dailyDialog.show();
           }
       });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                                    /**
                                     *要执行的操作
                                     */
                                    dailyDialog.dismiss();
                                    if (xing == 0) {
                                        finish();
                                    } else {
                                        SPUtil.put(WriteActivity.this, Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_WRITE_REVIEW);
                                        Intent intent = new Intent(WriteActivity.this, StoreDetailActivity.class);
                                        intent.putExtra("id", Integer.parseInt(shopid));
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            },2500);
    }
    public File savePhoto(Bitmap photos) {
        //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
        File nf = new File(Environment.getExternalStorageDirectory() + "/Licheepay");
        if (!nf.exists()) {
            nf.mkdir();
        }
        //在根目录下面的Licheepay文件夹下 创建head_img.png文件
        File mFile = new File(Environment.getExternalStorageDirectory() + "/Licheepay", "img_img.png");
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
}
