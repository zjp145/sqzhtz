package com.zhang.sqone.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Schedulemk;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utils.HttpUtilSc;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.DeleteF;
import com.zhang.sqone.views.TitleBarView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScheduleActivity extends BaseActivity implements AbsListView.OnScrollListener, PullToRefreshSwipeMenuListView.IXListViewListener, TextView.OnEditorActionListener {

    @Bind(R.id.rcap_tz)
    EditText rcapTz;
    @Bind(R.id.rcap_list)
    PullToRefreshSwipeMenuListView rcapList;
    @Bind(R.id.rc_title)
    TitleBarView rcTitle;
    /**
     * 加载数据
     */
    private Handler mHandler;

    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Schedulemk.ReqScheduleMk.GrScheduleList> fileList = new ArrayList<>();
//
    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Schedulemk.ReqScheduleMk.GrScheduleList> resultAdapter;
    /**
     * 控制列表中的页数
     */
    private int p = 1;
    /**
     * 控制数据是加载 还是刷新
     */
    private boolean isFlsh = true;
    /**
     * 判断是不是滑动到底部
     */
    private boolean isFlsh2 = false;
    private String srting = "";
    private List<Schedulemk.ReqScheduleMk.GrScheduleList> fileList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
//        rcapTz.setFocusable(false);
        rcapList.setPullRefreshEnable(true);
        rcapList.setPullLoadEnable(true);
        rcapList.setXListViewListener(this);
        rcapList.setOnScrollListener(this);
        rcTitle.setClickEnterButtonListener(new TitleBarView.OnClickEnterButtonListener() {
            @Override
            public void onClickEnterButton(View v) {
                fileList2.clear();
                Intent intent = new Intent(ScheduleActivity.this,NewScheduleActivity.class);
                startActivity(intent);
            }
        });

        mHandler = new Handler();
        rcapTz.setOnEditorActionListener(this);

    }

    @Override
    protected void onResume() {
        regRequest(srting);
        super.onResume();
    }

    /**
     * //     * 根据不同的标识符加载网络的数据
     * //
     */
    public void regRequest(String s) {
        final Schedulemk.ReqScheduleMk reqDocument = Schedulemk.ReqScheduleMk.newBuilder().setSid(User.sid).setAc("GRRCLB").setP(p + "").setCx(s).build();
        new HttpUtilSc() {
            @Override
            public <T> void analysisInputStreamData(Schedulemk.ReqScheduleMk index) throws IOException {
                fileList = index.getGrlistList();
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    Log.i("zhang", "页数" + p);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(ScheduleActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList2);
                    resultAdapter.notifyDataSetChanged();
                    rcapList.setRefreshTime(RefreshTime.getRefreshTime(ScheduleActivity.this));
                    rcapList.stopRefresh();
                    rcapList.stopLoadMore();
                    flsh=false;

                } else {
                    if (isFlsh) {
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数首" + fileList.size());
                        resultAdapter = new CommonAdapter<Schedulemk.ReqScheduleMk.GrScheduleList>(ScheduleActivity.this, fileList2, R.layout.schedule_items) {
                            @Override
                            public void convert(final ViewHolder holder, final Schedulemk.ReqScheduleMk.GrScheduleList fileMap) {

//ty字段 1是删除
                                holder.setText(R.id.rq_text1, fileMap.getRiq())
                                        .setText(R.id.time_text1, fileMap.getTim())
                                        .setText(R.id.con_1, fileMap.getShx());
                                Log.i("是否删除",fileMap.getTy()+fileMap.getRiq());
                                String i=fileMap.getTy();


                                if (i.equals("1")){

                                    holder.setVisible(R.id.teld_iamge,true);
                                }else {
                                    holder.setVisible(R.id.teld_iamge,false);
                                }


                                holder.setOnClickListener(R.id.teld_iamge, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                                DeleteF d = new DeleteF() {
                                                    @Override
                                                    public void determineButton() {
                                                        Schedulemk.ReqScheduleMk.GrScheduleList.Builder a = Schedulemk.ReqScheduleMk.GrScheduleList.newBuilder();
                                                        a.setId(fileMap.getId());
                                                        Schedulemk.ReqScheduleMk reqDocument = Schedulemk.ReqScheduleMk.newBuilder().setSid(User.sid).setAc("GRRCDEL").addGrlist(a).build();

                                                        new HttpUtilSc() {
                                                            @Override
                                                            public <T> void analysisInputStreamData(Schedulemk.ReqScheduleMk index) throws IOException {
                                                                Toast.makeText(ScheduleActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                                fileList2.remove(holder.getPosition());
                                                                resultAdapter.notifyDataSetChanged();

                                                            }
                                                        }.protocolBuffer(ScheduleActivity.this, Globals.LD_RUI, reqDocument, null);
                                                    }
                                                }.deleteDialog(ScheduleActivity.this, "是否确认删除？", "", "");
                                                /**删除文件*/



                                    }
                                });
                            }
                        };
                        rcapList.setAdapter(resultAdapter);
                    } else {
                        if (fileList.size() != 0) {
                            Log.i("zhang", "列表个数刷新" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数2" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            if (p > 1) {

                            } else {
                                resultAdapter.setData(fileList);
                                resultAdapter.notifyDataSetChanged();
                            }
                            Toast.makeText(ScheduleActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }


            }


        }.protocolBuffer(ScheduleActivity.this, Globals.LD_RUI, reqDocument, null);


    }

    /**
     * 模拟加载更多
     */
    private void onLoad() {
        Log.i("zhang", "onLoad: ");
        rcapList.setRefreshTime(RefreshTime.getRefreshTime(ScheduleActivity.this));
        rcapList.stopRefresh();
        rcapList.stopLoadMore();

    }

    /**
     * 下拉刷新数据
     */
    public void onRefresh() {
        p = 1;
        Log.i("zhang", "onRefresh: ");
        flsh = true;
        isFlsh = true;
        srting="";
        regRequest(srting);
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
    /**
     * 点击加载更多
     */
    public void onLoadMore() {
        Log.i("zhang", "onLoadMore: ");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 2000);
        if(p==1){
            if (fileList2.size() > 4) {
                p++;

                isFlsh = false;
                regRequest(srting);

            }else{
                Toast.makeText(ScheduleActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
if (flsh) {}else{
    if (fileList2.size() > 4) {
        p++;
        flsh = false;
        isFlsh = false;
        regRequest(srting);
    }
}

            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //以下方法防止两次发送请求
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_UP:
                    Log.i("zhang", "text====" + v.getText());
                    srting = v.getText().toString().trim();
                    p = 1;
                    fileList2.clear();
                    isFlsh = false;
                    regRequest(srting);
                    rcapList.setSelection(0);
                    return true;
                default:
                    return true;
            }
        }
        return false;

    }
}
