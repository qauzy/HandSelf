package cn.iyunbei.handself.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Set;

import cn.iyunbei.handself.R;
import jt.kundream.utils.ActivityUtil;

/*
       本程序供菜单项主界面的选项菜单“我的友好”调用，用于：
       （1）显示已配对的好友列表；
       （2）搜索可配对的好友进行配对
       （3）新选择并配对的蓝牙设备将刷新好友列表
       注意：发现新的蓝牙设备并请求配对时，需要对应接受
       关键技术：动态注册一个广播接收者，处理蓝牙设备扫描的结果
   */
public class DeviceList extends AppCompatActivity {
    private static final String TAG = DeviceList.class.getSimpleName();
    private BluetoothAdapter mBtAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";  //Mac地址

    public ScanCallback mScanCallback = new ScanCallback() {
        //当一个蓝牙ble广播被发现时回调
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //扫描类型有开始扫描时传入的ScanSettings相关
            //对扫描到的设备进行操作。如：获取设备信息。

            BluetoothDevice device = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                device = result.getDevice();
                Log.d(TAG,"发现设备:"+device.getName() + "地址:" + device.getAddress());
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }

        //批量返回扫描结果
        //@param results 以前扫描到的扫描结果列表。
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

        }

        //当扫描不能开启时回调
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            //扫描太频繁会返回ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED，表示app无法注册，无法开始扫描。

        }
    };




    //定义广播接收者，用于处理扫描蓝牙设备后的结果
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG,"发现设备:"+device.getName());
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);
        //在被调用活动里，设置返回结果码
        setResult(Activity.RESULT_CANCELED);
        init();  //活动界面
    }
    private void init() {
        Button scanButton = findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // 询问打开蓝牙
                if (mBtAdapter != null && !mBtAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    ActivityUtil.startActivityForResult(DeviceList.this,enableBtIntent, 1);
                    return;
                }

                Toast.makeText(DeviceList.this, R.string.scanning, Toast.LENGTH_LONG).show();
                //扫描之前先清除列表
                mNewDevicesArrayAdapter.clear();
                doDiscovery();  //搜索蓝牙设备

            }
        });
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //已配对蓝牙设备列表
        ListView pairedListView =findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);

        pairedListView.setOnItemClickListener(mPaireDeviceClickListener);
        //未配对蓝牙设备列表
        ListView newDevicesListView = findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mNewDeviceClickListener);
        //动态注册广播接收者
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress() );
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }


        //BLE蓝牙扫描器
        mBluetoothLeScanner=mBtAdapter.getBluetoothLeScanner();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }
    private void doDiscovery() {

        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
//        mBtAdapter.startDiscovery();  //开始搜索蓝牙设备并产生广播


        mBluetoothLeScanner.startScan(mScanCallback);

        //10s后停止扫描
        stopscan(10);
        //startDiscovery是一个异步方法
        //找到一个设备时就发送一个BluetoothDevice.ACTION_FOUND的广播
    }
    private AdapterView.OnItemClickListener mPaireDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mBtAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);  //Mac地址
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
    private AdapterView.OnItemClickListener mNewDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mBtAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);  //Mac地址
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
    };
    //回调方法：进入蓝牙配对设置界面返回后执行
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();  //刷新好友列表
    }

    //停止扫描
    private void stopscan(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(i*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBluetoothLeScanner.stopScan(mScanCallback);
                }

            }
        }).start();
    }
}