/**
 * Created by nihira on 2014/03/18.
 * Copyright (c) 2013- Houwa System Design. All rights reserved.
 */
package jp.co.houwajs.beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.util.Log;

/**
 * @author nihira
 *
 */
public class AvaragingRssiBeacon extends Beacon {
	//============================================================
	// Property
	//============================================================
	private static final String TAG = AvaragingRssiBeacon.class.getName();
	
	private static final int SAMPLE_COUNT = 10;
	private Queue<Integer> rssiSampleQueue = new LinkedList<Integer>();
		
	@Override
	public int getRssi(){
		try{
			List<Integer> sortedList = new ArrayList<Integer>(this.rssiSampleQueue);
			Collections.sort(sortedList);
			Log.w(TAG, "index:"+ (this.rssiSampleQueue.size()/2));
			super.setRssi(sortedList.get(this.rssiSampleQueue.size()/2));
		}catch (Exception e) {
			Log.w(TAG, "rssi計算時のエラー", e);
			super.setRssi(0);
		}
		return super.getRssi();
	}
	
	//============================================================
	// Method
	//============================================================
	public AvaragingRssiBeacon(Beacon base){
		this.setUuid(base.getUuid());
		this.setMajor(base.getMajor());
		this.setMinor(base.getMinor());
	}
		
	/** rssiの標本を追加 */
	public void addRssiSample(int rssi){
		if(this.rssiSampleQueue.size() >= SAMPLE_COUNT){
			this.rssiSampleQueue.poll();
		}
		this.rssiSampleQueue.offer(rssi);
	}
}
