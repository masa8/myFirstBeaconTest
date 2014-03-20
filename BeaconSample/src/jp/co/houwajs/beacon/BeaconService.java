/**
 * Created by nihira on 2014/03/10.
 * Copyright (c) 2013- Houwa System Design. All rights reserved.
 */
package jp.co.houwajs.beacon;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * @author nihira
 *
 */
public class BeaconService extends Service {
	//============================================================
	// Property
	//============================================================
	private static final String TAG = BeaconService.class.getName();

	/** Binder */
	public class LocalBinder extends Binder{
		public BeaconService getService(){return BeaconService.this;}
	}
	private IBinder localBinder = new LocalBinder();
	
	private BeaconManager beaconManager;
	
	//============================================================
	// Method
	//============================================================
	
	@Override
	public IBinder onBind(Intent arg0) {
		if(this.beaconManager == null){
			this.beaconManager = new BeaconManager(this);
			this.beaconManager.startBeaconScan();
		}
		
		return this.localBinder;
	}
	

}
