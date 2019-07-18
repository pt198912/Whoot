package com.app.whoot.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.adapter.GridViewAddImgesAdpter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.PermissonCode;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.dialog.CreaDialog;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.BitmapUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

/**
 * Created by Sunrise on 3/22/2019.
 */

public class WriteEditActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;

    @BindView(R.id.write_img)
    ImageView writeImg;
    @BindView(R.id.write_name)
    TextView writeName;
    @BindView(R.id.write_ex_0)
    ImageView writeEx0;
    @BindView(R.id.write_ex_1)
    ImageView writeEx1;
    @BindView(R.id.write_ex_2)
    ImageView writeEx2;
    @BindView(R.id.write_ex_3)
    ImageView writeEx3;
    @BindView(R.id.write_ex_4)
    ImageView writeEx4;
    @BindView(R.id.write_ping)
    TextView writePing;
    @BindView(R.id.write_cai)
    TextView writeCai;
    @BindView(R.id.write_dele)
    ImageView writeDele;
    @BindView(R.id.write_num)
    TextView writeNum;
    @BindView(R.id.write_edt)
    EditText writeEdt;
    @BindView(R.id.write_grid)
    MyGridView writeGrid;
    private String urlUtil;
    private int grade;
    private int couponId;
    private int useCount;
    private String imgURL;
    private String imgBucket;

    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final String IMAGE_DIR = Environment.getExternalStorageDirectory() + "/gridview/";
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private OkHttpClient okHttpClient;

    private String contract_name;
    private String contract_mobile;
    private String mark;
    private String m_nickname;
    private String m_mobile;
    private String m_address;

    private String imgurl;
    private List<String> img_list = new ArrayList<>();
    private String img_st = "";
    private String commentId;
    private String commodityId;
    private String commodityName;
    private String shopName;


    @Override
    public int getLayoutId() {
        return R.layout.activi_edit;
    }

    @Override
    public void onCreateInit() {

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
        okHttpClient = new OkHttpClient();
        titleBackTitle.setText(getResources().getString(R.string.pinglun));
        titleBackTxt.setVisibility(View.VISIBLE);
        titleBackTxt.setText(getResources().getString(R.string.save));

        urlUtil = (String) SPUtil.get(WriteEditActivity.this, "URL", "");
        Intent intent = getIntent();
        commentId = intent.getStringExtra("commentId");
        Http.OkHttpGet(WriteEditActivity.this, urlUtil + UrlUtil.comment + "/" + commentId, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("0")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Glide.with(WriteEditActivity.this)
                                .load(data.getString("shopImg"))
                                .centerCrop()
                                .skipMemoryCache(true)
                                .thumbnail(0.1f)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存
                                .error(R.drawable.review_pic_blank)
                                .into(new GlideDrawableImageViewTarget(writeImg, 1));
                        shopName = data.getString("shopName");
                        writeName.setText(shopName);
                        try {
                            grade = data.getInt("grade");
                        } catch (Exception e) {
                            grade = 0;
                        }
                        if (grade == 1) {
                            writeEx0.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx1.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx2.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx3.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx4.setBackgroundResource(R.drawable.icon_star_0);
                        } else if (grade == 2) {
                            writeEx0.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx1.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx2.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx3.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx4.setBackgroundResource(R.drawable.icon_star_0);
                        } else if (grade == 3) {
                            writeEx0.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx1.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx2.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx3.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx4.setBackgroundResource(R.drawable.icon_star_0);
                        } else if (grade == 4) {
                            writeEx0.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx1.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx2.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx3.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx4.setBackgroundResource(R.drawable.icon_star_0);
                        } else if (grade == 5) {
                            writeEx0.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx1.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx2.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx3.setBackgroundResource(R.drawable.icon_star_10);
                            writeEx4.setBackgroundResource(R.drawable.icon_star_10);
                        } else {
                            writeEx0.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx1.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx2.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx3.setBackgroundResource(R.drawable.icon_star_0);
                            writeEx4.setBackgroundResource(R.drawable.icon_star_0);
                        }
                        String updateTm = TimeUtil.timedateone(data.getLong("updateTm"));
                        writePing.setText(getResources().getString(R.string.redemp) + updateTm);
                        commodityName = data.getString("commodityName");
                        writeCai.setText(commodityName);
                        try {
                            couponId = data.getInt("couponId");
                        } catch (Exception e) {
                            couponId = 0;
                        }

                        if (couponId == 1) {
                            writeDele.setBackgroundResource(R.drawable.token_icon_bronze);
                        } else if (couponId == 2) {
                            writeDele.setBackgroundResource(R.drawable.token_icon_silver);
                        } else if (couponId == 3) {
                            writeDele.setBackgroundResource(R.drawable.token_icon_gold);
                        } else if (couponId == 4) {
                            writeDele.setBackgroundResource(R.drawable.token_icon_diamond);
                        } else if (couponId == 0) {
                            writeDele.setBackgroundResource(R.drawable.gifts_tab_icon_gift);
                        }
                        try {
                            useCount = data.getInt("useCount");
                        } catch (Exception e) {
                            useCount = 0;
                        }
                        if (useCount > 0) {
                            writeNum.setText("X" + useCount);
                        }
                        commentId = data.getString("Id");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        writeGrid.setAdapter(gridViewAddImgesAdpter);

        writeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                //获取权限
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
                }

            }
        });


    }
    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {

            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                //好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                //按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                String path = cursor.getString(column_index);


                uploadImage(path);
            }
        } else if (requestCode == 20) {
            contract_name = data.getExtras().getString("contract_name");
            contract_mobile = data.getExtras().getString("contract_mobile");
            mark = data.getExtras().getString("mark");
            m_nickname = data.getExtras().getString("m_nickname");
            m_mobile = data.getExtras().getString("m_mobile");
            m_address = data.getExtras().getString("m_address");
        }
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
                final File file = new File(dir + "/temp_photo" + System.currentTimeMillis() + ".jpg");
                try{
                    saveBitmap = BitmapUtil.saveBitmap(path, dir + "/temp_photo" + System.currentTimeMillis() + ".jpg");
                }catch (Exception e){

                }

                if (file.exists()) {
                    Log.d("images", "压缩后的文件存在" + file.getAbsolutePath());
                } else {
                    Log.d("images", "压缩后的不存在" + file.getAbsolutePath());
                }

                Message message = new Message();
                message.what = 0xAAAAAAAA;
                message.obj = file.getAbsolutePath();
                handler.sendMessage(message);

            }
        }.start();

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0xAAAAAAAA) {
                photoPath(msg.obj.toString());
            }

        }
    };

    public void photoPath(String path) {




        File file = new File(path);
        String fileMd5 = getFileMd5(file);
        Log.d("eee", fileMd5);
        RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileMd5+".png", filebody)
                .addFormDataPart("thum_w", String.valueOf(150))
                .addFormDataPart("bucket", imgBucket)
                .addFormDataPart("thum_h", String.valueOf(150))
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        imgurl = jsonObject.getString("data");
                        Log.d("tupianlianjie",path);
                        Log.d("tupianlianjie",imgurl);
                        img_list.add(imgurl);
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
    @OnClick({R.id.title_back_fl, R.id.title_back_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_txt:
                Write();

                break;
        }
    }

    private void Write(){
        SPUtil.put(WriteEditActivity.this,"proDia","2");
        CreaDialog creaDialog = new CreaDialog(WriteEditActivity.this, R.style.box_dialog);
        creaDialog.show();

        if (img_list.size() > 0) {
            String s = img_list.toString();
            String replace = s.replace("[", "");
            String replace1 = replace.replace("]", "");
            img_st = replace1;
        } else {
            img_st = "";
        }


        String trim = writeEdt.getText().toString().trim();

                FormBody body = new FormBody.Builder()
                        .add("content", trim)
                        .add("imgUrl", img_st)
                        .build();

                Http.OkHttpPatc(WriteEditActivity.this, urlUtil + UrlUtil.getComments+commentId, body, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        try {
                            Log.d("pinglunlibao", result);
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")) {
                                creaDialog.dismiss();
                                ToastUtil.showgravity(WriteEditActivity.this, getResources().getString(R.string.write_twe));
                                finish();

                            } else if (jsonObject.getString("code").equals("5700")){
                                creaDialog.dismiss();

                                ToastUtil.showgravity(WriteEditActivity.this, getResources().getString(R.string.bu_ya));
                            }else {
                                creaDialog.dismiss();

                                ToastUtil.showgravity(WriteEditActivity.this, getResources().getString(R.string.lun));
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
}
