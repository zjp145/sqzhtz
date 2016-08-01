package com.zhang.sqone.home;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Monitor;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.AlertUtil;
import com.zhang.sqone.utils.HttpUtilMonitor;
import com.zhang.sqone.utils.ImageUtil;
import com.zhang.sqone.utilss.SystemStatusManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 环境监测界面
 *
 * @author ZJP
 *         created at 2016/3/6 10:59
 */
public class HuanJingActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.huanjing_dizhi)
    /**地址*/
            LinearLayout huanjingDizhi;
    @Bind(R.id.huanjing_neirong)
    /**内容*/
            LinearLayout huanjingNeirong;
    @Bind(R.id.huanjing_tupian)
    /**图片*/
            ImageView huanjingTupian;
    @Bind(R.id.huanjing_tijing)
    /**提交*/
            Button huanjingTijing;
    @Bind(R.id.huanjing_add1)
            /**地址添加按钮*/
    ImageView huanjingAdd1;
    @Bind(R.id.huanjing_add2)
            /**内容添加按钮*/
    ImageView huanjingAdd2;
    @Bind(R.id.huanjing_dizhitext)
            /**地址回调添加数据*/
    TextView huanjingDizhitext;
    @Bind(R.id.huanjing_neirongtext)
            /**内容回调添加数据*/
    TextView huanjingNeirongtext;
    /**
     * 当添加了一张图片 的图片路径
     */
    private String pathImage;
    private AlertUtil au;
    private RequestParams params;
    /**
     * 回调数据中的环境地址的单位
     */
    private String hjdzdw="";
    /**
     * 回调数据中的环境地址id
     */
    private String hjdzid="";
    /**环境内容*/
    private String hjnr="";
    /**
     * 地址的名字
     */
    private String hjdzna = "";
    /**
     * 环境分数
     */
    private String fjfs;
    /**图片id*/
    private String tpid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_huan_jing);
        ButterKnife.bind(this);
        onClicks();

    }

    /**
     * 点击事件
     */
    private void onClicks() {
        huanjingDizhi.setOnClickListener(this);
        huanjingNeirong.setOnClickListener(this);
        huanjingTupian.setOnClickListener(this);
        huanjingTijing.setOnClickListener(this);
        huanjingAdd1.setOnClickListener(this);
        huanjingAdd2.setOnClickListener(this);
    }
    /**请求网络数据*/
    public void regRequest() {
        Monitor.MonIndex.submit.Builder submit =  Monitor.MonIndex.submit.newBuilder();
        submit.setAdress(hjdzna);
        Log.i("zhang", hjdzna);
        submit.setContent(hjnr);
        submit.setPhoto(tpid);
        submit.setCompany(hjdzdw);
        submit.setScore(fjfs);
        Monitor.MonIndex index = Monitor.MonIndex.newBuilder().setSub(submit).setAc("TIJIAO").build();
        new HttpUtilMonitor() {
            @Override
            public <T> void analysisInputStreamData(Monitor.MonIndex index) throws IOException {
                Toast.makeText(HuanJingActivity.this, index.getMsg(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }.protocolBuffer(HuanJingActivity.this, Globals.HJ_URI, index, null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //地址
            case R.id.huanjing_add1:
                Intent intent = new Intent(this, HuanJingDZActivity.class);
                startActivityForResult(intent, 1);
                break;
            //内容
            case R.id.huanjing_add2:
                if (hjdzid.equals("")||hjdzid==null){
                    Toast.makeText(HuanJingActivity.this, "请先添加地址", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent2 = new Intent(this, HuanJingNRActivity.class);
                    if (!TextUtils.isEmpty(hjdzid)){
                        intent2.putExtra(Globals.HJ_DZ_ID,hjdzid);
                    }
                    startActivityForResult(intent2, 3);
                }

                break;
            //图片
            case R.id.huanjing_tupian:
                if(hjnr.equals("")||hjnr==null){

                    Toast.makeText(HuanJingActivity.this, "请先添加地址和内容", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(HuanJingActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                    //选择图片
                    Intent intent1 = new Intent();
                    intent1.setClass(HuanJingActivity.this, SelectPicActivity.class);
                    intent1.putExtra("type", "picture");
                    intent1.putExtra("type2", "picture2");
                    startActivityForResult(intent1, 2);
                }

                break;
            //提交
            case R.id.huanjing_tijing:
                if(hjdzdw.equals("")){
                    Toast.makeText(HuanJingActivity.this, "请先添加地址再提交", Toast.LENGTH_SHORT).show();
                }
                else if(hjnr.equals("")){
                    Toast.makeText(HuanJingActivity.this, "请先添加内容再提交", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(pathImage)){
                    Toast.makeText(HuanJingActivity.this, "请先添加图片再提交", Toast.LENGTH_SHORT).show();
                }else{
                    regRequest();
                }
                break;
        }

    }
    //返回的时候获得界面的返回值
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = ImageUtil.getSmallBitmap(pathImage);
            huanjingTupian.setImageBitmap(addbmp);
            imageTj();
        }
        if (!TextUtils.isEmpty(hjdzdw) && !TextUtils.isEmpty(hjdzid)) {
            huanjingDizhitext.setText(hjdzdw + hjdzna);
        }
        if (!TextUtils.isEmpty(hjnr) ) {
            huanjingNeirongtext.setText(hjnr);
        }


    }
    /**上传图的使用网络请求*/
    private void imageTj() {
        au = new AlertUtil(this);
        AsyncHttpClient client = new AsyncHttpClient();
        params = new RequestParams();
        String req = "{\"Ac\":\"TPSC\",\"Para\":{\"SId\":\""
                + User.sid + "\",\"T\":\"2\"}}";
        params.put("id", req);
        //设置文件
        try {
            File imgFile = new File(getExternalCacheDir(),
                    "MyImg.png");
            FileOutputStream fos = new FileOutputStream(imgFile);
            ImageUtil.getSmallBitmap(pathImage).compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            params.put("MyImg.png", imgFile, "image/jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.post(HuanJingActivity.this, Globals.BM_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String s = new String(bytes);

                Log.i("swww1", s+"数据的长度"+s.length());
                tpid = s;
                au.closeDialog();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("swww2", "onSuccess1 ");
                Toast.makeText(HuanJingActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //跳转一会的数据返回
     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    hjdzdw = data.getStringExtra(Globals.HJ_DZ_DW);
                    hjdzid = data.getStringExtra(Globals.HJ_DZ_ID);
                    hjdzna = data.getStringExtra(Globals.HJ_DZ_NA);
                }
                break;
            case 2:
                Log.i("tupiance", "图片上传");
                if (data != null) {
                    // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                    Uri mImageCaptureUri = data.getData();
                    // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {
                        pathImage = getRealFilePath(this, mImageCaptureUri);
                        Log.i("tupiance", "获取到相册的图片");
                    } else {
                        if (data.getStringExtra("ok") != null) {
                            Log.i("tupiance", "获得拍照的图片");
                            String uri = data.getStringExtra("ok");
                            Uri imageUri = Uri.parse(uri);
                            pathImage = getRealFilePath(this, imageUri);
                        }
                    }
                }
                break;
            case  3:
                if (resultCode == Activity.RESULT_OK) {
                    hjnr = data.getStringExtra(Globals.HJ_NR);
                    fjfs =data.getStringExtra(Globals.HJ_FS);
                }
                break;
        }

    }

    /**将uri转换成String形式*/
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