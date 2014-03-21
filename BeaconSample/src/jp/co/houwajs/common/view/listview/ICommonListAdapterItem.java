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
public interface ICommonListAdapterItem {
	Drawable getIcon();
	void setIcon(Drawable value);
	
	String getTitle();
	void setTitle(String value);
	
	String getSummary();
	void setSummary(String value);
	
	Object getToken();
	void setToken(Object value);
	
	int getColor();
}
