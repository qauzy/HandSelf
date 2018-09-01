package cn.iyunbei.handself;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class Constants {

    public static final String ROOT_URL = "http://pda.iyunbei.cn/api/pda/v1/";

    /**
     * 登录
     */
    public static final String LOGIN = ROOT_URL + "shopmember/login";

    /**
     * 获取商品属性信息
     */
    public static String GET_GOODS = ROOT_URL + "goods/getgoods";

    /**
     * 获取用户信息
     */
    public static String USER_MSG = ROOT_URL + "shopmember/userinfo";

    /**
     * 获取订单列表  todo 新需求 只返包含今天的三天内数据   选择日期之后再请求选择日期的数据
     */
    public static String GET_ORDER_LIST = ROOT_URL + "order/orderlist";

    /**
     * 获取订单详情
     */
    public static final String GET_ORDER_DETAIL = ROOT_URL + "order/orders";

    /**
     * 盘点订单列表
     */
    public static String PANDIAN_LIST = ROOT_URL + "profit/getprofitlist";
}

