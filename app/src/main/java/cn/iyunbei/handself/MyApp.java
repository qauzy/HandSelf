package cn.iyunbei.handself;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.posapi.PosApi;
import android.preference.PreferenceManager;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;

import java.util.concurrent.TimeUnit;

import cn.iyunbei.handself.greendao.DaoMaster;
import cn.iyunbei.handself.greendao.DaoSession;
import jt.kundream.base.BaseApplication;
import jt.kundream.utils.ToastUtils;
import okhttp3.OkHttpClient;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class MyApp extends BaseApplication implements SharedPreferences.OnSharedPreferenceChangeListener{

    PosApi mPosApi = null;
    static MyApp instance = null;
    private static String mCurDev1 = "";
    public Boolean mUseConnecting;
    public Boolean mEnableScanOnly;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化配置
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        mUseConnecting = prefs.getBoolean(getString(R.string.use_connecting_key),false);
        mEnableScanOnly = prefs.getBoolean(getString(R.string.enable_scan_only_key),false);

        initOkGo();
//        initDatabase();
    }

    public MyApp() {
        super.onCreate();
        instance = this;
    }

    public static  MyApp getInstance(){
        if(instance==null){
            instance =new MyApp();
        }
        return instance;
    }



    private void initOkGo() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置全局读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //设置全局写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //设置全局链接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpHeaders headers = new HttpHeaders();
//        headers.put();

        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE)
                .setRetryCount(3);
//                .addCommonParams();
    }


    private void initPosapi() {
        //posap初始化
        mPosApi = PosApi.getInstance(this);
        //根据型号进行初始化mPosApi类
        if (Build.MODEL.contains("LTE") || android.os.Build.DISPLAY.contains("3508") ||
                android.os.Build.DISPLAY.contains("403") ||
                android.os.Build.DISPLAY.contains("35S09")) {
            mPosApi.initPosDev("ima35s09");
            setCurDevice("ima35s09");
        } else if (Build.MODEL.contains("5501")) {
            mPosApi.initPosDev("ima35s12");
            setCurDevice("ima35s12");
        } else {
            mPosApi.initPosDev(PosApi.PRODUCT_MODEL_IMA80M01);
            setCurDevice(PosApi.PRODUCT_MODEL_IMA80M01);
        }

        mPosApi.setOnComEventListener(new PosApi.OnCommEventListener() {
            @Override
            public void onCommState(int i, int i1, byte[] bytes, int i2) {

                switch (i) {
                    case PosApi.POS_INIT:
                        if (i1 == PosApi.COMM_STATUS_SUCCESS) {
                            ToastUtils.showShort(getApplicationContext(), "设备初始化成功");
                        } else {
                            ToastUtils.showShort(getApplicationContext(), "设备初始化失败");
                        }
                        break;
                }
            }
        });

    }

    public String getCurDevice() {
        return mCurDev1;
    }

    public static void setCurDevice(String mCurDev) {
        mCurDev1 = mCurDev;
    }

    //其他地方引用mPosApi变量
    public PosApi getPosApi() {
        return mPosApi;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key == getString(R.string.use_connecting_key)){
            mUseConnecting = sharedPreferences.getBoolean(key,false);
        }else if(key == getString(R.string.enable_scan_only_key)){
            mEnableScanOnly = sharedPreferences.getBoolean(key,false);
        }

    }
}
