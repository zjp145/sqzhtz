package com.zhang.sqone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.my.LoginActivity;
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utils.UpdateVersionService;
import com.zhang.sqone.utilss.HttpUtil;

import java.io.IOException;

/**
 * 进入程序的时候必须进入加载页面
 * 程序的欢迎的页面如果用户已经登录的情况下不需要登录直接进入社区界面
 *
 * @author ZJP
 *         created at 2016/2/29、 9:15
 */
public class SplashScreenActivity extends BaseActivity {
    //获取本地的轻量级的存储对象
    private SharedPreferences myPrefer;
    //欢迎页面的展示时间
    private final int SPLASH_DISPLAY_LENGHT = 1500;
    //获得的社区id ，和登录的标示符
    private String sqid, Sid;
    private UpdateVersionService updateVersionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("wwwwwwwwwwww", "onCreate ");
        setContentView(R.layout.activity_splash_screen);

        myPrefer = getSharedPreferences(Globals.SPLASH_USERTYPE, Activity.MODE_PRIVATE);



        //开启一个线程将本地保存的数据 登录
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //创建的如果本地没有存在数据的情况下跳转到登录的界面上 否则直接使用的本地的数据登录app
                if ("".equals(SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.USER_YD, ""))) {
                    SharedPreferencesUtils.saveString(SplashScreenActivity.this,
                            Globals.USER_YD, "1");
                    Intent intent = new Intent(SplashScreenActivity.this, YinDaoYeActivity.class);
                    startActivity(intent);
                    finish();
                } else if ("".equals(SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.USER_PHONE, ""))
                        && "".equals(SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.USER_PASSWORD, ""))) {
                    //改版之前
//                    Intent intent = new Intent(SplashScreenActivity.this, TiZhuActivity.class);
                    //改版之后
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    User.isLogin= false;
                    startActivity(intent);
                    SplashScreenActivity.this.finish();
                    Log.i("zhangjianpeng", "本地没有数据");
                } else {
                    //有账号登录(旧版本)
                    if(SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.C_id, null)==null){
                        //改版之后
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    User.isLogin= false;
                    startActivity(intent);
                    SplashScreenActivity.this.finish();
                    }else{
                        initLogin();
                    }

                    //改版之后
//                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
//                    User.isLogin= false;
//                    startActivity(intent);
//                    SplashScreenActivity.this.finish();

                }
            }
        }, SPLASH_DISPLAY_LENGHT);


    }

    /**本地有数据使用本地用户名和密码登录
     * 账号密码唯一id的验证（本地的验证）
     * */
    public void initLogin() {
        //从本地获得用户名密码
        //账号
        final String phone = SharedPreferencesUtils.getString(
                SplashScreenActivity.this, Globals.USER_PHONE, null);
        //密码
        final String pwd = SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.USER_PASSWORD, null);
        //唯有id
        final String wyid = SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.WY_id, null);
        final String cid = SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.C_id, null);
        //唯有id
        final String isponh = SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.I_isponh, null);


        //创建登录传递到服务器中的对象
        Index.ReqIndex.Login.Builder login = Index.ReqIndex.Login.newBuilder();
        login.setUsername(phone);
        login.setPassword(pwd);
        login.setCid(cid);
        login.setType(isponh);
        login.setPhncode(wyid);
        Log.i("zhang", "手机id===" + login.getPhncode());
        //给对象添加必要的信息
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setLogin(login).setAc("LOGIN").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                //0、用户不存在或密码错误；1、登录成功
                if (index.getScd().equals("1")) {
                    //跳转到首页（改版之前）
//                    Intent intent = new Intent(LoginActivity.this,
//                            TiZhuActivity.class);
                    //跳转到首页（改版之后）
                    Intent intent = new Intent(SplashScreenActivity.this,
                            TianZhuOATivity.class);
                    User.IconPath = index.getLogin().getPh();
                    User.wcjd = index.getLogin().getWccd();
                    User.tzts = index.getLogin().getTzts();
                    User.dbts = index.getLogin().getDbts();
                    User.sfzl = index.getLogin().getZlts();
                    User.xxzx = index.getLogin().getXxzxts();
                    /**是不是显示事项通知*/
                    User.sxtz = index.getLogin().getIsbgs();

                    /*是不是显示班子会议查询*/
                    User.bzhyc = index.getLogin().getIsbzhcx();
                    /*是不是显示班子会议汇总*/
                    User.bzhyh = index.getLogin().getIsbzhhz();
                    /**是不是显示政府章*/
                    User.zhfz = index.getLogin().getIszfz();
                    /**是不是显示党委章*/
                    User.dwz = index.getLogin().getIsdwz();


                    //    待办工作(显示不显示的判断)
                    User.dbgz_1 = index.getLogin().getRlist().getDbgz1();
                    //    收资料(显示不显示的判断)
                    User.szl_2 = index.getLogin().getRlist().getSzl2();
                    //    在办工作(显示不显示的判断)
                    User.zbgz_3 = index.getLogin().getRlist().getZbgz3();
                    //    办结工作(显示不显示的判断)
                    User.bjgz_4 = index.getLogin().getRlist().getBjgz4();
                    //    收藏工作(显示不显示的判断)
                    User.scgz_5 = index.getLogin().getRlist().getScgz5();
                    //    班子会议题审核(显示不显示的判断)
                    User.bzhysh_6 = index.getLogin().getRlist().getBzhsh6();
                    //    班子会议题查询(显示不显示的判断)
                    User.bzhycx_7 = index.getLogin().getRlist().getBzhcx7();
                    //    传资料(显示不显示的判断)
                    User.czl_8 = index.getLogin().getRlist().getCzl8();
                    //    已发送(显示不显示的判断)
                    User.yfs_9 = index.getLogin().getRlist().getYfs9();
                    //    会议室查询(显示不显示的判断)
                    User.hyscx_10 = index.getLogin().getRlist().getHyscx10();
                    //    我的文件(显示不显示的判断)
                    User.wdwj_11 = index.getLogin().getRlist().getWdwj11();
                    //    我的工资(显示不显示的判断)
                    User.wdgz_12 = index.getLogin().getRlist().getWdgz12();
                    //    党委文件(显示不显示的判断)
                    User.dwwj_13 = index.getLogin().getRlist().getDwwj13();
                    //    政府文件(显示不显示的判断)
                    User.zfwj_14 = index.getLogin().getRlist().getZfwj14();
                    //    天竺镇情(显示不显示的判断)
                    User.ttzq_15 = index.getLogin().getRlist().getTzzq15();
                    //    政府报告(显示不显示的判断)
                    User.zfbg_16 = index.getLogin().getRlist().getZfbg16();
                    //    党建学习(显示不显示的判断)
                    User.djxx_17 = index.getLogin().getRlist().getDjxx17();
                    //    廉政教育(显示不显示的判断)
                    User.lzjy_18 = index.getLogin().getRlist().getLzjy18();
                    //    规章制度(显示不显示的判断)
                    User.gzzd_19 = index.getLogin().getRlist().getGzzd19();
                    //    业务学习(显示不显示的判断)
                    User.ywxx_20 = index.getLogin().getRlist().getYwxx20();
                    //    参考资料(显示不显示的判断)
                    User.ckzl_21 = index.getLogin().getRlist().getCkzl21();
                    //    环境监测(显示不显示的判断)
                    User.hjjc_22 = index.getLogin().getRlist().getHjjc22();
                    //    领导日程(显示不显示的判断)
                    User.ldrc_23 = index.getLogin().getRlist().getLdrc23();
                    //    日程安排(显示不显示的判断)
                    User.rcap_24 = index.getLogin().getRlist().getRcap24();
                    //    事项通知(显示不显示的判断)
                    User.tzsx_25 = index.getLogin().getRlist().getSxtz25();
                    //    党委章查询(显示不显示的判断)
                    User.dwzcx_26 = index.getLogin().getRlist().getDwzcx26();
                    //    政府章查询(显示不显示的判断)
                    User.zfzcx_27 = index.getLogin().getRlist().getZfzcx27();
//                    班子会议上报
                    User.bzhysb_28 = index.getLogin().getRlist().getBzhsb28();
                    /*事假*/
                    User.sjsq_28 = index.getLogin().getSqlist().getSj1();
                    //    病假(显示不显示的判断)
                    User.bjsq_29 = index.getLogin().getSqlist().getBj2();
                    //    年假(显示不显示的判断)
                    User.njsq_30 = index.getLogin().getSqlist().getNj3();
                    //    党委章(显示不显示的判断)
                    User.dwz_31 = index.getLogin().getSqlist().getDwz4();
                    //    政府章(显示不显示的判断)
                    User.zfz_32 = index.getLogin().getSqlist().getZfz5();
                    //    会议室查询(显示不显示的判断)
                    User.hyscx_33 = index.getLogin().getSqlist().getHys6();
                    //    党委发文(显示不显示的判断)
                    User.dwfw_34 = index.getLogin().getSqlist().getDwfw7();
                    //    政府发文(显示不显示的判断)
                    User.zffw_35 = index.getLogin().getSqlist().getZffw8();
                    //    会议材料管理(显示不显示的判断)
                    User.hyclgl_36 = index.getLogin().getRlist().getHycl29();
                    Log.i("zhang", "___政府张" + User.zfz_32 + "_____党委章" + User.dwz_31);
                    Log.i("zhang", "___" + User.dbgz_1 + User.szl_2 + User.zbgz_3);

                    //登录成功的情况下给用户的实体类添加信息（全局使用）
                    User.isLogin = true;
                    User.sid = index.getLogin().getUsername();
                    //头像地址
                    User.IconPath = index.getLogin().getPh();
                    User.wcjd = index.getLogin().getWccd();
                    //添加密码
                    User.pwd = pwd;
                    //添加默认手机号码
                    User.phone = index.getLogin().getPhone();
                    User.nc = index.getLogin().getNa();
                    User.type = isponh;
                    User.cid = cid;
                    User.mis_id = wyid;
//                    Log.i("zhang", "我的账号：" +User.sid+"-----我的密码："+User.pwd+"-----cid："+User.cid+"-----type："+User.+"-----我的密码："+User.pwd);
                    //登录成功跳转到首页
                    startActivity(intent);
                    //退出页面
                    finish();
                }

            }

        }.protocolBuffer(SplashScreenActivity.this, Globals.WS_URI, index, null);


    }
    }


