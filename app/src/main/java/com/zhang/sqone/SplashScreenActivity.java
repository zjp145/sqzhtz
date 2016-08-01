package com.zhang.sqone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
//                    initLogin();
                    //改版之后
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    User.isLogin= false;
                    startActivity(intent);
                    SplashScreenActivity.this.finish();

                }
            }
        }, SPLASH_DISPLAY_LENGHT);


    }

    /**本地有数据使用本地用户名和密码登录
     * 账号密码唯一id的验证（本地的验证）
     * */
    public void initLogin() {
        Index.ReqIndex.Login.Builder login = Index.ReqIndex.Login.newBuilder();
        //从本地获得用户名密码
        //账号
        final String phone = SharedPreferencesUtils.getString(
                SplashScreenActivity.this, Globals.USER_PHONE, null);
        //密码
        final String pwd = SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.USER_PASSWORD, null);
        //唯有id
        final String wyid = SharedPreferencesUtils.getString(SplashScreenActivity.this, Globals.WY_id, null);

        //添加用户名
        login.setUsername(phone);
        //添加密码
        login.setPassword(pwd);
        login.setPhncode(wyid);
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setLogin(login).setAc("LOGIN").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                //登陆成功
                if(index.getScd().equals("1")){
                    Toast.makeText(SplashScreenActivity.this, index.getMsg(),
                            Toast.LENGTH_SHORT).show();
                    //跳转到首页(改版前)
//                    Intent intent = new Intent(SplashScreenActivity.this,
//                            TiZhuActivity.class);

                    Intent intent = new Intent(SplashScreenActivity.this,
                            TianZhuOATivity.class);
                    //登录成功的情况下给用户的实体类添加信息（全局使用）
                    User.isLogin=true;
                    User.sid = phone;
                    User.pwd = pwd;
                    //头像地址
                    User.IconPath = index.getLogin().getPh();
                    User.wcjd=index.getLogin().getWccd();
                    //添加默认手机号码
                    User.phone = index.getLogin().getPhone();
                    User.nc = index.getLogin().getNa();
                    startActivity(intent);
                    //退出页面
                    finish();
                }
//                Log.i("onsuccess", "集合数据个数" + index.getMsg());
            }
        }.protocolBuffer(SplashScreenActivity.this, Globals.WS_URI, index, null);

    }
    }


