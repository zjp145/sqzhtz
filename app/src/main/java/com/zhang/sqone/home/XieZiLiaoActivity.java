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
import com.zhang.sqone.bean.Outbox;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utilss.HttpUtilO;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.YueDuRenView;
import com.zhang.sqone.xiangqing.XieYouJianActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 写资料
 *
 * @author ZJP
 *         created at 2016/4/27 15:16
 */
public class XieZiLiaoActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {


    @Bind(R.id.xieyoujian_list)
    PullToRefreshSwipeMenuListView xieyoujianList;
    /**
     * 加载数据
     */
    private Handler mHandler;

    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Outbox.ReqOutBox.ymap> fileList = new ArrayList<>();
//
    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Outbox.ReqOutBox.ymap> resultAdapter;
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
    private List<Outbox.ReqOutBox.ymap> fileList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_xie_zi_liao);
        ButterKnife.bind(this);
        xieyoujianList.setPullRefreshEnable(true);
        xieyoujianList.setPullLoadEnable(true);
        xieyoujianList.setXListViewListener(this);
        xieyoujianList.setOnScrollListener(this);
        regRequest();
        mHandler = new Handler();
        //点击listview 进入详情
        xieyoujianList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(XieZiLiaoActivity.this, XieYouJianActivity.class);
                Bundle bundle = new Bundle();
                Log.i("zhang", "上传的是："+position);
                bundle.putSerializable(Globals.LIDS, fileList2.get(position - 1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        p = 1;
        Log.i("zhang", "onRefresh: ");
        flsh = false;
        isFlsh = true;
        regRequest();
    }

    /**
     * //     * 根据不同的标识符加载网络的数据
     * //
     */
    public void regRequest() {
        Outbox.ReqOutBox reqDocument = Outbox.ReqOutBox.newBuilder().setSid(User.sid).setAc("FZLLB").setP(p + "").build();
        new HttpUtilO() {
            @Override
            public <T> void analysisInputStreamData(Outbox.ReqOutBox index) throws IOException {
                fileList = index.getYlistList();
                Log.i("zhang", "集合数据" + fileList.size());
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(XieZiLiaoActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList2);
                    resultAdapter.notifyDataSetChanged();
                    xieyoujianList.setRefreshTime(RefreshTime.getRefreshTime(XieZiLiaoActivity.this));
                    xieyoujianList.stopRefresh();
                    xieyoujianList.stopLoadMore();
                    flsh = false;
                } else {
                    if (isFlsh) {
                        fileList2.clear();
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数" + fileList.size());
                        resultAdapter = new CommonAdapter<Outbox.ReqOutBox.ymap>(XieZiLiaoActivity.this, fileList, R.layout.xieziliao_litem) {
                            @Override
                            public void convert(ViewHolder holder, final Outbox.ReqOutBox.ymap fileMap) {
                                if (fileMap.getIsfujian().equals("0")) {
                                    holder.setImageResource(R.id.xieyoujian_iamgef, R.mipmap.wufujian);
                                } else {
                                    holder.setImageResource(R.id.xieyoujian_iamgef, R.mipmap.youfujian);
                                }
                                holder.setText(R.id.xieyoujian_title, fileMap.getTheme())
                                        .setText(R.id.xieyoujian_time, fileMap.getTime())
                                        .setText(R.id.xieyoujian_text, fileMap.getRecvicename());
                                holder.setOnClickListener(R.id.xieyoujian_layout, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Log.i("zhang", "onClick: ");
                                        Outbox.ReqOutBox index = Outbox.ReqOutBox.newBuilder().setSid(User.sid).setAc("FZLYD").setId(fileMap.getId()).build();
                                        new HttpUtilO() {
                                            @Override
                                            public <T> void analysisInputStreamData(Outbox.ReqOutBox index) throws IOException {
                                                YueDuRenView d1 = new YueDuRenView() {
                                                    @Override
                                                    public void determineButton() {

                                                    }
                                                }.deleteDialog(XieZiLiaoActivity.this,index.getRpe().getRead(),index.getRpe().getUnread());

                                            }
                                        }.protocolBuffer(XieZiLiaoActivity.this, Globals.ZL_URI, index, null);



                                    }
                                });

                                //下载头像
//                                ImageLoader.getInstance().displayImage(fileMap.getPurl(), holder.<CircularImage>getView(R.id.tzgg_iamge));
                            }
                        };
                        xieyoujianList.setAdapter(resultAdapter);
                    } else {
                        if (fileList.size() != 0) {
                            Log.i("zhang", "列表个数" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数2" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(XieZiLiaoActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }

            }


        }.protocolBuffer(XieZiLiaoActivity.this, Globals.ZL_URI, reqDocument, null);


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
     * 模拟加载更多
     */
    private void onLoad() {
        Log.i("zhang", "onLoad: ");

        xieyoujianList.setRefreshTime(RefreshTime.getRefreshTime(XieZiLiaoActivity.this));
        xieyoujianList.stopRefresh();
        xieyoujianList.stopLoadMore();

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
                Toast.makeText(XieZiLiaoActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
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
