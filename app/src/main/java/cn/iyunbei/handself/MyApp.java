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
import cn.iyunbei.handself.presenter.SpeechUtils;
import cn.iyunbei.handself.utils.ToolKit;
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
    private String mCurDev1 ;
    public Boolean mUseConnecting;
    public Boolean mEnableScanOnly;
    private SpeechUtils spk;

    @Override
    public void onCreate() {
        super.onCreate();

        mCurDev1 = ToolKit.getUniqueID(this);
        //初始化配置

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        prefs.registerOnSharedPreferenceChangeListener(this);
        mUseConnecting = prefs.getBoolean(getString(R.string.use_connecting_key),false);
        mEnableScanOnly = prefs.getBoolean(getString(R.string.enable_scan_only_key),false);
        spk = new SpeechUtils(this);
        initOkGo();
//        initDatabase();
    }

    public MyApp() {
        super.onCreate();
        instance = this;

    }
    public void say(String what){
        spk.speak(what);
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
    public String getCurDevice() {
        return mCurDev1;
    }

    public boolean isFiberHome() {
        return mCurDev1.compareToIgnoreCase(Constants.FiberHomeUUID) == 0;
    }

    public void setCurDevice(String mCurDev) {
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
