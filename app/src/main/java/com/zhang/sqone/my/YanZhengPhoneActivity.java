package com.zhang.sqone.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.utils.AppUtil;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 验证手机号码界面(两个)和修改手机号码
 * 通过intent的值判定使用
 *
 * @author ZJP
 *         created at 2016/2/24 9:10
 */
public class YanZhengPhoneActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.yanzheng_phone)
    /**手机号文字*/
            TextView yanzhengPhone;
    @Bind(R.id.yanzheng_phone_e)
    /**手机号码*/
            EditText yanzhengPhoneE;
    @Bind(R.id.yanzheng_yzm)
    /**验证码*/
            EditText yanzhengYzm;
    @Bind(R.id.yanzheng_yzm_button)
    /**发送验证码的按钮*/
            Button yanzhengYzmButton;

    @Bind(R.id.di_text)
    /**用户协议*/
            LinearLayout diText;
    @Bind(R.id.yanzheng_button)
    /**确定按钮*/
            Button yanzhengButton;
    /**
     * 判定key  手机号码，发送类型 验证码
     */
    String yz, sPhone, type, sYzm;
    @Bind(R.id.lianxi_kefu)
            /**联系客服*/
    TextView lianxiKefu;
    /**
     * 倒计时的设置
     */
    private CountDownTimer mCountDownTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_yan_zheng_phone);
        ButterKnife.bind(this);
        yanzhengYzmButton.setOnClickListener(this);
        yanzhengButton.setOnClickListener(this);
        lianxiKefu.setOnClickListener(this);
        yz = getIntent().getStringExtra(Globals.YZ);
        if (yz.equals("xgmm")) {
            //修改验证时候的的手机号码
            type = "1";
        } else {
            yanzhengPhone.setText("手机号码");
            diText.setVisibility(View.GONE);
            type = "4";
        }
    }


    @Override
    public void onClick(View v) {
        sPhone = yanzhengPhoneE.getText().toString().trim();
        switch (v.getId()) {
            //点击发送验证码
            case R.id.yanzheng_yzm_button:
                if ("".equals(sPhone)) {
                    Toast.makeText(YanZhengPhoneActivity.this, "手机号不能为空",
                            Toast.LENGTH_SHORT).show();
                }
                //正则判断手机号
                else if (!AppUtil.isMobileNO(sPhone)) {
                    Toast.makeText(YanZhengPhoneActivity.this, "手机号错误",
                            Toast.LENGTH_SHORT).show();
                } else {
                    getTime();
                    getCodeRequest();
                }
                break;
            case R.id.yanzheng_button:
                //判断协议是不是选中

                //获得用户所有的信息
                sYzm = yanzhengYzm.getText().toString().trim();

                if ("".equals(sPhone) || "".equals(sYzm)) {
                    Toast.makeText(YanZhengPhoneActivity.this, "您输入的信息不全，请查证后再确定",
                            Toast.LENGTH_SHORT).show();
                } else {
                    regRequest();
                }
                break;
            case R.id.lianxi_kefu:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "82896422");
                intent.setData(data);
                startActivity(intent);
                break;

        }
    }
    /**
     * 验证原手机号码
     */
    public void regRequest() {
        //设置注册的发送实例
        Index.ReqIndex.ReqUphone.Builder uphone = Index.ReqIndex.ReqUphone.newBuilder();
        //添加验证码
        uphone.setOyzm(sYzm);
        //添加手机号码
        uphone.setOphn(sPhone);
        //
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setRequphone(uphone).setAc("YZSJH").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                //注册成功
                if (index.getScd().equals("1")) {
                    Toast.makeText(YanZhengPhoneActivity.this, index.getMsg(),
                            Toast.LENGTH_SHORT).show();
                    //前往修改手机号码界面
                    startActivity(new Intent(YanZhengPhoneActivity.this,
                            XiuGaiPhoneActivity.class));
                    //退出页面
                    finish();
                }
            }
        }.protocolBuffer(YanZhengPhoneActivity.this, Globals.WS_URI, index, null);


    }

    //创建验证码倒计时的功能
    public void getTime() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                yanzhengYzmButton.setClickable(false);
                yanzhengYzmButton.setText("重新获取" + millisUntilFinished / 1000 + "");
                //点击的时候更换背景
                //zcYzmButton.setBackgroundResource(R.drawable.button_back_h);
            }

            public void onFinish() {
                yanzhengYzmButton.setClickable(true);
                yanzhengYzmButton.setText("重新获取");
                //点击的时候更换背景
                // zcYzmButton.setBackgroundResource(R.drawable.button_back);
            }
        };
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
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
        message.setMb(sPhone);
        //添加手机号码的类型
        message.setType(type);
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setReqmessage(message).setAc("SCD").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                Toast.makeText(YanZhengPhoneActivity.this, index.getMsg(),
                        Toast.LENGTH_SHORT).show();
            }
        }.protocolBuffer(YanZhengPhoneActivity.this, Globals.WS_URI, index, null);
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