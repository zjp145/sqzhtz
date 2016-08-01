package com.zhang.sqone.dbgw;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.Myself;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.HttpUtilM;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 值班安排模块
 *
 * @author ZJP
 *         created at 2016/2/1 13:44
 */
public class FragmentAuth extends Fragment implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener, TextView.OnEditorActionListener, View.OnClickListener,DatePickerDialog.OnDateSetListener {
    @Bind(R.id.zhiban_tz)
    EditText zhiBanTz;
    @Bind(R.id.zhiban_list)
    PullToRefreshSwipeMenuListView zhiBanList;
    /**
     * 用户预定会议的时候选择日期
     */
    final Calendar calendar = Calendar.getInstance();
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((DatePickerDialog.OnDateSetListener) this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
    public static final String DATEPICKER_TAG = "datepicker";
    /**加载数据*/
    private Handler mHandler;

    private boolean flsh = false;
    /**科室列表的详情数据*/
    private List<Myself.ReqMyself.DutyPlanMap> fileList = new ArrayList<>();
//
    /**科室列表的适配器*/
    private CommonAdapter<Myself.ReqMyself.DutyPlanMap> resultAdapter;
    /**控制列表中的页数*/
    private int p = 1;
    /**控制数据是加载 还是刷新*/
    private  boolean isFlsh = true;
    /**判断是不是滑动到底部*/
    private  boolean isFlsh2 = false;
    private String srting = "";
    private List<Myself.ReqMyself.DutyPlanMap> fileList2 = new ArrayList<>();
    private String moth1;
    private String day2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //锁定屏幕
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        ButterKnife.bind(this, view);
        zhiBanTz.setFocusable(false);
        zhiBanList.setPullRefreshEnable(true);
        zhiBanList.setPullLoadEnable(true);
        zhiBanList.setXListViewListener(this);
        zhiBanList.setOnScrollListener(this);
        zhiBanTz.setOnClickListener(this);
        regRequest(srting);
        mHandler = new Handler();
        zhiBanTz.setOnEditorActionListener(this);
        return view;
    }

    /**
     * 根据不同的标识符加载网络的数据
     * @param s
     */
    public void regRequest(String s ) {
        Log.i("zhang", "查询条件" + s);
        Myself.ReqMyself reqDocument  = Myself.ReqMyself.newBuilder().setSid(User.sid).setAc("ZBLB").setP(p+"").setTj(s).build();
        new HttpUtilM() {
            @Override
            public <T> void analysisInputStreamData(Myself.ReqMyself index) throws IOException {
                fileList = index.getDplistList();
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    Log.i("zhang", "页数" + p);
                    Log.i("zhang", "1个数" + fileList.size()+"==2个数"+fileList2.size());
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(getActivity(), df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    zhiBanList.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
                    zhiBanList.stopRefresh();
                    zhiBanList.stopLoadMore();

                } else {
                    if (isFlsh){
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数" + fileList.size());
                        resultAdapter = new CommonAdapter<Myself.ReqMyself.DutyPlanMap>(getActivity(), fileList, R.layout.zhiban_iltem) {
                            @Override
                            public void convert(ViewHolder holder, final Myself.ReqMyself.DutyPlanMap fileMap) {

                                holder.setText(R.id.renyuan, fileMap.getDuty())
                                        .setText(R.id.zhiban_time, fileMap.getTime());
                                      if (fileMap.getWeek().equals("星期一")){
                                         holder.setImageResource(R.id.zhou_1,R.mipmap.zb1);
                                      }
                                if (fileMap.getWeek().equals("星期二")){
                                    holder.setImageResource(R.id.zhou_1,R.mipmap.zb2);
                                }
                                if (fileMap.getWeek().equals("星期三")){
                                    holder.setImageResource(R.id.zhou_1,R.mipmap.zb3);
                                }   if (fileMap.getWeek().equals("星期四")){
                                    holder.setImageResource(R.id.zhou_1,R.mipmap.zb4);
                                }   if (fileMap.getWeek().equals("星期五")){
                                    holder.setImageResource(R.id.zhou_1,R.mipmap.zb5);
                                }   if (fileMap.getWeek().equals("星期六")){
                                    holder.setImageResource(R.id.zhou_1,R.mipmap.zb6);
                                }
                                if (fileMap.getWeek().equals("星期日")){
                                    holder.setImageResource(R.id.zhou_1,R.mipmap.zb7);
                                }
                            }
                        };
                        zhiBanList.setAdapter(resultAdapter);
                    }else{
                        if (fileList.size()!=0){
                            Log.i("zhang", "列表个数" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数2" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }


            }


        }.protocolBuffer(getActivity(), Globals.ZB_URI, reqDocument, null);


    }

    /**模拟加载更多*/
    private void onLoad() {
        Log.i("zhang", "onLoad: ");
        zhiBanList.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
        zhiBanList.stopRefresh();
        zhiBanList.stopLoadMore();

    }
    /**下拉刷新数据*/
    public void onRefresh() {
        p=1;
        Log.i("zhang", "onRefresh: ");
        flsh = true;
        isFlsh=true;
        regRequest(srting);
    }
    /**点击加载更多*/
    public void onLoadMore() {
        if(p==1){
            if (fileList2.size() > 4) {
                p++;

                isFlsh = false;
                regRequest(srting);

            }else{
                Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 用户list获得了最低部
     * */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码

                if(fileList2.size()>4){
                    p++;
                    flsh = false;
                    isFlsh= false;
                    regRequest(srting);
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    /**获得EditText的输入结果*/
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
        {
            Log.i("zhang", "text===="+v.getText());
            srting = v.getText().toString().trim();
            p=1;
            fileList2.clear();
            regRequest(srting);
            zhiBanList.setSelection(0);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        datePickerDialog.setVibrate(true);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(true);
        datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
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
        zhiBanTz.setText(year + "-" + moth1 + "-" + day2);
        p=1;
        fileList2.clear();
        srting= year + "-" + moth1 + "-" + day2;
        regRequest(srting);
        zhiBanList.setSelection(0);
    }

    @Override
    public void onResume() {
        initLogin();
        super.onResume();
    }

    public void initLogin() {
        Log.i("zhang", "我的用户名："+User.sid);
        Index.ReqIndex.Login.Builder login = Index.ReqIndex.Login.newBuilder();
        login.setUsername(User.sid);
        //添加密码
        login.setPassword(User.pwd);
        login.setCid(User.cid);
        login.setType(User.type);
        login.setPhncode(User.mis_id);
        //Index.ReqIndex.ReqRec.Builder message = Index.ReqIndex.ReqRec.newBuilder();
        //message.setPhone("15931295549");
        //message.setPwd("15910438651");
        //message.setYzm("123456");
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setLogin(login).setAc("LOGIN").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                //登陆成功
                if(index.getScd().equals("1")){
                    //头像地址
                    User.IconPath = index.getLogin().getPh();
                    User.wcjd=index.getLogin().getWccd();
                    User.tzts=index.getLogin().getTzts();
                    User.dbts=index.getLogin().getDbts();
                    User.sfzl=index.getLogin().getZlts();
                    User.xxzx = index.getLogin().getXxzxts();
                    Log.i("zhang", "完成度"+User.wcjd);
                    Log.i("zhang", "未读通知"+User.tzts);
                    Log.i("zhang", "未读工作"+User.dbts);
                    Log.i("zhang", "资料条数"+User.sfzl);
                    Log.i("zhang","消息中心"+User.xxzx);

                }

            }
        }.protocolBuffer(getActivity(), Globals.WS_URI, index, null);

    }

}

