/**
 * Created by nihira on 2014/03/10.
 * Copyright (c) 2013- Houwa System Design. All rights reserved.
 */
package jp.co.houwajs.beacon;

import java.nio.ByteBuffer;
import java.util.Date;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * @author nihira
 *
 */
public class Beacon {
	//============================================================
	// Property
	//============================================================
	private static final String TAG = Beacon.class.getName();
	
	private String uuid;
	public String getUuid(){return this.uuid;}
	protected void setUuid(String value){this.uuid = value;}
	
	private Integer major;
	public Integer getMajor(){return this.major;}
	protected void setMajor(Integer value){this.major = value;}
		
	private Integer minor;
	public Integer getMinor(){return this.minor;}
	protected void setMinor(Integer value){this.minor = value;}
	
	private Date lastScanDate;
	public Date getLastScanDate(){return this.lastScanDate;}
	protected void setLastScanDate(Date value){this.lastScanDate = value;}
	
	private int rssi;
	public int getRssi(){return this.rssi;}
	protected void setRssi(int value){this.rssi = value;}
	
	/** 距離(メートル) */
	public double getDistance(){
		//減衰率 (2.0~4.0らしい)
		double gensui = 2.5;
		//1mの離れた時のrssi
		double a = -58;
		double distance = Math.pow(10, ((double)this.getRssi() - a)/(-10*gensui));
		
		return distance;
	}
	
	//============================================================
	// Method
	//============================================================
	/** コンストラクタ */
	protected Beacon(){
	}
	
	public static Beacon createInstance(BluetoothDevice device, int rssi, byte[] bytes){
		try{
			if(bytes.length <= 30) return null;
			
			ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
			
			if(byteBuffer.get(5) != (byte)0x4c) return null;
			if(byteBuffer.get(6) != (byte)0x00) return null;
			if(byteBuffer.get(7) != (byte)0x02) return null;
			if(byteBuffer.get(8) != (byte)0x15) return null;
			
			byteBuffer.position(9);
			
		    	
	    	StringBuffer uuid = new StringBuffer();
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	uuid.append("-");
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	uuid.append("-");
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	uuid.append("-");
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	uuid.append("-");
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	appendByteStr(uuid, byteBuffer.get());
	    	
	    	Beacon newBeacon = new Beacon();
	        newBeacon.setUuid(uuid.toString());
	        newBeacon.setMajor((int)byteBuffer.getChar());
	        newBeacon.setMinor((int)byteBuffer.getChar());
	        newBeacon.setLastScanDate(new Date());
	        
	        newBeacon.setRssi(rssi);
	        //Log.d(TAG, newBeacon.toString());
	        
	        return newBeacon;
		}catch(Exception ex){
			Log.w(TAG, "Beacon UUIDの解析に失敗", ex);
		}
		
		return null;
	}
	
	/** StringBufferにByteの16進文字列を書き込む */
	private static void appendByteStr(StringBuffer sb, byte b){
		//Stringの生成が起きないこの方法が一番早いらしい
		sb.append(Character.forDigit(b >> 4 & 0xF, 16));
		sb.append(Character.forDigit(b & 0xF, 16));
	}
	
	@Override
	public String toString(){
		return this.getUuid() + "::" + String.valueOf(this.getMajor()) + "::" + String.valueOf(this.getMinor());
	}
}
