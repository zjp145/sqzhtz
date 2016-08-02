package com.zhang.sqone.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Monitor;
import com.zhang.sqone.utils.HttpUtilMonitor;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 环境检查的内容选择
 *
 * @author ZJP
 *         created at 2016/3/6 15:57
 */
public class HuanJingNRActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.huanjing_nr_list)
            /**展示列表页的数据*/
    ListView huanjingNrList;
    private String hjdzid;
    /**网络请求的数据集合*/
    private List<Monitor.MonIndex.contentMap> deta;
    /**万能适配器*/
    private CommonAdapter<Monitor.MonIndex.contentMap> resultAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_huan_jing_nr);
        ButterKnife.bind(this);
        //获得地址
        intent = getIntent();
        hjdzid =intent.getStringExtra(Globals.HJ_DZ_ID);
        //获得数据
        regRequest();

        huanjingNrList.setOnItemClickListener(this);
    }
    /**网络请求数据*/
    public void regRequest() {


        Monitor.MonIndex index = Monitor.MonIndex.newBuilder().setAc("JCNRL").build();

        new HttpUtilMonitor() {
            @Override
            public <T> void analysisInputStreamData(Monitor.MonIndex index) throws IOException {
              deta =index.getContentlistList();
                resultAdapter = new CommonAdapter<Monitor.MonIndex.contentMap>(HuanJingNRActivity.this,deta,R.layout.huanjing_nr_item) {
                    @Override
                    public void convert(ViewHolder holder, Monitor.MonIndex.contentMap detailMap) {
                                holder.setText(R.id.neirong_text,detailMap.getContentname());
                                holder.setText(R.id.nr_fenshu_text,detailMap.getScore()+"分");
                    }
                };
                huanjingNrList.setAdapter(resultAdapter);

            }
        }.protocolBuffer(HuanJingNRActivity.this, Globals.HJ_URI, index, null);
    }
    /*点击的数据添加到主页面上*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent.putExtra(Globals.HJ_NR,deta.get(position).getContentname());
        intent.putExtra(Globals.HJ_FS,deta.get(position).getScore());
         setResult(Activity.RESULT_OK, intent);//返回页面1
        finish();
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
