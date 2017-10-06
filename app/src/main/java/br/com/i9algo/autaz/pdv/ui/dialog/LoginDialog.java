package br.com.i9algo.autaz.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import br.com.i9algo.autaz.pdv.BuildConfig;
import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.constants.AuthAttr;
import br.com.i9algo.autaz.pdv.domain.interfaces.LoginInterface;

import butterknife.ButterKnife;

public class LoginDialog extends AlertDialog.Builder {
	
	private AlertDialog mDialog;

	public AlertDialog getDialog() {
		return mDialog;
	}

	public void dismiss() {
		this.mDialog.dismiss();
	}
	
	public LoginDialog(final LoginInterface owner) {
		super((Activity)owner.getContext());
		ButterKnife.bind((Activity)owner.getContext());

		final Activity activity = (Activity)owner.getContext();

		final LinearLayout layout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.dialog_layout_login, null);

		final EditText userView =  ButterKnife.findById(layout, R.id.txtUsername);
		final EditText passView =  ButterKnife.findById(layout, R.id.txtPassword);

		// Armazenar username em cache para exibir na tela de login posteriormente
		String username = PreferencesRepository.getValue(AuthAttr.USERNAME);
		userView.setText(username);

		if (username != null && (!username.isEmpty() || username != ""))
			passView.requestFocus();
		
		layout.findViewById(R.id.btnLoginOk).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				String username = userView.getText().toString();
				String password = passView.getText().toString();

                owner.onLogin(username, password);
			}
		});
		layout.findViewById(R.id.btnLoginCalcel).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				owner.onLoginCancel();
				mDialog.dismiss();
			}
		});

		// TODO - teste
		if (BuildConfig.DEBUG) {
			userView.setText("a.straube.m@gmail.com");
			passView.setText("123456");
		}
		
		setIcon(R.drawable.ic_action_accounts);
        setView(layout);
        setTitle(R.string.txt_login);
        setCancelable(false);
        setOnCancelListener(new AlertDialog.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				owner.onLoginCancel();
				mDialog.dismiss();
			}
		});
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter; 
        this.mDialog.show();
	}
}
