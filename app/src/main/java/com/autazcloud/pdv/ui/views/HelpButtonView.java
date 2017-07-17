package com.autazcloud.pdv.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.data.local.PreferencesRepository;
import com.autazcloud.pdv.domain.interfaces.HelpButtonInterface;
import com.autazcloud.pdv.executor.receivers.HelpReceiver;

public class HelpButtonView extends ImageTextView implements View.OnClickListener, HelpButtonInterface {

	private static HelpButtonView instance = null;
	private OnClickListener mOwner = null;
    
	public static final void onStart() {
		if (HelpButtonView.instance == null)
			return;
		
		HelpButtonView.instance.onStartHelp();
	}

	public static final void onStop() {
		if (HelpButtonView.instance == null)
			return;
		
		HelpButtonView.instance.onStopHelp();
	}
	
	public HelpButtonView(Context context, int sizeImage) {
		super(context, sizeImage);
		HelpButtonView.instance = this;
		instanciate();
	}
	
	public HelpButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		HelpButtonView.instance = this;
		instanciate();
	}
	
	private void instanciate() {
		//alarm = new AlarmReceiver();
		
		LayoutParams lp = new LayoutParams(100, LayoutParams.WRAP_CONTENT);
		//lp.setMargins(10, 10, 10, 10);
		setLayoutParams(lp);
		
		onChangeImage();
		setText(R.string.float_menu_item_help);
		setTag(R.string.float_menu_item_help);
		setOnClickListener(this);
	}
	
	public void setOwnerClickListener(OnClickListener owner) {
		this.mOwner = owner;
	}
	
	private void start() {
		onStartHelp();
		HelpReceiver.startHelp(getContext());
	}
	@Override
	public void onStartHelp() {
		this.setImageResource(R.drawable.ic_ladrao_on);
		PreferencesRepository.setHelpButton(true);
	}
	
	private void stop() {
		onStopHelp();
		HelpReceiver.stopHelp(getContext());
	}
	@Override
	public void onStopHelp() {
		this.setImageResource(R.drawable.ic_ladrao_off);
		PreferencesRepository.setHelpButton(false);
	}
	
	@Override
	public void onChangeImage() {
		int image = (PreferencesRepository.isHelpButtonON()) ? R.drawable.ic_ladrao_on : R.drawable.ic_ladrao_off;
		this.setImageResource(image);
	}
	
	@Override
	public void onClick(View v) {
		boolean oldState = PreferencesRepository.isHelpButtonON();
		boolean newState = !oldState;
		
		if (newState) {
			start();
		} else {
			stop();
		}
		
		if (this.mOwner != null)
			this.mOwner.onClick(v);
	}
}
