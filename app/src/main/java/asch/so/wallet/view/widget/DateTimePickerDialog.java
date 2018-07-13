package asch.so.wallet.view.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import asch.so.wallet.R;

/**
 * Created by handaolin on 2018/1/11.
 */

public class DateTimePickerDialog extends AlertDialog {
    DatePicker dpDialog;
    TimePicker tpDialog;
    Button tvDialogFilterCancel;
    Button tvDialogFilterPositive;
    TextView tvDialog;
    private Context context;
    private boolean dateShow = true, timeShow = true;
    private Calendar initTime, minTime, maxTime;
    private ClickListenerInterface listenerInterface;
    private String title;

    public DateTimePickerDialog(Context context, String title) {
        super(context);
        this.context = context;
        this.title = title;
    }

    /**
     * @param context
     * @param dateShow 是否显示日期
     * @param timeShow 是否显示时间
     */
    public DateTimePickerDialog(Context context, String title, boolean dateShow, boolean timeShow) {
        super(context);
        this.context = context;
        this.title = title;
        this.dateShow = dateShow;
        this.timeShow = timeShow;
    }

    /**
     * @param context
     * @param dateShow 是否显示日期
     * @param timeShow 是否显示时间
     * @param initTime dialog初试时间
     */
    public DateTimePickerDialog(Context context, String title, boolean dateShow, boolean timeShow, Calendar initTime) {
        super(context);
        this.context = context;
        this.title = title;
        this.dateShow = dateShow;
        this.timeShow = timeShow;
        this.initTime = initTime;
    }

    /**
     * @param context
     * @param dateShow 是否显示日期
     * @param timeShow 是否显示时间
     * @param initTime dialog初试时间
     * @param minTime  dialog最小时间
     * @param maxTime  dialog最大时间
     */
    public DateTimePickerDialog(Context context, String title, boolean dateShow, boolean timeShow, Calendar initTime, Calendar minTime, Calendar maxTime) {
        super(context);
        this.context = context;
        this.title = title;
        this.dateShow = dateShow;
        this.timeShow = timeShow;
        this.initTime = initTime;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public DateTimePickerDialog(Context context, String title, boolean dateShow, boolean timeShow, Calendar minTime, Calendar maxTime) {
        super(context);
        this.context = context;
        this.title = title;
        this.dateShow = dateShow;
        this.timeShow = timeShow;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public interface ClickListenerInterface {
        void doOk(Date time);
    }

    public void setClicklistener(ClickListenerInterface listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_time_picker);
        initView();
        dpDialog.setCalendarViewShown(false);
        tpDialog.setIs24HourView(true);
        initDate();
    }

    private void initView() {
        dpDialog = findViewById(R.id.dp_dialog);
        tpDialog = findViewById(R.id.tp_dialog);
        tvDialogFilterCancel = findViewById(R.id.tv_dialog_filter_cancel);
        tvDialogFilterPositive = findViewById(R.id.tv_dialog_filter_positive);
        tvDialog = findViewById(R.id.tv_dialog);
        tvDialogFilterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvDialogFilterPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.set(dpDialog.getYear(), dpDialog.getMonth(), dpDialog.getDayOfMonth(), tpDialog.getCurrentHour(), tpDialog.getCurrentMinute());
                listenerInterface.doOk(mCalendar.getTime());
                dismiss();
            }
        });
    }

    private void initDate() {
        if (title.equals("")) {
            tvDialog.setVisibility(View.GONE);
        } else {
            tvDialog.setText(title);
        }
        dpDialog.setVisibility(dateShow == true ? View.VISIBLE : View.GONE);
        tpDialog.setVisibility(timeShow == true ? View.VISIBLE : View.GONE);
        if (initTime != null) {
            dpDialog.updateDate(initTime.get(Calendar.YEAR), initTime.get(Calendar.MONTH), initTime.get(Calendar.DAY_OF_MONTH));
            tpDialog.setCurrentHour(initTime.get(Calendar.HOUR_OF_DAY));
            tpDialog.setCurrentMinute(initTime.get(Calendar.MINUTE));
        }
        if (minTime != null) {
            dpDialog.setMinDate(minTime.getTimeInMillis());
        }
        if (maxTime != null) {
            dpDialog.setMaxDate(maxTime.getTimeInMillis());
        }
    }
}
