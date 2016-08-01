package com.zhang.sqone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhang.sqone.adapter.CommonAdapter;
import com.zhang.sqone.adapter.ViewHolder;
import com.zhang.sqone.utilss.SystemStatusManager;
import com.zhang.sqone.views.DeleteF;
import com.zhang.sqone.views.TitleBarView;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的文件
 *
 * @author ZJP
 *         created at 2016/5/26 14:41
 */
public class SDCARD123Activity extends BaseActivity implements AdapterView.OnItemClickListener {
    public String[] allFiles;
    @Bind(R.id.wenjian_list)
    ListView wenjianList;
    public ArrayList<String> list = new ArrayList<>();
    @Bind(R.id.titel_button)
    TitleBarView titelButton;
    private CommonAdapter<String> resultAdapter;
    /**
     * 判定标识符（首页，传资料）
     */
    private String bsf;
    private Intent intent1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_sdcard123);
        ButterKnife.bind(this);

        titelButton.setClickEnterButtonListener(new TitleBarView.OnClickEnterButtonListener() {
            @Override
            public void onClickEnterButton(View v) {
                if (allFiles != null||list.size()!=0){
                    DeleteF d = new DeleteF() {
                        @Override
                        public void determineButton() {
                            deleteFile(new File(Environment.getExternalStorageDirectory() + "/zhtzwj"));
                            list.clear();
                            resultAdapter.notifyDataSetChanged();
                        }
                    }.deleteDialog(SDCARD123Activity.this, "是否要清空我的文件？", "", "");

                }else{
                    Toast.makeText(SDCARD123Activity.this, "没有文件", Toast.LENGTH_SHORT).show();
                }

            }
        });
        intent1 = getIntent();
        bsf = getIntent().getStringExtra("bsf");
        if (bsf.equals("sczl")) {titelButton.imbEnter.setVisibility(View.INVISIBLE);}
        File folder = new File(Environment.getExternalStorageDirectory() + "/zhtzwj");
        allFiles = folder.list();
        //   uriAllFiles= new Uri[allFiles.length];
        if (allFiles != null) {
            for (int i = 0; i < allFiles.length; i++) {
                list.add(allFiles[i]);
                System.out.println("文件" + allFiles[i]);
            }
            //  Uri uri= Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/yourfoldername/"+allFiles[0]));
            resultAdapter = new CommonAdapter<String>(SDCARD123Activity.this, list, R.layout.wenjian_list) {
                @Override
                public void convert(final ViewHolder holder, final String fileMap) {
                    holder.setText(R.id.wenjian_text, fileMap);
                    if (bsf.equals("sczl"))
                    {holder.setVisible(R.id.del_button,false);}else{
                        holder.setVisible(R.id.del_button,true);
                    }
                    holder.setOnClickListener(R.id.del_button, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DeleteF d = new DeleteF() {
                                @Override
                                public void determineButton() {
                                    deleteFile(new File(Environment.getExternalStorageDirectory() + "/zhtzwj/" + fileMap));
                                    list.remove(holder.getPosition());
                                    resultAdapter.notifyDataSetChanged();
                                }
                            }.deleteDialog(SDCARD123Activity.this, "是否确认删除文件？", "", "");
                            /**删除文件*/

                        }
                    });
                }
            };
            wenjianList.setAdapter(resultAdapter);
            wenjianList.setOnItemClickListener(this);

        } else {

            Toast.makeText(this, "没有文件", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (bsf.equals("sczl")) {

            Log.i("zhang", "返回的文件地址" + resultAdapter.getItem(i));
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/zhtzwj/" + resultAdapter.getItem(i));
            intent1.setData(uri);
            setResult(Activity.RESULT_OK, intent1);
            finish();
        } else {
            /*打开文件*/
            Intent intent = getFileIntent(new File(Environment.getExternalStorageDirectory() + "/zhtzwj/" + resultAdapter.getItem(i)));
            startActivity(intent);


        }

    }

    /**
     * 获取文件格式 返回的使用打开文件的intent
     */
    public Intent getFileIntent(File file) {
        //Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
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

    /**
     * 判断文件的格式使用不同的action打开文件
     */
    private String getMIMEType(File f) {
        //如果没有匹配的打开文件的情况现在所有的程序打开文件
        String type = "*/*";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;

    }

    private final String[][] MIME_MapTable = {
            //{后缀名， MIME类型}
            {"3gp", "video/3gpp"},
            {"apk", "application/vnd.android.package-archive"},
            {"asf", "video/x-ms-asf"},
            {"avi", "video/x-msvideo"},
            {"bin", "application/octet-stream"},
            {"bmp", "image/bmp"},
            {"c", "text/plain"},
            {"class", "application/octet-stream"},
            {"conf", "text/plain"},
            {"cpp", "text/plain"},
            {"doc", "application/msword"},
            {"docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"xls", "application/vnd.ms-excel"},
            {"xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"exe", "application/octet-stream"},
            {"gif", "image/gif"},
            {"gtar", "application/x-gtar"},
            {"gz", "application/x-gzip"},
            {"h", "text/plain"},
            {"htm", "text/html"},
            {"html", "text/html"},
            {"jar", "application/java-archive"},
            {"java", "text/plain"},
            {"jpeg", "image/jpeg"},
            {"jpg", "image/jpeg"},
            {"js", "application/x-javascript"},
            {"log", "text/plain"},
            {"m3u", "audio/x-mpegurl"},
            {"m4a", "audio/mp4a-latm"},
            {"m4b", "audio/mp4a-latm"},
            {"m4p", "audio/mp4a-latm"},
            {"m4u", "video/vnd.mpegurl"},
            {"m4v", "video/x-m4v"},
            {"mov", "video/quicktime"},
            {"mp2", "audio/x-mpeg"},
            {"mp3", "audio/x-mpeg"},
            {"mp4", "video/mp4"},
            {"mpc", "application/vnd.mpohun.certificate"},
            {"mpe", "video/mpeg"},
            {"mpeg", "video/mpeg"},
            {"mpg", "video/mpeg"},
            {"mpg4", "video/mp4"},
            {"mpga", "audio/mpeg"},
            {"msg", "application/vnd.ms-outlook"},
            {"ogg", "audio/ogg"},
            {"pdf", "application/pdf"},
            {"png", "image/png"},
            {"pps", "application/vnd.ms-powerpoint"},
            {"ppt", "application/vnd.ms-powerpoint"},
            {"pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {"prop", "text/plain"},
            {"rc", "text/plain"},
            {"rmvb", "audio/x-pn-realaudio"},
            {"rtf", "application/rtf"},
            {"sh", "text/plain"},
            {"tar", "application/x-tar"},
            {"tgz", "application/x-compressed"},
            {"txt", "text/plain"},
            {"wav", "audio/x-wav"},
            {"wma", "audio/x-ms-wma"},
            {"wmv", "audio/x-ms-wmv"},
            {"wps", "application/vnd.ms-works"},
            {"xml", "text/plain"},
            {"z", "application/x-compress"},
            {"zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };


    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            Log.i("zhang", "文件不存在");
        }
    }
}