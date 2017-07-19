package com.autazcloud.pdv.ui.base;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.autazcloud.pdv.BuildConfig;
import com.autazcloud.pdv.R;
import com.autazcloud.pdv.data.local.PreferencesRepository;
import com.autazcloud.pdv.data.remote.service.ApiService;
import com.autazcloud.pdv.executor.receivers.SampleAlarmReceiver;
import com.autazcloud.pdv.helpers.FormatUtil;
import com.autazcloud.pdv.helpers.IDManagement;
import com.autazcloud.pdv.injection.NetworkModule;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

public class CustomApplication extends Application {

	private ApiService _apiService;
	private SampleAlarmReceiver _alarm = new SampleAlarmReceiver();


	public ApiService getApiService() {
		return this._apiService;
	}

	public void startAlarmReceiver() {
		_alarm.setAlarm(this);
	}
	public SampleAlarmReceiver getAlarmReceiver () {
		return this._alarm;
	}


	@Override
	public void onCreate() {
		super.onCreate();

		ButterKnife.setDebug(BuildConfig.DEBUG);

		IDManagement.init(this);
		FormatUtil.init(this);
		PreferencesRepository.init(this);

		RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
			@Override
			public void handleError(Throwable e) {
				super.handleError(e);
				Log.e("CustomApplication", "handleError: " + e.toString());
			}
		});

		Realm.init(this);
		// Enable full log output when debugging
		/*if (BuildConfig.DEBUG) {
			RealmLog.setLevel(Log.VERBOSE);
		}*/
		//RealmConfiguration config = new RealmConfiguration.Builder().build();
        RealmConfiguration config = new RealmConfiguration.Builder()
				//.name("otherrealm.realm")
				//.encryptionKey(key)
				.schemaVersion(2)
                .build();
		//Realm.deleteRealm(config); // Remover bases existentes
		Realm.setDefaultConfiguration(config);

		/*final Realm realm = Realm.getDefaultInstance();
		RealmChangeListener realmListener = new RealmChangeListener() {
			@Override
			public void onChange(Object o) {
				Log.i("SaleControllActivity","getSalesByStatus some values in the database have been changed");
			}
		};
		realm.addChangeListener(realmListener);*/

		NetworkModule adapter = new NetworkModule();
		_apiService = adapter.provideApiService();

		PreferenceManager.setDefaultValues(this, R.xml.pref_general, true);
		
		/*final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    final int cacheSize = maxMemory / 8;
	    Log.e("CustomApplication", "cacheSize: " + cacheSize);
	    mListSalesCache = new LruCache<String, SaleModel>(cacheSize) {};*/
	}
	
	@Override
    public void onLowMemory() {
		super.onLowMemory();
		Log.i("Script", "onLowMemory()");
		
		//BitmapAjaxCallback.clearCache();
    }
	
	@Override
    public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		/*Log.i("Script", "onTrimMemory("+level+")");
		Log.i("Script", "TRIM_MEMORY_RUNNING_MODERATE: " + TRIM_MEMORY_RUNNING_MODERATE);
		Log.i("Script", "TRIM_MEMORY_RUNNING_LOW: " + TRIM_MEMORY_RUNNING_LOW);
		Log.i("Script", "TRIM_MEMORY_RUNNING_CRITICAL: " + TRIM_MEMORY_RUNNING_CRITICAL);
		Log.i("Script", "TRIM_MEMORY_UI_HIDDEN: " + TRIM_MEMORY_UI_HIDDEN);
		Log.i("Script", "TRIM_MEMORY_BACKGROUND: " + TRIM_MEMORY_BACKGROUND);
		Log.i("Script", "TRIM_MEMORY_MODERATE: " + TRIM_MEMORY_MODERATE);
		Log.i("Script", "TRIM_MEMORY_COMPLETE: " + TRIM_MEMORY_COMPLETE);*/
		
		if (level >= 15) {

		}
    }
	
	public void callBack () {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
	}
}
