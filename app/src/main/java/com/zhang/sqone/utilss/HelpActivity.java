package com.zhang.sqone.utilss;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    // 权限申请
    @TargetApi(Build.VERSION_CODES.M)
    private void handleIntent(Intent intent) {
        String[] permissions = intent.getStringArrayExtra("permissions");
        int requestCode = intent.getIntExtra("requestCode", 42);
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 返回结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionUtil.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
        finish();
    }
}
