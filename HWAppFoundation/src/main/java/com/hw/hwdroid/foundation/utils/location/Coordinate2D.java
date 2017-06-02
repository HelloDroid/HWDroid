package com.hw.hwdroid.foundation.utils.location;

/**
 * 定义经纬度类
 * <p>
 * Created by chen.jiana on 16/4/13.
 */
public class Coordinate2D {

    /**
     * 经度
     */
    public double longitude;
    /**
     * 纬度
     */
    public double latitude;

    /**
     * 定位provider
     */
    public String provider = "";

    /**
     * 精度
     */
    double accuracy;

    /**
     * 默认-180，非法的纬度
     */
    public final static double kInvalidLatitude = -180;

    /**
     * 默认-180，非法的经度
     */
    public final static double kInvalidLongitude = -180;


    /**
     * 默认构造方法，初始化数据为latitude=-180, longitude=-180, accuracy=-1。
     */
    public Coordinate2D() {
        this(kInvalidLongitude, kInvalidLatitude, -1);
    }

    /**
     * 默认精度为1
     *
     * @param longitude
     * @param latitude
     */
    public Coordinate2D(double longitude, double latitude) {
        this(longitude, latitude, 1);
    }

    /**
     * 指定经纬度和精度的构造方法。
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param accuracy  精度
     */
    public Coordinate2D(double longitude, double latitude, double accuracy) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.accuracy = accuracy;
    }

    public String toString() {
        return " longitude:" + longitude
                + " latitude:" + latitude + " accuracy:" + accuracy
                + " provider:" + provider;
    }

    @Override
    protected Coordinate2D clone() {
        Coordinate2D coordinate2D = null;
        try {
            coordinate2D = (Coordinate2D) super.clone();
        } catch (CloneNotSupportedException e) {
            coordinate2D = new Coordinate2D();
            coordinate2D.accuracy = this.accuracy;
            coordinate2D.latitude = this.latitude;
            coordinate2D.longitude = this.longitude;
            coordinate2D.provider = this.provider;
        }
        return coordinate2D;
    }

}
