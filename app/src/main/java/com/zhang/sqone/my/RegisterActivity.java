package com.zhang.sqone.my;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.TiZhuActivity;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.AppUtil;
import com.zhang.sqone.utils.SharedPreferencesUtils;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.CircularImage;
import com.zhang.sqone.views.DeleteF;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的界面
 *
 * @author ZJP
 *         created at 2016/2/24 9:29
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.myxx_zhanghu)
    /**账号信息*/
            LinearLayout myxxZhanghu;
    @Bind(R.id.myxx_shezhi)
    /**设置*/
            LinearLayout myxxShezhi;
    @Bind(R.id.myxx_banben)
    /**版本*/
            LinearLayout myxxBanben;

    @Bind(R.id.myxx_guanyu)
    /**关于*/
            LinearLayout myxxGuanyu;
    @Bind(R.id.myxx_tuicu_button)
    /**退出登录*/
            Button myxxTuicuButton;
    @Bind(R.id.my_banben)
    TextView myBanben;
    @Bind(R.id.myxx_tx_iamge)
            /**用户头像*/
    CircularImage myxxTxIamge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //添加版本
        myBanben.setText(AppUtil.getVersion());
        //添加用户头像
        if (!User.IconPath.equals("") && User.IconPath != null) {
            //下载头像
            ImageLoader.getInstance().displayImage(User.IconPath, myxxTxIamge);
        }
        clicks();

    }

    /**
     * 加载布局的点击事件
     */
    private void clicks() {
        myxxZhanghu.setOnClickListener(this);
        myxxBanben.setOnClickListener(this);
        myxxGuanyu.setOnClickListener(this);
        myxxTuicuButton.setOnClickListener(this);
        myxxShezhi.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!User.IconPath.equals("") && User.IconPath != null) {
            //下载头像
            ImageLoader.getInstance().displayImage(User.IconPath, myxxTxIamge);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击的是账号信息
            case R.id.myxx_zhanghu:
                Intent intent = new Intent(this, UserMessActivity.class);
                startActivity(intent);
                break;
            //点击的是设置
            case R.id.myxx_shezhi:
                Intent intent1 = new Intent(this, SetUpActivity.class);
                startActivity(intent1);
                break;
            //点击的是版本
            case R.id.myxx_banben:
                Toast.makeText(RegisterActivity.this, "当前版本是最新版本", Toast.LENGTH_SHORT).show();
                break;

            //点击的是关于
            case R.id.myxx_guanyu:
                Toast.makeText(RegisterActivity.this, "关于界面暂无", Toast.LENGTH_SHORT).show();
                break;
            //点击的是退出登录的按钮
            case R.id.myxx_tuicu_button:
                if (User.isLogin) {
                    DeleteF d = new DeleteF() {
                        @Override
                        public void determineButton() {
                            SharedPreferencesUtils.saveString(RegisterActivity.this,
                                    Globals.USER_PHONE, "");
                            SharedPreferencesUtils.saveString(RegisterActivity.this,
                                    Globals.USER_PASSWORD, "");
                            SharedPreferencesUtils.saveString(RegisterActivity.this,
                                    Globals.USER_NC, "");
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this, TiZhuActivity.class);
//                            intent.putExtra("loginInfo", "login");
                            startActivity(intent);
                            User.setLoginInfo("", false, "", "");
                            RegisterActivity.this.finish();
                        }
                    }.deleteDialog(RegisterActivity.this, "你确定要退出当前账号？", "", "");
                }
                break;
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
