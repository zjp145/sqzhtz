package com.zhang.sqone.xiangqing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Meetingdiscuss;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.home.TxlActivity;
import com.zhang.sqone.utils.HttpUtilDocumentme;
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utils.XiaZaiUtil;
import com.zhang.sqone.views.ListViewForScrollView;
import com.zhang.sqone.views.MasterLayout;
import com.zhang.sqone.views.TitleBarView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BanZiXqActivity extends BaseActivity {


    @Bind(R.id.xq_title)
    TitleBarView xqTitle;
    @Bind(R.id.zhuguan_text)
    TextView zhuguanText;
    @Bind(R.id.yiti_mc)
    EditText yitiMc;
    @Bind(R.id.male)
    RadioButton male;
    @Bind(R.id.female)
    RadioButton female;
    @Bind(R.id.sex)
    RadioGroup sex;
    @Bind(R.id.yuangyi)
    EditText yuangyi;
    @Bind(R.id.q_xiangqing_list)
    ListViewForScrollView qXiangqingList;
    @Bind(R.id.bianji1)
    LinearLayout bianji1;
    @Bind(R.id.bianji2)
    LinearLayout bianji2;
    @Bind(R.id.yiti_mc1)
    TextView yitiMc1;
    @Bind(R.id.shangchuan_button)
    Button shangchuanButton;
    @Bind(R.id.iamge_tianjia)
    ImageView iamgeTianjia;
    @Bind(R.id.zguz_mc)
    LinearLayout zguzMc;
    @Bind(R.id.yuangyi2)
    TextView yuangyi2;
    private Bundle ba;
    private Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap fileMap;
    private String xqid;
    private List<Meetingdiscuss.ReqMeetingDiscuss.FileList> zhang;
    private CommonAdapter<Meetingdiscuss.ReqMeetingDiscuss.FileList> ja;
    /**
     * 是不是新增议题
     */
    private String bsf;
    private String isXuanzhong;
    private Bundle bundle2;
    private String strFromAct2;
    /**
     * 是不是四大领导
     */
    private String ld;
    /*请求标识符*/
    private String bsfs;
    /*是审核还是查询*/
    private String bsf2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ban_zi_xq);
        ButterKnife.bind(this);
        xqTitle.setClickEnterButtonListener(new TitleBarView.OnClickEnterButtonListener() {
                                                @Override
                                                public void onClickEnterButton(View v) {
                                                    Log.i("zhang", "点击的是保存的按钮");
                                                    Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap.Builder builder = Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap.newBuilder();
                                                    if (yitiMc.getText().toString().trim().equals("") || zhuguanText.getText().toString().trim().equals("")) {
                                                        Toast.makeText(BanZiXqActivity.this, "会议名称和主管副职是必填项", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        if (bsf.equals("2")) {
                                                            builder.setId(fileMap.getId());
                                                            builder.setYtmc(yitiMc.getText().toString().trim());
                                                            builder.setZgfz(zhuguanText.getText().toString().trim());
                                                        } else {
                                                            Log.i("zhang", fileMap.getId());
                                                            builder.setId(fileMap.getId());
                                                            builder.setYtmc(yitiMc.getText().toString().trim());
                                                            builder.setQrstate(isXuanzhong);
                                                            builder.setExplain(yuangyi.getText().toString().trim());
                                                        }

                                                        Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc(bsfs).setMcddetail(builder).build();
                                                        new HttpUtilDocumentme() {
                                                            @Override
                                                            public <T> void analysisInputStreamData(Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
                                                                if (index.getScd().equals("1")) {
                                                                    Toast.makeText(BanZiXqActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(BanZiXqActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        }.protocolBuffer(BanZiXqActivity.this, Globals.BZHY_URI, reqDocument, null);
                                                    }
                                                }
                                            }
        );

        bsf = getIntent().getStringExtra("bsf");
        bsf2 = getIntent().getStringExtra("bsf2");
        fileMap = (Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap) getIntent().getSerializableExtra(Globals.LIDS);
        //是新增议题
        if (bsf.equals("2")) {
            xqTitle.setText("新增议题");

//            fileMap = (Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap) getIntent().getSerializableExtra(Globals.LIDS);
            ld = getIntent().getStringExtra("ld");
            if (ld.equals("0")) {
                if (bsf2.equals("3")) {
                    zguzMc.setVisibility(View.GONE);
                    Log.i("zhang", "1111");
                    bsfs = "BZSBNEWSAVE";
//                    fileMap = (Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap) getIntent().getSerializableExtra(Globals.LIDS);
                    zhuguanText.setText(fileMap.getZgfz());
                } else {
                    Log.i("zhang", "2222");
                    bsfs = "BZHSHXQBJ";
                    String mc = getIntent().getStringExtra("mc");
                    zhuguanText.setText(mc);
//                    zhuguanText.setVisibility(View.GONE);
                    iamgeTianjia.setVisibility(View.VISIBLE);
                    iamgeTianjia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent1 = new Intent();
                            intent1.putExtra("bzf", "3");
                            intent1.setClass(BanZiXqActivity.this, TxlActivity.class);
                            startActivityForResult(intent1, 0);
                        }
                    });

                }

            } else {
                if (bsf2.equals("3")) {
                    zguzMc.setVisibility(View.GONE);
                    bsfs = "BZSBNEWSAVE";
                } else {
                    bsfs = "BZHSHXQBJ";
                }

                if (User.sid.equals("wangjiang")) {
                    zhuguanText.setText("王江");
                } else if (User.sid.equals("yangkengke")) {
                    zhuguanText.setText("杨登科");
                } else if (User.sid.equals("qiaolong")) {

                    zhuguanText.setText("乔龙");
                } else if (User.sid.equals("wulianjun")) {
                    zhuguanText.setText("吴连军");
                }

            }
            yitiMc.setText(fileMap.getYtmc());
            bianji1.setVisibility(View.GONE);
            bianji2.setVisibility(View.GONE);
            yitiMc1.setVisibility(View.GONE);
            zhang = fileMap.getFilelistList();
            Log.i("zhang", "附件个数 " + zhang.size());
            //创建适配器对布局控件添加属性
            ja = new CommonAdapter<Meetingdiscuss.ReqMeetingDiscuss.FileList>(BanZiXqActivity.this, zhang, R.layout.xia_zai_item) {
                @Override
                public void convert(ViewHolder holder, Meetingdiscuss.ReqMeetingDiscuss.FileList ceshi) {
                    Log.i("zhang", "下载地址 " + ceshi.getUrl() + ceshi.getDx());
                    //添加文字
                    holder.setText(R.id.xiazai_item_dx, ceshi.getName());
                    if (!ceshi.getDx().equals("")) {
                        int i = Integer.parseInt(ceshi.getDx());
                        int i2 = i / 1024 + 1;
                        if (i2 > 1000) {
                            int i3 = i2 / 1024;
                            if (i3 == 0) {
                                i3++;
                            }
                            holder.setText(R.id.wenjian_dx, i3 + "MB");

                        } else {
                            holder.setText(R.id.wenjian_dx, i2 + "KB");
                        }
                    }

                    final String filename = ceshi.getName();
                    if (SharedPreferencesUtils.contains(BanZiXqActivity.this,ceshi.getFpk())) {
                        ImageButton imagebutton = holder.<ImageButton>getView(R.id.xiazai_iamge_button);
                        MasterLayout masterLayout = holder.<MasterLayout>getView(R.id.xiazai_zx);
                        TextView textView = holder.<TextView>getView(R.id.xiazai_sudu);
                        masterLayout.setVisibility(View.GONE);
                        imagebutton.setVisibility(View.VISIBLE);
                        imagebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = getFileIntent(new File(Environment.getExternalStorageDirectory() + "/zhtzwj/" + filename));
                                BanZiXqActivity.this.startActivity(intent);
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
//                        Log.i("zhang", "sd卡的路径：" + Environment.getExternalStorageDirectory().getPath()+"/"+filename);
                    //下载动画效果的帮助类  完成动态下载的效果
                    XiaZaiUtil xiaZaiUtil = new XiaZaiUtil(holder.<ImageButton>getView(R.id.xiazai_iamge_button), BanZiXqActivity.this, ceshi.getUrl(), holder.<MasterLayout>getView(R.id.xiazai_zx), holder.<TextView>getView(R.id.xiazai_sudu), ceshi.getName(),ceshi.getFpk());
//                        Log.i("zhang", ceshi.getFt());
                    String ft = ceshi.getFt();
                    if (ft.equals("doc") || ft.equals("docx")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.word);
                    } else if (ft.equals("txt")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.txt);
                    } else if (ft.equals("png")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.png);
                    } else if (ft.equals("xls") || ft.equals("xlsx")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.excel);
                    } else if (ft.equals("pdf")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.pdf);
                    } else if (ft.equals("ppt") || ft.equals("pptx")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.ppt);
                    }
                }

            };
            qXiangqingList.setAdapter(ja);

        }
//        不同模块的编辑
        else {

            if (bsf2.equals("1")) {
                bsfs = "BZHSHXQBC";
                if (User.sid.equals("yangchunhua") || User.sid.equals("mazhiyong")) {
                    bianji1.setVisibility(View.GONE);
                    bianji2.setVisibility(View.GONE);
                }
                male.setText("通过");
                female.setText("不通过");
            } else if (bsf2.equals("2")) {
                bsfs = "BZHCXXQBC";
            } else if (bsf2.equals("3")) {
                bianji1.setVisibility(View.GONE);
                bianji2.setVisibility(View.GONE);
                bsfs = "BZSBBJSAVE";

            }
//            fileMap = (Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap) getIntent().getSerializableExtra(Globals.LIDS);
            Log.i("zhang", "我的文件id" + fileMap.getFjids());

            zhuguanText.setText(fileMap.getZgfz());
            yitiMc.setText(fileMap.getYtmc());
            yuangyi.setText(fileMap.getExplain());
            sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (male.getId() == i) {
                        Log.i("zhang", "选中1");
                        isXuanzhong = "2";
                    }
                    if (female.getId() == i) {
                        Log.i("zhang", "未选中1");
                        isXuanzhong = "3";
                    }
                }
            });
            if (fileMap.getQrstate().equals("3")) {
                female.setChecked(true);
            } else {
                male.setChecked(true);
            }
            zhang = fileMap.getFilelistList();
            Log.i("zhang", "附件个数 " + zhang.size());
            //创建适配器对布局控件添加属性
            ja = new CommonAdapter<Meetingdiscuss.ReqMeetingDiscuss.FileList>(BanZiXqActivity.this, zhang, R.layout.xia_zai_item) {
                @Override
                public void convert(ViewHolder holder, Meetingdiscuss.ReqMeetingDiscuss.FileList ceshi) {
                    Log.i("zhang", "下载地址 " + ceshi.getUrl() + ceshi.getDx());
                    //添加文字
                    holder.setText(R.id.xiazai_item_dx, ceshi.getName());
                    if (!ceshi.getDx().equals("")) {
                        int i = Integer.parseInt(ceshi.getDx());
                        int i2 = i / 1024 + 1;
                        if (i2 > 1000) {
                            int i3 = i2 / 1024;
                            if (i3 == 0) {
                                i3++;
                            }
                            holder.setText(R.id.wenjian_dx, i3 + "MB");

                        } else {
                            holder.setText(R.id.wenjian_dx, i2 + "KB");
                        }
                    }

                    final String filename = ceshi.getName();
                    if (SharedPreferencesUtils.contains(BanZiXqActivity.this,ceshi.getFpk())) {
                        ImageButton imagebutton = holder.<ImageButton>getView(R.id.xiazai_iamge_button);
                        MasterLayout masterLayout = holder.<MasterLayout>getView(R.id.xiazai_zx);
                        TextView textView = holder.<TextView>getView(R.id.xiazai_sudu);
                        masterLayout.setVisibility(View.GONE);
                        imagebutton.setVisibility(View.VISIBLE);
                        imagebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = getFileIntent(new File(Environment.getExternalStorageDirectory() + "/zhtzwj/" + filename));
                                BanZiXqActivity.this.startActivity(intent);
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
//                        Log.i("zhang", "sd卡的路径：" + Environment.getExternalStorageDirectory().getPath()+"/"+filename);
                    //下载动画效果的帮助类  完成动态下载的效果
                    XiaZaiUtil xiaZaiUtil = new XiaZaiUtil(holder.<ImageButton>getView(R.id.xiazai_iamge_button), BanZiXqActivity.this, ceshi.getUrl(), holder.<MasterLayout>getView(R.id.xiazai_zx), holder.<TextView>getView(R.id.xiazai_sudu), ceshi.getName(),ceshi.getFpk());
//                        Log.i("zhang", ceshi.getFt());
                    String ft = ceshi.getFt();
                    if (ft.equals("doc") || ft.equals("docx")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.word);
                    } else if (ft.equals("txt")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.txt);
                    } else if (ft.equals("png")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.png);
                    } else if (ft.equals("xls") || ft.equals("xlsx")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.excel);
                    } else if (ft.equals("pdf")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.pdf);
                    } else if (ft.equals("ppt") || ft.equals("pptx")) {
                        holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.ppt);
                    }
                }

            };
            qXiangqingList.setAdapter(ja);
        }
        if (bsf.equals("0")) {
            bianji1.setVisibility(View.GONE);
            if(bsf2.equals("1")){
                bianji2.setVisibility(View.GONE);
            }else{
                bianji2.setVisibility(View.VISIBLE);
                yuangyi.setVisibility(View.GONE);
                yuangyi2.setVisibility(View.VISIBLE);
                yuangyi2.setText(fileMap.getExplain());
            }
            if(bsf2.equals("3")){

                if(!fileMap.getZgfz().equals(fileMap.getXm())){
                    bianji2.setVisibility(View.GONE);
                }
            }
            /**隐藏标题确定按钮*/
            xqTitle.imbEnter.setVisibility(View.GONE);
            yitiMc1.setVisibility(View.VISIBLE);
            yitiMc1.setText(fileMap.getYtmc());
            yitiMc.setVisibility(View.GONE);
            shangchuanButton.setVisibility(View.GONE);
        }
        shangchuanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BanZiXqActivity.this, ShangChuanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Globals.LIDS, fileMap);
                bundle.putString("bsf", bsf2);
                bundle.putString("bsf2", bsf);
                bundle.putString("ld", ld);
                bundle.putString("bsfs", yitiMc.getText().toString().trim());
                bundle.putString("mc", zhuguanText.getText().toString().trim());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (0 == requestCode) {
            if (!(data == null)) {
                bundle2 = data.getBundleExtra("bundle2");
                strFromAct2 = bundle2.getString("strResult");
                zhuguanText.setText(strFromAct2);
                zhuguanText.setVisibility(View.VISIBLE);
            }
        }
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
