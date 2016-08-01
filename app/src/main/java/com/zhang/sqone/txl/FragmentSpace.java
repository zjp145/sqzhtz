package com.zhang.sqone.txl;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
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
import com.zhang.sqone.views.CircularImage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通讯录模块
 *
 * @author ZJP
 *         created at 2016/2/1 13:44
 */
public class FragmentSpace extends Fragment implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener, TextView.OnEditorActionListener {
    @Bind(R.id.search_et_tz)
    EditText searchEtTz;
    @Bind(R.id.txl_list)
    PullToRefreshSwipeMenuListView txlList;

    /**加载数据*/
    private Handler mHandler;

    private boolean flsh = false;
    /**科室列表的详情数据*/
    private List<Noticecontacts.ReqNoticeAndContacts.ContactMap> fileList = new ArrayList<>();
//
    /**科室列表的适配器*/
    private CommonAdapter<Noticecontacts.ReqNoticeAndContacts.ContactMap> resultAdapter;
    /**控制列表中的页数*/
    private int p = 1;
    /**控制数据是加载 还是刷新*/
    private  boolean isFlsh = true;
    /**判断是不是滑动到底部*/
    private  boolean isFlsh2 = false;
    private String srting = "";
    private List<Noticecontacts.ReqNoticeAndContacts.ContactMap> fileList2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //锁定屏幕
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        ButterKnife.bind(this, view);

        txlList.setPullRefreshEnable(true);
        txlList.setPullLoadEnable(true);
        txlList.setXListViewListener(this);
        txlList.setOnScrollListener(this);
        regRequest(srting);
        mHandler = new Handler();
        searchEtTz.setOnEditorActionListener(this);
        return view;
    }

    /**
     //     * 根据不同的标识符加载网络的数据
     //     */
    public void regRequest(String s ) {
        Noticecontacts.ReqNoticeAndContacts reqDocument  = Noticecontacts.ReqNoticeAndContacts.newBuilder().setSid(User.sid).setAc("TXQBLLB").setP(p+"").setTj(s).build();
        new HttpUtilNot() {
            @Override
            public <T> void analysisInputStreamData(Noticecontacts.ReqNoticeAndContacts index) throws IOException {
                fileList = index.getClistList();
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    Log.i("zhang", "页数" + p);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(getActivity(), df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    txlList.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
                    txlList.stopRefresh();
                    txlList.stopLoadMore();
                    flsh= false;

                } else {
                    if (isFlsh){
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数首" + fileList.size());
                        resultAdapter = new CommonAdapter<Noticecontacts.ReqNoticeAndContacts.ContactMap>(getActivity(), fileList, R.layout.txl_item) {
                            @Override
                            public void convert(ViewHolder holder, final Noticecontacts.ReqNoticeAndContacts.ContactMap fileMap) {

                                holder.setText(R.id.f_txl_xm, fileMap.getPn())
                                        .setText(R.id.f_txl_yx,fileMap.getPhone())
                                        .setText(R.id.f_txl_ks,fileMap.getDname());
                                if (fileMap.getPhone().equals("")){
                                    holder.setText(R.id.f_txl_yx,"没有设置电话");
                                    holder.setVisible(R.id.yonghu_phone,false);
                                }
                                holder.setOnClickListener(R.id.f_txl_dh, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        Uri data = Uri.parse("tel:" + fileMap.getPhone());
                                        intent.setData(data);
                                        startActivity(intent);
                                    }
                                });
                                holder.setOnClickListener(R.id.f_txl_duxi, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        doSendSMSTo(fileMap.getPhone(),"");
                                    }
                                });
                                //下载头像
                                ImageLoader.getInstance().displayImage(fileMap.getPurl(), holder.<CircularImage>getView(R.id.f_txl_iamge));
                            }
                        };
                        txlList.setAdapter(resultAdapter);
                    }else{
                        if (fileList.size()!=0){
                            Log.i("zhang", "列表个数刷新" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数2" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            if (p>1){

                            }else{
                                resultAdapter.setData(fileList);
                                resultAdapter.notifyDataSetChanged();
                            }
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }


            }


        }.protocolBuffer(getActivity(), Globals.TZTX_URI, reqDocument, null);


    }

    /**模拟加载更多*/
    private void onLoad() {
        Log.i("zhang", "onLoad: ");
        txlList.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
        txlList.stopRefresh();
        txlList.stopLoadMore();

    }
    /**下拉刷新数据*/
    public void onRefresh() {
        p=1;
        Log.i("zhang", "onRefresh: ");
        flsh = true;
        isFlsh=true;
        regRequest(srting);
    }
    /**点击加载更多*/
    public void onLoadMore() {
        if(p==1){
            if (fileList2.size() > 8) {
                p++;

                isFlsh = false;
                regRequest(srting);

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

                }else{
                    if(fileList2.size()>8){
                        p++;
                        flsh = false;
                        isFlsh= false;
                        regRequest(srting);
                    }else{
                        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 调起系统发短信功能
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber,String message){
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //以下方法防止两次发送请求
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_UP:
                    Log.i("zhang", "text===="+v.getText());
                    srting = v.getText().toString().trim();
                    p=1;
                    fileList2.clear();
                    isFlsh= false;
                    regRequest(srting);
                    txlList.setSelection(0);
                    return true;
                default:
                    return true;
            }
        }
        return false;

    }

    @Override
    public void onResume() {
        initLogin();
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
        }.protocolBuffer(getActivity(), Globals.WS_URI, index, null);

    }
}
