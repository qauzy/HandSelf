package jt.kundream.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 版权所有,违法必究！！！
 *
 * @Company:NanYangYunBeiTeac
 * @Copyright:©2017-2018
 * @Author:YangTianKun Created at 2018/5/19 11
 * @e-mail:245086168@qq.com
 * @desc: 作者：fengmlo
 * 链接：https://www.jianshu.com/p/194f90fb4525
 * 來源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */

public class WXShare {

    // TODO: 2018/5/19 Appid需要改为自己申请的
    public static final String APP_ID = "wx46d98b2395512f4a";
    public static final String ACTION_SHARE_RESPONSE = "action_wx_share_response";
    public static final String EXTRA_RESULT = "result";

    private final Context context;
    private final IWXAPI api;
    private OnResponseListener listener;
    private ResponseReceiver receiver;

    public WXShare(Context context) {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        this.context = context;
    }

    public WXShare register() {
        // 微信分享
        api.registerApp(APP_ID);
        receiver = new ResponseReceiver();
        IntentFilter filter = new IntentFilter(ACTION_SHARE_RESPONSE);
        context.registerReceiver(receiver, filter);
        return this;
    }

    public void unregister() {
        try {
            api.unregisterApp();
            context.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WXShare share(String text) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        //        msg.title = "Will be ignored";
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        boolean result = api.sendReq(req);
        return this;
    }

    //flag用来判断是分享到微信好友还是分享到微信朋友圈，
    //0代表分享到微信好友，1代表分享到朋友圈
    public WXShare shareUrl(int flag, Context context, String url, String title, String descroption, Bitmap sharePic) {
        //初始化一个WXWebpageObject填写url
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        //用WXWebpageObject对象初始化一个WXMediaMessage，天下标题，描述
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = title;
        msg.description = descroption;
        /**
         *这块需要注意，图片的像素千万不要太大，不然的话会调不起来微信分享，
         我在做的时候和我们这的UIMM说随便给我一张图，她给了我一张1024*1024的图片
         当时也不知道什么原因，后来在我的机智之下换了一张像素小一点的图片好了！
         */
//        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_logo);
//        Bitmap thumb = null;
//        try {
//            thumb = Glide.with(context)
//                    .load(imgUrl)
//                    .asBitmap() //必须
//                    .centerCrop()
//                    .into(400, 400)
//                    .get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        msg.setThumbImage(sharePic);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        return this;
    }


    public IWXAPI getApi() {
        return api;
    }

    public void setListener(OnResponseListener listener) {
        this.listener = listener;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Response response = intent.getParcelableExtra(EXTRA_RESULT);
//            Logger.d("type: " + response.getType());
//            Logger.d("errCode: " + response.errCode);
            String result;
            if (listener != null) {
                if (response.errCode == BaseResp.ErrCode.ERR_OK) {
                    listener.onSuccess();
                } else if (response.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                    listener.onCancel();
                } else {
                    switch (response.errCode) {
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                            result = "发送被拒绝";
                            break;
                        case BaseResp.ErrCode.ERR_UNSUPPORT:
                            result = "不支持错误";
                            break;
                        default:
                            result = "发送返回";
                            break;
                    }
                    listener.onFail(result);
                }
            }
        }
    }

    public static class Response extends BaseResp implements Parcelable {

        public int errCode;
        public String errStr;
        public String transaction;
        public String openId;

        private int type;
        private boolean checkResult;

        public Response(BaseResp baseResp) {
            errCode = baseResp.errCode;
            errStr = baseResp.errStr;
            transaction = baseResp.transaction;
            openId = baseResp.openId;
            type = baseResp.getType();
            checkResult = baseResp.checkArgs();
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public boolean checkArgs() {
            return checkResult;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.errCode);
            dest.writeString(this.errStr);
            dest.writeString(this.transaction);
            dest.writeString(this.openId);
            dest.writeInt(this.type);
            dest.writeByte(this.checkResult ? (byte) 1 : (byte) 0);
        }

        protected Response(Parcel in) {
            this.errCode = in.readInt();
            this.errStr = in.readString();
            this.transaction = in.readString();
            this.openId = in.readString();
            this.type = in.readInt();
            this.checkResult = in.readByte() != 0;
        }

        public static final Creator<Response> CREATOR = new Creator<Response>() {
            @Override
            public Response createFromParcel(Parcel source) {
                return new Response(source);
            }

            @Override
            public Response[] newArray(int size) {
                return new Response[size];
            }
        };
    }
}
