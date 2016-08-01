package com.zhang.sqone.xiangqing;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Myself;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utilss.HttpUtilM;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DZXQActivity extends BaseActivity {

//    @Bind(R.id.gz_sj)
//    /**时间*/
//            TextView gzSj;
//    @Bind(R.id.gz_gzbz)
//    /**挂职补助*/
//            TextView gzGzbz;
//    @Bind(R.id.gz_kzzgjj)
//    /**扣住房公积金*/
//            TextView gzKzzgjj;
//    @Bind(R.id.gz_ylbxkk)
//    /**养老保险扣款*/
//            TextView gzYlbxkk;
//    @Bind(R.id.gz_shfgg)
//    /**实发工资*/
//            TextView gzShfgg;
//    @Bind(R.id.gz_jbgz)
//    /**基本工资*/
//            TextView gzJbgz;
//    @Bind(R.id.gz_sy)
//    /**失业*/
//            TextView gzSy;
//    @Bind(R.id.gz_xj)
//    /**小计*/
//            TextView gzXj;
//    @Bind(R.id.gz_bfygz)
//    /**补发月工资*/
//            TextView gzBfygz;
//    @Bind(R.id.gz_jsgz)
//    /**计税工资*/
//            TextView gzJsgz;
//    @Bind(R.id.gz_bz)
//    /**备注*/
//            TextView gzBz;
//    @Bind(R.id.gz_ygxm)
//    /**员工的姓名*/
//            TextView gzYgxm;
    @Bind(R.id.gz_list)
    ListView gzList;
    private String xqid;
    private List<Myself.ReqMyself.SalaryDetail> fileList;
    private CommonAdapter<Myself.ReqMyself.SalaryDetail> resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_dzxq);
        ButterKnife.bind(this);
        xqid = getIntent().getStringExtra(Globals.XQID);
        regRequest();
    }

    /**
     * 获得用户的信息
     */
    public void regRequest() {
        //设置注册的发送实例

        Myself.ReqMyself index = Myself.ReqMyself.newBuilder().setSid(User.sid).setAc("GZXQ").setId(xqid).build();
        new HttpUtilM() {
            @Override
            public <T> void analysisInputStreamData(Myself.ReqMyself index) throws IOException {
                fileList = index.getSdList();
                Log.i("zhang", "数据个数："+fileList.size());
//                gzYgxm.setText(index.getSd().getName());
//                gzSj.setText(index.getSd().getYear()+index.getSd().getMonth());
////                gzGzbz.getText(index.getSd().);
//                gzKzzgjj.setText(index.getSd().getHousingfund()+"元");
//                gzYlbxkk.setText(index.getSd().getEndowmentinsurance()+"元");
//                gzShfgg.setText(index.getSd().getNetpayroll()+"元");
//////                gzJbgz.setText(index.getSd().);
                resultAdapter = new CommonAdapter<Myself.ReqMyself.SalaryDetail>(DZXQActivity.this, fileList, R.layout.gz_litem) {
                    @Override
                    public void convert(ViewHolder holder, Myself.ReqMyself.SalaryDetail fileMap) {
                        if (fileMap.getName().equals("时间")||fileMap.getName().equals("员工姓名")||fileMap.getName().equals("备注")){
                            holder.setText(R.id.gz_v,fileMap.getName()).setText(R.id.gz_k,fileMap.getSalary());
                        }else{
                            holder.setText(R.id.gz_v,fileMap.getName()).setText(R.id.gz_k,fileMap.getSalary()+"元");
                        }


                    }
                };
                gzList.setAdapter(resultAdapter);

            }


        }.protocolBuffer(DZXQActivity.this, Globals.ZB_URI, index, null);

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
