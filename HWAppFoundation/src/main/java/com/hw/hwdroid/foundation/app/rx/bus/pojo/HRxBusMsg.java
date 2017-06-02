package com.hw.hwdroid.foundation.app.rx.bus.pojo;

/**
 * Created by Android on 2016/6/15.
 */
public class HRxBusMsg {
    public int code;
    public Object object;

    public HRxBusMsg(int code, Object object) {
        this.code = code;
        this.object = object;
    }

}
