package jt.kundream.utils;

import com.google.gson.Gson;

/**
 * 版权所有，违法必究！！！
 *
 * @author YangTianK on 2017/11/16 17:28;
 * @Company NanYangYunBeiTeac
 * 邮箱：245086168@qq.com
 * 说明： 获取的数据的解析，将json数据解析为对应的业务实体
 */
public class GsonUtils {
    /**
     * 将Json数据解析成相应的映射对象
     * @param jsonString   json字符串
     * @param type          业务实体  一般指bean
     * @param <T>           泛型T  代表最终要返回的业务实体
     * @return          返回值理论上一定是一个实体bean
     */
    public static <T> T parseJsonWithGson(String jsonString, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonString, type);
        return result;
    }
}
