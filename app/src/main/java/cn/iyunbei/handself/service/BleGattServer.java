package cn.iyunbei.handself.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.activity.BluetoothChat;
import cn.iyunbei.handself.bean.GoodsBean;
import jt.kundream.utils.ToastUtils;

public class BleGattServer {
    private static final String TAG = BleGattServer.class.getSimpleName();
    //状态代码
    private int mState;

    //服务 UUID
    private static final UUID UUID_SERVICE = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    //特征 UUID
    private static final UUID  UUID_BARCODE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    //特征 UUID
    private static final UUID  UUID_CHARACTERISTIC = UUID.fromString("0000ff11-0000-1000-8000-00805f9b34fb");
    private static final UUID  UUID_CHARACTERISTIC2 = UUID.fromString("0000ff12-0000-1000-8000-00805f9b34fb");

    //描述 UUID
    private static final UUID  UUID_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");



    private static final ParcelUuid parcelUuid = ParcelUuid.fromString("00002902-0000-1000-8000-00805f9b34fb");


    private final BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private BluetoothManager mBluetoothManager;

    private BluetoothGattServer mBluetoothGattServer;
    private BluetoothGattService mGattService;
    private BluetoothGattCharacteristic mGattCharacteristic;
    private BluetoothGattDescriptor mGattDescriptor;

    private final Handler mHandler;




    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG,"广播成功");

        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.d(TAG,"广播失败:"+errorCode);
        }
    };

    public BleGattServer(Context ctx, Handler handler){
        mHandler = handler;
        //构造方法完成蓝牙对象的创建
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 注意：必须要开启可连接的BLE广播，其它设备才能发现并连接BLE服务端!
        // =============启动BLE蓝牙服务端======================================
        // 创建BluetoothGattService时，传入两个参数：UUID和Service类型(主服务,次要服务)
        mGattService = new BluetoothGattService(UUID_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY);

//        BluetoothGattCharacteristic.PROPERTY_WRITE 表示特征支持写
//        BluetoothGattCharacteristic.PROPERTY_READ  表示特征支持读
//        BluetoothGattCharacteristic.PROPERTY_NOTIFY  表示特征支持通知

//        BluetoothGattCharacteristic.PERMISSION_WRITE 特征写权限
//        BluetoothGattCharacteristic.PERMISSION_READ 特征读权限

        // 创建BluetoothGattCharacteristic时，传入三个参数：UUID、特征属性 和 权限属性
        mGattCharacteristic = new BluetoothGattCharacteristic(UUID_BARCODE, BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE);

        //初始化描述
        mGattDescriptor = new BluetoothGattDescriptor(UUID_DESCRIPTOR, BluetoothGattDescriptor.PERMISSION_WRITE);



        mBluetoothManager  = (BluetoothManager)ctx.getSystemService(Context.BLUETOOTH_SERVICE);
        FireBluetoothGattServerCallback mBluetoothGattServerCallback = new FireBluetoothGattServerCallback();
        mBluetoothGattServer = mBluetoothManager.openGattServer(ctx, mBluetoothGattServerCallback);


        //特征值添加描述
        mGattCharacteristic.addDescriptor(mGattDescriptor);
        //服务添加特征值
        mGattService.addCharacteristic(mGattCharacteristic);

        //添加服务
        boolean result = mBluetoothGattServer.addService(mGattService);
        if (result){
            Toast.makeText(ctx,"添加服务成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(ctx,"添加服务失败",Toast.LENGTH_SHORT).show();
        }


        //开始广播
        startAdvertise();


        // 初始化广播
        BluetoothMonitorReceiver bleListenerReceiver = new BluetoothMonitorReceiver();
        IntentFilter intentFilter = new IntentFilter();
        // 监视蓝牙关闭和打开的状态
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        // 监视蓝牙设备与APP连接的状态
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

        // 注册广播
        ctx.registerReceiver(bleListenerReceiver, intentFilter);


    }
    public synchronized int getState() {
        return mState;
    }
    private synchronized void setState(int state) {
        mState = state;
        mHandler.obtainMessage(BluetoothChat.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public void startAdvertise(){

        //广播设置(必须)
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY) //广播模式: 低功耗,平衡,低延迟
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH) //发射功率级别: 极低,低,中,高
                .setTimeout(0)
                .setConnectable(true) //能否连接,广播分为可连接广播和不可连接广播
                .build();

        byte[] broadcastData ={0x34,0x56};
        //广播数据(必须，广播启动就会发送)
        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true) //包含蓝牙名称
//                .setIncludeTxPowerLevel(true) //包含发射功率级别
                .addManufacturerData(0x01AC, broadcastData) //设备厂商数据，自定义
                .build();


        //扫描响应数据(可选，当客户端扫描时才发送)
        AdvertiseData scanResponse = new AdvertiseData.Builder()
                .addManufacturerData(2, new byte[]{66, 66}) //设备厂商数据，自定义
                .addServiceUuid(parcelUuid) //服务UUID
//                .addServiceData(new ParcelUuid(UUID_SERVICE), new byte[]{2}) //服务数据，自定义
                .build();

        //设置设备蓝牙名称
        mBluetoothAdapter.setName("机灵猫");
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        //true,标识可以被连接
        //设为0时，代表无时间限制会一直广播
        mBluetoothLeAdvertiser.startAdvertising(settings, advertiseData,scanResponse, mAdvertiseCallback);

        Log.d(TAG,"开始广播...");
    }


    //定义蓝牙Gatt回调类
    public class FireBluetoothGattServerCallback extends BluetoothGattServerCallback {

        public FireBluetoothGattServerCallback(){

        }

        //设备连接/断开连接回调
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            //判断是否连接码
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //可延迟发现服务，也可不延迟
                Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_DEVICE_NAME);
                Bundle bundle = new Bundle();
                bundle.putString(BluetoothChat.DEVICE_NAME, device.getName());
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                setState(Constants.STATE_CONNECTED);
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                //判断是否断开连接码
                setState(Constants.STATE_NONE);
            }


        }

        //添加本地服务回调
        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
        }

        //特征值读取回调
        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            Log.d(TAG,"特征值读取回调");
        }

        //特征值写入回调
        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            Log.d(TAG,"特征写入回调:"+new String(value));

            mHandler.obtainMessage(BluetoothChat.MESSAGE_READ, value.length, -1, value).sendToTarget();
            //回复客户端,让客户端读取该特征新赋予的值，获取由服务端发送的数据
            // 响应客户端
            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS,
                    offset, value);
        }

        //描述读取回调
        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            Log.d(TAG,"描述读取回调");

        }

        //描述写入回调
        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            Log.d(TAG,"描述写入回调");
            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS,
                    offset, value);
        }
    }

    public class BluetoothMonitorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                        switch (blueState) {
                            case BluetoothAdapter.STATE_TURNING_ON:
                                Toast.makeText(context, "蓝牙正在打开", Toast.LENGTH_SHORT).show();
                                break;
                            case BluetoothAdapter.STATE_ON:
                                Toast.makeText(context, "蓝牙已经打开", Toast.LENGTH_SHORT).show();
                                //重新广播
                                startAdvertise();
                                break;
                            case BluetoothAdapter.STATE_TURNING_OFF:
                                Toast.makeText(context, "蓝牙正在关闭", Toast.LENGTH_SHORT).show();
                                break;
                            case BluetoothAdapter.STATE_OFF:
                                Toast.makeText(context, "蓝牙已经关闭", Toast.LENGTH_SHORT).show();
                                //发送蓝牙关闭通知
                                setState(Constants.STATE_NONE);
                                break;
                        }
                        break;

                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        Toast.makeText(context, "蓝牙设备已连接", Toast.LENGTH_SHORT).show();
                        break;

                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        Toast.makeText(context, "蓝牙设备已断开", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }

    }

}
