package com.zhang.sqone;

import android.app.Application;
import android.app.Service;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.support.multidex.MultiDex;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

/**
 * 首次启动初始化数据类 等等程序启动的时候将会首先创建实例化的对象
 * <p/>
 * 2015年4月16日13:14:10 fyf
 */

public class GuideApplication extends Application {
    //	public LocationClient mLocationClient;
//	public GeofenceClient mGeofenceClient;
//	public MyLocationListener mMyLocationListener;
    public Vibrator mVibrator;
//    File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
    public static LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        /**使用的sharedsdk*/
//        ShareSDK.initSDK(this);

        PushManager.getInstance().initialize(this.getApplicationContext());

//        SDKInitializer.initialize(getApplicationContext());
//        RequestManager.init(getApplicationContext());
        // 百度地图初始化
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        GuideApplication.initLocation();
        GuideApplication.mLocationClient.start();

//        mLocationClient.start();
//        SDKInitializer.initialize(getApplicationContext());
        // PropertyConfigurator.configure("log4j.properties");
        // // 给全局上下文赋值

        //程序崩溃处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        Globals.context = getApplicationContext();
        if (Globals.context != null) {
            initImageConfigure();
        }
//        RequestManager.init(this);
//		mLocationClient = new LocationClient(this.getApplicationContext());
//		mMyLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mMyLocationListener);
//		mGeofenceClient = new GeofenceClient(getApplicationContext());

        mVibrator = (Vibrator) getApplicationContext().getSystemService(
                Service.VIBRATOR_SERVICE);


    }


    private static File cacheDir;

    public static File getCacheFile(String fileName) {
        File file = new File(cacheDir, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }
    public static void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 初始化ImageLoader 设置了内存已经磁盘缓存 可以通过uri获取缓存图片
     */
    public static ImageLoaderConfiguration getImageLoaderConfiguration() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.tongxunlu) // resource or
                        // drawable
				.showImageForEmptyUri(R.mipmap.tongxunlu) // resource or
                        // drawable
                .showImageOnFail(R.mipmap.tongxunlu) // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.EXACTLY)
                        // .preProcessor(...)
                        // .postProcessor(...)
                        // .extraForDownloader(...)
                .considerExifParams(false) // default
                        // .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) //
                        // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                        // .decodingOptions(...)
                        // .displayer(new SimpleBitmapDisplayer()) // default
                        // .handler(new Handler()) // default
                .build();

        return new ImageLoaderConfiguration.Builder(Globals.context)
                // .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                // .diskCacheExtraOptions(480, 800, null)
                // .taskExecutor(...)
                // .taskExecutorForCachedImages(...)
                .threadPoolSize(2)
                        // default
                .threadPriority(Thread.NORM_PRIORITY - 2)
                        // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                        // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                        // default
                        // .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                        // .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(Globals.context)) // default
                        // .imageDecoder(new BaseImageDecoder()) // default
                        // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .defaultDisplayImageOptions(options)//
                .writeDebugLogs().build();
    }

    public static void initImageConfigure() {
        ImageLoader.getInstance().init(getImageLoaderConfiguration());
    }

}
