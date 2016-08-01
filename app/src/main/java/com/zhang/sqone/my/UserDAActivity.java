package com.zhang.sqone.my;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhang.sqone.BaseActivity;
import com.zhang.sqone.Globals;
import com.zhang.sqone.R;
import com.zhang.sqone.bean.Index;
import com.zhang.sqone.bean.Myself;
import com.zhang.sqone.bean.User;
import com.zhang.sqone.utils.AlertUtil;
import com.zhang.sqone.utils.ImageUtil;
import com.zhang.sqone.utilss.HttpUtil;
import com.zhang.sqone.utilss.HttpUtilM;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.CircularImage;
import com.zhang.sqone.zxing.encode.CodeCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 改版后的我的档案
 *
 * @author ZJP
 *         created at 2016/4/27 16:43
 */
public class UserDAActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.zhanghu_tx)
    LinearLayout zhanghuTx;
    @Bind(R.id.zhanghu_xingming_text)
    TextView zhanghuXingmingText;
    @Bind(R.id.zhanghu_zhiwu_text)
    TextView zhanghuZhiwuText;
    @Bind(R.id.zhanghu_shengri_text)
    TextView zhanghuShengriText;
    @Bind(R.id.zhanghu_nianxian_text)
    TextView zhanghuNianxianText;
    @Bind(R.id.zhanghu_nianjia_text)
    TextView zhanghuNianjiaText;
    @Bind(R.id.zhanghu_bumen_text)
    TextView zhanghuBumenText;
    @Bind(R.id.wode_iamge)
    ImageView wodeIamge;
    @Bind(R.id.zhanghu_tx_iamge)
    CircularImage zhanghuTxIamge;

    private String pathImage;
    private AlertUtil au;
    private RequestParams params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_user_da);
        ButterKnife.bind(this);
        ImageLoader.getInstance().displayImage(User.IconPath, zhanghuTxIamge);
        // 如果是用户是登陆的状态我们先获得用户的信息
        Bitmap bitmap = null;
        try {
            bitmap = CodeCreator.createQRCode("http://www.baidu.com");
            wodeIamge.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        regRequest();
        clicks();
        //添加用户头像

    }

    /**
     * 获得用户的信息
     */
    public void regRequest() {
        //设置注册的发送实例

        Myself.ReqMyself index = Myself.ReqMyself.newBuilder().setSid(User.sid).setAc("WDDA").build();
        new HttpUtilM() {
            @Override
            public <T> void analysisInputStreamData(Myself.ReqMyself index) throws IOException {
                zhanghuXingmingText.setText(index.getMm().getPn());
                zhanghuZhiwuText.setText(index.getMm().getZhiwu());
                zhanghuShengriText.setText(index.getMm().getBir());
                zhanghuNianxianText.setText(index.getMm().getCjgznx());
                zhanghuNianjiaText.setText(index.getMm().getSynx());
                zhanghuBumenText.setText(index.getMm().getDname());

            }
        }.protocolBuffer(UserDAActivity.this, Globals.ZB_URI, index, null);

    }

    /**
     * 获得用户的信息(返回界面)
     */
    public void regRequest2() {
        //设置注册的发送实例
        Index.ReqIndex.ReqCUI.Builder cui = Index.ReqIndex.ReqCUI.newBuilder();
        cui.setSid(User.sid);

        Index.ReqIndex index = Index.ReqIndex.newBuilder().setCui(cui).setAc("CUI").build();
        new HttpUtil() {
            @Override
            public <T> void analysisInputStreamData(Index.ReqIndex index) throws IOException {
                //获得成功

//                User.mid = index.getCui().getId();
//                User.nc = index.getCui().getNn();
//                User.phone = index.getCui().getPn();
//                // User.sid = index.getCui().getSid();
//                User.usercode = index.getCui().getUsercode();
//                zhanghuNcText.setText(index.getCui().getNn());
//                Log.i("ceshi", "昵称" + index.getCui().getNn());
//                zhanghuPhoneText.setText(User.phone.substring(0, 3) + "****" + User.phone.substring(7, User.phone.length()));
//                Log.i("ceshi", "电话" + index.getCui().getPn());
            }
        }.protocolBuffer(UserDAActivity.this, Globals.WS_URI, index, null);
    }

    /**
     * 布局的点击事件
     */
    private void clicks() {
        zhanghuTx.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击用户头像
            case R.id.zhanghu_tx:
                //上传头像界面
//                Intent intent = new Intent(this, UploadHeadActivity.class);
//                intent.putExtra("type", "picture");
//                intent.putExtra("type2", "picture2");
//                startActivityForResult(intent, 2);
                break;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {

            case 2:
                Log.i("tupiance", "图片上传");
                if (data != null) {
                    // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                    Uri mImageCaptureUri = data.getData();
//                    Log.i("s123s", AppUtil.getRealFilePath(this, mImageCaptureUri));


                    // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {
                        pathImage = getRealFilePath(this, mImageCaptureUri);
//                        suri.add(getRealFilePath(this, mImageCaptureUri));
                        Log.i("tupiance", "获取到相册的图片");
                    } else {
                        if (data.getStringExtra("ok") != null) {
                            Log.i("tupiance", "获得拍照的图片");
                            String uri = data.getStringExtra("ok");
                            Uri imageUri = Uri.parse(uri);
                            pathImage = getRealFilePath(this, imageUri);
//                                suri.add(getRealFilePath(this, imageUri));
                        }
                    }

                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        regRequest2();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = ImageUtil.getSmallBitmap(pathImage);
            zhanghuTxIamge.setImageBitmap(addbmp);
            imageTj();
        }

    }

    private void imageTj() {
        au = new AlertUtil(this);
        AsyncHttpClient client = new AsyncHttpClient();
        params = new RequestParams();
        Log.i("sid", User.sid);
        String req = "{\"Ac\":\"TPSC\",\"Para\":{\"SId\":\""
                + User.sid + "\",\"T\":\"1\"}}";
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


        client.post(UserDAActivity.this, Globals.WS_URI_POTO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String s = new String(bytes);

                Log.i("swww1", s + "数据的长度" + s.length());
                au.closeDialog();
//                finish();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("swww2", throwable.getMessage());
                Log.i("swww3", throwable.toString());
                Toast.makeText(UserDAActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });

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
}
