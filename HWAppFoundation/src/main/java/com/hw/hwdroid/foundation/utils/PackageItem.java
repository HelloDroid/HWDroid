/**
 * @(#)PackageItem.java 2014-1-21 Copyright 2014 . All rights
 *                      reserved.
 */

package com.hw.hwdroid.foundation.utils;

import android.graphics.drawable.Drawable;

/**
 * @author chenjian
 * @date 2014-1-21
 */

public class PackageItem {

    private Drawable icon;

    private String name;

    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
