package com.zhang.sqone.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;
import com.zhang.sqone.xiangqing.XiangQingActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 资料中心的列表页
 *
 * @author ZJP
 *         created at 2016/4/20 11:36
 */
public class ZiLiaoZhongXinActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener {

    /**
     * 资料列表
     */
    @Bind(R.id.ziliao_list)
    PullToRefreshSwipeMenuListView huiyiapList;
    /**标题*/
    @Bind(R.id.ziliao_title)
    TitleBarView ziliaoTitle;

    /**
     * 加载数据
     */
    private Handler mHandler;

    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Index.ReqIndex.dataMap> fileList = new ArrayList<>();

    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Index.ReqIndex.dataMap> resultAdapter;
    /**标题标示*/
    private int bsf;
    /**请求标示*/
    private String wbsf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_zi_liao_zhong_xin);
        ButterKnife.bind(this);
        //获得数据判断界面
        bsf = getIntent().getIntExtra(Globals.ZLWJ, 0);
        switch (bsf) {
            //点击规章制度
            case 1:
                //更换标题
                ziliaoTitle.setText("党委文件");
                //更改请求网络的标识符
                wbsf = "DWWJ";

                break;
            //点击人事任免
            case 2:
                ziliaoTitle.setText("政府文件");
                //更改请求网络的标识符
                wbsf = "ZFWJ";
                break;
            //重要文件
            case 3:
                ziliaoTitle.setText("政府报告");
                //更改请求网络的标识符
                wbsf = "ZFBG";
                break;
            //点击党建园地
            case 4:
                ziliaoTitle.setText("规章制度");
                //更改请求网络的标识符
                wbsf = "RULE";
                break;
            //参考资料
            case 5:
                ziliaoTitle.setText("天竺镇情");
                //更改请求网络的标识符
                wbsf = "TZZQ";
                break;
        }
            huiyiapList.setPullRefreshEnable(true);
            huiyiapList.setPullLoadEnable(true);
            huiyiapList.setXListViewListener(this);
            regRequest();
            mHandler = new Handler();
            //点击listview 进入详情
            huiyiapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ZiLiaoZhongXinActivity.this, XiangQingActivity.class);
                    intent.putExtra(Globals.XQID, fileList.get(position - 1).getFid());
                    intent.putExtra("bsf","zl");
                    startActivity(intent);
                }
            });

        }
        /**
         * 根据不同的标识符加载网络的数据
         */

    public void regRequest() {
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setSid(User.sid).setAc(wbsf).build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                fileList = index.getDatalistsList();
                if (flsh) {
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(getApplicationContext(), df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    huiyiapList.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
                    huiyiapList.stopRefresh();

                    huiyiapList.stopLoadMore();

                } else {
                    Log.i("zhang", "列表个数" + fileList.size());
                    //万能适配器
                    resultAdapter = new CommonAdapter<Index.ReqIndex.dataMap>(ZiLiaoZhongXinActivity.this, fileList, R.layout.lie_biao_item) {
                        @Override
                        public void convert(ViewHolder holder, Index.ReqIndex.dataMap fileMap) {
                            holder.setText(R.id.lie_biao_title, fileMap.getFt())
                                    .setText(R.id.lie_biao_time, fileMap.getFd());

                        }
                    };
                    huiyiapList.setAdapter(resultAdapter);
                }


            }
        }.protocolBuffer(ZiLiaoZhongXinActivity.this, Globals.WS_URI, index, null);

    }


    private void onLoad() {
        huiyiapList.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
        huiyiapList.stopRefresh();
        huiyiapList.stopLoadMore();
    }

    public void onRefresh() {
        flsh = true;
        regRequest();
    }

    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 2000);
        Toast.makeText(ZiLiaoZhongXinActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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



}

