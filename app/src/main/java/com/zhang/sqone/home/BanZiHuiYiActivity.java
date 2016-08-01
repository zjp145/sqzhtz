package com.zhang.sqone.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Meetingdiscuss;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utils.HttpUtilDocumentme;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 班子会议查询（班子会议审核）
 *
 * @author ZJP
 *         created at 2016/5/24 17:17
 */
public class BanZiHuiYiActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {


    @Bind(R.id.banzichaxun_title)
    TitleBarView banzichaxunTitle;
    @Bind(R.id.banzichaxun_list)
    PullToRefreshSwipeMenuListView banzichaxunList;
    /**
     * 加载数据
     */
    private Handler mHandler;

    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Meetingdiscuss.ReqMeetingDiscuss.MeetingCxMap> fileList = new ArrayList<>();

    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Meetingdiscuss.ReqMeetingDiscuss.MeetingCxMap> resultAdapter;
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
    private List<Meetingdiscuss.ReqMeetingDiscuss.MeetingCxMap> fileList2 = new ArrayList<>();
    /**控制班子会议的判断（审核还是查询）*/
    private int i;
    private String bsf;
    private String ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_ban_zi_hui_yi);
        ButterKnife.bind(this);
        i = getIntent().getIntExtra("shenhe", 1);
        if (i==1){
            bsf="BZHSH";
            banzichaxunTitle.setText("班子会议题审核");
        }else if(i==2){
            bsf="BZHCX";
            banzichaxunTitle.setText("班子会议题查询");
        }else if(i==3){
            bsf="BZSBLB";
            banzichaxunTitle.setText("班子会议题上报");
        }
        banzichaxunList.setPullRefreshEnable(true);
        banzichaxunList.setPullLoadEnable(true);
        banzichaxunList.setXListViewListener(this);
        banzichaxunList.setOnScrollListener(this);
//        regRequest();
        mHandler = new Handler();
        //点击listview 进入详情
        banzichaxunList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BanZiHuiYiActivity.this, BanZiCaoZuoActivity.class);
                /*查询详情使用的的年和期数*/
               intent.putExtra("nian",resultAdapter.getItem(position - 1).getYear());
                intent.putExtra("qs",resultAdapter.getItem(position - 1).getQs());
                /*点击的是查看*/
                intent.putExtra("bsf","0");
                /*判断的是班子会议审核还是班子会议的查询*/
                intent.putExtra("bsf2",i+"");
                /**判断是不是四大领导*/
                intent.putExtra("bsf3",ld);
                startActivity(intent);
            }
        });

    }

    /**
     * 根据不同的标识符加载网络的数据
     */
    public void regRequest() {

        Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc(bsf).setP(p + "").build();

        new HttpUtilDocumentme() {
            @Override
            public <T> void analysisInputStreamData(final Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
                fileList = index.getMcmlistList();
                ld = index.getLd();
                Log.i("zhang", "是不是四大领导"+ld);
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(BanZiHuiYiActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    banzichaxunList.setRefreshTime(RefreshTime.getRefreshTime(BanZiHuiYiActivity.this));
                    banzichaxunList.stopRefresh();
                    banzichaxunList.stopLoadMore();
                    flsh = false;

                } else {

                    if (isFlsh) {
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数---第一" + fileList.size());
                        resultAdapter = new CommonAdapter<Meetingdiscuss.ReqMeetingDiscuss.MeetingCxMap>(BanZiHuiYiActivity.this, fileList, R.layout.banzi_litems) {
                            @Override
                            public void convert(ViewHolder holder, final Meetingdiscuss.ReqMeetingDiscuss.MeetingCxMap fileMap) {
                                holder.setText(R.id.banzhi1_title, fileMap.getQs())
                                        .setText(R.id.banzhi1_shenhe, fileMap.getSt())
                                        .setText(R.id.banzhi1_text, fileMap.getSd());
                                if (fileMap.getSt().equals("已上报")||fileMap.getSt().equals("已审核")||fileMap.getSt().equals("已结束")){
                                    Log.i("zhang", "bsf是 "+fileMap.getSt());
                                    holder.setTextColorRes(R.id.banzhi1_shenhe,R.color.bs_zz);
                                }else{
                                    Log.i("zhang", "bsf是 "+fileMap.getSt());
                                    holder.setTextColorRes(R.id.banzhi1_shenhe,R.color.font_333);
                                }
                                Log.i("zhang", "bsf是 "+fileMap.getQs());
                                Log.i("zhang", "convert: "+fileMap.getIfcauo()+"------"+holder.getPosition());
                                if (fileMap.getIfcauo().equals("0")){
                                    holder.setVisible(R.id.caozuo_text, false);
                                }else{

                                    holder.setVisible(R.id.caozuo_text, true);
                                }

                                /*点击的操作的按钮*/
                                holder.setOnClickListener(R.id.caozuo_text, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(BanZiHuiYiActivity.this,BanZiCaoZuoActivity.class);
                                        intent.putExtra("nian",fileMap.getYear());
                                        intent.putExtra("qs",fileMap.getQs());
                                        /*点击的是操作*/
                                        intent.putExtra("bsf","1");
                                         /*判断的是班子会议审核还是班子会议的查询*/
                                        intent.putExtra("bsf2",i+"");
                                        /**判断是不是四大领导*/
                                        intent.putExtra("bsf3",ld);
                                        startActivity(intent);
                                    }
                                });
                            }
                        };
                        banzichaxunList.setAdapter(resultAdapter);
                    } else {
                        if (fileList.size() != 0) {
                            Log.i("zhang", "列表个数---第二————" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数---第三————" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(BanZiHuiYiActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }


            }


        }.protocolBuffer(BanZiHuiYiActivity.this, Globals.BZHY_URI, reqDocument, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        p = 1;
        Log.i("zhang", "onRefresh: ");
//        if (isL){
        flsh = false;
//            isL=false;
//        }else{
//            flsh=true;
//        }

        isFlsh = true;
        regRequest();
    }

    /**
     * 模拟加载更多
     */
    private void onLoad() {
        Log.i("zhang", "onLoad: ");
        banzichaxunList.setRefreshTime(RefreshTime.getRefreshTime(BanZiHuiYiActivity.this));
        banzichaxunList.stopRefresh();
        banzichaxunList.stopLoadMore();

    }

    /**
     * 下拉刷新数据
     */
    public void onRefresh() {
        p = 1;
        Log.i("zhang", "onRefresh: ");
        flsh = true;
        isFlsh = true;
        regRequest();
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
                regRequest();

            }else{
                Toast.makeText(BanZiHuiYiActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        }

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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码

                Log.i("zhang", "onLoadMore: ");
                if (flsh) {

                } else {

                    if (fileList2.size() > 4) {
                        p++;

                        isFlsh = false;
                        regRequest();
                    }
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}

