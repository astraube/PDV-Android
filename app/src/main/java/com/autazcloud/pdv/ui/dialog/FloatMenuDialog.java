package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.autazcloud.pdv.LayoutActivity;
import com.autazcloud.pdv.PreferenceActivity;
import com.autazcloud.pdv.ProductsActivity;
import com.autazcloud.pdv.R;
import com.autazcloud.pdv.SalesActivity;
import com.autazcloud.pdv.controllers.printer2.DiscoveryActivity;
import com.autazcloud.pdv.ui.views.ImageTextView;

public class FloatMenuDialog extends Dialog implements View.OnClickListener {
	
	private final String TAG;
	
	public Activity mActivity;
	private View mTrigger; //Bot�o que disparou o evento de exibir o dialog

	public FloatMenuDialog(Activity activity, View trigger) {
		super(activity, R.style.ConfigAlertDialog);
		this.mActivity = activity;
		this.mTrigger = trigger;
		this.TAG = getClass().getSimpleName();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_layout_menu_adjustments);
		
		getWindow().getAttributes().windowAnimations = R.style.DialogAnimationLeft; 
		createButtons();
	}

	@Override
	public void onClick(View v) {
		Log.v(TAG, "Clicked: " + v.getTag());

		String tag = v.getTag().toString();
		Intent intent = null;
		
		if (tag.equals(mActivity.getString(R.string.float_menu_item_adjustments)) || tag.equals(mActivity.getString(R.string.float_menu_item_style))) {
			// Ajustes
			intent = new Intent(mActivity, PreferenceActivity.class);
			
		} else if (tag.equals(mActivity.getString(R.string.float_menu_item_layout))) {
			// Layout
			intent = new Intent(mActivity, LayoutActivity.class);
			
		} else if (tag.equals(mActivity.getString(R.string.float_menu_item_products))) {
			// Produtos
			intent = new Intent(mActivity, ProductsActivity.class);
			
		} else if (tag.equals(mActivity.getString(R.string.float_menu_item_sales))) {
			// Vendas Sync
			intent = new Intent(mActivity, SalesActivity.class);
			
		} else if (tag.equals(mActivity.getString(R.string.float_menu_item_suggestion))) {
			// Sugest�es
			new SugestionDialog(mActivity);
			
		} else if (tag.equals(mActivity.getString(R.string.float_menu_item_calc))) {
			// Calculadora
			//ComponentName cn = new ComponentName("br.com.i9algo", "br.com.i9algo.calc");
			//ComponentName cn = new ComponentName("com.android", "com.android.calculator2");
			//intent = new Intent();
			//intent.setComponent(cn);
			
			intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(new ComponentName("com.android.calculator2", "com.android.calculator2.Calculator"));
			
		} else if (tag.equals(mActivity.getString(R.string.float_menu_item_printer))) {
			// Assistende da impressora
			intent = new Intent(mActivity, DiscoveryActivity.class);
			mActivity.startActivityForResult(intent, 0);
			return;
		}
		
		
		if (intent != null) {
			try {
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mActivity.startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Log.e(TAG, "Aplicativo calculadora n�o existe");
				e.printStackTrace();
			}
		}
		dismiss();
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		
		if (mTrigger != null)
			mTrigger.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void cancel() {
		super.cancel();
		
		if (mTrigger != null)
			mTrigger.setVisibility(View.VISIBLE);
	}
	
	//private static List<ImageTextView> BUTTONS = null;
	
	private void createButtons() {
		LinearLayout viewConfig = (LinearLayout)findViewById(R.id.viewConfig);
		String [] menuItems = getContext().getResources().getStringArray(R.array.arr_items_float_menu);
		String[] menuItemsIcon = getContext().getResources().getStringArray(R.array.arr_icons_float_menu);
		
		/*
		// TODO
		// Help Buttom
		HelpButtonView helpBtn = new HelpButtonView(getContext(), 70);
		helpBtn.setOwnerClickListener(this);
		viewConfig.addView(helpBtn);
		*/
		
		for (int i = 0; i < menuItems.length; i++) {
			ImageTextView btn;
			String text;
			int id_icon;
			
			text = getContext().getString(getContext().getResources().getIdentifier(menuItems[i], "string", getContext().getPackageName()));
			id_icon = getContext().getResources().getIdentifier(menuItemsIcon[i], "drawable", getContext().getPackageName());
			btn = new ImageTextView(getContext(), 70);
			
			LayoutParams lp = new LayoutParams(100, LayoutParams.WRAP_CONTENT);
			lp.setMargins(10, 10, 10, 10);
			btn.setLayoutParams(lp);
			btn.setImageResource(id_icon);
			btn.setText(text);
			btn.setOnClickListener(this);
			btn.setId(i);
			btn.setTag(text);
			
			viewConfig.addView(btn);
		}
		
		/*for (int i = 0; i < BUTTONS.size(); i++) {
			ImageTextView b = BUTTONS.get(i);
			b.setId(i);
			viewConfig.addView(b);
		}*/
		
		if (mTrigger != null)
			mTrigger.setVisibility(View.GONE);
	}
}