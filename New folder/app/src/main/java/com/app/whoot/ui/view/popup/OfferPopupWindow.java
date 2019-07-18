package com.app.whoot.ui.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CategoryBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.custom.RangeSeekBar;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.UrlUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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

public class OfferPopupWindow extends PopupWindow implements View.OnClickListener {
    private Button btnTakePhoto, btnSelect, btnCancel, btndel;
    private View mPopView;
    private OnItemClickListener mListener;

    private DecimalFormat df = new DecimalFormat("0.00");

    private List<CategoryBean> list=new ArrayList();
    private List<String> my_list=new ArrayList<>();
    private TagFlowLayout five_flowlayout;

    public OfferPopupWindow(Context context) {
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
        mPopView = inflater.inflate(R.layout.custom_popup_window, null);


        RangeSeekBar pop_rang = (RangeSeekBar)mPopView.findViewById(R.id.pop_rang);
        RangeSeekBar pop_con = (RangeSeekBar)mPopView.findViewById(R.id.pop_con);

        five_flowlayout = mPopView.findViewById(R.id.five_flowlayout);
        pop_rang.setValue(0f,2f);
        pop_con.setValue(10f,1000f);

        pop_rang.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                pop_rang.setLeftProgressDescription(df.format(min));
                pop_rang.setRightProgressDescription(df.format(max));
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        pop_con.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                pop_con.setLeftProgressDescription(df.format(min));
                pop_con.setRightProgressDescription(df.format(max));
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        String cfgURL = (String) SPUtil.get(context, "cfgURL", "");
        /**
         * 菜系
         * */
        Http.OkHttpGet(context, cfgURL, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject location = data.getJSONObject("Location");
                    JSONArray category = location.getJSONArray("Category");
                    for (int i = 0; i <category.length() ; i++) {
                        CategoryBean bean = GSonUtil.parseGson(category.getString(i), CategoryBean.class);
                        list.add(bean);
                    }
                    for (int i = 0; i <list.get(0).getSub().size() ; i++) {
                        my_list.add(list.get(0).getSub().get(i).getNameCh());
                    }
                    five_flowlayout.setAdapter(new TagAdapter<String>(my_list) {
                        @Override
                        public View getView(FlowLayout parent, int position, String categoryBean) {

                            TextView tv = (TextView) inflater.inflate(R.layout.tv, five_flowlayout, false);

                            tv.setText(categoryBean);
                            return tv;
                        }
                    });
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
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
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
