/**
 * Created by nihira on 2014/03/13.
 * Copyright (c) 2013- Houwa System Design. All rights reserved.
 */
package jp.co.houwajs.beaconsample;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jp.co.houwajs.beacon.Beacon;
import jp.co.houwajs.common.view.listview.CommonListAdapter;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * @author nihira
 *
 */
public class BeaconListFragment extends Fragment {
	//============================================================
	// Property
	//============================================================
	private static final String TAG = BeaconListFragment.class.getName();

	private CommonListAdapter listAdapter;

	private ScheduledExecutorService updateListExecutor;
	
	//============================================================
	// Method
	//============================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup returnValue = (ViewGroup)inflater.inflate(R.layout.beacon_list_fragment, container, false);
				
		this.listAdapter = new CommonListAdapter(this.getActivity());
		ListView listview = (ListView)returnValue.findViewById(R.id.frg_beacon_list_listview);
		listview.setAdapter(this.listAdapter);
			
		return returnValue;
	}

	
	@Override
	public void onStart() {
		super.onStart();
		
		if(this.updateListExecutor != null){
			this.updateListExecutor.shutdown();
			this.updateListExecutor = null;
		}
		
		this.updateListExecutor = Executors.newSingleThreadScheduledExecutor();
		this.updateListExecutor.scheduleWithFixedDelay(new Runnable(){
			@Override
			public void run(){
				try{
					listAdapter.clearItem();
					
					if(getActivity() == null || !(getActivity() instanceof MainActivity)) return;
					MainActivity mainActivity = (MainActivity)getActivity();
					
					
					List<Beacon> beaconList = mainActivity.getBeaconManager().getScannedBeaconList();
					for(Beacon beacon : beaconList){					
						listAdapter.addItem(null, beacon.toString(), "rssi:" + beacon.getRssi() + String.format(" distance:%.2fm", beacon.getDistance()));
					}
					
					(new Handler(Looper.getMainLooper())).post(new Runnable(){
						@Override
						public void run() {
							listAdapter.notifyDataSetChanged();
						}
					});
				}catch (Exception e) {
					Log.w(TAG, "update list has error", e);
				}
			}
		}, 1000, 1000, TimeUnit.MILLISECONDS);
	}

	@Override
	public void onStop() {
		if(this.updateListExecutor != null){
			this.updateListExecutor.shutdown();
			this.updateListExecutor = null;
		}
		
		super.onStop();
	}
	
	
}
