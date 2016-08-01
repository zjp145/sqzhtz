package com.zhang.sqone.my;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.AppUtil;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改手机号码
 *
 * @author ZJP
 *         created at 2016/2/29 14:16
 */
public class XiuGaiPhoneActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.xiugai_phone)
    TextView xiugaiPhone;

    @Bind(R.id.xiugai_phone_e)
            /**新手机号码*/
    EditText xiugaiPhoneE;
    @Bind(R.id.xiugai_yzm)
            /**新手机的验证码*/
    EditText xiugaiYzm;
    @Bind(R.id.xiugai_yzm_button)
            /**获得验证码的按钮*/
    Button xiugaiYzmButton;
    @Bind(R.id.xiugai_button)
            /**完成操作按钮*/
    Button xiugaiButton;
    /**获得用户手机号  验证码*/
    String sPhone,sYzm;

    /**倒计时的设置*/
    private CountDownTimer mCountDownTimer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_xiu_gai_phone);
        ButterKnife.bind(this);
        xiugaiYzmButton.setOnClickListener(this);
        xiugaiButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        sPhone = xiugaiPhoneE.getText().toString().trim();
        Log.i("zhng","有点击事件");
        switch (v.getId()){
            //点击发送验证码
            case R.id.xiugai_yzm_button:
                Log.i("zhng","点击发送验证码");
                if ("".equals(sPhone)) {
                    Toast.makeText(XiuGaiPhoneActivity.this, "手机号不能为空",
                            Toast.LENGTH_SHORT).show();
                }
                //正则判断手机号
                else if (!AppUtil.isMobileNO(sPhone)){
                    Toast.makeText(XiuGaiPhoneActivity.this, "手机号错误",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    getTime();
                    getCodeRequest();
                }
                break;
            case R.id.xiugai_button:
                //获得用户所有的信息
                sYzm = xiugaiYzm.getText().toString().trim();

                if ("".equals(sPhone) || "".equals(sYzm)) {
                    Toast.makeText(XiuGaiPhoneActivity.this, "您输入的信息不全，请查证后再确定",
                            Toast.LENGTH_SHORT).show();
                } else {
                    regRequest();
                }

                break;

        }
    }

    /**提交新手机号码*/
    public void regRequest() {
        //设置注册的发送实例
        Index.ReqIndex.ReqUphone.Builder uphone = Index.ReqIndex.ReqUphone.newBuilder();
        //添加验证码
        uphone.setNyzm(sYzm);
        //添加手机号码
        uphone.setNphn(sPhone);
        uphone.setSid(User.phone);
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setRequphone(uphone).setAc("UPHONE").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                //成功
                if(index.getScd().equals("1")){
                    Toast.makeText(XiuGaiPhoneActivity.this, index.getMsg(),
                            Toast.LENGTH_SHORT).show();
                    User.phone=sPhone;

                    finish();
                }
            }
        }.protocolBuffer(XiuGaiPhoneActivity.this, Globals.WS_URI, index, null);



    }



    /**点击验证码发送请求验证码的发送*/
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
        message.setType("2");
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setReqmessage(message).setAc("SCD").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                Toast.makeText(XiuGaiPhoneActivity.this, index.getMsg(),
                        Toast.LENGTH_SHORT).show();
            }
        }.protocolBuffer(XiuGaiPhoneActivity.this, Globals.WS_URI, index, null);
    }

    //创建验证码倒计时的功能
    public void getTime() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                xiugaiYzmButton.setClickable(false);
                xiugaiYzmButton.setText("重新获取" + millisUntilFinished / 1000 + "");
                //点击的时候更换背景
                //zcYzmButton.setBackgroundResource(R.drawable.button_back_h);
            }

            public void onFinish() {
                xiugaiYzmButton.setClickable(true);
                xiugaiYzmButton.setText("重新获取");
                //点击的时候更换背景
                // zcYzmButton.setBackgroundResource(R.drawable.button_back);
            }
        };
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
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

}
