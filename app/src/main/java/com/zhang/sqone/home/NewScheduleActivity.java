package com.zhang.sqone.home;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Schedulemk;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.HttpUtilSc;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;

import java.io.IOException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewScheduleActivity extends FragmentActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    /**
     * 用户预定会议的时候选择日期
     */
    final Calendar calendar = Calendar.getInstance();
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((DatePickerDialog.OnDateSetListener) this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
    public static final String DATEPICKER_TAG = "datepicker";
    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

    @Bind(R.id.xinzeng_rl)
    LinearLayout xinzengRl;
    @Bind(R.id.xinzeng_shixiang)
    EditText xinzengShixiang;
    @Bind(R.id.text_shijian)
    TextView textShijian;
    @Bind(R.id.title_xinzeng)
    TitleBarView titleXinzeng;
    @Bind(R.id.text_riqi)
    TextView textRiqi;
    @Bind(R.id.xinzeng_time)
    LinearLayout xinzengTime;
    private String moth1;
    private String day2;
    private String minute2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_new_schedule);
        ButterKnife.bind(this);
        titleXinzeng.setClickEnterButtonListener(new TitleBarView.OnClickEnterButtonListener() {
            @Override
            public void onClickEnterButton(View v) {
                if (xinzengShixiang.getText().toString().trim().equals("") || titleXinzeng.getText().toString().trim().equals("")) {
                    Toast.makeText(NewScheduleActivity.this, "请完整添加信息", Toast.LENGTH_SHORT).show();

                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(textRiqi.getText().toString().trim()).append(" ").append(textShijian.getText().toString().trim()).append(":00");
                    Schedulemk.ReqScheduleMk reqDocument = Schedulemk.ReqScheduleMk.newBuilder().setSid(User.sid).setDay(stringBuilder.toString()).setCx(xinzengShixiang.getText().toString().trim()).setAc("GRRCNEW").build();

                    new HttpUtilSc() {
                        @Override
                        public <T> void analysisInputStreamData(Schedulemk.ReqScheduleMk index) throws IOException {
                        finish();

                        }
                    }.protocolBuffer(NewScheduleActivity.this, Globals.LD_RUI, reqDocument, null);
                }
            }
        });
        xinzengRl.setOnClickListener(this);
        xinzengTime.setOnClickListener(this);
    }

    /**
     * 设置状态栏背景状态
     */
    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);//状态栏无背景
    }

    @Override
    public void onClick(View view) {
switch (view.getId()){
    case R.id.xinzeng_rl:
        datePickerDialog.setVibrate(true);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(true);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
        break;
    case R.id.xinzeng_time:
        timePickerDialog.show(getSupportFragmentManager(), "timepicker");

        break;
}

    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if (month == 12) {
            month = 1;
        } else {
            month++;
        }
        if (month < 10) {
            moth1 = "0" + month;
        } else {
            moth1 = "" + month;
        }
        if (day < 10) {
            day2 = "0" + day;
        } else {
            day2 = "" + day;
        }
        textRiqi.setText(year + "-" + moth1 + "-" + day2);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if (minute < 10) {
            minute2 = "0" + minute;
        } else {
            minute2 = "" + minute;
        }
        textShijian.setText(hourOfDay + ":" + minute2);
    }
}
