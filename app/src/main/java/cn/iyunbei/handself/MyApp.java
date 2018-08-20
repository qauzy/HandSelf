package cn.iyunbei.handself;

import android.os.Build;
import android.posapi.PosApi;

import jt.kundream.base.BaseApplication;
import jt.kundream.utils.ToastUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class MyApp extends BaseApplication {
    PosApi mPosApi=null;

    @Override
    public void onCreate() {
        super.onCreate();
        initPosapi();
    }

    private void initPosapi(){
        //posap初始化
        mPosApi=PosApi.getInstance(this);
        //根据型号进行初始化mPosApi类
        if (Build.MODEL.contains("LTE")||android.os.Build.DISPLAY.contains("3508")||
                android.os.Build.DISPLAY.contains("403")||
                android.os.Build.DISPLAY.contains("35S09")) {
            mPosApi.initPosDev("ima35s09");
        } else if(Build.MODEL.contains("5501")){
            mPosApi.initPosDev("ima35s12");
        }else{
            mPosApi.initPosDev(PosApi.PRODUCT_MODEL_IMA80M01);
        }
        mPosApi.setOnComEventListener(new PosApi.OnCommEventListener() {
            @Override
            public void onCommState(int i, int i1, byte[] bytes, int i2) {

                switch (i){
                    case  PosApi.POS_INIT:
                        if(i1==PosApi.COMM_STATUS_SUCCESS){
                            ToastUtils.showShort(getApplicationContext(),"设备初始化成功");
                        }else{
                            ToastUtils.showShort(getApplicationContext(),"设备初始化失败");
                        }
                        break;
                }
            }
        });

    }

}
