/**
 *
 */
package com.hw.hwdroid.foundation.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Assets Util
 *
 * @author chenj
 * @date 2014-12-18
 */
public class AssetsUtils {

    /**
     * 返回Assets中的Drawable对象
     *
     * @param fileName
     * @return
     */
    public static Drawable getDrawableFromAssetsFile(Context context, String fileName) {
        Drawable image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = Drawable.createFromStream(is, null);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 返回Assets中的Bitmap对象
     *
     * @param fileName
     * @return
     */
    public static Bitmap getBitmapFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
