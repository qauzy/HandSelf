package jt.kundream.utils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 版权所有，违法必究！
 *
 * @author YangTianKun
 *         简单解析json，得到的用于判断是否在其他设备登录了
 */

public class JsonUtils {

    public static int code;   //返回数据的状态
    private static String msg;


    public static int checkToken(String s) {
        if (s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                code = Integer.parseInt(String.valueOf(jsonObject.get("code")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return code;
    }

    public static String getMsg(String s) {
        if (s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                msg = (String) jsonObject.get("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return msg;
    }

    /**
     * 获取json中外层的某一个字段的值
     *
     * @param s   完整的json数据
     * @param key 字段
     * @return
     */

    public static String getOutMSG(String s, String key) {
        if (s != null) {
            try {
                msg = String.valueOf(new JSONObject(s).get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return msg;
    }

    /**
     * 此方法主要是为了使用JsonObject获取某些联网时候返回的某一个字段  因为有时候可能只用到了一个字段 用一次
     * 所以没必要创建一个bean
     *
     * @param s
     * @return
     */
    public static String getInnerStr(String s, String key) {
        String value = null;
        if (s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                /**
                 * 因为json的结构是外面是信息  里面是data包裹的
                 */
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                value = String.valueOf(jsonObject1.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    /**
     * 此方法主要是为了使用JsonObject获取某些联网时候返回的某一个字段  因为有时候可能只用到了一个字段 用一次
     * 所以没必要创建一个bean
     *
     * @param s
     * @return
     */
    public static int getInnerInt(String s, String key) {
        int value = -1;
        if (s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                /**
                 * 因为json的结构是外面是信息  里面是data包裹的
                 */
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                value = Integer.parseInt(String.valueOf(jsonObject1.get(key)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return value;
    }


    /**
     * object转jsonString
     *
     * @param object 实体
     * @return json字符串
     */
    public static String toJsonString(Object object) {
        return new Gson().toJson(object);
//        return JSON.toJSONString(object);
    }

    /**
     * json转业务对象
     *
     * @param clazz 业务实体
     * @param <T>   业务实体
     * @return 业务对象
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        return new Gson().fromJson(jsonString, clazz);
//        return JSON.parseObject(jsonString, clazz);
    }

}
