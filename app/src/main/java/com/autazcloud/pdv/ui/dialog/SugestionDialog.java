package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.enums.ContactTypes;
import com.autazcloud.pdv.domain.models.Contact;

public class SugestionDialog extends AlertDialog.Builder {
	
	private Activity mActivity;
	private AlertDialog mDialog;

	public SugestionDialog(final Activity activity) {
		super(activity);
		mActivity = activity;
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.dialog_layout_sugestion, null);
		
		final EditText txSubject = (EditText) layout.findViewById(R.id.txSubject);
		final EditText txText = (EditText) layout.findViewById(R.id.txText);
		
		layout.findViewById(R.id.btSend).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				
				Editable subject = txSubject.getText();
				Editable text = txText.getText();
				
				if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(text)) {
					Contact c = new Contact();
					c.setSubject(subject.toString());
					c.setBody(text.toString());
					c.setType(ContactTypes.SUGGESTION);
					c.setUserToken("sdfkjh786t"); // TODO inserir token do usuario
					// TODO enviar sugestao para servidor
					return;
				}
				
				new AlertDialog.Builder(mActivity)
		          .setTitle(getContext().getString(R.string.txt_form_incomplete))
		          .setMessage(getContext().getString(R.string.txt_form_fill))
		          .setCancelable(false)
		          .setPositiveButton(getContext().getString(R.string.action_ok), new OnClickListener() {
		              @Override
		              public void onClick(DialogInterface dialog, int which) {
		            	  dialog.dismiss();
		              }
		          }).create().show();  
			}
		});
		
		layout.findViewById(R.id.btnCancel).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		
		setIcon(R.mipmap.ic_launcher);
        setView(layout);
        setTitle(R.string.title_dialog_suggestion);
        setCancelable(true);
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter;
        this.mDialog.show();
	}
}