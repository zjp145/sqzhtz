package com.zhang.sqone.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.views.DeleteF;
import com.zhang.sqone.zxing.ZxingActivity;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * oa的首页(home模块)
 *
 * @author ZJP
 *         created at 2016/2/1 13:15
 */


public class FragmentAt extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    @Bind(R.id.oa_home_liuzhuan)
    /**流转跟踪*/
            LinearLayout oaHomeLiuzhuan;
    @Bind(R.id.oa_home_caogao)
    /**代办事项*/
            LinearLayout oaHomeCaogao;
    @Bind(R.id.oa_home_banjie)
    /**办结事项*/
            LinearLayout oaHomeBanjie;
    @Bind(R.id.oa_home_anpai)
    /**会议安排查询*/
            LinearLayout oaHomeAnpai;
    @Bind(R.id.oa_home_jiyao)
    /**会议预定*/
            LinearLayout oaHomeJiyao;
    @Bind(R.id.oa_home_sheshi)
    /**会议设施查询*/
            LinearLayout oaHomeSheshi;
    @Bind(R.id.oa_home_kshishch)
    /**科室之窗*/
            LinearLayout oaHomeKshishch;
    @Bind(R.id.oa_home_gongzuochaxun)
    /**科室工作查询*/
            LinearLayout oaHomeGongzuochaxun;
    @Bind(R.id.oa_home_geren)
    /**个人百科管理*/
            LinearLayout oaHomeGeren;
    @Bind(R.id.oa_home_shequ)
    /**社区之窗*/
            LinearLayout oaHomeShequ;
    @Bind(R.id.oa_home_shequgongzuo)
    /**社区工作查询*/
            LinearLayout oaHomeShequgongzuo;
    @Bind(R.id.oa_home_baike)
    /**社区业务百科*/
            LinearLayout oaHomeBaike;
    //    @Bind(R.id.oa_home_guizhang)
//    /**规章制度*/
//            LinearLayout oaHomeGuizhang;
    @Bind(R.id.oa_home_renshi)
    /**人事任免*/
            LinearLayout oaHomeRenshi;
    @Bind(R.id.oa_home_zhongyao)
    /**重要文件*/
            LinearLayout oaHomeZhongyao;
    @Bind(R.id.oa_home_dangjian)
    /**党建园地*/
            LinearLayout oaHomeDangjian;
    @Bind(R.id.oa_home_cankao)
    /**参考资料*/
            LinearLayout oaHomeCankao;
    @Bind(R.id.oa_home_fanhui)
    /**返回*/
            ImageView oaHomeFanhui;
    @Bind(R.id.oa_home_lianghongdeng)
    /**亮红灯*/
            LinearLayout oaHomeLianghongdeng;
    @Bind(R.id.oa_home_saoyisao)
    /**扫一扫*/
            LinearLayout oaHomeSaoyisao;
    @Bind(R.id.oa_home_huanjing)
    /**环境监测*/
            LinearLayout oaHomeHuanjing;

    @Bind(R.id.gome_view)
    WebView gomeView;
    /**
     * 用户预定会议的时候选择日期
     */
    final Calendar calendar = Calendar.getInstance();
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
    public static final String DATEPICKER_TAG = "datepicker";
    @Bind(R.id.oa_home_dangwei)
    /**党委文件*/
            LinearLayout oaHomeDangwei;
    @Bind(R.id.oa_home_zhengfuw)
    /**政府文件*/
            LinearLayout oaHomeZhengfuw;
    @Bind(R.id.oa_home_zhengfub)
    /**政府报告*/
            LinearLayout oaHomeZhengfub;
    @Bind(R.id.oa_home_guizhang1)
    /**规章制度*/
            LinearLayout oaHomeGuizhang1;
    @Bind(R.id.oa_home_tianzhuz)
    /**天竺镇情*/
            LinearLayout oaHomeTianzhuz;
    @Bind(R.id.oa_home_shoucang)
            /**收藏工作*/
    LinearLayout oaHomeShoucang;

    private String moth1;
    private String day2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_at, container, false);
        //获得控件
        ButterKnife.bind(this, view);
//        String s = Globals.OA_QQ+"?usercode="+ User.sid + "&pwd="+User.pwd+"";
        String s = Globals.OA_QQ + "?usercode=" + User.sid + "&pwd=" + User.pwd;
        Log.i("zhang", "网址" + s);

        gomeView.loadUrl(s);
        adnOnClicks();

        return view;
    }

    /**
     * 控件添加点击事件
     */
    private void adnOnClicks() {
        oaHomeDangjian.setOnClickListener(this);
//        oaHomeGuizhang.setOnClickListener(this);
        oaHomeCankao.setOnClickListener(this);
        oaHomeRenshi.setOnClickListener(this);
        oaHomeZhongyao.setOnClickListener(this);
        oaHomeLiuzhuan.setOnClickListener(this);
        oaHomeCaogao.setOnClickListener(this);
        oaHomeBanjie.setOnClickListener(this);
        oaHomeAnpai.setOnClickListener(this);
        oaHomeHuanjing.setOnClickListener(this);
        oaHomeFanhui.setOnClickListener(this);
        oaHomeSaoyisao.setOnClickListener(this);
        oaHomeKshishch.setOnClickListener(this);
        oaHomeJiyao.setOnClickListener(this);
        oaHomeKshishch.setOnClickListener(this);
        oaHomeGongzuochaxun.setOnClickListener(this);
        oaHomeDangwei.setOnClickListener(this);
        oaHomeZhengfuw.setOnClickListener(this);
        oaHomeZhengfub.setOnClickListener(this);
        oaHomeGuizhang1.setOnClickListener(this);
        oaHomeTianzhuz.setOnClickListener(this);
        oaHomeShoucang.setOnClickListener(this);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击流转跟踪
            case R.id.oa_home_liuzhuan:
                Intent intent5 = new Intent(getActivity(), DaiBanActivity.class);
                intent5.putExtra(Globals.GWGL, 1);
                startActivity(intent5);
                break;
            //代办事项
            case R.id.oa_home_caogao:
                Intent intent6 = new Intent(getActivity(), DaiBanActivity.class);
                intent6.putExtra(Globals.GWGL, 2);
                startActivity(intent6);
                break;
            //办结事项
            case R.id.oa_home_banjie:
                Intent intent7 = new Intent(getActivity(), DaiBanActivity.class);
                intent7.putExtra(Globals.GWGL, 3);
                startActivity(intent7);
                break;
            //办结事项
            case R.id.oa_home_shoucang:
                Intent intent15 = new Intent(getActivity(), DaiBanActivity.class);
                intent15.putExtra(Globals.GWGL, 4);
                startActivity(intent15);
                break;

            //点击党建园地
            case R.id.oa_home_dangjian:
                Intent intent = new Intent(getActivity(), DangJianYuansActivity.class);
                intent.putExtra(Globals.WDGL, 4);
                startActivity(intent);
                break;
            //点击人事任免
            case R.id.oa_home_renshi:
                Intent intent1 = new Intent(getActivity(), DangJianYuansActivity.class);
                intent1.putExtra(Globals.WDGL, 2);
                startActivity(intent1);
                break;
//            //点击规章制度
//            case R.id.oa_home_guizhang:
//                Intent intent2 = new Intent(getActivity(), DangJianYuansActivity.class);
//                intent2.putExtra(Globals.WDGL, 1);
//                startActivity(intent2);
//                break;
            //参考资料
            case R.id.oa_home_cankao:
                Intent intent3 = new Intent(getActivity(), DangJianYuansActivity.class);
                intent3.putExtra(Globals.WDGL, 5);
                startActivity(intent3);
                break;
            //重要文件
            case R.id.oa_home_zhongyao:
                Intent intent4 = new Intent(getActivity(), DangJianYuansActivity.class);
                intent4.putExtra(Globals.WDGL, 3);
                startActivity(intent4);
                break;
            //会议安排
            case R.id.oa_home_huanjing:
                Intent intent8 = new Intent(getActivity(), HuanJingActivity.class);
                startActivity(intent8);
                break;
            //点击返回按钮
            case R.id.oa_home_fanhui:

                DeleteF d = new DeleteF() {
                    @Override
                    public void determineButton() {
                        getActivity().finish();
                    }
                }.deleteDialog(getActivity(), "你确定要退回到应用首页吗？", "", "");
                break;
            //点击扫一扫
            case R.id.oa_home_saoyisao:
                Intent intent9 = new Intent(getActivity(), ZxingActivity.class);
                startActivity(intent9);
                break;
            //科室之窗
            case R.id.oa_home_kshishch:
                Intent intent10 = new Intent(getActivity(), KeShiZhiChuangActivity.class);
                startActivity(intent10);
                break;
            //会议预定
            case R.id.oa_home_jiyao:

                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(true);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
//                Intent intent13 = new Intent(getActivity(), TxlActivity.class);
//                startActivity(intent13);
                break;
            //科室工作查询
            case R.id.oa_home_gongzuochaxun:
                Intent intent11 = new Intent(getActivity(), KsgxcxActivity.class);
                startActivity(intent11);
                break;
            case R.id.oa_home_anpai:
                Intent intent12 = new Intent(getActivity(), HyapcxActivity.class);
                startActivity(intent12);
                break;
            /**党委文件*/
            case R.id.oa_home_dangwei:
                Intent intent21 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
                intent21.putExtra(Globals.ZLWJ, 1);
                startActivity(intent21);
                break;
            /**政府文件*/
            case R.id.oa_home_zhengfuw:
                Intent intent22 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
                intent22.putExtra(Globals.ZLWJ, 2);
                startActivity(intent22);
                break;
            /**政府报告*/
            case R.id.oa_home_zhengfub:
                Intent intent23 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
                intent23.putExtra(Globals.ZLWJ, 3);
                startActivity(intent23);
                break;
            /**规章制度*/
            case R.id.oa_home_guizhang1:
                Intent intent24 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
                intent24.putExtra(Globals.ZLWJ, 4);
                startActivity(intent24);
                break;
            /**天竺镇情*/
            case R.id.oa_home_tianzhuz:
                Intent intent25 = new Intent(getActivity(), ZiLiaoZhongXinActivity.class);
                intent25.putExtra(Globals.ZLWJ, 5);
                startActivity(intent25);
                break;
        }
//            case R.id.oa_home_kshishch:
//                break;

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if (month == 12) {
            month = 1;
        } else {
            month++;
        }
        if (month < 10) {
            moth1 = "0" + month;
        } else {
            moth1 = "" + month;
        }
        if (day < 10) {
            day2 = "0" + day;
        } else {
            day2 = "" + day;
        }
        Intent intent = new Intent(getActivity(), HyydActivity.class);
        intent.putExtra("day", year + "-" + moth1 + "-" + day2);
        startActivity(intent);
//        Toast.makeText(getContext(), "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
    }

}
