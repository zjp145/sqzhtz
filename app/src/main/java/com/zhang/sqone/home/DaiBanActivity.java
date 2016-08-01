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
import com.zhang.sqone.bean.Document;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.pullswipe.swipemenulistview.SwipeMenu;
import com.zhang.sqone.pullswipe.swipemenulistview.SwipeMenuCreator;
import com.zhang.sqone.pullswipe.swipemenulistview.SwipeMenuItem;
import com.zhang.sqone.utils.HttputilDocument;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;
import com.zhang.sqone.xiangqing.BanShiXQActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 代办事项 和办结事项 和 流转跟踪
 *
 * @author ZJP
 *         created at 2016/3/6 9:20
 */
public class DaiBanActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener {
    @Bind(R.id.gwgl_list)
    /**代办事项的列表*/
            PullToRefreshSwipeMenuListView mListView;
    @Bind(R.id.gwgl_title)
    /**标题*/
            TitleBarView gwglTitle;
    private Handler mHandler;
    private CommonAdapter<Document.ReqDocument.fileMap> resultAdapter;
    private boolean flsh = false;
    private List<Document.ReqDocument.fileMap> fileList;
    /**
     * 判断页面的显示
     */
    public int i;
    /**
     * 页面请求表示符
     */
    public String bsf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_dai_ban);
        ButterKnife.bind(this);
        //根据intent传递的不同的值显示不同的界面（请求不同的数据）
        i = getIntent().getIntExtra(Globals.GWGL, 1);
        switch (i) {
            //流转跟踪
            case 1:
                gwglTitle.setText("在办工作");
                bsf = "LZGZ";
                break;
            //代办事项
            case 2:
                gwglTitle.setText("待办工作");
                bsf = "DBSX";
                break;

            //办结事项
            case 3:
                gwglTitle.setText("已办工作");
                bsf = "BJSX";
                break;
            //收藏工作
            case 4:
                gwglTitle.setText("收藏工作");
                bsf = "SCLB";
                break;
        }
        //请求数据
        if (i!=2){
            regRequest();
        }
        //可以刷新  也可以有左滑收藏功能的使用的listview
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        // step 1. create a MenuCreator
        //添加左滑的按钮的属性
        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
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
        // set creator、
//        list中添加按钮使用
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        //点击了收藏按钮的操作
        mListView.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                Log.i("zhang", "点击的单击的按钮1" + "position" + position + "index" + index);
                switch (index) {
                    case 0:
                        //如果没有收藏就收藏信息 收藏就取消收藏
                        if (fileList.get(position).getIs().equals("0")) {
                            //获得文件数据（赶紧点击的list获得  wid 和 inid 改变收藏状态）
                            String wid = fileList.get(position).getWid();
                            String inid = fileList.get(position).getInid();
                            //改变服务器的收藏状态
                            regRequest2("GWSC", wid, inid);

                        } else {
                            String wid = fileList.get(position).getWid();
                            String inid = fileList.get(position).getInid();
                            regRequest2("DLSC", wid, inid);
                        }

                        break;
                    case 1:
                        //如果有多个情况
//                        Log.i("zhang", "点击的单击的按钮3");
//                        menu.removeMenuItem(deleteItem);
//                        menu.addMenuItem(deleteItem1);

                        break;
                }
            }
        });

        // set SwipeListener
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
        //点击listview 进入详情
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DaiBanActivity.this, BanShiXQActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Globals.LIDS, fileList.get(position - 1));
                bundle.putInt(Globals.BDID, i);
                bundle.putString(Globals.BSF, "bs");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /**
     * 收藏 和取消收藏
     */
    private void regRequest2(String bsf, String wid, String inid) {
        Document.ReqDocument.param.Builder param = Document.ReqDocument.param.newBuilder();
        param.setWid(wid);
        param.setInid(inid);

        Log.i("zhang", "sid" + User.sid);
        Document.ReqDocument reqDocument = Document.ReqDocument.newBuilder().setSid(User.sid).setAc(bsf).setPa(param).build();
        new HttputilDocument() {
            @Override
            public <T> void analysisInputStreamData(Document.ReqDocument index) throws IOException {
                Toast.makeText(DaiBanActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                //点击后重新刷新界面
                regRequest();
            }
        }.protocolBuffer(DaiBanActivity.this, Globals.GW_URI, reqDocument, null);
    }

    /**
     * 根据不同的标识符加载网络的数据
     */
    public void regRequest() {
        Document.ReqDocument reqDocument = Document.ReqDocument.newBuilder().setSid(User.sid).setAc(bsf).build();
        new HttputilDocument() {
            @Override
            public <T> void analysisInputStreamData(Document.ReqDocument index) throws IOException {
                fileList = index.getFlistList();
                if (flsh) {
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(DaiBanActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    mListView.setRefreshTime(RefreshTime.getRefreshTime(DaiBanActivity.this));
                    mListView.stopRefresh();

                    mListView.stopLoadMore();

                } else {
                    Log.i("zhang", "列表个数" + fileList.size());
                    resultAdapter = new CommonAdapter<Document.ReqDocument.fileMap>(DaiBanActivity.this, fileList, R.layout.dbgw_litm) {
                        @Override
                        public void convert(ViewHolder holder, Document.ReqDocument.fileMap fileMap) {

                            holder.setText(R.id.banshi_title, fileMap.getDn())
                                    .setText(R.id.banshi_time, fileMap.getDt())
                                    .setText(R.id.banshi_name_f, fileMap.getSpna())
                                    .setText(R.id.banshi_name_d, fileMap.getAsna())
                                    //新版本添加的流程标识
//                                    .setText(R.id.bs_cy, "超")
//                                    .setText(R.id.bs_tianshus, "99天")
                                    .setText(R.id.banshi_tishi, fileMap.getFn());
                            if (i == 1) {
                                holder.setVisible1(R.id.banshi_layout, false);
                            }

                            if(fileMap.getDs().equals("已办")){
                            holder.setText(R.id.bs_cy, "已办");
                                holder.setVisible1(R.id.bs_tianshus,false);
                                holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br_yb);
//
                            }else{
                                /**新版本添加图片上使用文字*/
                                if (fileMap.getIst().equals("0")) {
                                    holder.setText(R.id.bs_cy, "余")
                                            .setText(R.id.bs_tianshus, fileMap.getDays()+"天");
                                    holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br_yu);
                                } else if(fileMap.getIst().equals("1")) {
                                    holder.setText(R.id.bs_cy, "超")

                                            .setText(R.id.bs_tianshus, fileMap.getDays()+"天");
                                    holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br);

                                }
                            }
//                            if (bsf.equals("DBSX")){
//                                /**新版本添加图片上使用文字*/
//                                if (fileMap.getIst().equals("0")) {
//                                    holder.setText(R.id.bs_cy, "余")
//                                            .setText(R.id.bs_tianshus, fileMap.getDays()+"天");
//                                    holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br_yu);
//                                } else if(fileMap.getIst().equals("1")) {
//                                    holder.setText(R.id.bs_cy, "超")
//
//                                            .setText(R.id.bs_tianshus, fileMap.getDays()+"天");
//                                    holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br);
//
//                                }else{
//
//                                }
//                            }
//                            else{
//                                Log.i("zhang", "状态+++++++" + fileMap.getDs());
//                            if(fileMap.getDs().equals("已办")){
//
//
//                            }else if(fileMap.getDs().equals("终止")){
//                                holder.setText(R.id.bs_cy, "终止");
//                                holder.setVisible1(R.id.bs_tianshus,false);
//                                holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br_zz);
//                            }else if(fileMap.getDs().equals("重办")){
//                                holder.setText(R.id.bs_cy, "重办");
//                                holder.setVisible1(R.id.bs_tianshus,false);
//                                holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br_cb);
//                            }else if(fileMap.getDs().equals("驳回")){
//                                holder.setText(R.id.bs_cy, "驳回");
//                                holder.setVisible1(R.id.bs_tianshus,false);
//                                holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br_bh);
//                            }else if(fileMap.getDs().equals("取消")){
//                                holder.setText(R.id.bs_cy, "取消");
//                                holder.setVisible1(R.id.bs_tianshus,false);
//                                holder.setBackgroundRes(R.id.iamge_bsf,R.drawable.gz_br_qx);
////
//                            }
//
//                            }
//

                            if (fileMap.getIs().equals("0")) {
                                //隐藏
                                holder.setVisible(R.id.shoucang_iamge, false);
                                seti(1);
                            } else {
                                //显示
                                holder.setVisible(R.id.shoucang_iamge, true);
                                seti(3);
                            }
                        }
                    };
                    mListView.setAdapter(resultAdapter);
                }


            }


        }.protocolBuffer(DaiBanActivity.this, Globals.GW_URI, reqDocument, null);

    }

    /**
     * 模拟加载更多
     */
    private void onLoad() {
        mListView.setRefreshTime(RefreshTime.getRefreshTime(DaiBanActivity.this));
        mListView.stopRefresh();
        mListView.stopLoadMore();
    }

    /**
     * 下拉刷新数据
     */
    public void onRefresh() {
        flsh = true;
        regRequest();
    }

    /**
     * 点击加载更多
     */
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 2000);
        Toast.makeText(DaiBanActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();

    }

    /**
     * 收藏按钮的高度使用
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        if (i==2){
            /*加载数据*/
            Log.i("zhang", "重新加载这数据 ");
            regRequest();
            initLogin();
        }
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
        }.protocolBuffer(DaiBanActivity.this, Globals.WS_URI, index, null);

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
