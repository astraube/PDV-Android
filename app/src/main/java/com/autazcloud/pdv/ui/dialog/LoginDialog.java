package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.data.local.PreferencesRepository;
import com.autazcloud.pdv.data.remote.subscribers.SubscriberInterface;
import com.autazcloud.pdv.domain.constants.AuthAttr;
import com.autazcloud.pdv.domain.interfaces.LoginInterface;

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

		String username = PreferencesRepository.getValue(AuthAttr.USERNAME);
		userView.setText(username);

		if (username != null && (!username.isEmpty() || username != ""))
			passView.requestFocus();
		
		layout.findViewById(R.id.btnLoginOk).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				String username = userView.getText().toString();
				String password = passView.getText().toString();

                if (!owner.onLogin(username, password)) {
					String msg = owner.getContext().getString(R.string.err_login_msg);

					((SubscriberInterface)owner).onSubscriberError(new Throwable(msg), owner.getContext().getString(R.string.err_login_title), msg);
				}
			}
		});
		layout.findViewById(R.id.btnLoginCalcel).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				owner.onLoginCancel();
				mDialog.dismiss();
			}
		});
		
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
