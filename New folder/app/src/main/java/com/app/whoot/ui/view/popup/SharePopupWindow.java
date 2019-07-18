package com.app.whoot.ui.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.FavBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.app.whoot.base.BaseApplication.SCREEN_HEIGHT;


/**
 * Created by Sunrise on 4/2/2018.
 */

public class SharePopupWindow extends PopupWindow  {

    private View mPopView;
    private OnItemClickListener mListener;

    private DecimalFormat df = new DecimalFormat("0.00");
    private LinearLayout pop_whats, pop_dires, pop_face, pop_mess, pop_line, pop_wech, pop_moent, pop_email, pop_messa;
    private TextView pop_cancel;

    private List<FavBean> list = new ArrayList<FavBean>();

    private String WHATSAPP = "com.whatsapp";
    private String FACEBOOK = "com.facebook.katana";
    private String FACEMESSENGER = "com.facebook.orca";
    private String INSTGRAM = "com.instagram.android";
    private String LINE = "jp.naver.line.android";
    private String WEIXIN = "com.tencent.mm";

    private List<String> name = new ArrayList<String>();
    private SimpleAdapter saImageItems;

    private int[] image = {R.drawable.storedetail_share_icon_whatsapp, R.drawable.storedetail_share_icon_direct, R.drawable.storedetail_share_icon_fb,
            R.drawable.storedetail_share_icon_fbmessenger, R.drawable.storedetail_share_icon_line, R.drawable.storedetail_share_icon_wehcat,
            R.drawable.storedetail_share_icon_wehatmoments, R.drawable.storedetail_share_icon_email, R.drawable.storedetail_share_icon_message};
    private GridView gridView;


    public SharePopupWindow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
        setPopupWindow();


    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);

        List<HashMap<String, Object>> shareList = new ArrayList<HashMap<String, Object>>();
        //绑定布局
        mPopView = inflater.inflate(R.layout.share_pop, null);
        pop_whats = mPopView.findViewById(R.id.pop_whats);
        pop_dires = mPopView.findViewById(R.id.pop_dires);
        pop_face = mPopView.findViewById(R.id.pop_face);
        pop_mess = mPopView.findViewById(R.id.pop_mess);
        pop_line = mPopView.findViewById(R.id.pop_line);
        pop_wech = mPopView.findViewById(R.id.pop_wech);
        pop_moent = mPopView.findViewById(R.id.pop_moent);
        pop_email = mPopView.findViewById(R.id.pop_email);
        pop_messa = mPopView.findViewById(R.id.pop_messa);
        pop_cancel = mPopView.findViewById(R.id.pop_cancel);
        gridView = mPopView.findViewById(R.id.share_gridView);

        /*pop_whats.setOnClickListener(this);
        pop_dires.setOnClickListener(this);
        pop_face.setOnClickListener(this);
        pop_mess.setOnClickListener(this);
        pop_line.setOnClickListener(this);
        pop_wech.setOnClickListener(this);
        pop_moent.setOnClickListener(this);
        pop_email.setOnClickListener(this);
        pop_messa.setOnClickListener(this);
        pop_cancel.setOnClickListener(this);*/

        boolean isWhat = isApp(context, WHATSAPP);
        boolean isFacebook = isApp(context, FACEBOOK);
        boolean isFacemesseng = isApp(context, FACEMESSENGER);
        boolean isDisect = isApp(context, INSTGRAM);
        boolean isLine = isApp(context, LINE);
        boolean isWeixin = isApp(context, WEIXIN);

        name.add(context.getResources().getString(R.string.whats));
        name.add(context.getResources().getString(R.string.dires));
        name.add(context.getResources().getString(R.string.face));
        name.add(context.getResources().getString(R.string.mess));
        name.add(context.getResources().getString(R.string.line));
        name.add(context.getResources().getString(R.string.wech));
        name.add(context.getResources().getString(R.string.moment));
        name.add(context.getResources().getString(R.string.email));
        name.add(context.getResources().getString(R.string.messa));

        for (int i = 0; i < name.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (i == 0) {
                if (isWhat) {

                    map.put("ItemImage", image[0]);
                    map.put("ItemText", name.get(0));
                    shareList.add(map);
                }
            }
            if (i == 1) {
                if (isDisect) {
                    map.put("ItemImage", image[1]);
                    map.put("ItemText", name.get(1));
                    shareList.add(map);
                }
            }
            if (i == 2) {
                if (isFacebook) {
                    map.put("ItemImage", image[2]);
                    map.put("ItemText", name.get(2));
                    shareList.add(map);
                }
            }
            if (i == 3) {
                if (isFacemesseng) {
                    map.put("ItemImage", image[3]);
                    map.put("ItemText", name.get(3));
                    shareList.add(map);
                }
            }
            if (i == 4) {
                if (isLine) {
                    map.put("ItemImage", image[4]);
                    map.put("ItemText", name.get(4));
                    shareList.add(map);
                }
            }
            if (i == 5) {
                if (isWeixin) {

                    map.put("ItemImage", image[5]);
                    map.put("ItemText", name.get(5));
                    shareList.add(map);
                }
            }
            if (i == 6) {
                if (isWeixin) {

                    map.put("ItemImage", image[6]);
                    map.put("ItemText", name.get(6));
                    shareList.add(map);
                }
            }

            if (i == 7) {
                map.put("ItemImage", image[7]);
                map.put("ItemText", name.get(7));
                shareList.add(map);
            }
            if (i == 8) {
                map.put("ItemImage", image[8]);
                map.put("ItemText", name.get(8));
                shareList.add(map);
            }
        }
        saImageItems = new SimpleAdapter(context, shareList, R.layout.share_item, new String[]{"ItemImage", "ItemText"}, new int[]{R.id.imageView1, R.id.textView1});
        gridView.setAdapter(saImageItems);


    }
    public void setOnItemClickListener(OnItemClickListener listener) {
                 gridView.setOnItemClickListener(listener);
             }
    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(SCREEN_HEIGHT / 5 * 2);// 设置弹出窗口的高

        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明


        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.share_p).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {


                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    //检测app是否安装
    private boolean isApp(Context context, String packname) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener extends AdapterView.OnItemClickListener {
        //void setOnItemClick(View v);

        @Override
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

  /*  public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }*/

   /* @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }*/
}
