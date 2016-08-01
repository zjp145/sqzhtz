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
import com.zhang.sqone.bean.Meetingroom;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utils.HttpUtilDocumentwh;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;
import com.zhang.sqone.xiangqing.HYCLXQActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 会议材料管理
 *
 * @author ZJP
 *         created at 16/7/18 下午3:24
 */

public class HYZLActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {


    @Bind(R.id.huiyianp_title)
    TitleBarView huiyianpTitle;
    @Bind(R.id.hyclgl_list)
//            列表
    PullToRefreshSwipeMenuListView hyclglList;
    /**
     * 加载数据
     */
    private Handler mHandler;

    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Meetingroom.ReqMeetingRoom.meetingMaterialMap> fileList = new ArrayList<>();

    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Meetingroom.ReqMeetingRoom.meetingMaterialMap> resultAdapter;
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
    private List<Meetingroom.ReqMeetingRoom.meetingMaterialMap> fileList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_hyzl);
        ButterKnife.bind(this);
        hyclglList.setPullRefreshEnable(true);
        hyclglList.setPullLoadEnable(true);
        hyclglList.setXListViewListener(this);
        hyclglList.setOnScrollListener(this);
        regRequest();
        mHandler = new Handler();
        //点击listview 进入详情
        hyclglList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HYZLActivity.this, HYCLXQActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Globals.LIDS, resultAdapter.getItem(position - 1));
//                bundle.putInt(Globals.BDID, i);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /**
     * 根据不同的标识符加载网络的数据
     */
    public void regRequest() {

        Meetingroom.ReqMeetingRoom reqDocument = Meetingroom.ReqMeetingRoom.newBuilder().setSid(User.sid).setAc("HYCLLB").setP(p + "").build();

        new HttpUtilDocumentwh() {
            @Override
            public <T> void analysisInputStreamData(Meetingroom.ReqMeetingRoom index) throws IOException {
                fileList = index.getMmlistList();
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(HYZLActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    hyclglList.setRefreshTime(RefreshTime.getRefreshTime(HYZLActivity.this));
                    hyclglList.stopRefresh();
                    hyclglList.stopLoadMore();
                    flsh = false;

                } else {

                    if (isFlsh) {
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数---第一" + fileList.size());
                        resultAdapter = new CommonAdapter<Meetingroom.ReqMeetingRoom.meetingMaterialMap>(HYZLActivity.this, fileList, R.layout.dbgw_litm) {
                            @Override
                            public void convert(ViewHolder holder, Meetingroom.ReqMeetingRoom.meetingMaterialMap fileMap) {
//                                holder.setVisible(R.id.hyap_cx, true);
                                holder.setText(R.id.banshi_title, fileMap.getName())
                                        .setText(R.id.banshi_time, fileMap.getStarttime())
                                        .setText(R.id.banshi_tishi, fileMap.getMeetingroom())
                                        .setText(R.id.banshi_name_zhu,fileMap.getRemark());
//

                                holder.setImageResource(R.id.banshi_moren,R.mipmap.zhu);
                                holder.setVisible1(R.id.banshi_layout, false);
                                holder.setVisible1(R.id.iamge_bsf, false);
                                holder.setVisible1(R.id.zhu_layout, true);




//                            if (fileMap.getIs().equals("0")) {
                                //隐藏
                                holder.setVisible(R.id.shoucang_iamge, false);
//                                seti(1);
//                            } else {
//                                //显示
//                                holder.setVisible(R.id.shoucang_iamge, true);
//                                seti(3);
//                            }
                            }
                        };
                        hyclglList.setAdapter(resultAdapter);
                    } else {
                        if (fileList.size() != 0) {
                            Log.i("zhang", "列表个数---第二————" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数---第三————" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(HYZLActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }


            }


        }.protocolBuffer(HYZLActivity.this, Globals.HYAP_URI, reqDocument, null);

    }

    /**
     * 模拟加载更多
     */
    private void onLoad() {
        Log.i("zhang", "onLoad: ");
        hyclglList.setRefreshTime(RefreshTime.getRefreshTime(HYZLActivity.this));
        hyclglList.stopRefresh();
        hyclglList.stopLoadMore();

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
        if (p == 1) {
            if (fileList2.size() > 4) {
                p++;

                isFlsh = false;
                regRequest();

            } else {
                Toast.makeText(HYZLActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
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
