package com.zhang.sqone.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Departmentwork;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utils.HttpUtilDocumentw;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 科员风采界面
 *
 * @author ZJP
 *         created at 16/3/25 上午10:52
 */
public class KyfcFragment extends Fragment implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {

    @Bind(R.id.keyuan_list)
    PullToRefreshSwipeMenuListView keyuanList;
    /**加载数据的列表*/
    private List<Departmentwork.ReqDepartmentWork.OfficerMap> fileList = new ArrayList<>();
    /**刷新的判断*/
    private boolean flsh= false;
    /**数据的适配器*/
    private CommonAdapter<Departmentwork.ReqDepartmentWork.OfficerMap> resultAdapter;
    /**数据的刷新还是加载*/
    private boolean isFlsh=true;
    /**请求数据*/
    private String cid;
    /**判断是不是滑动到底部*/
    private  boolean isFlsh2 = false;
    private Handler mHandler;
    private int p =1;
    private List<Departmentwork.ReqDepartmentWork.OfficerMap> fileList2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kyfc, container, false);
        ButterKnife.bind(this, view);
        cid =getArguments().getString("pid");
        keyuanList.setPullRefreshEnable(true);
        keyuanList.setPullLoadEnable(true);
        keyuanList.setXListViewListener(this);
        keyuanList.setOnScrollListener(this);
        mHandler = new Handler();
        //请求数据
        regRequest();
        
        return view;
    }

    /**
     * 根据不同的标识符加载网络的数据
     */
    public void regRequest() {
        Departmentwork.ReqDepartmentWork reqDocument  = Departmentwork.ReqDepartmentWork.newBuilder().setSid(User.sid).setAc("KYFC").setDcode(cid).setP(p+"").build();
        new HttpUtilDocumentw() {
            @Override
            public <T> void analysisInputStreamData(Departmentwork.ReqDepartmentWork index) throws IOException {

                fileList = index.getOlistList();
                Log.i("zhang", "列表个数" + fileList.size());
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(getActivity(), df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    keyuanList.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
                    keyuanList.stopRefresh();

                    keyuanList.stopLoadMore();

                } else {
                    if (isFlsh){
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数");
                        Log.i("zhang", "列表个数" + fileList.size());
                        resultAdapter = new CommonAdapter<Departmentwork.ReqDepartmentWork.OfficerMap>(getActivity(), fileList, R.layout.kskyfc_item) {
                            @Override
                            public void convert(ViewHolder holder, Departmentwork.ReqDepartmentWork.OfficerMap fileMap) {
                                //下载图片
                                ImageLoader.getInstance().displayImage(fileMap.getPurl(), holder.<ImageView>getView(R.id.keshi_fc_iamge));
                                holder.setText(R.id.keshi_fc_title,fileMap.getOn()).setText(R.id.keshi_fc_dihua,fileMap.getPhone())
                                        .setText(R.id.keshi_fc_keshi,fileMap.getDname()).setText(R.id.keshi_fc_shengri,fileMap.getDt())
                                .setText(R.id.keshi_fc_zhiwei,fileMap.getZhiwu());
                            }
                        };
                        keyuanList.setAdapter(resultAdapter);
                    }else{
                        if (fileList.size()!=0){
                            Log.i("zhang", "列表个数" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数2" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }


            }


        }.protocolBuffer(getActivity(), Globals.KS_URI, reqDocument, null);

    }

    /**模拟加载更多*/
    private void onLoad() {
        Log.i("zhang", "onLoad: ");

        keyuanList.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
        keyuanList.stopRefresh();
        keyuanList.stopLoadMore();

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
            onLoad();
            isFlsh2=false;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
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

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
