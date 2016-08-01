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
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.Noticecontacts;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.HttpUtilNot;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.xiangqing.TzGgXqActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通知公告
 *
 * @author ZJP
 *         created at 2016/4/27 15:16
 */
public class TongZhigonggao extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {

    @Bind(R.id.tzgg_list)
    PullToRefreshSwipeMenuListView tzggList;
    /**
     * 加载数据
     */
    private Handler mHandler;

    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Noticecontacts.ReqNoticeAndContacts.NoticeMap> fileList = new ArrayList<>();
//
    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Noticecontacts.ReqNoticeAndContacts.NoticeMap> resultAdapter;
    /**
     * 控制列表中的页数
     */
    private int p = 1;
    private boolean isL=true;
    /**
     * 控制数据是加载 还是刷新
     */
    private boolean isFlsh = true;
    /**
     * 判断是不是滑动到底部
     */
    private boolean isFlsh2 = false;
    private List<Noticecontacts.ReqNoticeAndContacts.NoticeMap> fileList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_tong_zhigonggao);
        ButterKnife.bind(this);
        tzggList.setPullRefreshEnable(true);
        tzggList.setPullLoadEnable(true);
        tzggList.setXListViewListener(this);
        tzggList.setOnScrollListener(this);
//        regRequest();
        mHandler = new Handler();
        //点击listview 进入详情
        tzggList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TongZhigonggao.this, TzGgXqActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Globals.LIDS,fileList2.get(position - 1) );
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /**
     //     * 根据不同的标识符加载网络的数据
     //     */
    public void regRequest() {
        Noticecontacts.ReqNoticeAndContacts reqDocument  = Noticecontacts.ReqNoticeAndContacts.newBuilder().setSid(User.sid).setAc("TZLB").setP(p+"").build();
        new HttpUtilNot() {
            @Override
            public <T> void analysisInputStreamData(Noticecontacts.ReqNoticeAndContacts index) throws IOException {
                fileList = index.getNlistList();
                Log.i("zhang", "集合数据" + fileList.size());
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(TongZhigonggao.this, df.format(new Date()));
                    resultAdapter.setData(fileList2);
                    resultAdapter.notifyDataSetChanged();
                    tzggList.setRefreshTime(RefreshTime.getRefreshTime(TongZhigonggao.this));
                    tzggList.stopRefresh();
                    tzggList.stopLoadMore();
                    flsh = false;

                } else {
                    if (isFlsh){
                        fileList2.clear();
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数" + fileList.size());
                        resultAdapter = new CommonAdapter<Noticecontacts.ReqNoticeAndContacts.NoticeMap>(TongZhigonggao.this, fileList, R.layout.tzgg_item) {
                            @Override
                            public void convert(ViewHolder holder, Noticecontacts.ReqNoticeAndContacts.NoticeMap fileMap) {
                                Log.i("zhang", "convert "+fileMap.getFbr());
                                holder.setText(R.id.tzgg_title, fileMap.getT())
                                        .setText(R.id.tzgg_time, fileMap.getDt());

                                if (fileMap.getState().equals("0")){
                                    holder.setImageResource(R.id.tzgg_iamge,R.mipmap.tzgg_weidu);
                                }else{
                                    holder.setImageResource(R.id.tzgg_iamge,R.mipmap.tzgg_yidu);
                                }
                                //下载头像
//                                ImageLoader.getInstance().displayImage(fileMap.getPurl(), holder.<CircularImage>getView(R.id.tzgg_iamge));
                            }
                        };
                        tzggList.setAdapter(resultAdapter);
                    }else{
                        if (fileList.size()!=0){
                            Log.i("zhang", "列表个数" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数2" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(TongZhigonggao.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }

            }


        }.protocolBuffer(TongZhigonggao.this, Globals.TZTX_URI, reqDocument, null);


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
        tzggList.setRefreshTime(RefreshTime.getRefreshTime(TongZhigonggao.this));
        tzggList.stopRefresh();
        tzggList.stopLoadMore();


    }

    @Override
    protected void onResume() {
        super.onResume();
          /*加载数据*/
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
        initLogin();
    }
    public void initLogin() {
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
        }.protocolBuffer(TongZhigonggao.this, Globals.WS_URI, index, null);

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
                  Toast.makeText(TongZhigonggao.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
              }
          }

        }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
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
