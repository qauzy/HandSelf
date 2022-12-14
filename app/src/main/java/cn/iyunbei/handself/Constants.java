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
    public  static String FiberHomeUUID = "00000000-47fe-b93e-0033-c5870033c587";

    public static final String ROOT_URL = "http://pda.iyunbei.cn/api/pda/v1/";

    /**
     * 登录
     */
    public static final String LOGIN = ROOT_URL + "shopmember/login";

    /**
     * 获取商品属性信息
     */
//    public static String GET_GOODS = "https://www.mxnzp.com/api/barcode/goods/details";
    public static String GET_GOODS = "http://47.115.166.195:9999/api/v1/goods";

    /**
     * 获取商品列表
     */
    public static String GET_GOODS_LIST = "http://47.115.166.195:9999/api/v1/goods/list";

    /**
     * 盘点时候的单品数量录入
     */
    public static String GOODS_SAVE = "http://47.115.166.195:9999/api/v1/goods/update";

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
    /**
     * 盘点中订单详情
     */
    public static String PANDIAN_ING = ROOT_URL + "profit/profitgoods";
    /**
     * 盘点时候的单品数量录入
     */
    public static String PD_SAVE = ROOT_URL + "profit/upprofitmum";
    /**
     * 设置盘点订单完成
     */
    public static String SET_PD_OK = ROOT_URL + "profit/setprofitstatusok";
    /**
     * 支付宝支付
     */
    public static String ALI_PAY = ROOT_URL + "order/alibarpay";
    /**
     * 现金支付
     */
    public static String CASH_PAY = ROOT_URL + "order/xianjinpay";
    /**
     * 生成新的盘点单
     */
    public static String CREATE_PROFIT = ROOT_URL + "profit/createprofit";
    /**
     * 支付方式的列表选择
     */
    public static String PAY_TYPE = ROOT_URL + "payment/paylist";
    /**
     * 订单列表中的订单分页
     */
    public static String LIST_NEXT = ROOT_URL + "order/orderpage";
    /**
     * 当日销售统计
     */
    public static String DAY_SELL = ROOT_URL + "order/daysell";
    /**
     * 当月销售数据统计
     */
    public static String MONTH_SELL = ROOT_URL + "order/monthsell";



    //消息代码
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;


    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
}

