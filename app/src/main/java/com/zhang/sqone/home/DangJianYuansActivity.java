package com.zhang.sqone.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.zhang.sqone.pullswipe.swipemenulistview.SwipeMenu;
import com.zhang.sqone.pullswipe.swipemenulistview.SwipeMenuCreator;
import com.zhang.sqone.pullswipe.swipemenulistview.SwipeMenuItem;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;
import com.zhang.sqone.xiangqing.XiangQingActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 没有搜索功能的党建园地 (廉政教育，业务学习，党建学习，参考资料)
 *
 * @author ZJP
 *         created at 2016/3/2 11:04
 */
public class DangJianYuansActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener {
    @Bind(R.id.djyl_title)
    TitleBarView djylTitle;
    private Handler mHandler;
    @Bind(R.id.main_lv_search_results)
    PullToRefreshSwipeMenuListView mListView;
    private CommonAdapter<Index.ReqIndex.fileMap> resultAdapter;
    private boolean flsh = false;
    private List<Index.ReqIndex.fileMap> fileList;
    /**
     * 获得判断界面的标识符
     */
    private int bsf;

    /**
     * 网络数据请求的标识符
     */
    private String wbsf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_dang_jian_yuans);
        ButterKnife.bind(this);

        //获得数据判断界面
        bsf = getIntent().getIntExtra(Globals.WDGL, 0);
        switch (bsf) {
            //点击规章制度
            case 1:
                //更换标题
                djylTitle.setText("规章制度");
                //更改请求网络的标识符
                wbsf="GZZD";

                break;
            //点击人事任免
            case 2:
                djylTitle.setText("廉政教育");
                //更改请求网络的标识符
                wbsf="RSRM";
                break;
            //重要文件
            case 3:
                djylTitle.setText("业务学习");
                //更改请求网络的标识符
                wbsf="ZYWJ";
                break;
            //点击党建园地
            case 4:
                djylTitle.setText("党建学习");
                //更改请求网络的标识符
                wbsf="FLIST";
                break;
            //参考资料
            case 5:
                djylTitle.setText("参考资料");
                //更改请求网络的标识符
                wbsf="CKZL";
                break;


        }

        regRequest();
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                if (menu.getViewType() == 3) {
                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getApplication());
                //    deleteItem.setTitle("zhang");
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xe9, 0xe9, 0xe9)));

                    // set item width
                    deleteItem.setWidth(dp2px(90));
                    // set a icon
                    deleteItem.setIcon(R.mipmap.xin);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                } else {

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getApplication());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFB, 0x84, 0x72)));
                    // set item width
                    deleteItem.setWidth(dp2px(90));
                    // set a icon
                    deleteItem.setIcon(R.mipmap.xin);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {

                    case 0:
                        if (fileList.get(position).getIs().equals("0")){
                            //获得文件数据
                            String fid = fileList.get(position).getFid();
                            regRequest2("TJSC",fid);

                        }else{
                            String fid = fileList.get(position).getFid();
                            regRequest2("QXSC",fid);
                        }
                        break;
                    case 1:

                        break;
                }
            }
        });

        mListView.setOnSwipeListener(new PullToRefreshSwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DangJianYuansActivity.this, XiangQingActivity.class);
                intent.putExtra(Globals.XQID, fileList.get(position - 1).getFid());
                intent.putExtra("bsf","dj");
                startActivity(intent);
            }
        });

    }

    /**收藏 和取消收藏*/
    private void regRequest2(String bsf,String fid) {

        Log.i("wid", "regRequest2 " + User.sid);
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setSid(User.sid).setAc(bsf).setId(fid).build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                Toast.makeText(DangJianYuansActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                //点击后重新刷新界面
                regRequest();
            }
        }.protocolBuffer(DangJianYuansActivity.this, Globals.WS_URI, index, null);
    }
    /**
     * 加载列表页
     */
    public void regRequest() {
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setSid(User.sid).setAc(wbsf).build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                fileList = index.getFlistList();
                if (flsh) {
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(getApplicationContext(), df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    mListView.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
                    mListView.stopRefresh();

                    mListView.stopLoadMore();

                } else {
                    Log.i("zhang", "列表个数" + fileList.size());
                    //万能适配器
                    resultAdapter = new CommonAdapter<Index.ReqIndex.fileMap>(DangJianYuansActivity.this, fileList, R.layout.lie_biao_item) {
                        @Override
                        public void convert(ViewHolder holder, Index.ReqIndex.fileMap fileMap) {
                            holder.setText(R.id.lie_biao_title, fileMap.getFt())
                                    .setText(R.id.lie_biao_time, fileMap.getFd());

                            if (fileMap.getIs().equals("0")) {
                                holder.setVisible(R.id.dingjian_iamge, false);
                                seti(1);
                            } else {
                                holder.setVisible(R.id.dingjian_iamge, true);
                                seti(3);
                            }
                        }
                    };
                    mListView.setAdapter(resultAdapter);
                }


            }
        }.protocolBuffer(DangJianYuansActivity.this, Globals.WS_URI, index, null);


    }

    private void onLoad() {
        mListView.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
        mListView.stopRefresh();
        mListView.stopLoadMore();
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
        Toast.makeText(DangJianYuansActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();

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
