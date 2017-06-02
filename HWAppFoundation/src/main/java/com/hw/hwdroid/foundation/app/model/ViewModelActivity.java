package com.hw.hwdroid.foundation.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ViewModle Data
 * <p>
 * Created by ChenJ on 2017/2/16.
 */

public class ViewModelActivity extends ViewModel implements Serializable, Cloneable {

    public final ArrayList<String> dialogFragmentTags = new ArrayList<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        ViewModelActivity cloneObj = (ViewModelActivity) super.clone();
        return cloneObj;
    }


}
