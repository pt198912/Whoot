package com.app.whoot.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class SpannableUtils {
    public static SpannableString getSpannable(String origin,int startPos,int endPos,int startPos1,int endPos1){
        SpannableString ss=new SpannableString(origin);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FC5D34"));
        ss.setSpan(colorSpan,startPos,endPos, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FC5D34"));
        ss.setSpan(colorSpan1,startPos1,endPos1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
