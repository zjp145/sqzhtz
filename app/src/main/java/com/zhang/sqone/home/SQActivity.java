package com.zhang.sqone.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.bean.WenJianBean;
import com.zhang.sqone.utils.GsonUtils;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.ListViewForScrollView;
import com.zhang.sqone.views.TitleBarView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 请假申请
 *
 * @author ZJP
 *         created at 2016/5/6 16:02
 */
public class SQActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.qj_title)
    TitleBarView qjTitle;
    @Bind(R.id.qingjia_h5)
    WebView qingjiaH5;
    @Bind(R.id.sq_list)
    /**请假列表*/
            ListViewForScrollView sqList;
    @Bind(R.id.ceshi_button)
    /**测试使用*/
            Button ceshiButton;
    @Bind(R.id.view_bar)
    View viewBar;
    @Bind(R.id.shenqing_fj)
    LinearLayout shenqingFj;
    private boolean isone = true;

    private String title;
    private CommonAdapter<String> resultAdapter;
    private List<String> fileList = new ArrayList<>();
    private Intent intent;
    private RequestParams params;
    private int i = 1;
    private ProgressBar progressBar;
    private AlertDialog downLoadDialog;
    private String wid;
    private List<String> fileListid = new ArrayList<>();
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
    private String zhang;

    private void updateTitle() {
        shenqingFj.setVisibility(View.GONE);
        sqList.setVisibility(View.GONE);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sq);
        ButterKnife.bind(this);
        ceshiButton.setOnClickListener(this);
        title = getIntent().getStringExtra(Globals.QJBS);
        qjTitle.setText(title);
        qingjiaH5.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("zhang", "点击链接" + url);
                if (url.equals(Globals.MY_URI + "navigator.jsp")) {
                    Log.i("zhang", "进入返回");
                    finish();
                }
                if (url.equals(Globals.MY_URI + "login.jsp")) {
                    Log.i("zhang", "进入返回");
                    finish();
                }
                view.loadUrl(url);
                return true;
            }
        });

        qingjiaH5.setWebChromeClient(new WebChromeClient());
        //支持javascript
        qingjiaH5.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        qingjiaH5.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        qingjiaH5.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        qingjiaH5.getSettings().setUseWideViewPort(true);
        qingjiaH5.getSettings().setDomStorageEnabled(true);
        //自适应屏幕
        qingjiaH5.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        qingjiaH5.getSettings().setLoadWithOverviewMode(true);
        qingjiaH5.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        qingjiaH5.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
//        String s = "http://192.168.1.161:8280/tzzhsq/login.cmd?usercode="+ User.sid + "&pwd="+User.pwd+"";
//        Log.i("zhang", "网址"+s);
//        wv.loadUrl(s);
//        qingjiaH5.addJavascriptInterface(new JsInterface(getContext()), "AndroidWebView");

        if (title.equals("事假申请")) {
            setTranslucentStatus();
            qingjiaH5.loadUrl(Globals.MY_URI + "oa_office/startup.cmd?$ACTION=begin&flowcode=leave");
        }

        if (title.equals("病假申请")) {
            setTranslucentStatus();
            shenqingFj.setVisibility(View.VISIBLE);
            qingjiaH5.loadUrl(Globals.MY_URI + "oa_office/startup.cmd?$ACTION=begin&flowcode=sickleave");

        }
        if (title.equals("年假申请")) {
            setTranslucentStatus();

            qingjiaH5.loadUrl(Globals.MY_URI + "oa_office/startup.cmd?$ACTION=begin&flowcode=leven");
        }
        if (title.equals("党委章")) {
            setTranslucentStatus();
            shenqingFj.setVisibility(View.VISIBLE);
            qingjiaH5.loadUrl(Globals.MY_URI + "oa_office/startup.cmd?$ACTION=begin&flowcode=dzsy");
        }
        if (title.equals("政府章")) {
            setTranslucentStatus();
            shenqingFj.setVisibility(View.VISIBLE);
            qingjiaH5.loadUrl(Globals.MY_URI + "oa_office/startup.cmd?$ACTION=begin&flowcode=sealapply");
        }
        if (title.equals("党委发文")) {
            setTranslucentStatus();
            shenqingFj.setVisibility(View.VISIBLE);
            qingjiaH5.loadUrl(Globals.MY_URI + "workflow/startup.cmd?$ACTION=begin&flowcode=dispatchflowdw");
        }
        if (title.equals("政府发文")) {
            setTranslucentStatus();
            shenqingFj.setVisibility(View.VISIBLE);
            qingjiaH5.loadUrl(Globals.MY_URI + "workflow/startup.cmd?$ACTION=begin&flowcode=dispatchflow");
        }
        if (title.equals("会议室预定")) {
            viewBar.setVisibility(View.GONE);
            qingjiaH5.loadUrl(Globals.MY_URI + "oa_office/startup.cmd?$ACTION=begin&flowcode=hyyd");
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

    @Override
    public void onClick(View view) {
        showFileChooser();

    }

    /**
     * 根据返回选择的文件，来进行上传操作
     **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String url = getRealFilePath(this, uri);
            Log.i("ht", "url----" + getRealFilePath(this, uri));
//            String filename = url.substring(url.lastIndexOf('/') + 1);
            if (url == null){
                Toast.makeText(this,"请选择文件管理中的文件",Toast.LENGTH_SHORT).show();
            }else{
                zhang = url;
                AsyncHttpClient client = new AsyncHttpClient();
                params = new RequestParams();
                Log.i("sid", User.sid);
                String req = "{\"Ac\":\"TPSC\",\"Para\":{\"SId\":\""
                        + User.sid + "\",\"T\":\"1\",\"Wid\":\""+wid+"\"}}";
                params.put("id", req);

                File imgFile = new File(url);
                String filename = imgFile.getName();
                fileList.add(filename);
                if (isone) {
                    resultAdapter = new CommonAdapter<String>(SQActivity.this, fileList, R.layout.fayoujian_litem) {
                        @Override
                        public void convert(final ViewHolder holder, final String fileMap) {
                            String end = fileMap.substring(fileMap.lastIndexOf(".") + 1, fileMap.length()).toLowerCase();
                            if (end.equals("doc") || end.equals("docx")) {
                                holder.setImageResource(R.id.fj_wenjian, R.mipmap.word);

                            } else if (end.equals("xls") || end.equals("xlsx")) {
                                holder.setImageResource(R.id.fj_wenjian, R.mipmap.excel);
                            } else if (end.equals("png")|| end.equals("jpg")) {
                                holder.setImageResource(R.id.fj_wenjian, R.mipmap.png);
                            } else if (end.equals("txt")) {
                                holder.setImageResource(R.id.fj_wenjian, R.mipmap.txt);
                            } else if (end.equals("pdf")) {
                                holder.setImageResource(R.id.fj_wenjian, R.mipmap.pdf);
                            } else if (end.equals("ppt") || end.equals("pptx")) {
                                holder.setImageResource(R.id.fj_wenjian, R.mipmap.ppt);
                            }
                            holder.setText(R.id.fj_mingzi, fileMap);
                            holder.setOnClickListener(R.id.fj_chanchu, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fileList.remove(holder.getPosition());
                                    loadData(fileListid.get(holder.getPosition()));
                                    fileListid.remove(holder.getPosition());
                                    resultAdapter.setData(fileList);
                                    resultAdapter.notifyDataSetChanged();
                                    if (fileListid.size()==0){
                                        qingjiaH5.loadUrl("javascript:fujian('')");
                                    }
                                }
                            });

                        }
                    };
                    sqList.setAdapter(resultAdapter);
                    isone = false;
                } else {
                    resultAdapter.setData(fileList);
                    resultAdapter.notifyDataSetChanged();
                }

                if (imgFile.exists() && imgFile.length() > 0) {
                    try {
                        params.put("Zhang.png", imgFile);
                        showDownloadDialog(1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    client.post(SQActivity.this, Globals.WS_URI_WJ, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                        StringBuffer s = new StringBuffer();
//                        for (int j = 0; j<bytes.length-1;j++){
//                            Log.i("swww1", "++"+j+"++" +(char)bytes[j]);
//                            s.append((char)bytes[j]);
//                        }
                            String  s = new String(bytes);
                            downLoadDialog.dismiss();
                            WenJianBean s1 = GsonUtils.json2bean(s.toString(),
                                    WenJianBean.class);
                            if (s1 == null || !(s1.getStu() == 1)) {
                                Toast.makeText(SQActivity.this, Globals.SER_ERROR,
                                        Toast.LENGTH_SHORT).show();
                                fileListid.add("-1");
                            } else {
                                String ss = "sssss";
                                Log.i("swww1", "成功---"+s1.getRst().getPo()+ss);
                                fileListid.add(s1.getRst().getPo());
                                StringBuilder stringBuilder  = new StringBuilder();
                                for (int i1 = 0;i1<fileListid.size();i1++){
                                    stringBuilder.append(fileListid.get(i1)+",");

                                }
                                Log.i("zhang", "添加id+++"+stringBuilder.substring(0,stringBuilder.length()-1).toString());
                            qingjiaH5.loadUrl("javascript:fujian('" + stringBuilder.substring(0,stringBuilder.length()-1).toString() + "')");
                            }

                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            Log.i("swww2", throwable.getMessage());
                            Log.i("swww3", throwable.toString());
                            Toast.makeText(SQActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);

                            int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                            progressBar.setProgress(count);
                            Log.e("上传 Progress>>>>>", bytesWritten + " / " + totalSize);
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // TODO Auto-generated method stub
                            super.onRetry(retryNo);
                            // 返回重试次数
                        }
                    });
                } else {
                    Toast.makeText(SQActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
                }

            }




        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void loadData(String id) {
        AsyncHttpClient client = new AsyncHttpClient();
        params = new RequestParams();
        Log.i("sid", User.sid);
        String req = "{\"Ac\":\"TPSC\",\"Para\":{\"SId\":\""
                + User.sid + "\",\"T\":\"2\",\"Wid\":\""+id+"\"}}";
        params.put("id", req);

        File imgFile = new File(zhang);
        if (imgFile.exists() && imgFile.length() > 0) {
            try {
                params.put("Zhang.png", imgFile);
                showDownloadDialog(2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            client.post(SQActivity.this, Globals.WS_URI_WJ, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                        StringBuffer s = new StringBuffer();
//                        for (int j = 0; j<bytes.length-1;j++){
//                            Log.i("swww1", "++"+j+"++" +(char)bytes[j]);
//                            s.append((char)bytes[j]);
//                        }
                    String  s = new String(bytes);
                    downLoadDialog.dismiss();
                    WenJianBean s1 = GsonUtils.json2bean(s.toString(),
                            WenJianBean.class);
                    if (s1 == null || !(s1.getStu() == 1)) {
                        Toast.makeText(SQActivity.this, Globals.SER_ERROR,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String ss = "sssss";
                        Log.i("swww1", "成功---"+s1.getRst().getPo()+ss);
//                            qingjiaH5.loadUrl("javascript:fujian('" + s1.getRst().getPo() + "')");
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.i("swww2", throwable.getMessage());
                    Log.i("swww3", throwable.toString());
                    Toast.makeText(SQActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(SQActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 下载的提示框
     */
    protected void showDownloadDialog(int i) {
        {
            // 构造软件下载对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if(i==1){
                builder.setTitle("正在上传");
            }else{
                builder.setTitle("正在删除");
            }

            builder.setCancelable(false);
            // 给下载对话框增加进度条
            final LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(R.layout.downloaddialog, null);
            progressBar = (ProgressBar) v.findViewById(R.id.updateProgress);
            builder.setView(v);
            downLoadDialog = builder.create();
            downLoadDialog.show();

        }

    }

    /**
     * 调用文件选择软件来选择文件
     **/
    private void showFileChooser() {
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    1);
        } catch (ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //将uri转换成String形式
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
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
            wid=name;
        }
    }
}
