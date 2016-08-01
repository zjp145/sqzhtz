package com.zhang.sqone.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utils.XiaZaiUtil;
import com.zhang.sqone.views.MasterLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 天竺期刊界面
 *
 * @author ZJP
 *         created at 16/3/29 下午5:40
 */
public class TzqkFragment extends Fragment implements PullToRefreshSwipeMenuListView.IXListViewListener, AbsListView.OnScrollListener {

    /**列表使用的list*/
    @Bind(R.id.qikan_list)
    PullToRefreshSwipeMenuListView qikanList;
    /**数据集合*/
    private List<Departmentwork.ReqDepartmentWork.PeriodicalMap> fileList = new ArrayList<>();;
    /**刷新状态*/
    private boolean flsh=false;
    /**适配器*/
    private CommonAdapter<Departmentwork.ReqDepartmentWork.PeriodicalMap> resultAdapter;
    /**刷新和加载的状态*/
    private boolean isFlsh  = true;
    /**请求数据*/
    private String cid;
    private Handler mHandler;
    private int p =1;
    /**判断是不是滑动到底部*/
    private  boolean isFlsh2 = false;
    private List<Departmentwork.ReqDepartmentWork.PeriodicalMap> fileList2 = new ArrayList<>();;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tzqk, container, false);
        ButterKnife.bind(this, view);
        cid =getArguments().getString("pid");
        qikanList.setPullRefreshEnable(true);
        qikanList.setPullLoadEnable(true);
        qikanList.setXListViewListener(this);
        qikanList.setOnScrollListener(this);
//        请求数据
        regRequest();
        mHandler = new Handler();
        return view;


    }

    /**
     * 根据不同的标识符加载网络的数据
     */
    public void regRequest() {
        Departmentwork.ReqDepartmentWork reqDocument  = Departmentwork.ReqDepartmentWork.newBuilder().setSid(User.sid).setAc("QKLB").setP(p+"").setDcode(cid).build();
        new HttpUtilDocumentw() {
            @Override
            public <T> void analysisInputStreamData(Departmentwork.ReqDepartmentWork index) throws IOException {
                fileList = index.getPlistList();
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(getActivity(), df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    qikanList.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
                    qikanList.stopRefresh();
                    qikanList.stopLoadMore();

                } else {
                    if (isFlsh){
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数" + fileList.size());
//                        Log.i("zhang", "链接" + fileList.get(1).getFurl());
                        resultAdapter = new CommonAdapter<Departmentwork.ReqDepartmentWork.PeriodicalMap>(getActivity(), fileList, R.layout.tzqk_lb_item) {
                            @Override
                            public void convert(ViewHolder holder, Departmentwork.ReqDepartmentWork.PeriodicalMap fileMap) {
                                final String filename = fileMap.getFurl().substring(fileMap.getFurl().lastIndexOf('/') + 1);
                                if(SharedPreferencesUtils.contains(getActivity(),fileMap.getFpk())){
                                    ImageButton imagebutton = holder.<ImageButton>getView(R.id.tzqk_dk_iamge);
                                    MasterLayout masterLayout = holder.<MasterLayout>getView(R.id.qikan_xz);
                                    TextView textView = holder.<TextView>getView(R.id.tzqk_dx);
                                    masterLayout.setVisibility(View.GONE);
                                    imagebutton.setVisibility(View.VISIBLE);
                                    imagebutton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = getFileIntent(new File(Environment.getExternalStorageDirectory() + "/zhtzwj/" + filename));
                                            getActivity().startActivity(intent);
                                        }
                                    });
                                    textView.setVisibility(View.GONE);
//                            Log.i("zhang", "有文件");

                                }else{
                                    ImageButton imagebutton = holder.<ImageButton>getView(R.id.xiazai_iamge_button);
                                    MasterLayout masterLayout = holder.<MasterLayout>getView(R.id.xiazai_zx);
                                    TextView textView = holder.<TextView>getView(R.id.xiazai_sudu);
                                    masterLayout.setVisibility(View.VISIBLE);
                                    imagebutton.setVisibility(View.GONE);
                                    textView.setVisibility(View.VISIBLE);
                                    Log.i("zhang", "没有文件");
                                }
                                //下载动画效果的帮助类  完成动态下载的效果
                                XiaZaiUtil xiaZaiUtil = new XiaZaiUtil(holder.<ImageButton>getView(R.id.tzqk_dk_iamge), getActivity(), fileMap.getFurl(), holder.<MasterLayout>getView(R.id.qikan_xz), holder.<TextView>getView(R.id.tzqk_dx),fileMap.getGtr(),fileMap.getFpk());

                                //下载图片
                                ImageLoader.getInstance().displayImage(fileMap.getPic(), holder.<ImageView>getView(R.id.tzqk_iamge));
                                holder.setText(R.id.tzqk_title_text,fileMap.getPn()).setText(R.id.tzqk_gt,fileMap.getGtr())
                                        .setText(R.id.tzqk_zz,fileMap.getAt()).setText(R.id.tzqk_t,fileMap.getPt());
                            }
                        };
                        qikanList.setAdapter(resultAdapter);
                        Log.i("zhang", "适配个数"+ resultAdapter.getCount());
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**模拟加载更多*/
    private void onLoad() {
        Log.i("zhang", "onLoad: ");

        qikanList.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
        qikanList.stopRefresh();
        qikanList.stopLoadMore();

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
                Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        }
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
    /**
     * 获取文件格式 返回的使用打开文件的intent
     */
    public Intent getFileIntent(File file) {
        //Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    /**
     * 判断文件的格式使用不同的action打开文件
     */
    private String getMIMEType(File f) {
        //如果没有匹配的打开文件的情况现在所有的程序打开文件
        String type = "*/*";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;

    }
    private final String[][] MIME_MapTable = {
            //{后缀名， MIME类型}
            {"3gp", "video/3gpp"},
            {"apk", "application/vnd.android.package-archive"},
            {"asf", "video/x-ms-asf"},
            {"avi", "video/x-msvideo"},
            {"bin", "application/octet-stream"},
            {"bmp", "image/bmp"},
            {"c", "text/plain"},
            {"class", "application/octet-stream"},
            {"conf", "text/plain"},
            {"cpp", "text/plain"},
            {"doc", "application/msword"},
            {"docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"xls", "application/vnd.ms-excel"},
            {"xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"exe", "application/octet-stream"},
            {"gif", "image/gif"},
            {"gtar", "application/x-gtar"},
            {"gz", "application/x-gzip"},
            {"h", "text/plain"},
            {"htm", "text/html"},
            {"html", "text/html"},
            {"jar", "application/java-archive"},
            {"java", "text/plain"},
            {"jpeg", "image/jpeg"},
            {"jpg", "image/jpeg"},
            {"js", "application/x-javascript"},
            {"log", "text/plain"},
            {"m3u", "audio/x-mpegurl"},
            {"m4a", "audio/mp4a-latm"},
            {"m4b", "audio/mp4a-latm"},
            {"m4p", "audio/mp4a-latm"},
            {"m4u", "video/vnd.mpegurl"},
            {"m4v", "video/x-m4v"},
            {"mov", "video/quicktime"},
            {"mp2", "audio/x-mpeg"},
            {"mp3", "audio/x-mpeg"},
            {"mp4", "video/mp4"},
            {"mpc", "application/vnd.mpohun.certificate"},
            {"mpe", "video/mpeg"},
            {"mpeg", "video/mpeg"},
            {"mpg", "video/mpeg"},
            {"mpg4", "video/mp4"},
            {"mpga", "audio/mpeg"},
            {"msg", "application/vnd.ms-outlook"},
            {"ogg", "audio/ogg"},
            {"pdf", "application/pdf"},
            {"png", "image/png"},
            {"pps", "application/vnd.ms-powerpoint"},
            {"ppt", "application/vnd.ms-powerpoint"},
            {"pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {"prop", "text/plain"},
            {"rc", "text/plain"},
            {"rmvb", "audio/x-pn-realaudio"},
            {"rtf", "application/rtf"},
            {"sh", "text/plain"},
            {"tar", "application/x-tar"},
            {"tgz", "application/x-compressed"},
            {"txt", "text/plain"},
            {"wav", "audio/x-wav"},
            {"wma", "audio/x-ms-wma"},
            {"wmv", "audio/x-ms-wmv"},
            {"wps", "application/vnd.ms-works"},
            {"xml", "text/plain"},
            {"z", "application/x-compress"},
            {"zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };


}
