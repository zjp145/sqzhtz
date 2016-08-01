package com.zhang.sqone.my;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.DeleteF;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置界面和我的界面相似
 *
 * @author ZJP
 *         created at 2016/2/25 10:51
 */
public class SetUpActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.shezhi_dl)
    /**设置自动登录*/
            ImageButton shezhiDl;
    @Bind(R.id.shezhi_ts)
    /**设置推送消息*/
            ImageButton shezhiTs;
    @Bind(R.id.shezhi_qlhc)
            /**清理缓存*/
    LinearLayout shezhiQlhc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        if (User.isAuto) {
            shezhiDl.setImageResource(R.mipmap.on);

        } else {
            shezhiDl.setImageResource(R.mipmap.off);
        }

        if (User.isPosh) {
            shezhiTs.setImageResource(R.mipmap.on);

        } else {
            shezhiTs.setImageResource(R.mipmap.off);
        }
        shezhiDl.setOnClickListener(this);
        shezhiTs.setOnClickListener(this);
        shezhiQlhc.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //自动登录
            case R.id.shezhi_dl:
                if (User.isAuto) {
                    shezhiDl.setImageResource(R.mipmap.off);
                    User.isAuto = false;

                } else {
                    shezhiDl.setImageResource(R.mipmap.on);
                    User.isAuto = true;
                }
                break;
            //消息推送
            case R.id.shezhi_ts:
                if (User.isPosh) {
                    shezhiTs.setImageResource(R.mipmap.off);
                    User.isPosh = false;

                } else {
                    shezhiTs.setImageResource(R.mipmap.on);
                    User.isPosh = true;
                }

                break;
            //清理缓存
            case R.id.shezhi_qlhc:
                DeleteF d = new DeleteF() {
                    @Override
                    public void determineButton() {
//                        SharedPreferencesUtils.saveString(SetUpActivity.this,
//                                Globals.USER_PHONE, "");
//                        SharedPreferencesUtils.saveString(SetUpActivity.this,
//                                Globals.USER_PASSWORD, "");
//                        SharedPreferencesUtils.saveString(SetUpActivity.this,
//                                Globals.USER_NC, "");
//                        Intent intent = new Intent();
//                        intent.setClass(SetUpActivity.this, TiZhuActivity.class);
////                            intent.putExtra("loginInfo", "login");
//                        startActivity(intent);
//                        User.setLoginInfo("", false, "", "");
//                        SetUpActivity.this.finish();
                    }
                }.deleteDialog(SetUpActivity.this, "你确定要清理缓存吗？", "确定", "取消");
                break;
        }
    }
}
