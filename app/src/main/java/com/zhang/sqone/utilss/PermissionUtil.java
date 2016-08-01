package com.zhang.sqone.utilss;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    private PermissionResultCallBack mPermissionResultCallBack;
    private volatile static PermissionUtil instance;
    private int mRequestCode;
    private Context mContext;
    private Fragment mFragment;
    private List mPermissionListNeedReq;
    private String[] mPermissions;
    private ArrayList list =new ArrayList();

    public static PermissionUtil getInstance() {
        if (instance == null) {
            synchronized (PermissionUtil.class) {
                if (instance == null) {
                    instance = new PermissionUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 用于fragment中请求权限
     * @param fragment
     * @param permissions
     * @param requestCode
     * @param callBack
     */
    public void request(@NonNull Fragment fragment, @NonNull String[] permissions, @NonNull int requestCode, PermissionResultCallBack callBack) {
        this.mFragment = fragment;
        this.request(fragment.getActivity(), permissions, requestCode, callBack);
    }

    /**
     * 用于activity中请求权限
     * @param context
     * @param permissions
     * @param requestCode
     * @param callBack
     */
    public void request(@NonNull Context context,@NonNull String[] permissions,@NonNull int requestCode, PermissionResultCallBack callBack) {

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("request permission only can run in MainThread!");
        }

        if (permissions.length == 0) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onGranted();
            return;
        }

        this.mContext = context;
        this.mPermissions = permissions;
        this.mRequestCode = requestCode;
        this.mPermissionResultCallBack = callBack;
        this.mPermissionListNeedReq = new ArrayList();

        if (needToRequest()) {
            requestPermissions();
        } else {
            onGranted();
        }
    }

    /**
     * 通过开启一个新的activity作为申请权限的媒介
     */
    private void requestPermissions() {

        Intent intent = new Intent(mContext, HelpActivity.class);
        intent.putExtra("permissions", mPermissions);
        intent.putExtra("requestCode", mRequestCode);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 检查是否需要申请权限
     * @return
     */
    private boolean needToRequest()  {
        PackageManager pm = mContext.getPackageManager();
        for (String permission : mPermissions) {
            int checkRes = ContextCompat.checkSelfPermission(mContext, permission);
            if (checkRes != PackageManager.PERMISSION_GRANTED) {
                PermissionInfo info = null;
                try {
                    info = pm.getPermissionInfo(permission,0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (mContext instanceof Activity &&
                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission)) {
//                    info.setRationalNeed(true);
                }
                mPermissionListNeedReq.add(info);
                list.add(permission);
            }
        }

        if (mPermissionListNeedReq.size() > 0) {
            mPermissions = new String[mPermissionListNeedReq.size()];
            for (int i = 0; i < mPermissionListNeedReq.size(); i++) {
                mPermissions[i] = (String) list.get(i);
            }
            return true;
        }

        return false;
    }

    /**
     * 申请权限结果返回
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == mRequestCode) {

            if (mContext != null && mContext instanceof Activity) {
                ((Activity) mContext).onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

            if (mFragment != null) {
                mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

            boolean isAllGranted = true;
//            List needRationalPermissionList = new ArrayList();
//            List deniedPermissionList = new ArrayList();
//            for (int i = 0; i < permissions.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    if (true) {
//                        needRationalPermissionList.add(mPermissionListNeedReq.get(i));
//                    } else {
//                        deniedPermissionList.add(mPermissionListNeedReq.get(i));
//                    }
//                    isAllGranted = false;
//                }
//            }

//            if (needRationalPermissionList.size() != 0) {
//                showRational(needRationalPermissionList);
//            }
//
//            if (deniedPermissionList.size() != 0) {
//                onDenied(deniedPermissionList);
//            }

            if (isAllGranted) {
                onGranted();
            }

        }
    }

    /**
     * 权限被用户许可之后回调的方法
     */
    private void onGranted() {
        if (mPermissionResultCallBack != null) {
            mPermissionResultCallBack.onPermissionGranted();
        }
    }

    /**
     * 权限申请被用户否定之后的回调方法,这个主要是当用户点击否定的同时点击了不在弹出,
     * 那么当再次申请权限,此方法会被调用
     * @param list
     */
    private void onDenied(List list) {
//        if(list == null || list.size() == 0) return;
//
//        String[] permissions = new String[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            permissions[i] = list.get(i).getName();
//        }
//
//        if (mPermissionResultCallBack != null) {
//            mPermissionResultCallBack.onPermissionDenied(permissions);
//        }
    }

    /**
     * 权限申请被用户否定后的回调方法,这个主要场景是当用户点击了否定,但未点击不在弹出,
     * 那么当再次申请权限的时候,此方法会被调用
     * @param list
     */
    private void showRational(List list) {
        if(list == null || list.size() == 0) return;

        String[] permissions = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            permissions[i] = (String) this.list.get(i);
        }

        if (mPermissionResultCallBack != null) {
            mPermissionResultCallBack.onRationalShow(permissions);
        }
    }
    public interface PermissionResultCallBack {

        /**
         * 当所有权限的申请被用户同意之后,该方法会被调用
         */
        void onPermissionGranted();

        /**
         * 当权限申请中的某一个或多个权限,被用户曾经否定了,并确认了不再提醒时,也就是权限的申请窗口不能再弹出时,
         * 该方法将会被调用
         * @param permissions
         */
        void onPermissionDenied(String... permissions);

        /**
         * 当权限申请中的某一个或多个权限,被用户否定了,但没有确认不再提醒时,也就是权限窗口申请时,但被否定了之后,
         * 该方法将会被调用.
         * @param permissions
         */
        void onRationalShow(String... permissions);
    }
}