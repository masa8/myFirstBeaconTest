/**
 * Created by nihira on 2014/03/10.
 * Copyright (c) 2013- Houwa System Design. All rights reserved.
 */
package jp.co.houwajs.beacon;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.Context;
import android.util.Log;

/**
 * @author nihira
 *
 */
public class BeaconManager {
	//============================================================
	// Property
	//============================================================
	private static final String TAG = BeaconManager.class.getName();
	
	private Context context;
	private LeScanCallback activeScanCallback;
	
	private Map<String, Beacon> scannedBeaconMap;
	
	//============================================================
	// Method
	//============================================================
	/** コンストラクタ */
	public BeaconManager(Context context){
		this.context = context;
		this.scannedBeaconMap = new ConcurrentHashMap<String, Beacon>();
	}
	
	/** Beaconのスキャンを開始 */
	public void startBeaconScan(){
		//BluetoothAdapterの取得
		BluetoothAdapter btAdapter = this.getBluetoothAdapter();
		if(btAdapter == null) return;
		
		if(this.activeScanCallback != null) btAdapter.stopLeScan(this.activeScanCallback);
		//ScanのCallBack
		this.activeScanCallback = new BluetoothAdapter.LeScanCallback() {
			@Override
			public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord){
				Beacon newBeacon = Beacon.createInstance(device, rssi, scanRecord);
				if(newBeacon != null){
					synchronized(scannedBeaconMap){
						AvaragingRssiBeacon avaBeacon = (AvaragingRssiBeacon)scannedBeaconMap.get(newBeacon.toString());
						if(avaBeacon == null){
							avaBeacon = new AvaragingRssiBeacon(newBeacon);
							scannedBeaconMap.put(avaBeacon.toString(), avaBeacon);
						}
						avaBeacon.addRssiSample(newBeacon.getRssi());
						avaBeacon.setLastScanDate(new Date());
					}
				}
			}
		};
		btAdapter.startLeScan(this.activeScanCallback);
	}
		
	/** beaconのスキャンを停止 */
	public void stopBeaconScan(){
		if(this.activeScanCallback == null) return;
		
		//BluetoothAdapterの取得
		BluetoothAdapter btAdapter = this.getBluetoothAdapter();
		if(btAdapter == null) return;
		
		btAdapter.stopLeScan(this.activeScanCallback);
		
		this.activeScanCallback = null;		
	}
	
	/** BluetoothAdapterの取得 */
	private BluetoothAdapter getBluetoothAdapter(){
		final BluetoothManager bluetoothManager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);
		final BluetoothAdapter btAdapter = bluetoothManager.getAdapter();	
		if(btAdapter == null){
			Log.d(TAG, "can not get BluetoothAdapter");
			return null;
		}
		
		return btAdapter;
	}
	
	/** スキャンしたBeaconの一覧を返す */
	public List<Beacon> getScannedBeaconList(){
		List<Beacon> returnValue = new ArrayList<Beacon>();
		synchronized(this.scannedBeaconMap){
			long now = (new Date()).getTime();
			for(Beacon beacon : this.scannedBeaconMap.values()){
				if(now - beacon.getLastScanDate().getTime() > 10000){
					this.scannedBeaconMap.remove(beacon.toString());
				}else{
					returnValue.add(beacon);
				}
			}
		}
		return returnValue;
	}
}
