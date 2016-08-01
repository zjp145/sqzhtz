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
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Departmentwork;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utils.HttpUtilDocumentw;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 科室之窗的列表展示页面
 *
 * @author ZJP
 *         created at 16/3/24 上午11:38
 */

public class KeShiZhiChuangActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {


    @Bind(R.id.keshi_list)
    /**加载科室信息的list*/
            PullToRefreshSwipeMenuListView keshiList;

    private boolean flsh = false;
    /**科室列表的详情数据*/
    private List<Departmentwork.ReqDepartmentWork.DepartmentMap> fileList= new ArrayList<>();

    /**科室列表的适配器*/
    private CommonAdapter<Departmentwork.ReqDepartmentWork.DepartmentMap> resultAdapter;
    /**控制列表中的页数*/
    private int p = 1;
    /**控制数据是加载 还是刷新*/
    private  boolean isFlsh = true;
    private Handler mHandler;
    /**判断是不是滑动到底部*/
    private  boolean isFlsh2 = false;
    private List<Departmentwork.ReqDepartmentWork.DepartmentMap> fileList2= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_ke_shi_zhi_chuang);
        ButterKnife.bind(this);
        //请求数据
        regRequest();

        keshiList.setPullRefreshEnable(true);
        keshiList.setPullLoadEnable(true);
        keshiList.setXListViewListener(this);
        keshiList.setOnScrollListener(this);
        mHandler = new Handler();
        //点击listview 进入详情
        keshiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KeShiZhiChuangActivity.this, KeShiXQActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Globals.KSCID, resultAdapter.getItem(position-1).getDcode());
                bundle.putString(Globals.KSTP, resultAdapter.getItem(position-1).getFurl());
                bundle.putString(Globals.KSMC, resultAdapter.getItem(position-1).getDn());
                bundle.putString(Globals.KSTX, resultAdapter.getItem(position-1).getCon());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        Log.i("zhang", "适配个数"+resultAdapter.getCount());

    }


    /**
     * 根据不同的标识符加载网络的数据
     */
    public void regRequest() {
        Departmentwork.ReqDepartmentWork reqDocument  = Departmentwork.ReqDepartmentWork.newBuilder().setSid(User.sid).setAc("KSLB").setP(p+"").build();
        new HttpUtilDocumentw() {
            @Override
            public <T> void analysisInputStreamData(Departmentwork.ReqDepartmentWork index) throws IOException {
                fileList = index.getDlistList();
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(KeShiZhiChuangActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    keshiList.setRefreshTime(RefreshTime.getRefreshTime(KeShiZhiChuangActivity.this));
                    keshiList.stopRefresh();
                    keshiList.stopLoadMore();

                } else {
                    if (isFlsh){
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数" + fileList.size());
                        resultAdapter = new CommonAdapter<Departmentwork.ReqDepartmentWork.DepartmentMap>(KeShiZhiChuangActivity.this, fileList, R.layout.keshizc_lb_item) {
                            @Override
                            public void convert(ViewHolder holder, Departmentwork.ReqDepartmentWork.DepartmentMap fileMap) {

                                holder.setText(R.id.keshi_lb_title_text, fileMap.getDn())
                                        .setText(R.id.keshi_lb_jj, fileMap.getCon());
                                //下载头像
                                ImageLoader.getInstance().displayImage(fileMap.getFurl(), holder.<ImageView>getView(R.id.keshi_lb_image));
                            }
                        };
                        keshiList.setAdapter(resultAdapter);
                    }else{

                        if (fileList.size()!=0){
                            Log.i("zhang", "列表个数" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数2" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            keshiList.setRefreshTime(RefreshTime.getRefreshTime(KeShiZhiChuangActivity.this));
                            keshiList.stopRefresh();
                            keshiList.stopLoadMore();
                        }else{
                            Toast.makeText(KeShiZhiChuangActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            keshiList.setRefreshTime(RefreshTime.getRefreshTime(KeShiZhiChuangActivity.this));
                            keshiList.stopRefresh();
                            keshiList.stopLoadMore();
                        }

                    }

                }


            }


        }.protocolBuffer(KeShiZhiChuangActivity.this, Globals.KS_URI, reqDocument, null);

    }

    /**模拟加载更多*/
    private void onLoad() {
        Log.i("zhang", "onLoad: ");

        keshiList.setRefreshTime(RefreshTime.getRefreshTime(KeShiZhiChuangActivity.this));
        keshiList.stopRefresh();
        keshiList.stopLoadMore();

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
        if(isFlsh2){

        }else{
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onLoad();
                }
            }, 2000);
            isFlsh2=false;
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

                if(fileList2.size()>4){
                    p++;
                    flsh = false;
                    isFlsh= false;
                    regRequest();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
