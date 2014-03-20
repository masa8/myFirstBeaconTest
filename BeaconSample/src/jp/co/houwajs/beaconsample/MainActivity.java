package jp.co.houwajs.beaconsample;

import jp.co.houwajs.beacon.BeaconManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;

public class MainActivity extends Activity {
	private BeaconManager beaconManager;
	public BeaconManager getBeaconManager(){return this.beaconManager;}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void showBeaconListFragment(){
		Fragment fragment = new BeaconListFragment();
		FragmentTransaction tran = this.getFragmentManager().beginTransaction();
		tran.replace(R.id.main_main_contents_placeholder, fragment);
		tran.commit();
	}
	
	/* (非 Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if(this.beaconManager == null){
			this.beaconManager = new BeaconManager(this);
			this.beaconManager.startBeaconScan();
		}
		
		this.showBeaconListFragment();
	}

	/* (非 Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		if(this.beaconManager != null){
			this.beaconManager.stopBeaconScan();
			this.beaconManager = null;
		}
		
		super.onStop();
	}

	
	
}
