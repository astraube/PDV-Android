package br.com.i9algo.autaz.pdv.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Random;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.controllers.printer2.PrinterEpson;
import br.com.i9algo.autaz.pdv.data.remote.service.ApiService;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.constants.Constants;
import br.com.i9algo.autaz.pdv.helpers.Logger;
import br.com.i9algo.autaz.pdv.ui.components.BaseViewInterface;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class BaseActivity extends Activity implements BaseViewInterface, SubscriberInterface {


	private final String LOG_TAG = getClass().getSimpleName();
	private CustomApplication app;
	private SweetAlertDialog sweetDialog = null;

	private MixpanelAPI mMixpanel = null;
	private static final String MIXPANEL_DISTINCT_ID_NAME = "Mixpanel Example $distinctid";


	public CustomApplication getApp() {
		return (this.app);
	}


	@Override
	protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			if (requestCode == Constants.REQ_COD_DISCOVERY_PRINTER) {
				Logger.v(LOG_TAG, "-----> data: " + data.getDataString());

				String target = data.getStringExtra(PrinterEpson.TARGET);
				if (target != null) {
					PrinterEpson.PRINTER_TARGET = target;
				}
			}
		}
	}

	/****************************************************************
	 * Mixpanel
	****************************************************************/
	public MixpanelAPI getMixpanel() {
		return (this.mMixpanel);
	}
	public void startMixPanelApi(Context context) {
		/**
		 * - Mixpanel identify = UUID device + token usuario
		 * - Quando o User logar vai criar um ID unico e cria um perfil no MixPanel
		 * - Se o User logar em outro Device, criará um novo perfil no MixPanel, com um ID novo
		 * - Se outro usuario se logar no mesmo Device, criar um ID unico e cria um perfil no MixPanel
		 */
		final String trackingDistinctId = getMixpanelTrackingDistinctId();


		// Initialize the Mixpanel library for tracking and push notifications.
		mMixpanel = MixpanelAPI.getInstance(context, Constants.TOKEN_MIX_PANEL);

		if (!trackingDistinctId.isEmpty()) {
			// We also identify the current user with a distinct ID, and
			// register ourselves for push notifications from Mixpanel.
			mMixpanel.identify(trackingDistinctId); //this is the distinct_id value that
			mMixpanel.getPeople().identify(trackingDistinctId);
			mMixpanel.getPeople().initPushHandling(Constants.GOOGLE_NOTIFICATION_SENDER_ID);
		}
	}
	public void setMixpanelTrackingDistinctId(@NonNull String trackingID) {
		final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

		String currentID = prefs.getString(MIXPANEL_DISTINCT_ID_NAME, null);

		if (!currentID.equals(trackingID)) {
			final SharedPreferences.Editor prefsEditor = prefs.edit();
			prefsEditor.putString(MIXPANEL_DISTINCT_ID_NAME, trackingID);
			prefsEditor.commit();

			startMixPanelApi(this);
		}
	}
	public String getMixpanelTrackingDistinctId() {
		final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

		String ret = prefs.getString(MIXPANEL_DISTINCT_ID_NAME, null);
		if (ret == null) {
			ret = generateDistinctId();
			final SharedPreferences.Editor prefsEditor = prefs.edit();
			prefsEditor.putString(MIXPANEL_DISTINCT_ID_NAME, ret);
			prefsEditor.commit();
		}

		return ret;
	}
	// These disinct ids are here for the purposes of illustration.
	// In practice, there are great advantages to using distinct ids that
	// are easily associated with user identity, either from server-side
	// sources, or user logins. A common best practice is to maintain a field
	// in your users table to store mixpanel distinct_id, so it is easily
	// accesible for use in attributing cross platform or server side events.
	private String generateDistinctId() {
		final Random random = new Random();
		final byte[] randomBytes = new byte[32];
		random.nextBytes(randomBytes);
		return Base64.encodeToString(randomBytes, Base64.NO_WRAP | Base64.NO_PADDING);
	}
	public void recordRevenue() {
		final MixpanelAPI.People people = getMixpanel().getPeople();
		// Total Revenue
		people.trackCharge(1.50, null);
	}
	public String domainFromEmailAddress(String email) {
		String ret = "";
		final int atSymbolIndex = email.indexOf('@');
		if ((atSymbolIndex > -1) && (email.length() > atSymbolIndex)) {
			ret = email.substring(atSymbolIndex + 1);
		}
		return ret;
	}
	/****************************************************************
	 * End Mixpanel
	 ****************************************************************/


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
	protected void onResume() {
		super.onResume();

		// If you have surveys or notifications, and you have set AutoShowMixpanelUpdates set to false,
		// the onResume function is a good place to call the functions to display surveys or
		// in app notifications. It is safe to call both these methods right after each other,
		// since they do nothing if a notification or survey is already showing.
		mMixpanel.getPeople().showNotificationIfAvailable(this);
	}

	@Override
	protected void onDestroy() {
		mMixpanel.flush();
		super.onDestroy();
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

	/**
	 * Method for showing the Keyboard
	 * @param context The context of the activity
	 * @param editText The edit text for which we want to show the keyboard
	 */
	public void showTheKeyboard(Context context, SearchView editText){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * Method for showing the Keyboard when a QWERTY (physical keyboard is enabled)
	 * @param context The context of the activity
	 * @param editText The edit text for which we want to show the keyboard
	 */
	public void showTheKeyboardWhenQWERTY(Context context, SearchView editText){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, 0);
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

	public void setSweetProgress(String message) {
		this.setSweetProgress(message, getContext().getString(R.string.please_wait));
	}

	public void setSweetProgress(String message, String title) {
		try {
			SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
			pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
			pDialog.setTitleText(title);
			pDialog.setTitle(title);
			pDialog.setContentText(message);
			pDialog.setCancelable(false);
			setSweetDialog(pDialog);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void hideDialog() {
		try {
			if (this.sweetDialog != null) {
				this.sweetDialog.hide();
				this.sweetDialog.dismiss();
				this.sweetDialog.cancel();
				this.sweetDialog = null;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
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
			Log.e(LOG_TAG, "onSubscriberError - " + e.getMessage());
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

					//d.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
					d.setTitle(title);
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
