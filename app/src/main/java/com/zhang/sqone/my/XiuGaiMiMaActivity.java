package com.zhang.sqone.my;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改手机号码
 *
 * @author ZJP
 *         created at 2016/2/25 14:29
 */
public class XiuGaiMiMaActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.xgmm_omima)
    /**原密码*/
            EditText xgmmOmima;
    @Bind(R.id.xgmm_nmima)
    /**新密码*/
            EditText xgmmNmima;
    @Bind(R.id.xgmm_qr_nmima)
    /**确认新密码*/
            EditText xgmmQrNmima;
    @Bind(R.id.xgmm_qr_button)
    /**确认按钮*/
            Button xgmmQrButton;

    /**
     * 原密码，新密码， 确认新密码
     */
    String soPwd1, snPwd1, snPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_yan_zheng_shou_ji);
        ButterKnife.bind(this);
        xgmmQrButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        soPwd1 = xgmmOmima.getText().toString().trim();
        snPwd1 = xgmmNmima.getText().toString().trim();
        snPwd2 = xgmmQrNmima.getText().toString().trim();
        if ("".equals(soPwd1) || "".equals(snPwd1) || "".equals(snPwd2)) {
            Toast.makeText(XiuGaiMiMaActivity.this, "您输入的信息不全，请查证后再确定",
                    Toast.LENGTH_SHORT).show();
        } else {

            if (snPwd1.equals(snPwd2)) {

                regRequest();

            } else {

                Toast.makeText(XiuGaiMiMaActivity.this, "您输入的两次新密码不匹配",
                        Toast.LENGTH_SHORT).show();
            }


        }
    }

    /**
     * 注册提交用户信息
     */
    public void regRequest() {
        if (snPwd1.length() < 6) {
            Toast.makeText(XiuGaiMiMaActivity.this, "密码不足6位！", Toast.LENGTH_SHORT).show();
        } else {
            if(snPwd1.equals(soPwd1)){
                Toast.makeText(XiuGaiMiMaActivity.this, "新密码和旧密码不能相同！", Toast.LENGTH_SHORT).show();
            }else{
                //设置注册的发送实例
                Index.ReqIndex.ReqUpwd.Builder reqrec = Index.ReqIndex.ReqUpwd.newBuilder();
                reqrec.setSid(User.phone);
                //添加原密码
                reqrec.setOpwd(soPwd1);
                //添加新密码
                reqrec.setNpwd(snPwd1);
                //修改密码
                Index.ReqIndex index = Index.ReqIndex.newBuilder().setSid(User.sid).setRequpwd(reqrec).setAc("UPWD").build();
                new HttpUtil() {
                    @Override
                    public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                        //成功
                        if (index.getScd().equals("1")) {
                            Toast.makeText(XiuGaiMiMaActivity.this, index.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            User.pwd=snPwd1;
                            SharedPreferencesUtils.saveString(XiuGaiMiMaActivity.this,
                                    Globals.USER_PASSWORD, snPwd1);
                            //退出页面
                            finish();
                        }else{
                            Toast.makeText(XiuGaiMiMaActivity.this, index.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }.protocolBuffer(XiuGaiMiMaActivity.this, Globals.WS_URI, index, null);

            }

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
