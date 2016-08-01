package com.zhang.sqone.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Meetingdiscuss;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.pullswipe.PullToRefreshSwipeMenuListView;
import com.zhang.sqone.pullswipe.pulltorefresh.RefreshTime;
import com.zhang.sqone.utils.HttpUtilDocumentme;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.DeleteF;
import com.zhang.sqone.views.TitleBarView;
import com.zhang.sqone.xiangqing.BanZiXqActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 班子会议操作
 *
 * @author ZJP
 *         created at 2016/5/24 17:17
 */
public class BanZiCaoZuoActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener {


    @Bind(R.id.banzicaozuo_title)
    TitleBarView banzicaozuoTitle;
    @Bind(R.id.banzicaozuo_list)
    PullToRefreshSwipeMenuListView banzicaozuoList;
    @Bind(R.id.xieshu_button)
    LinearLayout xieshuButton;
    @Bind(R.id.shuji_huiyi)
    TextView shujiHuiyi;
    @Bind(R.id.bzhy_sb_quxiao)
    LinearLayout bzhySbQuxiao;
    @Bind(R.id.bzhy_sb_zhuanfa)
    LinearLayout bzhySbZhuanfa;
    @Bind(R.id.bahy_sb_layout)
    LinearLayout bahySbLayout;
    /**
     * 加载数据
     */
    private Handler mHandler;

    private boolean flsh = false;
    /**
     * 科室列表的详情数据
     */
    private List<Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap> fileList = new ArrayList<>();

    /**
     * 科室列表的适配器
     */
    private CommonAdapter<Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap> resultAdapter;
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
    private List<Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap> fileList2 = new ArrayList<>();
    /**
     * 获得年和期数
     */
    private String nian;
    private String qs;
    /**
     * 判断是查看还是选择的操作
     */
    private String bsf;
    /**
     * 判定是不是可以编辑
     */
    private Boolean aBoolean;
    /**
     * 判断是审核还是查看
     */
    private String bsf2;
    /**
     * 不同的请求标识符
     */
    private String bsf3;
    /**
     * 是不是四大领导
     */
    private String bsf4;
    /**
     * 点击查询和审核还是查询
     */
    private String bsfs;
    private   ArrayList<String> select = new ArrayList<>();
    private StringBuilder stringBuilder = new StringBuilder();
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_ban_zi_cao_zuo);
        ButterKnife.bind(this);
        /**点击结束按钮*/
        xieshuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aBoolean) {
                    DeleteF d1 = new DeleteF() {
                        @Override
                        public void determineButton() {
                            Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc(bsfs).setDt(nian).setTj(qs).build();
                            new HttpUtilDocumentme() {
                                @Override
                                public <T> void analysisInputStreamData(Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
                                    if (index.getScd().equals("1")) {
                                        Toast.makeText(BanZiCaoZuoActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(BanZiCaoZuoActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }.protocolBuffer(BanZiCaoZuoActivity.this, Globals.BZHY_URI, reqDocument, null);
                        }
                    }.deleteDialog(BanZiCaoZuoActivity.this, "你确定"+shujiHuiyi.getText().toString().trim()+"吗？", "", "");

                } else {
                    Toast.makeText(BanZiCaoZuoActivity.this, "对不起，本次书记会议还没有结束", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bzhySbQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0;i<select.size();i++){
                    stringBuilder.append(select.get(i)+",");
                    Log.i("zhang", "提交的id有"+select.get(i));
                }
                if(stringBuilder.length()!=0){
                     s =  stringBuilder.substring(0, stringBuilder.length() - 1).toString();
                    Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc("BZSBTJ").setDt(nian).setTj(qs).setId(s).build();
                    new HttpUtilDocumentme() {
                        @Override
                        public <T> void analysisInputStreamData(Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
                            if (index.getScd().equals("1")) {
                                Toast.makeText(BanZiCaoZuoActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();

                                stringBuilder.delete(0,stringBuilder.length());
                                select.clear();

                                finish();
//                                p = 1;
//                                Log.i("zhang", "onRefresh: ");
//                                flsh = false;
//                                isFlsh = true;
//                                regRequest();
                            } else {
                                Toast.makeText(BanZiCaoZuoActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.protocolBuffer(BanZiCaoZuoActivity.this, Globals.BZHY_URI, reqDocument, null);
                }else{
                    Toast.makeText(BanZiCaoZuoActivity.this, "没有可提交议题", Toast.LENGTH_SHORT).show();
                }


            }
        });

        bzhySbZhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0;i<select.size();i++){
                    stringBuilder.append(select.get(i)+",");
                    Log.i("zhang", "提交的id有"+select.get(i));
                }
                if(stringBuilder.length()!=0){
                     s =  stringBuilder.substring(0, stringBuilder.length() - 1).toString();
                    Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc("BZSBDEL").setDt(nian).setTj(qs).setId(s).build();
                    new HttpUtilDocumentme() {
                        @Override
                        public <T> void analysisInputStreamData(Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
                            if (index.getScd().equals("1")) {
                                Toast.makeText(BanZiCaoZuoActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                                stringBuilder.delete(0,stringBuilder.length());
                                select.clear();
//                                finish();
                                p = 1;
                                Log.i("zhang", "onRefresh: ");
                                flsh = false;
                                isFlsh = true;
                                regRequest();
                            } else {
                                Toast.makeText(BanZiCaoZuoActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.protocolBuffer(BanZiCaoZuoActivity.this, Globals.BZHY_URI, reqDocument, null);

                }else{
                    Toast.makeText(BanZiCaoZuoActivity.this, "没有可删除议题", Toast.LENGTH_SHORT).show();
                }


            }
        });
        /**点击新增会议*/
        banzicaozuoTitle.setClickEnterButtonListener(new TitleBarView.OnClickEnterButtonListener() {
            @Override
            public void onClickEnterButton(View v) {
                Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc("BZHSHNEW").setDt(nian).setTj(qs).build();
                new HttpUtilDocumentme() {
                    @Override
                    public <T> void analysisInputStreamData(Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
                        Log.i("zhang", "新增一条id" + index.getMcddetail().getId());
                        Intent intent = new Intent(BanZiCaoZuoActivity.this, BanZiXqActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("bsf", "2");
                        bundle.putSerializable(Globals.LIDS, index.getMcddetail());
                        bundle.putString("ld", bsf4);
                        bundle.putString("bsf2", bsf2);
                        bundle.putString("mc","");
                        intent.putExtras(bundle);
                        startActivity(intent);


                    }
                }.protocolBuffer(BanZiCaoZuoActivity.this, Globals.BZHY_URI, reqDocument, null);

            }
        });
        nian = getIntent().getStringExtra("nian");
        qs = getIntent().getStringExtra("qs");
        bsf = getIntent().getStringExtra("bsf");
        bsf2 = getIntent().getStringExtra("bsf2");
        if (bsf2.equals("1")) {
            //审核
            bsfs = "BZHSHJS";

            shujiHuiyi.setText("结束本次书记会议");
            //  是不是四大领导
            bsf4 = getIntent().getStringExtra("bsf3");
            if (bsf4.equals("0")) {
                xieshuButton.setVisibility(View.GONE);
            }
            bsf3 = "BZHSHXQ";
        } else if (bsf2.equals("2")) {
            //查询
            bsfs = "BZHCXJS";
            bsf3 = "BZHCXXQ";
            banzicaozuoTitle.imbEnter.setVisibility(View.GONE);
        } else if (bsf2.equals("3")) {
            //结束使用
            bsfs = "BZHSHJS";

            shujiHuiyi.setText("结束本次书记会议");
            //  是不是四大领导
            bsf4 = getIntent().getStringExtra("bsf3");
            if (bsf4.equals("0")) {
                xieshuButton.setVisibility(View.GONE);
            }
            bsf3 = "BZSBXQLB";
            //上报
            bahySbLayout.setVisibility(View.VISIBLE);
            xieshuButton.setVisibility(View.GONE);

        }
        // 当前是查看的功能
        if (bsf.equals("0")) {
            xieshuButton.setVisibility(View.GONE);
            banzicaozuoTitle.setText("查看");
            bahySbLayout.setVisibility(View.GONE);
            banzicaozuoTitle.imbEnter.setVisibility(View.GONE);
        }
        banzicaozuoList.setPullRefreshEnable(true);
        banzicaozuoList.setPullLoadEnable(true);
        banzicaozuoList.setXListViewListener(this);

//        regRequest();
        mHandler = new Handler();
        //点击listview 进入详情
        banzicaozuoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BanZiCaoZuoActivity.this, BanZiXqActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Globals.LIDS, resultAdapter.getItem(position - 1));
                bundle.putString("bsf", "0");
                bundle.putString("bsf2", bsf2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /**
     * 根据不同的标识符加载网络的数据
     */
    public void regRequest() {

        Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc(bsf3).setP(p + "").setDt(nian).setTj(qs).build();

        new HttpUtilDocumentme() {
            @Override
            public <T> void analysisInputStreamData(final Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
                fileList = index.getMcdList();
                if (flsh) {
                    fileList2.clear();
                    fileList2.addAll(fileList);
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                    RefreshTime.setRefreshTime(BanZiCaoZuoActivity.this, df.format(new Date()));
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                    banzicaozuoList.setRefreshTime(RefreshTime.getRefreshTime(BanZiCaoZuoActivity.this));
                    banzicaozuoList.stopRefresh();
                    banzicaozuoList.stopLoadMore();
                    flsh = false;

                } else {

                    if (isFlsh) {
                        fileList2.addAll(fileList);
                        Log.i("zhang", "列表个数---第一" + fileList.size());
                        resultAdapter = new CommonAdapter<Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap>(BanZiCaoZuoActivity.this, fileList, R.layout.banzi_litems) {
                            @Override
                            public void convert(ViewHolder holder, final Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap fileMap) {
                                if (bsf2.equals("3")){
                                    holder.setVisible1(R.id.bzhy_sb, true);
                                    holder.setVisible(R.id.banzichaxun_iamge,false);
                                    holder.setText(R.id.banzhi1_text, fileMap.getKsmc());
                                }else {
                                    holder.setVisible1(R.id.bzhy_sb, false);
                                    holder.setVisible(R.id.banzichaxun_iamge,true);
                                    holder.setText(R.id.banzhi1_text, fileMap.getZgfz());
                                }

                                if (bsf.equals("0")) {
                                    holder.setVisible(R.id.lint1, false);
                                    holder.setVisible1(R.id.bzhy_sb, false);
                                    holder.setVisible(R.id.banzichaxun_iamge,true);
                                } else {
                                    holder.setVisible(R.id.lint1, true);
                                }

                                holder.setText(R.id.banzhi1_title, fileMap.getYtmc())
                                        .setText(R.id.banzhi1_shenhe, fileMap.getSt());

                                if (fileMap.getYtmc().equals("无")) {
                                    holder.setVisible(R.id.caozuo_text, false);
                                } else {
                                    holder.setVisible(R.id.caozuo_text, true);
                                }
                                holder.setText(R.id.caozuo_text, "编辑 >");

                                if (fileMap.getIfjs().equals("0")) {
                                    aBoolean = false;

                                } else {
                                    aBoolean = true;
                                }

                                if (fileMap.getSt().equals("已确认")) {
                                    holder.setTextColorRes(R.id.banzhi1_shenhe, R.color.bs_zz);
                                } else {
                                    holder.setTextColorRes(R.id.banzhi1_shenhe, R.color.font_333);
                                }

                                holder.setOnClickListener(R.id.caozuo_text, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(BanZiCaoZuoActivity.this, BanZiXqActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable(Globals.LIDS, fileMap);
                                        bundle.putString("bsf", "1");
                                        bundle.putString("bsf2", bsf2);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                                final CheckBox sbox = (CheckBox) holder.getView(R.id.bzhy_sb);

                                if (select.contains(fileMap.getId())) {
                                    Log.i("zhang", "选中"+fileMap.getId());
                                       sbox.setChecked(true);
                                } else {
                                    Log.i("zhang", "未选中"+fileMap.getId());
                                      sbox.setChecked(false);
                                }
                                sbox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (sbox.isChecked()) {
                                            Log.i("zhang", "添加" + fileMap.getId());
                                            select.add(fileMap.getId());

                                        } else {
                                            if (select.contains(fileMap.getId())) {
                                                Log.i("zhang", "删除了" + fileMap.getId());
                                                select.remove(fileMap.getId());
                                            }

                                        }
                                    }
                                });
//                                sbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                    @Override
//                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                        if (isChecked) {
//                                                   Log.i("zhang", "添加" + fileMap.getId());
//                                                   select.add(fileMap.getId());
//
//                                        } else {
//                                            if (select.contains(fileMap.getId()))   {
//                                                  Log.i("zhang","删除了"+fileMap.getId() );
//                                                 select.remove(fileMap.getId());
//                                            }
//
//
//
//                                        }
//                                    }
//                                });


                            }
                        };
                        banzicaozuoList.setAdapter(resultAdapter);
                    } else {
                        if (fileList.size() != 0) {
                            Log.i("zhang", "列表个数---第二————" + fileList.size());
                            fileList2.addAll(fileList);
                            Log.i("zhang", "列表个数---第三————" + fileList2.size());
                            resultAdapter.setData(fileList2);
                            resultAdapter.notifyDataSetChanged();
                            onLoad();
                        } else {
                            Toast.makeText(BanZiCaoZuoActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            onLoad();
                        }
                    }

                }


            }


        }.protocolBuffer(BanZiCaoZuoActivity.this, Globals.BZHY_URI, reqDocument, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }

    /**
     * 模拟加载更多
     */
    private void onLoad() {
        Log.i("zhang", "onLoad: ");
        banzicaozuoList.setRefreshTime(RefreshTime.getRefreshTime(BanZiCaoZuoActivity.this));
        banzicaozuoList.stopRefresh();
        banzicaozuoList.stopLoadMore();

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
        Toast.makeText(BanZiCaoZuoActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
        if (isFlsh2) {

        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onLoad();
                }
            }, 2000);
            isFlsh2 = false;
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

//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//            // 判断是否滚动到底部
//            if (view.getLastVisiblePosition() == view.getCount() - 1) {
//                //加载更多功能的代码
//
//                Log.i("zhang", "onLoadMore: ");
//                if (flsh) {
//
//                } else {
//
//                    if (fileList2.size() > 4) {
//                        p++;
//
//                        isFlsh = false;
//                        regRequest();
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//    }
}


