package com.zhang.sqone.my;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.TitleBarView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 忘记密码的界面（注册界面）
 * 通过tab标记注册和忘记密码
 *
 * @author ZJP
 *         created at 2016/2/23 16:55
 */
public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.zc_phone)
    /**输入的手机号码*/
            EditText zcPhone;
    @Bind(R.id.zc_yzm)
    /**输入的验证码*/
            EditText zcYzm;
    @Bind(R.id.zc_yzm_button)
    /**点击发送验证码*/
            Button zcYzmButton;
    @Bind(R.id.zc_xmm)
    /**新密码的标题*/
            TextView zcXmm;
    @Bind(R.id.zc_xmm_e)
    /**输入的新密码*/
            EditText zcXmmE;
    @Bind(R.id.zc_qrxmm)
    /**确认新密码的*/
            TextView zcQrxmm;
    @Bind(R.id.zc_qrxmm_e)
    /**确认的新密码*/
            EditText zcQrxmmE;
    @Bind(R.id.zc_mxy)
    /**协议选中*/
            CheckBox zcMxy;
    @Bind(R.id.zc_dl)
    /**确认注册按钮*/
            Button zcDl;
    @Bind(R.id.zc_title)
    /**标题*/
            TitleBarView zcTitle;
    /**
     * 判断用户跳转的intent
     */
    String tab;
    /**
     * 用户输入的手机号码
     */
    String sPhone;
    /**
     * 倒计时的设置
     */
    private CountDownTimer mCountDownTimer = null;
    /**
     * 手机验证码的类型  用户输入的验证码 两次输入的密码
     */
    private String type, sYzm, sPwd1, sPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
        zcYzmButton.setOnClickListener(this);
        zcDl.setOnClickListener(this);
        //判断
        tab = getIntent().getStringExtra(Globals.TAB);
        //如果程序出现问题友好的提示用户
        if (tab.equals("") || tab == null) {
            Toast.makeText(Globals.context, "服务器异常请稍后重试。", Toast.LENGTH_SHORT).show();
        } else {
            //注册
            if (tab.equals("zc")) {
                //更改标题等必要文字
                zcTitle.setText("注册");
                zcXmm.setText("密码");
                zcXmmE.setHint("请输入您的密码");
                zcQrxmm.setText("确认密码");
                zcQrxmmE.setHint("请再次输入密码");
                zcDl.setText("注册");
                type = "0";

            }
            //忘记密码
            else {
                type = "3";
            }


        }


    }

    @Override
    public void onClick(View v) {
        //获得手机号码
        sPhone = zcPhone.getText().toString().trim();
        switch (v.getId()) {

            //点击发送验证码
            case R.id.zc_yzm_button:
                if ("".equals(sPhone)) {
                    Toast.makeText(ForgetActivity.this, "用户帐号不能为空",
                            Toast.LENGTH_SHORT).show();
                }
                //正则判断手机号
//                else if (!AppUtil.isMobileNO(sPhone)) {
//                    Toast.makeText(ForgetActivity.this, "手机号错误",
//                            Toast.LENGTH_SHORT).show();
//                }

                else {
                    getTime();
                    getCodeRequest();
                }
                break;
            //点击注册将数据上传到服务器中
            case R.id.zc_dl:
                //判断协议是不是选中
//                if (zcMxy.isChecked()) {
                    //获得用户所有的信息
                    sYzm = zcYzm.getText().toString().trim();
                    sPwd1 = zcXmmE.getText().toString().trim();
                    sPwd2 = zcQrxmmE.getText().toString().trim();
                    if ("".equals(sPhone) || "".equals(sYzm) || "".equals(sPwd1) || "".equals(sPwd2)) {
                        Toast.makeText(ForgetActivity.this, "您输入的信息不全，请查证后再确定",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (sPwd1.equals(sPwd2)) {
                            if (tab.equals("zc")) {
                                regRequest();
                            } else {
                                FpwodRequest();
                            }

                        } else {
                            Toast.makeText(ForgetActivity.this, "您输入的两次密码不匹配",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

//                } else {
//
//                    Toast.makeText(ForgetActivity.this, "没有选中协议不能注册",
//                            Toast.LENGTH_SHORT).show();
//                }

                break;

        }
    }

    //创建验证码倒计时的功能
    public void getTime() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                zcYzmButton.setClickable(false);
                zcYzmButton.setText("重新获取" + millisUntilFinished / 1000 + "");
                //点击的时候更换背景
                //zcYzmButton.setBackgroundResource(R.drawable.button_back_h);
            }

            public void onFinish() {
                zcYzmButton.setClickable(true);
                zcYzmButton.setText("重新获取");
                //点击的时候更换背景
                // zcYzmButton.setBackgroundResource(R.drawable.button_back);
            }
        };
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }

    }

    /**
     * 注册提交用户信息
     */
    public void regRequest() {
        if (sPwd1.length() < 6) {
            Toast.makeText(ForgetActivity.this, "密码不足6位！", Toast.LENGTH_SHORT).show();
        } else {

            //设置注册的发送实例
            Index.ReqIndex.ReqRec.Builder reqrec = Index.ReqIndex.ReqRec.newBuilder();
            //添加验证码
            reqrec.setYzm(sYzm);
            //添加手机号码
            reqrec.setPhone(sPhone);
            //添加密码
            reqrec.setPwd(sPwd1);
            //注册
            Index.ReqIndex index = Index.ReqIndex.newBuilder().setReqrec(reqrec).setAc("REC").build();
            new HttpUtil() {
                @Override
                public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                    //注册成功
                    if (index.getScd().equals("1")) {
                        Toast.makeText(ForgetActivity.this, index.getMsg(),
                                Toast.LENGTH_SHORT).show();
                        //返回登录界面
                        startActivity(new Intent(ForgetActivity.this,
                                LoginActivity.class));
                        //退出页面
                        finish();
                    } else {
                        Toast.makeText(ForgetActivity.this, index.getMsg(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }.protocolBuffer(ForgetActivity.this, Globals.WS_URI, index, null);

        }

    }

    /**
     * 判断用户是忘记密码的用户根据忘记密码的请求提交数据
     */
    public void FpwodRequest() {
        if (sPwd1.length() < 6) {
            Toast.makeText(ForgetActivity.this, "密码不足6位！", Toast.LENGTH_SHORT).show();
        } else {
            //设置忘记密码的实例
            Index.ReqIndex.ReqFpwd.Builder reqfpwd = Index.ReqIndex.ReqFpwd.newBuilder();
            //添加验证码
            reqfpwd.setYzm(sYzm);
            //添加手机号码
            reqfpwd.setUsercode(sPhone);
            reqfpwd.setPhonenum(User.mis_id);
            //添加密码
            reqfpwd.setNpwd(sPwd1);
            //忘记密码
            Index.ReqIndex index = Index.ReqIndex.newBuilder().setReqfpwd(reqfpwd).setAc("FUPWD").build();
            new HttpUtil() {
                @Override
                public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                    //忘记密码成功
                    if (index.getScd().equals("1")) {
                        Toast.makeText(ForgetActivity.this, index.getMsg(),
                                Toast.LENGTH_SHORT).show();
                        //返回登录界面
                        startActivity(new Intent(ForgetActivity.this,
                                LoginActivity.class));
                        //退出页面
                        finish();
                    }else{
                        Toast.makeText(ForgetActivity.this, index.getMsg(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }.protocolBuffer(ForgetActivity.this, Globals.WS_URI, index, null);

        }
    }


    /**
     * 点击验证码发送请求验证码的发送
     */
    public void getCodeRequest() {
//        Index.ReqIndex.ReqRec.Builder message = Index.ReqIndex.ReqRec.newBuilder();
//        message.setPhone("15931295549");
//        message.setPwd("15910438651");
//        message.setYzm("123456");

        //获得修改手机号的发送实例
        Index.ReqIndex.ReqMessage.Builder message = Index.ReqIndex.ReqMessage.newBuilder();
        //添加手机号码
        message.setUsercode(sPhone);
        //添加手机号码的类型
        message.setPhonenum(User.mis_id);
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setReqmessage(message).setAc("SYZM").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                if (index.getScd().equals("1")){
                    zcYzm.setText(index.getReqmessage().getYzm());
                    Toast.makeText(ForgetActivity.this, index.getMsg(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgetActivity.this, index.getMsg(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        }.protocolBuffer(ForgetActivity.this, Globals.WS_URI, index, null);
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
}
