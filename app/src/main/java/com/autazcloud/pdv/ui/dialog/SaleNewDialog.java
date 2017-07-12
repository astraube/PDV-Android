package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.interfaces.SaleControllerInterface;
import com.autazcloud.pdv.domain.models.Client;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.data.local.PreferencesRepository;

public class SaleNewDialog extends AlertDialog.Builder {
	
	private AlertDialog mDialog;
	private SaleControllerInterface mOwner;
	
	public SaleNewDialog(final Activity activity, final SaleControllerInterface owner, final SaleModel sale) {
		super(activity);
		mOwner = owner;
		
		if (PreferencesRepository.isShowDialogNewSale()) {
			mOwner.onAddNewSale(null, "", "");
			return;
		}
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.dialog_layout_sale_new, null);
		
		final EditText txtClientName = (EditText) layout.findViewById(R.id.txtClientName);
		final EditText txtClientEmail = (EditText) layout.findViewById(R.id.txtClientEmail);
		final EditText txtClientCpf = (EditText) layout.findViewById(R.id.txtClientCpf);
		final EditText txtCodeSale = (EditText) layout.findViewById(R.id.txtCodeSale);
		final EditText txtSellerCode = (EditText) layout.findViewById(R.id.txtSellerCode);
		final CheckBox cbShowDialog = (CheckBox) layout.findViewById(R.id.cbShowDialog);
		
		if (sale != null) {
			txtClientName.setText(sale.getClientName());
			txtClientEmail.setText(sale.getClientEmail());
			txtClientCpf.setText(sale.getClientCpf());

			txtCodeSale.setText(sale.getCodeControll());
			txtSellerCode.setText(sale.getSaller());
		}
		
		cbShowDialog.setChecked(PreferencesRepository.isShowDialogNewSale());
		cbShowDialog.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferencesRepository.setShowDialogNewSale(!isChecked);
			}
		});
		
		layout.findViewById(R.id.btnOk).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				
				if (sale == null) {
					Client client = new Client();
					client.setName(txtClientName.getText().toString());
					client.setEmail(txtClientEmail.getText().toString());
					client.setCpfCnpj(txtClientCpf.getText().toString());

					mOwner.onAddNewSale(
							client,
							txtCodeSale.getText().toString(),
							txtSellerCode.getText().toString());
				} else {
					sale.setClientName(txtClientName.getText().toString());
					sale.setClientEmail(txtClientEmail.getText().toString());
					sale.setClientCpf(txtClientCpf.getText().toString());
					sale.setCodeControll(txtCodeSale.getText().toString());
					sale.setSaller(txtSellerCode.getText().toString());
					mOwner.onChangeInfoSale(sale);
				}
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
        setTitle(R.string.title_dialog_new_sale);
        setCancelable(true);
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter;
        this.mDialog.show();
	}
}
