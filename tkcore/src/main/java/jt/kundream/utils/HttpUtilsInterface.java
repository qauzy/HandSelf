package jt.kundream.utils;

import com.lzy.okgo.OkGo;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Company: YunBeiTeac
 * Copyright: Copyright (c) 2017
 *
 * @author YangTianKun
 * @Version 1.0
 * @Since 2017/12/20/020 and 15:01
 * @Email 245086168@qq.com
 * Desc:抽取的联网的接口回调，所有的使用okgo操作的网络连接，必须实现此接口
 */
public interface HttpUtilsInterface {

    //主要用于showProgress
    void onStart(Request request);

    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2017/12/20/020 and 15:07
     * @Email 245086168@qq.com
     * describe:这里比较关键，返回值到底是要什么？s还是bean？ 个人觉得bean比较合理
     */
    void onSuccess(String s, Call call, Response response);

    /**
     * 这里是在请求网络之后  不管成功或者失败  都可以使用   主要是hideProgress
     * @param s
     * @param e
     */
    void onAfter(String s, Exception e);

    void onError(Call call, Response response, Exception e);
}
