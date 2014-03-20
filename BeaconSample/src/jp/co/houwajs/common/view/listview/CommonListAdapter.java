/**
 * Created by nihira on 2013/04/12.
 * Copyright (c) 2013- Houwa System Design. All rights reserved.
 */
package jp.co.houwajs.common.view.listview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author nihira
 *
 */
public class CommonListAdapter extends BaseAdapter {
	//private static final String TAG = CommonListAdapter.class.getName();

	protected List<ICommonListAdapterItem> itemList = null;
	protected Context context;
	
	protected int titleTextStyle = android.R.attr.textAppearanceMedium;
	/** TitleのTextViewに設定するStyle (default:android.R.attr.textAppearanceMedium) */
	public int getTitleTextStyle(){return this.titleTextStyle;}
	/** TitleのTextViewに設定するStyle (default:android.R.attr.textAppearanceMedium) */
	public void setTitleTextStyle(int value){this.titleTextStyle = value;}
	
	protected int iconWidth = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
	/** iconに設定するWidth (default : android.view.ViewGroup.LayoutParams.WRAP_CONTENT) */
	public int getIconWidth(){return this.iconWidth;}
	/** iconに設定するWidth (default : android.view.ViewGroup.LayoutParams.WRAP_CONTENT) */
	public void setIconWidth(int value){this.iconWidth = value;}
	
	/** コンストラクタ */
	public CommonListAdapter(Context context){
		this.context = context;
		this.itemList = new ArrayList<ICommonListAdapterItem>();
	}
	
	/** 要素の追加 */
	public void addItem(String title){
		this.addItem(null, title, null, null);
	}
	/** 要素の追加 */
	public void addItem(Drawable icon, String title){
		this.addItem(icon, title, null, null);
	}
	/** 要素の追加 */
	public void addItem(Drawable icon, String title, String summary){
		this.addItem(icon, title, summary, null);
	}
	
	/** 要素の追加 */
	public void addItem(Drawable icon, String title, String summary, Object token){
		CommonListAdapterItem item = new CommonListAdapterItem();
		item.setIcon(icon);
		item.setTitle(title);
		item.setSummary(summary);
		item.setToken(token);
		
		this.addItem(item);
	}
	/** 要素の追加 */
	public void addItem(ICommonListAdapterItem item){
		this.itemList.add(item);
	}
	
	/** 要素のクリア */
	public void clearItem(){
		this.itemList.clear();
	}
	
	public void removeItem(int position){
		this.itemList.remove(position);
	}
	public void removeItem(ICommonListAdapterItem item){
		this.itemList.remove(item);
	}
	
	
	public ICommonListAdapterItem getItemAt(int position){
		return (ICommonListAdapterItem)this.getItem(position);
	}
	
	@Override
	public int getCount(){
		if(this.itemList != null){
			return this.itemList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(this.itemList != null && this.itemList.size() > position){
			return this.itemList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ICommonListAdapterItem item = (ICommonListAdapterItem)this.getItem(position);
		if(item == null) return null;
		
		View returnValue = convertView;
		if(returnValue == null){
			LinearLayout baseView = new LinearLayout(this.context);
			
			baseView.setOrientation(LinearLayout.HORIZONTAL);
			baseView.setGravity(Gravity.CENTER_VERTICAL);
			baseView.setPadding(5, 0, 5, 0);
			
			if(item.getIcon() != null){
				ImageView iconView = new ImageView(this.context);
				iconView.setLayoutParams(new LayoutParams(this.getIconWidth(), LayoutParams.WRAP_CONTENT));
				iconView.setScaleType(ScaleType.FIT_CENTER);
				iconView.setAdjustViewBounds(true);
				int paddingSize = (int)convertDpToPixel(this.context, 3);
				iconView.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
				
				iconView.setImageDrawable(item.getIcon());
				iconView.setTag("CommonListAdapterItemViewIcon");
				baseView.addView(iconView);
			}
			
			LinearLayout textLayout = new LinearLayout(this.context);
			textLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			textLayout.setOrientation(LinearLayout.VERTICAL);
			
			TextView textView = new TextView(this.context, null, android.R.attr.textAppearanceMedium);
			textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			textView.setText(item.getTitle());
			textView.setTag("CommonListAdapterItemViewText");
			textLayout.addView(textView);
			
			TextView summaryTextView = new TextView(this.context, null, android.R.attr.textAppearanceSmall);
			summaryTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			if(item.getSummary() == null || item.getSummary().length() == 0){
				summaryTextView.setVisibility(View.GONE);
				summaryTextView.setText(null);
			}else{
				summaryTextView.setVisibility(View.VISIBLE);
				summaryTextView.setText(item.getSummary());
			}
			summaryTextView.setTag("CommonListAdapterItemViewSummaryText");
			textLayout.addView(summaryTextView);
			
			baseView.addView(textLayout);
			
			returnValue = baseView;
		}else{
			//icon
			if(item.getIcon() != null){
				ImageView iconView = (ImageView)returnValue.findViewWithTag("CommonListAdapterItemViewIcon");
				iconView.setImageDrawable(item.getIcon());
			}
		
			//Title
			TextView txtTitle = (TextView)returnValue.findViewWithTag("CommonListAdapterItemViewText");
			txtTitle.setText(item.getTitle());
			
			//Summary
			TextView summaryTextView = (TextView)returnValue.findViewWithTag("CommonListAdapterItemViewSummaryText");
			if(item.getSummary() == null || item.getSummary().length() == 0){
				summaryTextView.setVisibility(View.GONE);
				summaryTextView.setText(null);
			}else{
				summaryTextView.setVisibility(View.VISIBLE);
				summaryTextView.setText(item.getSummary());
			}
		}
						
		return returnValue;
	}
	
	/***
	 * dp(dip)からpixelへの変換を行います
	 * @param context
	 * @param value
	 * @return
	 */
	private static float convertDpToPixel(Context context, float value){
		return context.getResources().getDisplayMetrics().density * value;
	}
}
