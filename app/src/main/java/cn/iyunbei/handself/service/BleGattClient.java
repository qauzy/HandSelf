package cn.iyunbei.handself.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.activity.BluetoothChat;
import jt.kundream.utils.ToastUtils;

public class BleGattClient {
    //状态代码
    private int mState;

    private static final String TAG = BleGattClient.class.getSimpleName();

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

    private  String preMacAddressStr;
    private BluetoothDevice mBluetoothDevice;

    //定义子线程handle，用于在BluetoothGattCallback中回调方法中的操作抛到该线程工作。
    private Handler mHandler;
    //定义handler工作的子线程
    private HandlerThread mHandlerThread;


    private BluetoothManager mBluetoothmanager;

    private BluetoothAdapter mBluetoothAdapter;
    //定义Gatt实现类
    private BluetoothGatt mBluetoothGatt;

    private BluetoothGattService mGattService;
    private BluetoothGattCharacteristic mGattCharacteristic;
    private BluetoothGattDescriptor mGattDescriptor;
    private boolean mReady = false;
    private Context ctx;

    //创建Gatt回调
    private BluetoothGattCallback mGattCallback = new FireBluetoothGattCallback();


    public BleGattClient(Context context){
        ctx = context;
        mBluetoothmanager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothmanager.getAdapter();


        //初始化handler
        mHandlerThread = new HandlerThread("daqi");
        mHandlerThread.start();
        //将handler绑定到子线程中
        mHandler = new Handler(mHandlerThread.getLooper());


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

    public void connect(String macAddressStr){
        preMacAddressStr = macAddressStr;
        //获取蓝牙设备对象进行连接
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(macAddressStr);
        //连接设备
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBluetoothGatt = mBluetoothDevice.connectGatt(ctx,
                    false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            mBluetoothGatt = mBluetoothDevice.connectGatt(ctx, false, mGattCallback);
        }
    }

    public synchronized int getState() {
        return mState;
    }
    private synchronized void setState(int state) {
        mState = state;
        mHandler.obtainMessage(BluetoothChat.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     *
     */
    public void SendBarcode(String code){
        if(!isReady()){
            ToastUtils.showShort(ctx, "设备未就绪");
            return;
        }
        Log.d(TAG,"开始写入特征");

        mGattCharacteristic.setValue(code);
        mBluetoothGatt.writeCharacteristic(mGattCharacteristic);

    }

    public boolean isReady(){
        if(mReady){
            return mBluetoothmanager.getConnectionState(mBluetoothDevice,BluetoothProfile.GATT) == BluetoothProfile.STATE_CONNECTED;
        }else{
            return false;
        }

    }


    public class FireBluetoothGattCallback extends BluetoothGattCallback {
        //服务发现回调
        //连接状态回调
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            // status 用于返回操作是否成功,会返回异常码。
            // newState 返回连接状态，如BluetoothProfile#STATE_DISCONNECTED、BluetoothProfile#STATE_CONNECTED

            //操作成功的情况下
            if (status == BluetoothGatt.GATT_SUCCESS){
                //判断是否连接码
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    //可延迟发现服务，也可不延迟
                    mHandler.post(() ->{
                                //发现服务
                                mBluetoothGatt.discoverServices();
                                Log.d(TAG,"开始发现服务");
                            }
                    );
                    Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_DEVICE_NAME);
                    Bundle bundle = new Bundle();
                    bundle.putString(BluetoothChat.DEVICE_NAME, gatt.getDevice().getAddress());
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    setState(Constants.STATE_CONNECTED);


                }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                    //判断是否断开连接码
                    setState(Constants.STATE_NONE);
                    mReady = false;
                }
            }else{
                //异常码
                Log.d(TAG,"BLE服务异常");
                setState(Constants.STATE_NONE);
                mReady = false;
            }
        }



        //服务发现回调
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            mGattService = mBluetoothGatt.getService(UUID_SERVICE);

            //获取到特定的服务不为空
            if(mGattService != null){
                mGattCharacteristic = mGattService.getCharacteristic(UUID_BARCODE);
                mReady =true;
                //mBluetoothGatt.disconnect();
            }else{
                //获取特定服务失败
                Log.d(TAG,"获取特定服务失败");

            }

        }

        //特征写入回调
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG,"特征写入回调:"+characteristic.getStringValue(0));
        }

        //外设特征值改变回调
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d(TAG,"外设特征值改变回调");
        }

        //描述写入回调
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG,"描述写入回调");
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
//                                //如果不为空重新连接
//                                if(!preMacAddressStr.isEmpty()){
//                                    connect(preMacAddressStr);
//                                }
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
