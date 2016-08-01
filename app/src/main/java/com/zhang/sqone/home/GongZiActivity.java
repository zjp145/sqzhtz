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
import com.zhang.sqone.bean.Myself;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utilss.HttpUtilM;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.xiangqing.DZXQActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GongZiActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {

    @Bind(R.id.mgz_list)
    PullToRefreshSwipeMenuListView mgzList;
    private Handler mHandler;
    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Myself.ReqMyself.SalaryMap> fileList = new ArrayList<>();
//
    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Myself.ReqMyself.SalaryMap> resultAdapter;
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
    private List<Myself.ReqMyself.SalaryMap> fileList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_gong_zi);
        ButterKnife.bind(this);
        mgzList.setPullRefreshEnable(true);
        mgzList.setPullLoadEnable(true);
        mgzList.setXListViewListener(this);
        mgzList.setOnScrollListener(this);
        regRequest();
        mHandler = new Handler();
        //点击listview 进入详情
        mgzList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GongZiActivity.this, DZXQActivity.class);
                intent.putExtra(Globals.XQID, fileList.get(position - 1).getId());
                intent.putExtra("bsf", "gz");
                startActivity(intent);
            }
        });

    }

    /**
     //     * 根据不同的标识符加载网络的数据
     //     */
    public void regRequest() {
        Myself.ReqMyself reqDocument  = Myself.ReqMyself.newBuilder().setSid(User.sid).setAc("GZLB").setP(p+"").build();
        new HttpUtilM() {
            @Override
            public <T> void analysisInputStreamData(Myself.ReqMyself index) throws IOException {
                fileList = index.getSlistList();
                Log.i("zhang", "集合数据" + fileList.size());
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(GongZiActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    mgzList.setRefreshTime(RefreshTime.getRefreshTime(GongZiActivity.this));
                    mgzList.stopRefresh();
                    mgzList.stopLoadMore();
                    flsh=false;

                } else {
                    if (isFlsh){
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数" + fileList.size());
                        resultAdapter = new CommonAdapter<Myself.ReqMyself.SalaryMap>(GongZiActivity.this, fileList, R.layout.gongzi_litem) {
                            @Override
                            public void convert(ViewHolder holder, Myself.ReqMyself.SalaryMap fileMap) {
//                                Log.i("zhang", "convert "+fileMap.getFbr());`
                                holder.setText(R.id.gongzhi_text, fileMap.getNetpayroll())
                                        .setText(R.id.gongzhi_time, fileMap.getYear()+fileMap.getMonth());
//                                //下载头像
//                                ImageLoader.getInstance().displayImage(fileMap.getPurl(), holder.<CircularImage>getView(R.id.tzgg_iamge));
                            }
                        };
                        mgzList.setAdapter(resultAdapter);
                    }else{
                        if (fileList.size()!=0){
                            Log.i("zhang", "列表个数" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数2" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(GongZiActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }

            }


        }.protocolBuffer(GongZiActivity.this, Globals.ZB_URI, reqDocument, null);


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

    /**模拟加载更多*/
    private void onLoad() {
        Log.i("zhang", "onLoad: ");

        mgzList.setRefreshTime(RefreshTime.getRefreshTime(GongZiActivity.this));
        mgzList.stopRefresh();
        mgzList.stopLoadMore();

    }
    /**下拉刷新数据*/
    public void onRefresh() {
        p=1;
        Log.i("zhang", "onRefresh: ");
        flsh = true;
        isFlsh=true;
        regRequest();
    }
    /**点击加载更多*/
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
                Toast.makeText(GongZiActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                if (flsh){}else{
                    //加载更多功能的代码
                    if(fileList2.size()>4){
                        p++;
                        flsh = false;
                        isFlsh= false;
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
