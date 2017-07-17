package com.autazcloud.pdv.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.data.remote.service.ApiService;
import com.autazcloud.pdv.data.remote.subscribers.SubscriberInterface;
import com.autazcloud.pdv.ui.views.BaseViewInterface;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class BaseActivity extends Activity implements BaseViewInterface, SubscriberInterface {
	
	private final String TAG;
	private CustomApplication app;
	private SweetAlertDialog sweetDialog = null;

	public BaseActivity () {
		this.TAG = getClass().getSimpleName();
	}
	
	public CustomApplication getApp() {
		return (this.app);
	}

	public void fullScreen() {
		Window w = getWindow();
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	w.setFlags(	WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		this.app = ((CustomApplication) getApplicationContext());
	}
	
	@Override
    protected void onStart() {
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onStart();
	}
	
	@Override
    protected void onPause() {
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onPause();
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public SweetAlertDialog getSweetDialog() {
		return this.sweetDialog;
	}

	public void setSweetDialog(SweetAlertDialog dialog) {
		hideDialog();

		this.sweetDialog = dialog;

		if (this.sweetDialog != null)
			this.sweetDialog.show();
	}

	public void hideDialog() {
		if (this.sweetDialog != null) {
			this.sweetDialog.hide();
			this.sweetDialog.dismiss();
			this.sweetDialog.cancel();
			this.sweetDialog = null;
		}
	}


	@Override
	public ApiService getApiService() {
		return getApp().getApiService();
	}


	@Override
	public void onSubscriberCompleted() {
		hideDialog();
	}

	@Override
	public void onSubscriberError(Throwable e, final String title, final String msg) {
		/*if (e != null) {
			Log.e(TAG, "onSubscriberError - " + e.getMessage());
		}*/
		final String _title;
		final String _msg;

		if (!title.isEmpty() && !msg.isEmpty()) {
			_title = getContext().getString(R.string.err_oops);
			_msg = getContext().getString(R.string.err_try_again);
		} else {
			_title = title;
			_msg = msg;
		}
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					SweetAlertDialog d = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
							.setTitleText(title)
							.setContentText(msg)
							.setConfirmText(getContext().getString(R.string.action_try_again));

					setSweetDialog(d);
				}
			});

		} catch (Exception ex) {
			hideDialog();
			ex.printStackTrace();
		}
	}

	@Override
	public void onSubscriberNext(Object t) {
		hideDialog();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window w = getWindow();
		w.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		w.setFlags(	WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
    	Log.v("BaseActivity", "onWindowFocusChanged - " + hasFocus);
	}
}
