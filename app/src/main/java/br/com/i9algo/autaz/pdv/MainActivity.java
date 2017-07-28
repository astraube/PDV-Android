package br.com.i9algo.autaz.pdv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.data.remote.ResultDefault;
import br.com.i9algo.autaz.pdv.data.remote.repositoryes.AuthRepository;
import br.com.i9algo.autaz.pdv.data.remote.service.ApiService;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.constants.AuthAttr;
import br.com.i9algo.autaz.pdv.domain.interfaces.LoginInterface;
import br.com.i9algo.autaz.pdv.helpers.defaults.MainThreadBus;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;
import br.com.i9algo.autaz.pdv.ui.dialog.LoginDialog;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import br.com.i9algo.autaz.pdv.SaleControllActivity;
import br.com.i9algo.autaz.pdv.SalesGridActivity;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements LoginInterface, SubscriberInterface {

	private final String TAG;

	private int mLayoutSelected = 0;

	public static Keyboard mKeyboard;
	public static KeyboardView mKeyboardView;

	private LoginDialog loginDialog = null;
	private ApiService advService;

	private AuthRepository mAuthRepo;

	@Inject
	MainThreadBus bus;


	public MainActivity () {
		super();
		this.TAG = getClass().getSimpleName();
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fullScreen();
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		//presenter.bindView(this);
		//bus.register(this);

		advService = getApp().getApiService();

		this.mAuthRepo = new AuthRepository(this);
		
		/*
		String deviceId = Settings.System.getString(getContentResolver(), Secure.ANDROID_ID);
		DeviceUuidFactory device = new DeviceUuidFactory(this);
		Log.i("TAG","android.os.Build.SERIAL: " + deviceId);
		Log.i("TAG","device.getDeviceUuid(): " + device.getDeviceUuid());
		*/
		//Log.v(TAG, "onCreate");
	}

	@Override
	public void onStart() {
		super.onStart();
		//Log.v(TAG, "onStart");

		//((CustomApplication)getApplication()).isFirstAcccess()

		/*if (BuildConfig.DEBUG) {
			onLoginDebug();
			return;
		}*/

		if (PreferencesRepository.isValueEmpty(AuthAttr.USER_API_TOKEN) || PreferencesRepository.isValueEmpty(AuthAttr.ACCOUNT_PUBLIC_TOKEN)) {
			showLoginView();
		} else {
			initialize();
		}
	}
	
	public void initialize() {
		hideLoginView();
		hideDialog();

		mLayoutSelected = PreferencesRepository.getLayout();
		
		Intent intent = getIntent();
		
		switch (mLayoutSelected) {
		case 0 :
			intent = new Intent(MainActivity.this, SalesGridActivity.class);
			break;
		case 1 :
			intent = new Intent(MainActivity.this, SaleControllActivity.class); // TODO implementar abertura na tela de venda
			break;
		default:
			intent = new Intent(MainActivity.this, SalesGridActivity.class);
			break;
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void showLoginView() {
		((Activity)getContext()).runOnUiThread(new Runnable() {
			public void run() {
				loginDialog = new LoginDialog((MainActivity)getContext());
			}
		});
	}

	public void hideLoginView() {
		if (this.loginDialog != null)
			this.loginDialog.dismiss();
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public void onLogin(String username, String password) {
		if(TextUtils.isEmpty(username.trim()) || TextUtils.isEmpty(password.trim()))  {
			onSubscriberError(null, getContext().getString(R.string.err_oops), getContext().getString(R.string.txt_form_fill));
			return;
		}

		// Armazenar username em cache
		PreferencesRepository.setValue(AuthAttr.USERNAME, username);

		try {
			this.mAuthRepo.onLogin(username, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Apenas para debug
	 */
	private void onLoginDebug() {
		PreferencesRepository.setValue(AuthAttr.USER_NAME, "Andre Straube");
		PreferencesRepository.setValue(AuthAttr.USER_EMAIL, "a.straube.m@gmail.com");
		PreferencesRepository.setValue(AuthAttr.USER_API_TOKEN, "fc8425fdf0350941c9bff17a4b4e42bbaf45189df25b1142000c47f9e4752663");
		PreferencesRepository.setValue(AuthAttr.USER_PUBLIC_TOKEN, "19dc8dd3cfe1cf9a08624a74c2de19f2");

		PreferencesRepository.setValue(AuthAttr.ACCOUNT_CLIENT_ID, "865f7ac2b211e5f6b8b88a15c6f73ca28abb3c34a1e39fa38663398b983e174c");
		PreferencesRepository.setValue(AuthAttr.ACCOUNT_PUBLIC_TOKEN, "7c25b7f2be31e30bb96ed3097feec183");

		initialize();
	}

    @Override
    public void onLoginSuccess(ResultDefault object) {
		((Activity)getContext()).runOnUiThread(new Runnable() {
			public void run() {
				SimpleToast.info((MainActivity)getContext(), getContext().getString(R.string.txt_login_success), "{fa-user}");
			}
		});

		Log.i(TAG, "onLoginSuccess");
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(object.data);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        PreferencesRepository.setValue(AuthAttr.USER_NAME, jsonObject.get(AuthAttr.USER_NAME).getAsString());
        PreferencesRepository.setValue(AuthAttr.USER_EMAIL, jsonObject.get(AuthAttr.USER_EMAIL).getAsString());
        PreferencesRepository.setValue(AuthAttr.USER_API_TOKEN, jsonObject.get(AuthAttr.USER_API_TOKEN).getAsString());
        PreferencesRepository.setValue(AuthAttr.USER_PUBLIC_TOKEN, jsonObject.get(AuthAttr.PUBLIC_TOKEN).getAsString());

		JsonObject account = jsonObject.getAsJsonObject("account");
		PreferencesRepository.setValue(AuthAttr.ACCOUNT_CLIENT_ID, account.get(AuthAttr.ACCOUNT_CLIENT_ID).getAsString());
		PreferencesRepository.setValue(AuthAttr.ACCOUNT_PUBLIC_TOKEN, account.get(AuthAttr.PUBLIC_TOKEN).getAsString());

		initialize();
    }
	
	@Override
	public void onLoginCancel() {
		System.exit(0);
	}
	
	@Override
	public void onSignUp() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLogout() {
		// TODO Auto-generated method stub
	}
}
