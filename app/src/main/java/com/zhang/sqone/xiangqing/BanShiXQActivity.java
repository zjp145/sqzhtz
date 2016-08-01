package com.zhang.sqone.xiangqing;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Departmentworkdoc;
import com.zhang.sqone.bean.Document;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.dbgw.BsFargment;
import com.zhang.sqone.dbgw.H5Fargment;
import com.zhang.sqone.utils.HttpUtilDocumentwc;
import com.zhang.sqone.utils.HttputilDocument;
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utils.XiaZaiUtil;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.ListViewForScrollView;
import com.zhang.sqone.views.MasterLayout;
import com.zhang.sqone.views.TitleBarView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 办事详情界面
 *
 * @author ZJP
 *         created at 2016/3/4 11:22
 */
public class BanShiXQActivity extends FragmentActivity implements View.OnClickListener

{

    @Bind(R.id.banshi_xiangqing_title)
    /**办事详情title*/
            TitleBarView banshiXiangqingTitle;
    @Bind(R.id.banshi_title)
    /**item 标题*/
            TextView banshiTitle;
    @Bind(R.id.banshi_tishi)
    /**item  提示*/
            TextView banshiTishi;
    @Bind(R.id.banshi_time)
    /**item 的时间*/
            TextView banshiTime;
    @Bind(R.id.banshi_moren)
    /**item 发送人的图标*/
            ImageView banshiMoren;
    @Bind(R.id.banshi_name_f)
    /**发送人的人名*/
            TextView banshiNameF;
    @Bind(R.id.banshi_name_d)
    /**递交人的人名*/
            TextView banshiNameD;
    @Bind(R.id.shoucang_iamge)
    /**收藏图片*/
            ImageView shoucangIamge;
    @Bind(R.id.banshi_xiangqiang_text)
    /**选择 文字中的 详情*/
            TextView banshiXiangqiangText;
    @Bind(R.id.banshi_liuzhuan_text)
    /**选择文字中的 流转跟踪*/
            TextView banshiLiuzhuanText;
    @Bind(R.id.banshi_view)
    /**分割线*/
            View banshiView;
    @Bind(R.id.banshi_xianshi)
    /**单击切换的布局*/
            FrameLayout banshiXianshi;
    @Bind(R.id.banshi_xl)
    LinearLayout banshiXl;
    //    @Bind(R.id.banshi_image)
//    /**流程标识*/
//            ImageView banshiImage;
    @Bind(R.id.banshi_layout)
    /**发  递人*/
            LinearLayout banshiLayout;
    @Bind(R.id.gw_xiangqing_list)
    /**详情列表使用的是list(下载功能)*/
            ListViewForScrollView gwXiangqingList;
    @Bind(R.id.bs_cy)
    /**流程标识*/
            TextView bsCy;
    @Bind(R.id.bs_tianshus)
    /**流程的天数*/
            TextView bsTianshus;
    @Bind(R.id.iamge_bsf)
    LinearLayout iamgeBsf;
    @Bind(R.id.lingdao_list)
    ListViewForScrollView lingdaoList;
    @Bind(R.id.keshifuzheren_list)
    ListViewForScrollView keshifuzherenList;
    @Bind(R.id.ld_list1)
    TextView ldList1;
    @Bind(R.id.ks_list1)
    TextView ksList1;

    /**
     * 判断选择中的是哪个
     */
    private boolean aBoolean = true;
    private boolean bBoolean = true;
    private BsFargment bsFargment;
    private Document.ReqDocument.fileMap fileMap;
    private int i;
    public String inid;
    public String wid;
    private H5Fargment h5Fargment;
    private Bundle ba;
    private String ss;
    /**
     * 文件的附件集合
     */
    private List<Document.ReqDocument.FileList> filelistList;

    /**
     * 文件的附件集合
     */
    private List<Departmentworkdoc.ReqDepartmentWorkDoc.FileList> filelistList2;
    /**
     * 附件适配器
     */
    private CommonAdapter<Document.ReqDocument.FileList> ja;
    /**
     * 附件适配器
     */
    private CommonAdapter<Departmentworkdoc.ReqDepartmentWorkDoc.FileList> ja2;
    private Departmentworkdoc.ReqDepartmentWorkDoc.fileMap fileMap2;
    private String baf;
    private CommonAdapter<String> jb;
    private CommonAdapter<String> jc;
    private String yjbsf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.banshi_xiangqing);
        ButterKnife.bind(this);
        //获得
        ba = getIntent().getExtras();
        i = ba.getInt(Globals.BDID);

        baf = ba.getString(Globals.BSF);


        if (i == 1) {
            banshiLayout.setVisibility(View.GONE);
        }

        if (baf.equals("bs")) {
            fileMap = (Document.ReqDocument.fileMap) getIntent().getSerializableExtra(Globals.LIDS);

            inid = fileMap.getInid();
            wid = fileMap.getWid();

            banshiTitle.setText(fileMap.getDn());
            banshiTime.setText(fileMap.getDt());
            banshiNameF.setText(fileMap.getSpna());
            banshiNameD.setText(fileMap.getAsna());
            banshiTishi.setText(fileMap.getFn());

            if (i == 2) {
                /**新版本添加图片上使用文字*/
                if (fileMap.getIst().equals("0")) {
                    bsCy.setText("余");
                    bsTianshus.setText(fileMap.getDays() + "天");
                    iamgeBsf.setBackgroundResource(R.drawable.gz_br_yu);
                } else {
                    bsCy.setText("超");
                    bsTianshus.setText(fileMap.getDays() + "天");
                    iamgeBsf.setBackgroundResource(R.drawable.gz_br);

                }
            } else {
                Log.i("zhang", "状态+++++++" + fileMap.getDs());
                if (fileMap.getDs().equals("已办")) {
                    bsCy.setText("已办");
                    bsTianshus.setVisibility(View.GONE);
                    iamgeBsf.setBackgroundResource(R.drawable.gz_br_yb);

                } else if (fileMap.getDs().equals("终止")) {
                    bsCy.setText("终止");
                    bsTianshus.setVisibility(View.GONE);
                    iamgeBsf.setBackgroundResource(R.drawable.gz_br_zz);
                } else if (fileMap.getDs().equals("重办")) {
                    bsCy.setText("重办");
                    bsTianshus.setVisibility(View.GONE);
                    iamgeBsf.setBackgroundResource(R.drawable.gz_br_cb);
                } else if (fileMap.getDs().equals("驳回")) {
                    bsCy.setText("驳回");
                    bsTianshus.setVisibility(View.GONE);
                    iamgeBsf.setBackgroundResource(R.drawable.gz_br_bh);
                } else if (fileMap.getDs().equals("取消")) {
                    bsCy.setText("取消");
                    bsTianshus.setVisibility(View.GONE);
                    iamgeBsf.setBackgroundResource(R.drawable.gz_br_qx);
//
                }

            }
//            if (fileMap.getDs().equals("已办")) {
//                banshiImage.setImageResource(R.mipmap.yiban);
//
//            } else if (fileMap.getDs().equals("终止")) {
//                banshiImage.setImageResource(R.mipmap.zhognzhi);
//
//            } else if (fileMap.getDs().equals("重办")) {
//                banshiImage.setImageResource(R.mipmap.chongban);
//
//            } else if (fileMap.getDs().equals("驳回")) {
//                banshiImage.setImageResource(R.mipmap.bohui);
//
//            } else if (fileMap.getDs().equals("取消")) {
//                banshiImage.setImageResource(R.mipmap.quxiao);
//
//            }

            if (fileMap.getIs().equals("0")) {
                //隐藏
                shoucangIamge.setVisibility(View.INVISIBLE);

            } else {
                //显示
                shoucangIamge.setVisibility(View.VISIBLE);
            }
            regRequest();
        }
        if (baf.equals("ks")) {
            fileMap2 = (Departmentworkdoc.ReqDepartmentWorkDoc.fileMap) getIntent().getSerializableExtra(Globals.LIDS);
            inid = fileMap2.getInid();
            wid = fileMap2.getWid();
            banshiTitle.setText(fileMap2.getDn());
            banshiTime.setText(fileMap2.getDt());
            banshiNameF.setText(fileMap2.getSpna());
            banshiNameD.setText(fileMap2.getAsna());
            banshiTishi.setText(fileMap2.getFn());
//            if (fileMap2.getDs().equals("已办")) {
//                banshiImage.setImageResource(R.mipmap.yiban);
//
//            } else if (fileMap2.getDs().equals("终止")) {
//                banshiImage.setImageResource(R.mipmap.zhognzhi);
//
//            } else if (fileMap2.getDs().equals("重办")) {
//                banshiImage.setImageResource(R.mipmap.chongban);
//
//            } else if (fileMap2.getDs().equals("驳回")) {
//                banshiImage.setImageResource(R.mipmap.bohui);
//
//            } else if (fileMap2.getDs().equals("取消")) {
//                banshiImage.setImageResource(R.mipmap.quxiao);
//
//            }

            //隐藏
            shoucangIamge.setVisibility(View.INVISIBLE);
            regRequest2();
        }
        onClicks();

    }

    private void onClicks() {
        banshiXiangqiangText.setOnClickListener(this);
        banshiLiuzhuanText.setOnClickListener(this);
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
     * 获得详情数据
     */
    public void regRequest() {

        //设置注册的发送实例
//        Index.ReqIndex.Builder sid = Index.ReqIndex.newBuilder();
//        sid.setSid(User.sid);
        Document.ReqDocument.param.Builder param = Document.ReqDocument.param.newBuilder();
        param.setInid(inid);
        param.setWid(wid);

        Document.ReqDocument reqDocument = Document.ReqDocument.newBuilder().setSid(User.sid).setAc("GWXQ").setPa(param).build();
        new HttputilDocument() {
            @Override
            public <T> void analysisInputStreamData(Document.ReqDocument index) throws IOException {

                ss = index.getPa().getH5Url();
                Log.i("zhang", "analysisInputStreamData " + ss);
                // 实例化Fragment页面
                h5Fargment = new H5Fargment();
                Bundle args = new Bundle();
                args.putString("pid", index.getPa().getH5Url());
                h5Fargment.setArguments(args);
                // 得到Fragment事务管理器
                FragmentTransaction fragmentTransaction = BanShiXQActivity.this
                        .getSupportFragmentManager().beginTransaction();
                // 替换当前的页面
                fragmentTransaction.replace(R.id.banshi_xianshi, h5Fargment);
                // 事务管理提交
                fragmentTransaction.commit();
                banshiXl.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                yjbsf=index.getPa().getStatus();
                if (yjbsf.equals("0")) {

                } else {
                    ldList1.setVisibility(View.VISIBLE);
                    ksList1.setVisibility(View.VISIBLE);
                    String[] b = index.getPa().getZgldyj().split(",");
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < b.length; i++) {
                        arrayList.add(b[i]);
                    }
                    jb = new CommonAdapter<String>(BanShiXQActivity.this, arrayList, R.layout.lingdaoyijian_litm) {
                        @Override
                        public void convert(ViewHolder holder, String s) {
                            holder.setText(R.id.lingdao_text, s);
                        }
                    };
                    lingdaoList.setAdapter(jb);
                    String[] c = index.getPa().getKsferyj().split(",");
                    ArrayList list = new ArrayList();
                    for (int i = 0; i < c.length; i++) {
                        list.add(c[i]);
                    }
                    Log.i("zhang", "科室负责人意见"+list.size());
                    jc = new CommonAdapter<String>(BanShiXQActivity.this, list, R.layout.lingdaoyijian_litm) {
                        @Override
                        public void convert(ViewHolder holder, String s) {
                            holder.setText(R.id.lingdao_text, s);
                        }
                    };
                    keshifuzherenList.setAdapter(jc);
                }

                filelistList = index.getPa().getFilelistList();
                ja = new CommonAdapter<Document.ReqDocument.FileList>(BanShiXQActivity.this, filelistList, R.layout.xia_zai_item) {
                    @Override
                    public void convert(ViewHolder holder, Document.ReqDocument.FileList ceshi) {
                        //添加文字
                        holder.setText(R.id.xiazai_item_dx, ceshi.getName());
                        Log.i("zhang", "下载的地址111" + ceshi.getUrl());
                        final String filename = ceshi.getName();
                        if (SharedPreferencesUtils.contains(BanShiXQActivity.this,ceshi.getFpk())) {
                            ImageButton imagebutton = holder.<ImageButton>getView(R.id.xiazai_iamge_button);
                            MasterLayout masterLayout = holder.<MasterLayout>getView(R.id.xiazai_zx);
                            TextView textView = holder.<TextView>getView(R.id.xiazai_sudu);
                            masterLayout.setVisibility(View.GONE);
                            imagebutton.setVisibility(View.VISIBLE);
                            imagebutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = getFileIntent(new File(Environment.getExternalStorageDirectory() + "/zhtzwj/" + filename));
                                    BanShiXQActivity.this.startActivity(intent);
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
                        XiaZaiUtil xiaZaiUtil = new XiaZaiUtil(holder.<ImageButton>getView(R.id.xiazai_iamge_button), BanShiXQActivity.this, ceshi.getUrl(), holder.<MasterLayout>getView(R.id.xiazai_zx), holder.<TextView>getView(R.id.xiazai_sudu),ceshi.getName(),ceshi.getFpk());
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
                gwXiangqingList.setAdapter(ja);

            }


        }.protocolBuffer(BanShiXQActivity.this, Globals.GW_URI, reqDocument, null);
    }


    /**
     * 获得详情数据
     */
    public void regRequest2() {

        //设置注册的发送实例
//        Index.ReqIndex.Builder sid = Index.ReqIndex.newBuilder();
//        sid.setSid(User.sid);
        Departmentworkdoc.ReqDepartmentWorkDoc.param.Builder param = Departmentworkdoc.ReqDepartmentWorkDoc.param.newBuilder();
        param.setInid(inid);
        param.setWid(wid);

        Departmentworkdoc.ReqDepartmentWorkDoc reqDocument = Departmentworkdoc.ReqDepartmentWorkDoc.newBuilder().setSid(User.sid).setAc("KSGZXQ").setPa(param).build();
        new HttpUtilDocumentwc() {
            @Override
            public <T> void analysisInputStreamData(Departmentworkdoc.ReqDepartmentWorkDoc index) throws IOException {

                ss = index.getPa().getH5Url();
                Log.i("zhang", "analysisInputStreamData " + ss);
                // 实例化Fragment页面
                h5Fargment = new H5Fargment();
                Bundle args = new Bundle();
                args.putString("pid", index.getPa().getH5Url());
                h5Fargment.setArguments(args);
                // 得到Fragment事务管理器
                FragmentTransaction fragmentTransaction = BanShiXQActivity.this
                        .getSupportFragmentManager().beginTransaction();
                // 替换当前的页面
                fragmentTransaction.replace(R.id.banshi_xianshi, h5Fargment);
                // 事务管理提交
                fragmentTransaction.commit();
                banshiXl.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                filelistList2 = index.getPa().getFilelistList();
                ja2 = new CommonAdapter<Departmentworkdoc.ReqDepartmentWorkDoc.FileList>(BanShiXQActivity.this, filelistList2, R.layout.xia_zai_item) {
                    @Override
                    public void convert(ViewHolder holder, Departmentworkdoc.ReqDepartmentWorkDoc.FileList ceshi) {
                        //添加文字
                        holder.setText(R.id.xiazai_item_dx, ceshi.getName());
                        Log.i("zhang", "下载的地址" + ceshi.getUrl());
                        final String filename = ceshi.getName();
                        if (SharedPreferencesUtils.contains(BanShiXQActivity.this,ceshi.getFpk())) {
                            ImageButton imagebutton = holder.<ImageButton>getView(R.id.xiazai_iamge_button);
                            MasterLayout masterLayout = holder.<MasterLayout>getView(R.id.xiazai_zx);
                            TextView textView = holder.<TextView>getView(R.id.xiazai_sudu);
                            masterLayout.setVisibility(View.GONE);
                            imagebutton.setVisibility(View.VISIBLE);
                            imagebutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = getFileIntent(new File(Environment.getExternalStorageDirectory() + "/zhtzwj/" + filename));
                                    BanShiXQActivity.this.startActivity(intent);
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
                        XiaZaiUtil xiaZaiUtil = new XiaZaiUtil(holder.<ImageButton>getView(R.id.xiazai_iamge_button), BanShiXQActivity.this, ceshi.getUrl(), holder.<MasterLayout>getView(R.id.xiazai_zx), holder.<TextView>getView(R.id.xiazai_sudu),ceshi.getName(),ceshi.getFpk());
//                        Log.i("zhang", ceshi.getFt());
                        String ft = ceshi.getFt();
                        if (ft.equals("doc") || ft.equals("docx")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.word);

                        } else if (ft.equals("xls") || ft.equals("xlsx")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.excel);
                        } else if (ft.equals("pdf")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.pdf);
                        } else if (ft.equals("ppt") || ft.equals("pptx")) {
                            holder.setImageResource(R.id.xiazai_item_tp, R.mipmap.ppt);
                        }
                    }
                };
                //给list添加数据
                gwXiangqingList.setAdapter(ja2);

            }


        }.protocolBuffer(BanShiXQActivity.this, Globals.KSCX_URI, reqDocument, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banshi_xiangqiang_text:
                //点击的时候按钮时没有选中的状态
                if (bBoolean) {
                    if (yjbsf.equals("0")){

                    }else{
                        ldList1.setVisibility(View.VISIBLE);
                        ksList1.setVisibility(View.VISIBLE);
                        lingdaoList.setVisibility(View.VISIBLE);
                        keshifuzherenList.setVisibility(View.VISIBLE);

                    }
                    gwXiangqingList.setVisibility(View.VISIBLE);
                    banshiXiangqiangText.setTextColor(this.getResources().getColor(R.color.font_fff));
                    banshiXiangqiangText.setBackgroundResource(R.color.rul_bj);

                    banshiLiuzhuanText.setTextColor(this.getResources().getColor(R.color.rul_bj));
                    banshiLiuzhuanText.setBackgroundResource(R.color.font_fff);
                    bBoolean = false;
                    aBoolean = true;
                    banshiXl.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                    // 实例化Fragment页面
                    h5Fargment = new H5Fargment();
//                    Bundle args = new Bundle();
//                    args.putString("pid", pid);
                    Bundle args = new Bundle();
                    args.putString("pid", ss);
                    h5Fargment.setArguments(args);
                    // 得到Fragment事务管理器
//                    h5Fargment.setArguments();
                    // 得到Fragment事务管理器
                    FragmentTransaction fragmentTransaction = this
                            .getSupportFragmentManager().beginTransaction();
                    // 替换当前的页面
                    fragmentTransaction.replace(R.id.banshi_xianshi, h5Fargment);
                    // 事务管理提交
                    fragmentTransaction.commit();
                    banshiXl.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                }


                break;
            case R.id.banshi_liuzhuan_text:

                if (aBoolean) {
                    ldList1.setVisibility(View.GONE);
                    ksList1.setVisibility(View.GONE);
                    lingdaoList.setVisibility(View.GONE);
                    gwXiangqingList.setVisibility(View.GONE);
                    keshifuzherenList.setVisibility(View.GONE);
                    banshiXiangqiangText.setTextColor(this.getResources().getColor(R.color.rul_bj));
                    banshiXiangqiangText.setBackgroundResource(R.color.font_fff);


                    banshiLiuzhuanText.setTextColor(this.getResources().getColor(R.color.font_fff));
                    banshiLiuzhuanText.setBackgroundResource(R.color.rul_bj);
                    bBoolean = true;
                    aBoolean = false;
                    // 实例化Fragment页面
                    bsFargment = new BsFargment();
                    // 得到Fragment事务管理器

                    Bundle args = new Bundle();
                    Log.i("zhang", "没传到前" + inid + "   " + wid);
                    args.putString("inid", inid);
                    args.putString("wid", wid);
                    args.putString(Globals.BSF, baf);
                    bsFargment.setArguments(args);
                    FragmentTransaction fragmentTransaction = this
                            .getSupportFragmentManager().beginTransaction();
                    // 替换当前的页面
                    fragmentTransaction.replace(R.id.banshi_xianshi, bsFargment);
                    // 事务管理提交
                    fragmentTransaction.commit();
                    banshiXl.setBackgroundResource(R.drawable.banshi_xiangqiang_br);
                }
                break;

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
