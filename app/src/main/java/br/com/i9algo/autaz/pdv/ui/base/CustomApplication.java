package br.com.i9algo.autaz.pdv.ui.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import br.com.i9algo.autaz.pdv.BuildConfig;
import br.com.i9algo.autaz.pdv.MainActivity;
import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.data.remote.service.ApiService;
import br.com.i9algo.autaz.pdv.domain.models.Account;
import br.com.i9algo.autaz.pdv.domain.models.Corporate;
import br.com.i9algo.autaz.pdv.domain.models.User;
import br.com.i9algo.autaz.pdv.executor.receivers.SampleAlarmReceiver;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import br.com.i9algo.autaz.pdv.helpers.IDManagement;
import br.com.i9algo.autaz.pdv.injection.NetworkModule;

import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

public class CustomApplication extends Application {

	private ApiService _apiService;
	private SampleAlarmReceiver _alarm = new SampleAlarmReceiver();

	//private Account _sessionAccount = null;
	//private User _sessionUser = null;
	//private Corporate _sessionCorporate = null;


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


		/**
		 * Fabric Crashlytics
		 */
		Crashlytics crashlyticsKit = new Crashlytics.Builder()
				//.core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
				.core(new CrashlyticsCore.Builder().build())
				.build();
		Fabric.with(this, crashlyticsKit, new Answers());


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

	public void forceCrash(String msg) {
		throw new RuntimeException(msg);
	}

	private void RestartApp() {
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);

		System.exit(1);
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
