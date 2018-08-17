package jt.kundream.base;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Company: YunBeiTeac
 * Copyright: Copyright (c) 2017
 *
 * @author YangTianKun
 * @Version 1.0
 * @Since 2017/12/20/020 and 15:30
 * @Email 245086168@qq.com
 * Desc:抽取出来的baseApplication
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initOKGO();
    }

    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2017/12/20/020 and 15:31
     * @Email 245086168@qq.com
     * describe:因为我个人项目中使用的联网框架都是OKGO  所以抽取出来此方法
     */
    private void initOKGO() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(20000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式为先使用缓存，不管是否存在缓存，都请求网络
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)  //缓存永不过期
                .setRetryCount(3);
    }

    /**
     * Company:YunBeiTeac
     *
     * @author YangTianKun
     * @date created at 2017/12/2 9:10
     * Describe：此方法和下一个方法  为设置app字体不根据系统字体大小变化
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) {
            getResources();
        }
    }

    /**
     * Company:YunBeiTeac
     *
     * @author YangTianKun
     * @date created at 2017/12/2 9:11
     * Describe：配合上一个方法，设置app字体不根据系统设置变化
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration configuration = new Configuration();
            configuration.setToDefaults();//设置默认
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return res;
    }
}
