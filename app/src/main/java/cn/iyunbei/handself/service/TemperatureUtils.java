package cn.iyunbei.handself.service;

        import android.content.Context;
        import android.net.TrafficStats;
        import android.os.Handler;
        import android.os.Message;
        import android.util.Log;

        import com.fiberhome.duotellib.HumidityControlUtil;

        import java.text.DecimalFormat;
        import java.util.Timer;
        import java.util.TimerTask;

/**
 * Created by Ricky on 2016/10/13.
 */
public class TemperatureUtils {
    private Context context;
    private Handler mHandler;

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;
    private  HumidityControlUtil humidityControlUtil;

    public TemperatureUtils(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
        humidityControlUtil = new HumidityControlUtil();
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            showNetSpeed();
        }
    };

    public void startShowNetSpeed() {
        lastTotalRxBytes = getTotalRxBytes();
        lastTimeStamp = System.currentTimeMillis();
        new Timer().schedule(task, 1000, 1000); // 1s后启动任务，每2s执行一次

    }

    private long getTotalRxBytes() {
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }

    private void showNetSpeed() {
        long nowTotalRxBytes = getTotalRxBytes();
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
        long speed2 = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 % (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;

        Message msg = mHandler.obtainMessage();
        msg.what = 100;
        msg.obj ="温度:" + humidityControlUtil.getTemperature() + "℃\n湿度:" + humidityControlUtil.getHumidity() + "%";

//        msg.obj = String.valueOf(speed) + "." + String.valueOf(speed2) + " kb/s";
        mHandler.sendMessage(msg);//更新界面
    }

    private String getTotalSpeed(long speed, long speed2) {
        DecimalFormat showFloatFormat =new DecimalFormat("0.00");
        Log.d("速度", "speed: "+speed+",speed2"+speed2);
        String totalSpeedStr = "";
        if (speed >= 1024) {
            totalSpeedStr = showFloatFormat.format(speed/1024)+" M/s";
        } else {
            totalSpeedStr =showFloatFormat.format(speed+speed2/1024)+"K/s";
        }
        return totalSpeedStr;
    }
}



