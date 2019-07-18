package com.app.whoot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.BuxBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sunrise on 1/11/2019.
 */

public class BuxAdapter extends ListBaseAdapter<BuxBean> {
    public BuxAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_bux;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        BuxBean item = mDataList.get(position);

        TextView item_bux_name = holder.getView(R.id.item_bux_name);
        TextView item_bux_time = holder.getView(R.id.item_bux_time);
        TextView item_bux_num = holder.getView(R.id.item_bux_num);
        long createTm = item.getCreateTm();
        String timedate = TimeUtil.timedate(createTm);
        item_bux_time.setText(timedate);
        int grade = item.getGrade();
        item_bux_num.setText("+" + grade);
        int type = item.getType();
        String cfgURL = (String) SPUtil.get(mContext, "cfgURL", "");
        Http.OkHttpGet(mContext, cfgURL, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray category = data.getJSONArray("Bux");
                    String luchang = (String) SPUtil.get(mContext, "luchang", "");
                    boolean zh = TimeUtil.isZh(mContext);
                    if (luchang.equals("0")){
                        if (zh){
                            luchang="3";
                        }else {
                            luchang="2";
                        }
                    }
                    for (int i = 0; i < category.length(); i++) {
                        JSONObject jsonObject1 = category.getJSONObject(i);
                        int id = jsonObject1.getInt("type");
                        if (type == id) {

                            if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
                                try {
                                    item_bux_name.setText(jsonObject1.getString("NameCh"));
                                } catch (Exception e) {
                                }

                            } else if (luchang.equals("3")) {
                                try {
                                    item_bux_name.setText(jsonObject1.getString("NameEn"));
                                } catch (Exception e) {
                                }

                            }
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

    }
}
