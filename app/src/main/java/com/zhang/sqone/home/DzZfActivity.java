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
import com.zhang.sqone.bean.Document;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utils.HttputilDocument;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;
import com.zhang.sqone.xiangqing.DzZfXqActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 党章政府章得查询
 *
 * @author ZJP
 *         created at 2016/5/19 13:25
 */
public class DzZfActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {


    @Bind(R.id.dzzf_list_list)
    PullToRefreshSwipeMenuListView dzzfListList;
    @Bind(R.id.dzzf_title)
    TitleBarView dzzfTitle;
    /**
     * 加载数据
     */
    private Handler mHandler;

    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Document.ReqDocument.yzMap> fileList = new ArrayList<>();

    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Document.ReqDocument.yzMap> resultAdapter;
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
    private List<Document.ReqDocument.yzMap> fileList2 = new ArrayList<>();
    private String title;
    private String bsf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_dz_zf);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra(Globals.QJBS);
        if (title.equals("党委章查询")){
            bsf="DWZCX";
        }
        if (title.equals("政府章查询")){
            bsf="ZFZCX";
        }
        dzzfTitle.setText(title);
        dzzfListList.setPullRefreshEnable(true);
        dzzfListList.setPullLoadEnable(true);
        dzzfListList.setXListViewListener(this);
        dzzfListList.setOnScrollListener(this);
        regRequest();
        mHandler = new Handler();
        //点击listview 进入详情
        dzzfListList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DzZfActivity.this, DzZfXqActivity.class);
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

        Document.ReqDocument reqDocument = Document.ReqDocument.newBuilder().setSid(User.sid).setAc(bsf).setP(p + "").build();

        new HttputilDocument() {
            @Override
            public <T> void analysisInputStreamData(Document.ReqDocument index) throws IOException {
                fileList = index.getYzlistList();
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(DzZfActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    dzzfListList.setRefreshTime(RefreshTime.getRefreshTime(DzZfActivity.this));
                    dzzfListList.stopRefresh();
                    dzzfListList.stopLoadMore();
                    flsh = false;

                } else {

                    if (isFlsh) {
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数---第一" + fileList.size());
                        resultAdapter = new CommonAdapter<Document.ReqDocument.yzMap>(DzZfActivity.this, fileList, R.layout.dbgw_litm) {
                            @Override
                            public void convert(ViewHolder holder, Document.ReqDocument.yzMap fileMap) {
                                holder.setVisible(R.id.hyap_cx, false);
                                holder.setText(R.id.banshi_title, fileMap.getDn())
                                        .setText(R.id.banshi_time, fileMap.getDate())
                                        .setText(R.id.banshi_name_f, fileMap.getSqr())
                                        .setText(R.id.banshi_name_d, fileMap.getSqdept());
                                holder.setImageResource(R.id.banshi_moren,R.mipmap.shen)
                                    .setImageResource(R.id.banshi_iamge12,R.mipmap.ke);

                                holder.setVisible(R.id.banshi_tishi, false);
//                            if (i==1){

//                                holder.setImageResource(R.id.banshi_moren,R.mipmap.shen);
                                holder.setVisible1(R.id.banshi_layout, true);
//                                holder.setVisible(R.id.banshi_name_d, false);

//                            }

                                /**改版*/

                                    holder.setBackgroundRes(R.id.iamge_bsf, R.drawable.gz_br_yb);
                                    holder.setVisible1(R.id.bs_cy, false);
                                    holder.setText(R.id.bs_tianshus, "已办");


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
                        dzzfListList.setAdapter(resultAdapter);
                    } else {
                        if (fileList.size() != 0) {
                            Log.i("zhang", "列表个数---第二————" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数---第三————" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(DzZfActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }


            }


        }.protocolBuffer(DzZfActivity.this, Globals.GW_URI, reqDocument, null);

    }

    /**
     * 模拟加载更多
     */
    private void onLoad() {
        Log.i("zhang", "onLoad: ");
        dzzfListList.setRefreshTime(RefreshTime.getRefreshTime(DzZfActivity.this));
        dzzfListList.stopRefresh();
        dzzfListList.stopLoadMore();

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
                Toast.makeText(DzZfActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
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
