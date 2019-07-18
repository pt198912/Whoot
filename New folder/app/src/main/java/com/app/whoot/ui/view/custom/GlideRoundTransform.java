package com.app.whoot.ui.view.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Sunrise on 4/25/2018.
 */

public class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 0f;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int dp) {

        super(context);
        radius = Resources.getSystem().getDisplayMetrics().density * dp;

    }

    public GlideRoundTransform(BitmapPool bitmapPool) {

        super(bitmapPool);

    }

    @Override

    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i1) {

        return roundCrop(bitmapPool, bitmap);

    }

    private Bitmap roundCrop(BitmapPool bitmapPool, Bitmap bitmap) {

        if (bitmap == null) {
            return null;
        }

        Bitmap result = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {

            result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        }

        Canvas canvas = new Canvas(result);

        Paint paint = new Paint();

        paint.setShader(new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0f, 0f, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    @Override

    public String getId() {

        return getClass().getName() + Math.round(radius);

    }

}
