package com.zhang.sqone.dbgw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.Document;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.HttputilDocument;
import com.zhang.sqone.views.ListViewForScrollView;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 代办详情中的流转跟踪
 *
 * @author ZJP
 * created at 2016/3/4 13:18
 */
public class BsFargment extends Fragment {

    @Bind(R.id.liucheng_genzhong)
            /**流程*/
            ListViewForScrollView liuchengGenzhong;
    /**访问使用的inid(请求数据的时候需要提供的id)*/
    private String inid;
    /**访问使用的wid(请求数据的时候需要提供的id)*/
    private String wid;
    /**展示的数据列表*/
    private List<Document.ReqDocument.FlowMap> listDate;
    /**服务器返回的使用list显示的待办的流程跟踪信息*/
    private CommonAdapter<Document.ReqDocument.FlowMap> resultAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dbwg_xq_liucheng, container, false);
        //获得
        inid = getArguments().getString("inid");
        wid = getArguments().getString("wid");
        Log.i("zhang", "跳转 "+inid+"        "+wid);
        ButterKnife.bind(this, view);

        //获得数据
        regRequest();

        return view;
    }

    /**
     * 加载列表页
     */
    public void regRequest() {

        //设置注册的发送实例
//        Index.ReqIndex.Builder sid = Index.ReqIndex.newBuilder();
//        sid.setSid(User.sid);
      Document.ReqDocument.param.Builder param = Document.ReqDocument.param.newBuilder();
        param.setInid(inid);
        param.setWid(wid);
        Document.ReqDocument reqDocument  = Document.ReqDocument.newBuilder().setPa(param).setSid(User.sid).setAc("LCJD").build();
        new HttputilDocument() {
            @Override
            public <T> void analysisInputStreamData(Document.ReqDocument index) throws IOException {
                    listDate=index.getFlowlistList();
                Log.i("zhang", "集合个数"+listDate.size());
                resultAdapter = new CommonAdapter<Document.ReqDocument.FlowMap>(getActivity(), listDate, R.layout.liuchenggenzong_item){

                    @Override
                    public void convert(ViewHolder holder, Document.ReqDocument.FlowMap fileMap) {
                        holder.setText(R.id.time_text, fileMap.getDt());
                        holder.setText(R.id.neirong_liucheng_text, fileMap.getCon());
                        int i =listDate.indexOf(fileMap);

                        if(i==0){
                            //隐藏
                            holder.setVisible(R.id.shang_view,false);
                            holder.setVisible(R.id.xia_view,true);
                            holder.setImageResource(R.id.liucheng_tp,R.mipmap.lcicons);
                        }else if (i==listDate.size()-1){
                            Log.i("zhang", "奇怪"+i);
                            holder.setVisible(R.id.xia_view,false);
                        }






                        Log.i("zhang", "convert "+i);

                    }
                };

                liuchengGenzhong.setAdapter(resultAdapter);
            }


        }.protocolBuffer(getActivity(), Globals.GW_URI, reqDocument, null);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
