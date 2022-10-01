package cn.iyunbei.handself.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;


import com.fiberhome.duotellib.HumidityControlUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import cn.iyunbei.handself.presenter.SpeechUtils;

/**
 * 描述:
 * 保活
 * @author sxk
 * @date 2019/05/14
 */

public class LiveService extends Service {
    private final static String TAG = "LiveService";

    private HumidityControlUtil humidityControlUtil;
    private WebSocketClient client;

    /* 用于 udpReceiveAndTcpSend 的3个变量 */
    Socket socket = null;
    MulticastSocket ms = null;
    DatagramPacket dp;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    WifiManager.MulticastLock mMulticastLock;

    private SpeechUtils spk;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "---->onCreate,启动服务");

        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        mWakeLock.acquire();

        WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mMulticastLock=wifiManager.createMulticastLock("multicast.test");
        WifiManager.WifiLock wifiLock= wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "WifiLocKManager");

        wifiLock.acquire();


        Context ctx =  getApplicationContext();
        spk = new SpeechUtils(ctx);

        Log.d(TAG, TAG + "---->initWebSocket,启动服务");
        //初始化WebSocket
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        if (client == null) {
            Log.d(TAG, "``````````````````````onCreate");
            initWebSocket();
        } else if (!client.isOpen()) {
            reconnectWs();//进入页面发现断开开启重连
        }

        humidityControlUtil = new HumidityControlUtil();
//
//        /* 开一个线程 接收udp多播*/
//        new LiveService.udpReceive().start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //1.通知栏占用，不清楚的看官网或者音乐类APP的效果

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("title")
                .setContentText("text");

        //创建NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String notificationId = "TgStartServiceId";
            String notificationName = "TgStartServiceName";
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            builder.setChannelId(notificationId);
        }

        Notification notification = builder.build();


        startForeground(100,notification);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG + "---->onCreate,停止服务");
        mWakeLock.release();
        // 重启
        Intent intent = new Intent(getApplicationContext(), LiveService.class);
        startService(intent);

    }

    // 服务是否运行
    public boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();


        for (ActivityManager.RunningAppProcessInfo info : lists) {// 获取运行服务再启动
            System.out.println(info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;

    }



    /*接收udp多播 并 发送tcp 连接*/
    private class udpReceive extends  Thread {
        @Override
        public void run() {

            byte[] data = new byte[1024];
            try {
                InetAddress groupAddress = InetAddress.getByName("224.0.0.1");
                ms = new MulticastSocket(6789);
                ms.setNetworkInterface(NetworkInterface.getByName("wlan0"));
                ms.setSoTimeout(60000);
                ms.joinGroup(groupAddress);

            } catch (Exception e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    dp = new DatagramPacket(data, data.length);
                    if (ms != null)
                        ms.receive(dp);
                } catch (Exception e) {
                    e.printStackTrace();
                    spk.speak("组播监听可能被关闭,请检查设备");
                    sendMsg("组播监听可能被关闭,请检查设备");
                }

                if (dp.getAddress() != null) {
                    final String quest_ip = dp.getAddress().toString();

                    /* 若udp包的ip地址 是 本机的ip地址的话，丢掉这个包(不处理)*/

                    //String host_ip = getLocalIPAddress();

                    String host_ip = getLocalHostIp();

                    System.out.println("host_ip:  --------------------  " + host_ip);
                    System.out.println("quest_ip: --------------------  " + quest_ip.substring(1));
                    Log.e(TAG, "quest_ip: --------------------  " + quest_ip.substring(1));
                    if( (!host_ip.equals(""))  && host_ip.equals(quest_ip.substring(1)) ) {
                        continue;
                    }

                    final String codeString = new String(data, 0, dp.getLength());
                    spk.speak(codeString);

                }
            }
//            wakeLock.release();//需要在合适的地方释放
        }
    }

    public String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        catch(SocketException e)
        {
            Log.e(TAG, "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;
    }

    private String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }





        /**
         * 初始化websocket
         */
        public void initWebSocket() {
            Log.e(TAG, "websocket 的地址是：ws://47.115.166.195:9999/api/v1/ws/leffss");
            URI uri = URI.create("ws://47.115.166.195:9999/api/v1/ws/leffss");
            //TODO 创建websocket
            client = new WebSocketClient(uri,new Draft_6455() {},null,100000) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.i(TAG, "云端连接成功"+handshakedata.toString());
                    spk.speak("云端连接成功");
                }

                @Override
                public void onMessage(String message) {

                    if (message.equals("Heartbeat")){
                        Log.i(TAG, "websocket收到Heartbeat消息");

                    }else if (message.equals("clock")){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

                        String t=format.format(new Date());
                        spk.speak("现在时间"+t+",实时温度"+humidityControlUtil.getTemperature()+"度，相对湿度百分之"+humidityControlUtil.getHumidity());
                        Log.i(TAG, "websocket收到消息：" + message);
                    }else{
                                              spk.speak(message);
                        Log.i(TAG, "websocket收到消息：" + message);

                    }

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    if (code!=1000) {
                        reconnectWs();//意外断开马上重连
                    }
                    Log.i(TAG, "websocket断开连接：·code:" + code + "·reason:" + reason + "·remote:" + remote);
                }

                @Override
                public void onError(Exception ex) {

                }

            };
            //TODO 设置超时时间
            client.setConnectionLostTimeout(110 * 1000);
            //TODO 连接websocket
            new Thread() {
                @Override
                public void run() {
                    try {
                        //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                        client.connectBlocking();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        /**
         * 发送消息
         *
         * @param msg
         */
        public void sendMsg(String msg) {
            if (null != client) {
                Log.e("", "^_^Websocket发送的消息：-----------------------------------^_^" + msg);
                if (client.isOpen()) {
                    client.send(msg);
                }

            }
        }

        /**
         * 开启重连
         */
        private void reconnectWs() {
//            mHandler.removeCallbacks(heartBeatRunnable);
            new Thread() {
                @Override
                public void run() {
                    try {
                        Log.e("开启重连", "");
                        client.reconnectBlocking();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        /**
         * 断开连接
         */
        private void closeConnect() {
            try {
                //关闭websocket
                if (null != client) {
                    client.close();
                }
                //停止心跳
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client = null;
            }
        }

        //    -------------------------------------websocket心跳检测------------------------------------------------
        private static final long HEART_BEAT_RATE = 60 * 1000;//每隔10秒进行一次对长连接的心跳检测
        private Handler mHandler = new Handler();
        private Runnable heartBeatRunnable = new Runnable() {
            @Override
            public void run() {
                if (client != null) {
                    if (client.isClosed()) {
                        Log.e("心跳包检测websocket连接状态1", client.isOpen() + "/" );
                        reconnectWs();//心跳机制发现断开开启重连
                    } else {
                        Log.e("心跳包检测websocket连接状态2", client.isOpen() + "/");
                        sendMsg("Heartbeat");
                    }
                } else {
                    Log.e("心跳包检测websocket连接状态重新连接", "");
                    //如果client已为空，重新初始化连接
                    client = null;
                    initWebSocket();
                }
                //每隔一定的时间，对长连接进行一次心跳检测
                mHandler.postDelayed(this, HEART_BEAT_RATE);
            }
        };

}