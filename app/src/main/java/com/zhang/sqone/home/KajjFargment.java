package com.zhang.sqone.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 科室介绍添加的选择出现的fragment
 *
 * @author ZJP
 *         created at 16/3/24 下午5:14
 */
public class KajjFargment extends Fragment {
    @Bind(R.id.keshi_jj_image)
    ImageView keshiJjImage;
    @Bind(R.id.keshi_jj_title)
    TextView keshiJjTitle;
    @Bind(R.id.keshi_jj_con)
    TextView keshiJjCon;
    /**请求使用的数据*/
    private String cid;
    private String tp;
    private String tx;
    private String mc;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kajj_fargment, container, false);
        ButterKnife.bind(this, view);

        cid =getArguments().getString("pid");
        tp = getArguments().getString(Globals.KSTP);
        tx = getArguments().getString(Globals.KSTX);
        mc = getArguments().getString(Globals.KSMC);
        ImageLoader.getInstance().displayImage(tp, keshiJjImage);
        keshiJjTitle.setText(mc);
        keshiJjCon.setText(tx);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
