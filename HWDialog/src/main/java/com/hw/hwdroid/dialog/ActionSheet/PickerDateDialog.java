package com.hw.hwdroid.dialog.ActionSheet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.hw.hwdroid.dialog.R;
import com.hw.hwdroid.dialog.utils.Util;
import com.hw.hwdroid.dialog.wegit.GregorianLunarCalendarView;

import java.util.Calendar;


/**
 * 日期选择器
 * <p>
 * Created by carbs on 2016/7/12.
 */
public class PickerDateDialog extends Dialog {

    private Context mContext;
    private GregorianLunarCalendarView mGLCView;

    private Calendar initCal;
    private int yearInit;
    private int yearStart;
    private int yearEnd;
    private boolean isFromBottom;

    public PickerDateDialog(Context context) {
        this(context, false, 0, 0, 0, Calendar.getInstance());
    }

    public PickerDateDialog(Context context, boolean isFromBottom, int yearInit, int yearStart, int yearEnd, Calendar initCal) {
        super(context, R.style.DialogTheme_Full);
        this.mContext = context;
        this.isFromBottom = isFromBottom;

        this.initCal = initCal;
        this.yearInit = yearInit;
        this.yearStart = yearStart;
        this.yearEnd = yearEnd;

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_glc, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.width = isFromBottom ? Util.getScreenWidth(getContext()) : Util.dp2px(getContext(), 320F);
        setContentView(view, params);
        initWindow();

        mGLCView = (GregorianLunarCalendarView) view.findViewById(R.id.calendar_view);

        view.findViewById(R.id.cancel_btn).setOnClickListener(v -> {
            yearInit = initCal.get(Calendar.YEAR);
            mGLCView.initYearSpan(yearInit, yearStart, yearEnd);
            mGLCView.init(initCal);
            //            String showToast = "Gregorian : " + initCal.get(Calendar.YEAR) + "-"
            //                    + (initCal.get(Calendar.MONTH) + 1) + "-"
            //                    + initCal.get(Calendar.DAY_OF_MONTH);
            //            Toast.makeText(mContext.getApplicationContext(), showToast, Toast.LENGTH_LONG).show();
            dismiss();
        });

        view.findViewById(R.id.ok_btn).setOnClickListener(v -> {
            GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
            //            Calendar calendar = calendarData.getCalendar();
            //            String showToast = "Gregorian : " + calendar.get(Calendar.YEAR) + "-"
            //                    + (calendar.get(Calendar.MONTH) + 1) + "-"
            //                    + calendar.get(Calendar.DAY_OF_MONTH) + "\n"
            //                    + "Lunar     : " + calendar.get(ChineseCalendar.CHINESE_YEAR) + "-"
            //                    + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "-"
            //                    + calendar.get(ChineseCalendar.CHINESE_DATE);
            //            Toast.makeText(mContext.getApplicationContext(), showToast, Toast.LENGTH_LONG).show();

            initCal = calendarData.chineseCalendar;
            dismiss();

            if (null != onOkClickListener) {
                onOkClickListener.onClick(calendarData);
            }
        });

        toGregorianMode();
        initCalendar();
    }

    private void initCalendar() {
        mGLCView.initYearSpan(yearInit, yearStart, yearEnd);
        mGLCView.init(initCal);
    }

    private void toGregorianMode() {
        mGLCView.toGregorianMode();
    }

    private void initWindow() {
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        if (isFromBottom) {
            window.setWindowAnimations(R.style.Slide_FromBottom);
        }

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.9876f;
        lp.dimAmount = 0.6789f;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = isFromBottom ? Gravity.BOTTOM : Gravity.CENTER;
        lp.width = isFromBottom ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private OnOkClickListener onOkClickListener;

    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    public interface OnOkClickListener {
        void onClick(GregorianLunarCalendarView.CalendarData calendarData);
    }

}