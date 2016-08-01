package com.zhang.sqone.xiangqing;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Meetingroom;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.HttpUtilDocumentwh;
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utils.XiaZaiUtil;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.ListViewForScrollView;
import com.zhang.sqone.views.MasterLayout;
import com.zhang.sqone.views.TitleBarView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HYCLXQActivity extends BaseActivity {

    @Bind(R.id.huiyi_yuding)
    TitleBarView huiyiYuding;
    @Bind(R.id.cc_sqbm)
    TextView ccSqbm;
    @Bind(R.id.cc_hyssqr)
    TextView ccHyssqr;
    @Bind(R.id.cc_sqsj)
    TextView ccSqsj;
    @Bind(R.id.cc_hyrq)
    TextView ccHyrq;
    @Bind(R.id.cc_hymc)
    TextView ccHymc;
    @Bind(R.id.cc_hymm)
    TextView ccHymm;
    @Bind(R.id.cc_yqld)
    TextView ccYqld;
    @Bind(R.id.cc_cxry)
    TextView ccCxry;
    @Bind(R.id.cc_sb)
    TextView ccSb;
    @Bind(R.id.cc_qtxq)
    TextView ccQtxq;
    @Bind(R.id.cc_xiangqing_list)
    ListViewForScrollView ccXiangqingList;
    private Meetingroom.ReqMeetingRoom.meetingMaterialMap fileMap;
    private String id;
    private String meetingid;
    private Meetingroom.ReqMeetingRoom.meetingMaterialDeail mmd;
    private List<Meetingroom.ReqMeetingRoom.DataFileList> filelistList;
    private CommonAdapter<Meetingroom.ReqMeetingRoom.DataFileList> ja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_hyclxq);

        ButterKnife.bind(this);
        fileMap = (Meetingroom.ReqMeetingRoom.meetingMaterialMap) getIntent().getSerializableExtra(Globals.LIDS);
        id = fileMap.getId();
        meetingid = fileMap.getMeetingid();
        regRequest();

    }

    /**
     * 根据不同的标识符加载网络的数据
     */
    public void regRequest() {
        Meetingroom.ReqMeetingRoom.meetingMaterialDeail.Builder builder = Meetingroom.ReqMeetingRoom.meetingMaterialDeail.newBuilder();
        builder.setId(id);
        builder.setMeetingid(meetingid);
        Meetingroom.ReqMeetingRoom reqDocument = Meetingroom.ReqMeetingRoom.newBuilder().setSid(User.sid).setMmd(builder).setAc("HYCLXQ").build();

        new HttpUtilDocumentwh() {
            @Override
            public <T> void analysisInputStreamData(Meetingroom.ReqMeetingRoom index) throws IOException {
                mmd = index.getMmd();
                ccSqbm.setText(mmd.getSqdeptname());
                ccHyssqr.setText(mmd.getScheduleer());
                ccSqsj.setText(mmd.getCreatetime());
                ccHyrq.setText(mmd.getMeetingdate());
                ccHymc.setText(mmd.getName());
                ccHymm.setText(mmd.getDescribe());
                ccYqld.setText(mmd.getJoinleader());
                ccCxry.setText(mmd.getMeetingpeople());
                ccSb.setText(mmd.getEquipment());
                ccQtxq.setText(mmd.getService());
                filelistList = mmd.getFilelistList();
                ja = new CommonAdapter<Meetingroom.ReqMeetingRoom.DataFileList>(HYCLXQActivity.this, filelistList, R.layout.xia_zai_item) {
                    @Override
                    public void convert(ViewHolder holder, Meetingroom.ReqMeetingRoom.DataFileList ceshi) {
                        //添加文字
                        holder.setText(R.id.xiazai_item_dx, ceshi.getName());
                        Log.i("zhang", "下载的地址111" + ceshi.getUrl());
                        final String filename = ceshi.getName();
                        if (SharedPreferencesUtils.contains(HYCLXQActivity.this,ceshi.getFpk())) {
                            ImageButton imagebutton = holder.<ImageButton>getView(R.id.xiazai_iamge_button);
                            MasterLayout masterLayout = holder.<MasterLayout>getView(R.id.xiazai_zx);
                            TextView textView = holder.<TextView>getView(R.id.xiazai_sudu);
                            masterLayout.setVisibility(View.GONE);
                            imagebutton.setVisibility(View.VISIBLE);
                            imagebutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = getFileIntent(new File(Environment.getExternalStorageDirectory() + "/zhtzwj/" + filename));
                                    HYCLXQActivity.this.startActivity(intent);
                                }
                            });
                            textView.setVisibility(View.GONE);
//                            Log.i("zhang", "有文件");

                        } else {
                            ImageButton imagebutton = holder.<ImageButton>getView(R.id.xiazai_iamge_button);
                            MasterLayout masterLayout = holder.<MasterLayout>getView(R.id.xiazai_zx);
                            TextView textView = holder.<TextView>getView(R.id.xiazai_sudu);
                            masterLayout.setVisibility(View.VISIBLE);
                            imagebutton.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            Log.i("zhang", "没有文件");
                        }

                        //下载动画效果的帮助类  完成动态下载的效果
                        XiaZaiUtil xiaZaiUtil = new XiaZaiUtil(holder.<ImageButton>getView(R.id.xiazai_iamge_button), HYCLXQActivity.this, ceshi.getUrl(), holder.<MasterLayout>getView(R.id.xiazai_zx), holder.<TextView>getView(R.id.xiazai_sudu),ceshi.getName(),ceshi.getFpk());
//                        Log.i("zhang", ceshi.getFt());
                        String ft = ceshi.getFt();
                        if (ft.equals("doc") || ft.equals("docx")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.word);

                        } else if (ft.equals("xls") || ft.equals("xlsx")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.excel);
                        } else if (ft.equals("png")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.png);
                        } else if (ft.equals("txt")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.txt);
                        } else if (ft.equals("pdf")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.pdf);
                        } else if (ft.equals("ppt") || ft.equals("pptx")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.ppt);
                        }
                    }
                };
                //给list添加数据
                ccXiangqingList.setAdapter(ja);
            }


        }.protocolBuffer(HYCLXQActivity.this, Globals.HYAP_URI, reqDocument, null);

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
