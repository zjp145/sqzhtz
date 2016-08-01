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
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改昵称
 *
 * @author ZJP
 *         created at 2016/2/25 11:49
 */
public class NickNameActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.xiugainc_nc)
            /**用户输入的昵称*/
    EditText xiugaincNc;
    @Bind(R.id.xiugainc_button)
            /**用户确认按钮*/
    Button xiugaincButton;

    /**用户输入的昵称*/
    String nc ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_nick_name);
        ButterKnife.bind(this);
        xiugaincButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //获得昵称
        nc = xiugaincNc.getText().toString().trim();
        if ("".equals(nc)){
            Toast.makeText(NickNameActivity.this, "昵称不能为空",
                    Toast.LENGTH_SHORT).show();

        }else{
            getCodeRequest();
        }

    }

    /**网络请求昵称的修改*/
    public void getCodeRequest() {
//        Index.ReqIndex.ReqRec.Builder message = Index.ReqIndex.ReqRec.newBuilder();
//        message.setPhone("15931295549");
//        message.setPwd("15910438651");
//        message.setYzm("123456");

        //获得修改手机号的发送实例
        Index.ReqIndex.Upname.Builder upname = Index.ReqIndex.Upname.newBuilder();
        //添加昵称
        upname.setNc(nc);
        //添加id
        upname.setNid(User.phone);
        Index.ReqIndex index = Index.ReqIndex.newBuilder().setUpname(upname).setAc("XGNC").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                Toast.makeText(NickNameActivity.this, index.getMsg(),
                        Toast.LENGTH_SHORT).show();
            }
        }.protocolBuffer(NickNameActivity.this, Globals.WS_URI, index, null);
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
