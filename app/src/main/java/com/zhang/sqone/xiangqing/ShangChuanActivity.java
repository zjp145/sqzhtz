package com.zhang.sqone.xiangqing;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.zhang.sqone.bean.Meetingdiscuss;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.bean.WenJianBean;
import com.zhang.sqone.utils.GsonUtils;
import com.zhang.sqone.utils.HttpUtilDocumentme;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.ListViewForScrollView;
import com.zhang.sqone.views.TitleBarView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ShangChuanActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.fujian1_button)
    Button fujian1Button;
    @Bind(R.id.fujian1_fj)
    LinearLayout fujian1Fj;
    @Bind(R.id.fujian1_list)
    ListViewForScrollView fujian1List;
    @Bind(R.id.shangchuan_title)
    TitleBarView shangchuanTitle;
    private boolean isone = true;
    private CommonAdapter<String> resultAdapter;
    private List<String> fileList = new ArrayList<>();
    private Intent intent;
    private RequestParams params;
    private int i = 1;
    private ProgressBar progressBar;
    private AlertDialog downLoadDialog;
    private String wid;
    private List<String> fileListid = new ArrayList<>();
    private String zhang;
    private Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap fileMap;
    private Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap file;
    private String bsf2;
    private String bsf;
    private String bsfs;
    private String ld;
    private String bafs1;
    private String mc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_shang_chuan);
        ButterKnife.bind(this);
        bsf2 = getIntent().getStringExtra("bsf");
        bsf = getIntent().getStringExtra("bsf2");
        ld = getIntent().getStringExtra("ld");
       bafs1 =  getIntent().getStringExtra("bsfs");
        if(bsf2.equals("2")){
            bsfs = "BZHCXXQFJ";
        }else{
            bsfs = "BZHSHXQFJ";
        }
        shangchuanTitle.setClickEnterButtonListener(new TitleBarView.OnClickEnterButtonListener() {
            @Override
            public void onClickEnterButton(View v) {
                Log.i("zhang", "附件的请求标识"+bsfs);
                final Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap.Builder  builder = Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap.newBuilder();
                    builder.setId(fileMap.getId());

                StringBuffer s = new StringBuffer();
                        for (int j = 0; j<fileListid.size();j++){
                            s.append(fileListid.get(j)+",");
                        }
if (fileListid.size()==0){
    builder.setFjids(s.toString());
}else{
    builder.setFjids(s.substring(0,s.length()-1).toString());
}

                Log.i("zhang", "会议议题"+bafs1);
                builder.setYtmc(bafs1);
                Meetingdiscuss.ReqMeetingDiscuss reqDocument = Meetingdiscuss.ReqMeetingDiscuss.newBuilder().setSid(User.sid).setAc(bsfs).setMcddetail(builder).build();
                new HttpUtilDocumentme() {
                    @Override
                    public <T> void analysisInputStreamData(Meetingdiscuss.ReqMeetingDiscuss index) throws IOException {
                        if (index.getScd().equals("1")) {
                            Log.i("zhang", "点击的是长传的按钮");
                            Toast.makeText(ShangChuanActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                            file =  index.getMcd(0);
                            Log.i("zhang", "返回的文件个数： "+file.getFilelistList().size());
                            Log.i("zhang", "返回的文件id： "+file.getFjids());
                            Intent intent = new Intent(ShangChuanActivity.this, BanZiXqActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Globals.LIDS, file);
                            if (bsf.equals("2")){
                                bundle.putString("bsf", "2");
                            }else{
                                bundle.putString("bsf", "1");
                            }
                            bundle.putString("ld",ld);
                            bundle.putString("bsf2", bsf2);
                            bundle.putString("mc",mc);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ShangChuanActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }.protocolBuffer(ShangChuanActivity.this, Globals.BZHY_URI, reqDocument, null);
            }
        });
        fileMap = (Meetingdiscuss.ReqMeetingDiscuss.MeetingCxDetailMap) getIntent().getSerializableExtra(Globals.LIDS);
         mc = getIntent().getStringExtra("mc");
        fujian1Button.setOnClickListener(this);
        if (bsf.equals("2")){
            List<Meetingdiscuss.ReqMeetingDiscuss.FileList> file1 = fileMap.getFilelistList();
            if (file1.size() != 0) {
                for (int i = 0; i < file1.size(); i++) {
                    fileList.add(file1.get(i).getName());
                }
                String[] sourceStrArray = fileMap.getFjids().split(",");
                for (int i = 0; i < sourceStrArray.length; i++) {
                    fileListid.add(sourceStrArray[i]);
                    Log.i("zhang", "文件的id"+i+"---"+sourceStrArray[i]);
                }
            }

        }else{
            List<Meetingdiscuss.ReqMeetingDiscuss.FileList> file1 = fileMap.getFilelistList();
            if (file1.size() != 0) {
                for (int i = 0; i < file1.size(); i++) {
                    fileList.add(file1.get(i).getName());
                }
                String[] sourceStrArray = fileMap.getFjids().split(",");
                for (int i = 0; i < sourceStrArray.length; i++) {
                    fileListid.add(sourceStrArray[i]);
                    Log.i("zhang", "文件的id"+i+"---"+sourceStrArray[i]);
                }
            }
        }


        resultAdapter = new CommonAdapter<String>(ShangChuanActivity.this, fileList, R.layout.fayoujian_litem) {
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
                        if(zhang==null||zhang.equals("")){

                        }else{
                            loadData(fileListid.get(holder.getPosition()));
                        }

                        fileListid.remove(holder.getPosition());
                        resultAdapter.setData(fileList);
                        resultAdapter.notifyDataSetChanged();

                    }
                });

            }
        };
        fujian1List.setAdapter(resultAdapter);
        isone = false;
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
            if (url == null) {
                Toast.makeText(this, "请选择文件管理中的文件", Toast.LENGTH_SHORT).show();
            } else {
                zhang = url;
                AsyncHttpClient client = new AsyncHttpClient();
                params = new RequestParams();
//                Log.i("sid", User.sid);
                String req = "{\"Ac\":\"TPSC\",\"Para\":{\"SId\":\""
                        + User.sid + "\",\"T\":\"1\",\"Wid\":\"0\"}}";
                params.put("id", req);

                File imgFile = new File(url);
                String filename = imgFile.getName();
                fileList.add(filename);
                resultAdapter.setData(fileList);
                resultAdapter.notifyDataSetChanged();

                if (imgFile.exists() && imgFile.length() > 0) {
                    try {
                        params.put("Zhang.png", imgFile);
                        showDownloadDialog(1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    client.post(ShangChuanActivity.this, Globals.WS_URI_WJ, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                        StringBuffer s = new StringBuffer();
//                        for (int j = 0; j<bytes.length-1;j++){
//                            Log.i("swww1", "++"+j+"++" +(char)bytes[j]);
//                            s.append((char)bytes[j]);
//                        }
                            String s = new String(bytes);
                            downLoadDialog.dismiss();
                            WenJianBean s1 = GsonUtils.json2bean(s.toString(),
                                    WenJianBean.class);
                            if (s1 == null || !(s1.getStu() == 1)) {
                                Toast.makeText(ShangChuanActivity.this, Globals.SER_ERROR,
                                        Toast.LENGTH_SHORT).show();
                                fileListid.add("-1");
                            } else {
                                String ss = "sssss";
                                Log.i("swww1", "成功---" + s1.getRst().getPo() + ss);
                                fileListid.add(s1.getRst().getPo());
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i1 = 0; i1 < fileListid.size(); i1++) {
                                    stringBuilder.append(fileListid.get(i1));
                                }
                            }

                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            Log.i("swww2", throwable.getMessage());
                            Log.i("swww3", throwable.toString());
                            Toast.makeText(ShangChuanActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ShangChuanActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
                }

            }


        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void loadData(String id) {
        AsyncHttpClient client = new AsyncHttpClient();
        params = new RequestParams();
        Log.i("sid", User.sid);
        String req = "{\"Ac\":\"TPSC\",\"Para\":{\"SId\":\""
                + User.sid + "\",\"T\":\"2\",\"Wid\":\"" + id + "\"}}";
        params.put("id", req);


        File imgFile = new File(zhang);
        if (imgFile.exists() && imgFile.length() > 0) {
            try {
                params.put("Zhang.png", imgFile);
                showDownloadDialog(2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            client.post(ShangChuanActivity.this, Globals.WS_URI_WJ, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                        StringBuffer s = new StringBuffer();
//                        for (int j = 0; j<bytes.length-1;j++){
//                            Log.i("swww1", "++"+j+"++" +(char)bytes[j]);
//                            s.append((char)bytes[j]);
//                        }
                    String s = new String(bytes);
                    downLoadDialog.dismiss();
                    WenJianBean s1 = GsonUtils.json2bean(s.toString(),
                            WenJianBean.class);
                    if (s1 == null || !(s1.getStu() == 1)) {
                        Toast.makeText(ShangChuanActivity.this, Globals.SER_ERROR,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String ss = "sssss";
                        Log.i("swww1", "成功---" + s1.getRst().getPo() + ss);
//                            qingjiaH5.loadUrl("javascript:fujian('" + s1.getRst().getPo() + "')");
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.i("swww2", throwable.getMessage());
                    Log.i("swww3", throwable.toString());
                    Toast.makeText(ShangChuanActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(ShangChuanActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 下载的提示框
     */
    protected void showDownloadDialog(int i) {
        {
            // 构造软件下载对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (i == 1) {
                builder.setTitle("正在上传");
            } else {
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

    @Override
    public void onClick(View view) {
        showFileChooser();
    }
}
