/**
 * Created by nihira on 2013/04/12.
 * Copyright (c) 2013- Houwa System Design. All rights reserved.
 */
package jp.co.houwajs.common.view.listview;

import android.graphics.drawable.Drawable;

/**
 * @author nihira
 *
 */
public class CommonListAdapterItem implements ICommonListAdapterItem {
	//private static final String TAG = CommonListAdapterItem.class.getName();
	
	private Drawable icon;
	@Override
	public Drawable getIcon(){return this.icon;}	
	@Override
	public void setIcon(Drawable value) {this.icon = value;}

	private String title;
	@Override
	public String getTitle(){return this.title;}
	@Override
	public void setTitle(String value) {this.title = value;}
	
	private String summary;
	@Override
	public String getSummary(){return this.summary;}
	@Override
	public void setSummary(String value) {this.summary = value;}
	
	private Object token;
	@Override
	public Object getToken() {return this.token;}
	@Override
	public void setToken(Object value) { this.token = value;}
}
