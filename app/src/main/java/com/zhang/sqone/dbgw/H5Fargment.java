package com.zhang.sqone.dbgw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.views.ListViewForScrollView;

/**
*公文详情中的h5的界面
*@author ZJP
*created at 2016/3/7 17:16
*/
public class H5Fargment extends Fragment {
    private WebView wv;
    private View viewById;
    private ListView listView;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            //更新UI
            switch (msg.what)
            {
                case 1:
                    updateTitle();
                    break;
            }
        }
    };
    private TextView textview1;
    private ListViewForScrollView listView2;
    private TextView textview2;
    private ListViewForScrollView listView1;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h5_webview_layout, container, false);
        viewById = getActivity().findViewById(R.id.dbgw_li);
        listView = (ListView) getActivity().findViewById(R.id.gw_xiangqing_list);
        textview1 = (TextView)getActivity().findViewById(R.id.ld_list1);
        listView1 = (ListViewForScrollView) getActivity().findViewById(R.id.lingdao_list);
        textview2 = (TextView)getActivity().findViewById(R.id.ks_list1);
        listView2 = (ListViewForScrollView) getActivity().findViewById(R.id.keshifuzheren_list);
        wv = (WebView) view.findViewById(R.id.statement_wv);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("zhang", "点击链接"+url);
                if (url.equals(Globals.MY_URI+"navigator.jsp")){
                    Log.i("zhang", "进入返回");
                    getActivity().finish();
                }
                if (url.equals(Globals.MY_URI+"login.jsp")){
                    Log.i("zhang", "进入返回");
                    getActivity().finish();
                }
                view.loadUrl(url);
                return true;
            }
        });
        //支持javascript
        wv.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        wv.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wv.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        wv.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        String s = "http://192.168.1.161:8280/tzzhsq/login.cmd?usercode="+ User.sid + "&pwd="+User.pwd+"";
//        Log.i("zhang", "网址"+s);
//        wv.loadUrl(s);
        wv.addJavascriptInterface(new JsInterface(getContext()), "AndroidWebView");

        wv.loadUrl(getArguments().getString("pid"));

        return view;
    }
    public void updateTitle()
    {
        viewById.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        listView1.setVisibility(View.GONE);
        textview1.setVisibility(View.GONE);
        textview2.setVisibility(View.GONE);
        listView2.setVisibility(View.GONE);
    }
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJs(String name) {
            Log.i("zhang", "showInfoFromJs1:--- "+name);
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void WidFromJs(String name) {
            Log.i("zhang", "showInfoFromJs2:--- "+name);
        }
    }


}
