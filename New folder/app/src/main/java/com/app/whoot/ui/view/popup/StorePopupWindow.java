package com.app.whoot.ui.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.adapter.PopAdapter;
import com.app.whoot.bean.FavBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.SearchActivity;
import com.app.whoot.ui.activity.StoreDetailActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.custom.MyListView;
import com.app.whoot.ui.view.custom.RangeSeekBar;
import com.app.whoot.ui.view.recycler.decoration.RefreshRecyclerView;
import com.app.whoot.util.Constants;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.app.whoot.base.BaseApplication.SCREEN_HEIGHT;


/**
 * Created by Sunrise on 4/2/2018.
 */

public class StorePopupWindow extends PopupWindow implements View.OnClickListener {

    private View mPopView;
    private OnItemClickListener mListener;

    private DecimalFormat df = new DecimalFormat("0.00");
    private MyListView pop_list;
    private TextView pop_text;

    private List<FavBean> list=new ArrayList<FavBean>();
    private ImageView pop_img;
    private String urlUtil;

    public StorePopupWindow(Context context) {
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
        //绑定布局
        mPopView = inflater.inflate(R.layout.store_popup_window, null);

        urlUtil = (String) SPUtil.get(context, "URL", "");
        pop_list = mPopView.findViewById(R.id.pop_list);
        pop_text = mPopView.findViewById(R.id.pop_text);
        pop_img = mPopView.findViewById(R.id.pop_img);
        String lastidu = (String) SPUtil.get(context, "lastidu", "");
        String longtidu = (String) SPUtil.get(context, "longtidu", "");
        Http.OkHttpGet(context, urlUtil+ UrlUtil.shop_fav+"?localId="+1+"&latitude="+lastidu+"&longitude="+longtidu, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.getString("code");
                    if (code.equals("0")){
                        JSONArray data = object.getJSONArray("data");
                        if (data.length()>0){
                            pop_text.setVisibility(View.GONE);
                            pop_img.setVisibility(View.GONE);
                            pop_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i <data.length() ; i++) {
                                FavBean bean = GSonUtil.parseGson(data.getString(i), FavBean.class);
                                list.add(bean);
                            }
                            PopAdapter popAdapter = new PopAdapter(context, list);
                            pop_list.setAdapter(popAdapter);
                            popAdapter.notifyDataSetChanged();
                        }else {
                            pop_text.setVisibility(View.VISIBLE);
                            pop_img.setVisibility(View.VISIBLE);
                            pop_list.setVisibility(View.GONE);
                        }
                    }else if (code.equals("5200")){
                        pop_text.setVisibility(View.VISIBLE);
                        pop_img.setVisibility(View.VISIBLE);
                        pop_list.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        pop_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int id1 = list.get(position).getId();
                SPUtil.put(context, Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_STORE_POPWINDOW);
                Intent intent = new Intent(context, StoreDetailActivity.class);
                double distance = list.get(position).getDistance();
                intent.putExtra("distances",distance+"");
                intent.putExtra("id",id1);
                context.startActivity(intent);
            }
        });

    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(SCREEN_HEIGHT/3*2);// 设置弹出窗口的高

        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明

        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.store_olo).getTop();
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



    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }

}
