package com.zhang.sqone.my;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.TianZhuOATivity;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.AppUtil;
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utils.UpdateVersionService;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.PermissionUtil;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

/**
 * 用户的登录界面
 *
 * @author ZJP
 *         created at 2016/2/25 14:51
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.dl_phone)
    /**用户的手机号码*/
            EditText dlPhone;
    @Bind(R.id.dl_poss)
    /**用户的密码*/
            EditText dlPoss;
    @Bind(R.id.dl_wjmm)
    /**忘记密码*/
            TextView dlWjmm;
    @Bind(R.id.dl_zc)
    /**注册*/
            TextView dlZc;
    @Bind(R.id.dl_dl_button)
    /**登录按钮*/
            Button dlDlButton;
//    @Bind(R.id.dl_qq)
//    /**第三方qq*/
//            ImageView dlQq;
//    @Bind(R.id.dl_weixin)
//    /**第三方微信*/
//            ImageView dlWeixin;
//
//    @Bind(R.id.dl_xinlang)
//    /**第三方新浪*/
//            ImageView dlXinlang;
    @Bind(R.id.dl_mxy)
    /**记住密码*/
            CheckBox dlMxy;
    @Bind(R.id.dl_jjmm)
    /**记住密码*/
            TextView dlJjmm;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 用户密码
     */
    private String poss;
    private UpdateVersionService updateVersionService;
    /** 是手机还是平板*/
    private String isponh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTranslucentStatus();
        ButterKnife.bind(this);
        PermissionUtil.getInstance().request(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 10,
                new PermissionUtil.PermissionResultCallBack() {
                    @Override
                    public void onPermissionGranted() {
                        Log.i("zhang", "当所有权限的申请被用户同意之后,该方法会被调用");
                        // 当所有权限的申请被用户同意之后,该方法会被调用
                    }

                    @Override
                    public void onPermissionDenied(String... permissions) {
                        Log.i("zhang", "当权限申请中的某一个或多个权限,被用户曾经否定了,并确认了不再提醒时,也就是权限的申请窗口不能再弹出时,该方法将会被调用,该方法会被调用");
                        // 当权限申请中的某一个或多个权限,被用户曾经否定了,并确认了不再提醒时,也就是权限的申请窗口不能再弹出时,该方法将会被调用
                    }

                    @Override
                    public void onRationalShow(String... permissions) {
                        Log.i("zhang", "当权限申请中的某一个或多个权限,被用户否定了,但没有确认不再提醒时,也就是权限窗口申请时,但被否定了之后,该方法将会被调用.");
                        // 当权限申请中的某一个或多个权限,被用户否定了,但没有确认不再提醒时,也就是权限窗口申请时,但被否定了之后,该方法将会被调用.
                    }
                });
        if (SharedPreferencesUtils.getString(LoginActivity.this, Globals.USER_JJMM, "0").equals("1")) {
            dlMxy.setChecked(true);
            dlPhone.setText(SharedPreferencesUtils.getString(LoginActivity.this, Globals.USER_PHONE, null));
            dlPoss.setText(SharedPreferencesUtils.getString(LoginActivity.this, Globals.USER_PASSWORD, null));
        } else {

        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                updateVersionService = new UpdateVersionService(
                        Globals.VERSION_XML, LoginActivity.this);// 创建更新业务对象
                updateVersionService.checkUpdate();// 调用检查更新的方法,如果可以更新.就更新
            }
        });
        Log.i("zhang", "手机id===" + AppUtil.isTablet(this));
        if (AppUtil.isTablet(this)) {
            Log.i("zhang", "是平板");
            User.mis_id =AppUtil.getDeviceId2();
            User.type = "1";
            isponh = "1";
        } else {
            Log.i("zhang", "是手机");
            User.mis_id =AppUtil.getDeviceId();
            User.type = "0";
            isponh = "0";
        }
        onClicks();

    }
    private void onClicks() {
        //登录按钮的点击事件
        dlDlButton.setOnClickListener(this);
        // 注册的点击事件
        dlZc.setOnClickListener(this);
        // 忘记密码的点击事件
        dlWjmm.setOnClickListener(this);
//        dlXinlang.setOnClickListener(this);
//        dlQq.setOnClickListener(this);
//        dlWeixin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击登录按钮
            case R.id.dl_dl_button:
                //获得手机号码和密码
                phone = dlPhone.getText().toString().trim();
                poss = dlPoss.getText().toString().trim();
                if ("".equals(phone) || "".equals(poss)) {
                    Toast.makeText(LoginActivity.this, "手机号、用户名和密码不能为空!",
                            Toast.LENGTH_SHORT).show();
                } else {
//                        if (poss.length()<6){
//                            Toast.makeText(LoginActivity.this,"密码不足6位！",Toast.LENGTH_SHORT).show();
//                        }else{
                    login();
//                    cs();
//                        }
                }
                break;
            //点击注册
            case R.id.dl_zc:
                Intent intent1 = new Intent(this, ForgetActivity.class);
                intent1.putExtra(Globals.TAB, "zc");
                startActivity(intent1);
                break;
            //点击忘记密码
            case R.id.dl_wjmm:
                Intent intent2 = new Intent(this, ForgetActivity.class);
                intent2.putExtra(Globals.TAB, "wjmm");
                startActivity(intent2);
                break;
//            //第三方登录
//            case R.id.dl_xinlang:
//                showDlu(SinaWeibo.NAME);
//                break;
//            //第三方登录
//            case R.id.dl_qq:
////                showDlu(QQ.NAME);
//                Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc("JBD").setId(Globals.JBD_BS).build();
//                new HttpUtilDocumentme() {
//                    @Override
//                    public <T> void analysisInputStreamData(Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
//                        if (index.getScd().equals("1")) {
//                            Toast.makeText(LoginActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(LoginActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                }.protocolBuffer(LoginActivity.this, Globals.BZHY_URI, reqDocument, null);
//                break;
//            //第三方登录
//            case R.id.dl_weixin:
//                showDlu(Wechat.NAME);
//                break;
        }

    }

    /**
     * 第三方登录
     */
    private void showDlu(String a) {
        Platform plat = ShareSDK.getPlatform(this, a);
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    Log.i("zhangzhang", "onComplete ");
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    platDB.getToken();
                    platDB.getUserGender();
                    platDB.getUserIcon();
                    platDB.getUserId();
                    platDB.getUserName();
                    Log.i("zhangzhang", "\nplatDB.getToken()" + platDB.getToken() +
                            "\nplatDB.getUserGender()" + platDB.getUserGender() +
                            "\nplatDB.getUserIcon()" + platDB.getUserIcon() +
                            "\nplatDB.getUserId()" + platDB.getUserId() +
                            "\nplatDB.getUserName()" + platDB.getUserName());
                }
            }


            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        plat.showUser(null);
        plat.removeAccount(true);
        //关闭SSO授权
//        plat.SSOSetting(true);

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


//    private void cs(){
//        //给对象添加必要的信息
//        Test.ReqTest index = Test.ReqTest.newBuilder().setAc("TEXT").build();
//        new HttpUtilCs() {
//            @Override
//            public <T> void analysisInputStreamData(Test.ReqTest index) throws IOException {
//
//                Log.i("zhang", "____"+index.getName());
//            }
//        }.protocolBuffer(LoginActivity.this, Globals.MY_CS, index, null);
//    }

    /**
     * 登录方法 将账户和密码提交到接口
     */
    private void login() {
//        final Test1.ReqTest11 index = Test1.ReqTest11.newBuilder().setAc("TEST11").build();
//        new TextUtilHttp() {
//            @Override
//            public <T> void analysisInputStreamData(Test1.ReqTest11 index) throws IOException {
//
//            }
//
//            @Override
//            public <T> void test(OutputStream outputStream) throws IOException {
//                index.writeTo(outputStream);
//            }
//
//            @Override
//            public <T> void test2(InputStream inputStream) throws IOException {
//                Test1.ReqTest11 jhon1 = Test1.ReqTest11.parseFrom(inputStream);
//                Log.i("zhang", "test2: "+jhon1.getB().getXm());
//            }
//        }.protocolBuffer(LoginActivity.this, "http://192.168.1.50:8280/test11", index, null);



        //创建登录传递到服务器中的对象
        Index.ReqIndex.Login.Builder login = Index.ReqIndex.Login.newBuilder();
        login.setUsername(phone);
        login.setPassword(poss);
        login.setCid(User.cid);
        login.setType(isponh);
        login.setPhncode(User.mis_id);
        Log.i("zhang", "手机id===" + login.getPhncode());
        //给对象添加必要的信息
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setLogin(login).setAc("LOGIN").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                Toast.makeText(LoginActivity.this, index.getMsg(),
                        Toast.LENGTH_SHORT).show();
                //0、用户不存在或密码错误；1、登录成功
                if (index.getScd().equals("1")) {
                    //跳转到首页（改版之前）
//                    Intent intent = new Intent(LoginActivity.this,
//                            TiZhuActivity.class);
                    //跳转到首页（改版之后）
                    Intent intent = new Intent(LoginActivity.this,
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
                    User.zhfz=index.getLogin().getIszfz();
                    /**是不是显示党委章*/
                    User.dwz=index.getLogin().getIsdwz();


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
                    User.hyclgl_36=index.getLogin().getRlist().getHycl29();
                    Log.i("zhang", "___政府张"+User.zfz_32+"_____党委章"+User.dwz_31);
                    Log.i("zhang", "___"+User.dbgz_1+User.szl_2+User.zbgz_3);

                    if (dlMxy.isChecked()) {
                        Log.i("zhang", "选择中记住密码");
                        SharedPreferencesUtils.saveString(LoginActivity.this,
                                Globals.USER_JJMM, "1");
                    } else {
                        Log.i("zhang", "没有选择中记住密码");
                        SharedPreferencesUtils.saveString(LoginActivity.this,
                                Globals.USER_JJMM, "0");
                    }

                    //将用的账号密码保存在本地用于下次登录(用户的头像的网络地址)
                    SharedPreferencesUtils.saveString(LoginActivity.this,
                            Globals.USER_PHONE, index.getLogin().getUsername());
                    SharedPreferencesUtils.saveString(LoginActivity.this,
                            Globals.USER_PASSWORD, poss);
                    SharedPreferencesUtils.saveString(LoginActivity.this,
                            Globals.USER_SH, index.getLogin().getPh());
                    SharedPreferencesUtils.saveString(LoginActivity.this,
                            Globals.WY_id, User.mis_id);
                    SharedPreferencesUtils.saveString(LoginActivity.this,
                            Globals.C_id, User.cid);
                    SharedPreferencesUtils.saveString(LoginActivity.this,
                            Globals.I_isponh, isponh);

                    //登录成功的情况下给用户的实体类添加信息（全局使用）
                    User.isLogin = true;
                    User.sid = index.getLogin().getUsername();
                    //头像地址
                    User.IconPath = index.getLogin().getPh();
                    User.wcjd = index.getLogin().getWccd();
                    //添加密码
                    User.pwd = poss;
                    //添加默认手机号码
                    User.phone = index.getLogin().getPhone();
                    User.nc = index.getLogin().getNa();
                    Log.i("zhang", "我的账号：" +User.sid+"-----我的密码："+User.pwd);
                    //登录成功跳转到首页
                    startActivity(intent);
                    //退出页面
                    finish();
                }

            }
//                Log.i("onsuccess", "集合数据个数" + index.getMsg());

        }.protocolBuffer(LoginActivity.this, Globals.WS_URI, index, null);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭
        ShareSDK.stopSDK(this);
    }




}
