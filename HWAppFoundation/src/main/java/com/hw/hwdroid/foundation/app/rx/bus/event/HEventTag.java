package com.hw.hwdroid.foundation.app.rx.bus.event;

/**
 * Created by Android on 2016/6/17.
 */
public enum HEventTag {
    DEFAULT(0),
    SUCCESS(-100),
    FAILED(-300),
    UPDATE(-200),
    ERROR(-404);

    private int value = 0;

    HEventTag(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static int getTag(HEventTag eventTag) {
        int i = 0;
        switch (eventTag) {
            case DEFAULT:
                i = 0;
                break;
            case SUCCESS:
                i = -100;
                break;
            case FAILED:
                i = -1;
                break;
            case UPDATE:
                i = -200;
                break;
            case ERROR:
                i = -404;
                break;
        }
        return i;
    }
}
