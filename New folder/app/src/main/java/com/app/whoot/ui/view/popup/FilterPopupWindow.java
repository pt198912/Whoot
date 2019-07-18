package com.app.whoot.ui.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.bean.CategoryBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.custom.FindRangeSeekBar;
import com.app.whoot.ui.view.custom.PopRangeSeekBar;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.app.whoot.base.BaseApplication.SCREEN_HEIGHT;


/**
 * Created by Sunrise on 4/2/2018.
 */

public class FilterPopupWindow extends PopupWindow implements View.OnClickListener {
    private Button btnTakePhoto, btnSelect, btnCancel, btndel;
    private View mPopView;
    private OnItemClickListener mListener;

    private DecimalFormat df = new DecimalFormat("0.00");


    private List<CategoryBean> list=new ArrayList();
    private List<String> my_list=new ArrayList<>();
    private TagFlowLayout five_flowlayout;
    private TextView pop_deter;
    private String selec;
    private TextView custom_txt;
    private TextView custom_tx;
    private TextView custom_con_txt;
    private TextView custom_con_tx;
    private TagAdapter<String> tagAdapter;
    private LayoutInflater inflater;
    private ImageView custom_gender_img;
    private ImageView custom_gender_img_one;
    private boolean flag=false;
    private String cfgURL;

    public FilterPopupWindow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
        setPopupWindow();
        pop_deter.setOnClickListener(this);

    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.custom_popup_window, null);

        cfgURL = (String) SPUtil.get(context, "cfgURL", "");
        PopRangeSeekBar pop_rang = (PopRangeSeekBar)mPopView.findViewById(R.id.pop_rang);
        PopRangeSeekBar pop_con = (PopRangeSeekBar)mPopView.findViewById(R.id.pop_con);
        custom_txt = mPopView.findViewById(R.id.custom_txt);
        custom_tx = mPopView.findViewById(R.id.custom_tx);
        custom_con_txt = mPopView.findViewById(R.id.custom_con_txt);
        custom_con_tx = mPopView.findViewById(R.id.custom_con_tx);
        TextView pop_reset = mPopView.findViewById(R.id.pop_reset);
        pop_deter = mPopView.findViewById(R.id.pop_deter);
        custom_gender_img = mPopView.findViewById(R.id.custom_gender_img);
        custom_gender_img_one = mPopView.findViewById(R.id.custom_gender_img_one);
        LinearLayout custom_gender_mly = mPopView.findViewById(R.id.custom_gender_mly);
        custom_gender_mly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag){
                    custom_gender_img.setVisibility(View.VISIBLE);
                    custom_gender_img_one.setVisibility(View.GONE);

                    flag=false;
                }else {
                    custom_gender_img_one.setVisibility(View.VISIBLE);
                    custom_gender_img.setVisibility(View.GONE);
                    flag=true;
                }
            }
        });


        five_flowlayout = mPopView.findViewById(R.id.five_flowlayout);

        pop_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop_rang.setValue(0, 2);
                custom_txt.setText("0km");
                custom_tx.setText("2km");
                pop_con.setValue(0, 1000);
                custom_con_txt.setText("$10");
                custom_con_tx.setText("$1000+");
                my_list.clear();
                tagAdapter.notifyDataChanged();
                custom_gender_img.setVisibility(View.VISIBLE);
                custom_gender_img_one.setVisibility(View.GONE);
                flag=false;
                getDAta(context);
            }
        });
        pop_rang.setRules(0, 2, 0, 1);
        pop_rang.setValue(0, 2);
        pop_con.setRules(0, 1000, 0, 1);
        pop_con.setValue(0, 1000);
        pop_rang.setOnRangeChangedListener(new PopRangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(PopRangeSeekBar view, float min, float max) {
                custom_txt.setText(df.format(min)+"km");
                custom_tx.setText(df.format(max)+"km");


            }
        });
        pop_con.setOnRangeChangedListener(new PopRangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(PopRangeSeekBar view, float min, float max) {
                custom_con_txt.setText("$"+Math.round(min));
                custom_con_tx.setText("$"+Math.round(max));


            }
        });
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
                /*    five_flowlayout.setAdapter(new TagAdapter<String>(my_list) {
                        @Override
                        public View getView(FlowLayout parent, int position, String categoryBean) {

                            TextView tv = (TextView) inflater.inflate(R.layout.tv, five_flowlayout, false);

                            tv.setText(categoryBean);

                            return tv;
                        }
                    });*/

                    tagAdapter = new TagAdapter<String>(my_list) {
                        @Override
                        public View getView(FlowLayout parent, int position, String categoryBean) {
                            TextView tv = (TextView) inflater.inflate(R.layout.tv, five_flowlayout, false);

                            tv.setText(categoryBean);

                            return tv;
                        }
                    };

                    five_flowlayout.setAdapter(tagAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        five_flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
               // Toast.makeText(context,my_list.get(position),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        five_flowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Set newsTitleList = new HashSet();
                if (selectPosSet.size()>0){
                    for (int a:selectPosSet){
                        a++;
                        newsTitleList.add(a);
                    }
                    selec=newsTitleList.toString();
                }else {
                    selec = selectPosSet.toString();
                }

                //获取标记
               // Toast.makeText(context,selec,Toast.LENGTH_SHORT).show();
            }
        });
    }
     public void getDAta(Context context){
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

                            TextView tv = (TextView)inflater.inflate(R.layout.tv, five_flowlayout, false);

                            tv.setText(categoryBean);

                            return tv;
                        }
                    });

                     five_flowlayout.setAdapter(tagAdapter);
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
        void setOnItem(String a,String b,String c,String d,String e,boolean f);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
            String a = custom_txt.getText().toString();

            String b = custom_tx.getText().toString();
            String c = custom_con_txt.getText().toString();
            String d = custom_con_tx.getText().toString();


            if (selec==null){
                mListener.setOnItem(a.substring(0,a.length()-2),b.substring(0,b.length()-2),c.substring(1,c.length()),d.substring(1,d.length()),"",flag);
            }else {

                String replace = selec.replace("[", "");
                String replace1 = replace.replace("]", "");
                mListener.setOnItem(a.substring(0,a.length()-2),b.substring(0,b.length()-2),c.substring(1,c.length()),d.substring(1,d.length()),replace1,flag);
            }



        }
    }

}
