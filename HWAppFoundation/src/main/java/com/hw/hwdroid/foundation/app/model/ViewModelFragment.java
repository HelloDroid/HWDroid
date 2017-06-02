package com.hw.hwdroid.foundation.app.model;

import java.io.Serializable;

/**
 * View Data Fragment
 * <p>
 * Created by ChenJ on 2017/4/21.
 */

public class ViewModelFragment extends ViewModel implements Serializable, Cloneable {


    public String pageCode = "";
    public String pageDescription = "";


    @Override
    public Object clone() throws CloneNotSupportedException {
        ViewModelFragment cloneObj = (ViewModelFragment) super.clone();
        return cloneObj;
    }

}
