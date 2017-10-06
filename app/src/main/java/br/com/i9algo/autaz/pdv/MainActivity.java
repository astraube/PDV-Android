package br.com.i9algo.autaz.pdv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import br.com.i9algo.autaz.pdv.data.local.AccountRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.DeviceRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.data.local.SalesRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.UserRealmRepository;
import br.com.i9algo.autaz.pdv.data.remote.repositoryes.DeviceRepository;
import br.com.i9algo.autaz.pdv.domain.models.Account;
import br.com.i9algo.autaz.pdv.data.remote.repositoryes.AuthRepository;
import br.com.i9algo.autaz.pdv.data.remote.service.ApiService;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.interfaces.LoginInterface;
import br.com.i9algo.autaz.pdv.domain.models.Device;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.domain.models.User;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ResultStatusDefault;
import br.com.i9algo.autaz.pdv.domain.models.inbound.UserWrapper;
import br.com.i9algo.autaz.pdv.helpers.defaults.MainThreadBus;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;
import br.com.i9algo.autaz.pdv.ui.dialog.LoginDialog;

import com.crashlytics.android.Crashlytics;
import com.github.pierry.simpletoast.SimpleToast;
import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

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
        startMixPanelApi(this);
		fullScreen();
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		advService = getApp().getApiService();

		this.mAuthRepo = new AuthRepository(this);

		// TODO verificar atualizacao por enquanto no servidor
		//new UpdateRunnable(this, new Handler()).start();


		/*
		GPSTracker gps = GPSTracker.getInstance(mContext);
		// checa se o GPS esta habilitado
		if(gps.canGetLocation()){
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			infos.setLatitude(latitude);
			infos.setLongitude(longitude);
		}*/
	}

	@Override
	public void onStart() {
		super.onStart();
		//Log.v(TAG, "onStart");

		User model = UserRealmRepository.getFirst();
		if (model != null && !StringUtils.isEmpty(model.getPublicToken()) && !StringUtils.isEmpty(model.getApiToken())) {
			verifyCredentials();
		} else {
			showLoginView();
		}
	}

	private void verifyCredentials() {
		// API WEB - Repository "Auth"
		this.mAuthRepo = new AuthRepository(this);

		// API WEB - Verifica as credenciais do usuario
		this.mAuthRepo.onValidateCredentialsUser(this);
	}

	public void initialize() {
        User user = UserRealmRepository.getFirst();
        Account account = AccountRealmRepository.getFirst();

		// Cadastra Device
		/* TODO - desabilidade temporariamente, esta com porblemas para editar no API WEB
		DeviceRepository dRepo = new DeviceRepository(this);
		try {
			Device d = DeviceRealmRepository.getFirst();
			if (d == null || StringUtils.isEmpty(d.getPublicToken()))
                d = new Device(this);

			dRepo.onDeviceStore(d);

		} catch (Exception e) {
			e.printStackTrace();
		}*/

        //String idUniq = IDManagement.getDeviceUuid().toString(); // TODO - enviar UUID para API WEB

		/**
		 * Crashlytics
		 */
		Crashlytics.setUserIdentifier(user.getPublicToken());
		Crashlytics.setUserEmail(user.getEmail());
		Crashlytics.setUserName(user.getName());

		/****************************************************************
		 * Mixpanel
		 ****************************************************************/
		final MixpanelAPI.People people = getMixpanel().getPeople();

		people.set("$first_name", user.getName());
		people.set("$last_name", "");
		people.set("$email", user.getEmail());
		people.set("$created", user.getCreatedAt());
		people.set("$last_login", new Date()); // Ultimo login

		if (account != null && account.getSignaturePlan() != null) {
			people.set("expire_at", account.getSignaturePlan().getExpireAt());
		}
        //people.set("credits", name);
        //people.set("gender", "Male");
        //people.increment("Update Count", 1L);// Acompanhar quantas vezes o usuario atualizou seu perfil

        try {
            final JSONObject domainProperty = new JSONObject();
            domainProperty.put("user domain", domainFromEmailAddress(user.getEmail()));
            domainProperty.put("user_public_token", user.getPublicToken());
            getMixpanel().registerSuperProperties(domainProperty);

        } catch (JSONException e) {
            throw new RuntimeException("Could not encode hour first viewed as JSON");
        }
        // New Line Activity Feed
		getMixpanel().track("sessao iniciada", null);

		/****************************************************************
		 * End Mixpanel
		 ****************************************************************/

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

		try {
			this.mAuthRepo.onLogin(username, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLoginSuccess(UserWrapper object) {
		Log.i(TAG, "API WEB - onLoginSuccess - UserWrapper");

		// Adicionar o token do usuario como DistinctId no mixpanel
		setMixpanelTrackingDistinctId(object.getModel().getPublicToken());

		((Activity)getContext()).runOnUiThread(new Runnable() {
			public void run() {
				SimpleToast.info((MainActivity)getContext(), getContext().getString(R.string.txt_login_success), "{fa-user}");
			}
		});

		initialize();
	}

	@Override
	public void onLoginError(ResultStatusDefault resultStatus) {
		Log.e(TAG, "API WEB - onLoginError - UserWrapper");

		showLoginView();
		onSubscriberError(resultStatus.error, resultStatus.title, resultStatus.message);
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
