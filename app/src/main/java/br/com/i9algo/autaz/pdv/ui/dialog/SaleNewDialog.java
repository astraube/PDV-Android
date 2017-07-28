package br.com.i9algo.autaz.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.pierry.simpletoast.SimpleToast;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.domain.interfaces.SaleControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Client;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;

public class SaleNewDialog extends MaterialDialog {

	private SaleControllerInterface mOwner;
	
	public SaleNewDialog(final Activity activity, final SaleControllerInterface owner, final Sale sale) {
		super(new MaterialDialog.Builder(activity));
		mOwner = owner;

		boolean showAgainDialog = PreferencesRepository.isShowDialogNewSale();

		if (showAgainDialog) {
			mOwner.onAddNewSale(null, "", "");
			return;
		}

		this.builder.cancelable(true);
		this.builder.autoDismiss(false);
		this.builder.title(R.string.title_dialog_new_sale);
		this.builder.customView(R.layout.dialog_layout_sale_new, true);
		//this.builder.checkBoxPromptRes(R.string.action_yes_remove, showAgainDialog, null);
		this.builder.positiveText(R.string.action_ok);
		this.builder.negativeText(R.string.action_cancel);
		this.builder.show();

		View view = getCustomView();
		
		final EditText txtClientName = (EditText) view.findViewById(R.id.txtClientName);
		final EditText txtClientEmail = (EditText) view.findViewById(R.id.txtClientEmail);
		final EditText txtClientCpf = (EditText) view.findViewById(R.id.txtClientCpf);
		final EditText txtCodeSale = (EditText) view.findViewById(R.id.txtCodeSale);
		final EditText txtSellerCode = (EditText) view.findViewById(R.id.txtSellerCode);
		final CheckBox cbShowDialog = (CheckBox) view.findViewById(R.id.cbShowDialog);
		
		if (sale != null) {
			txtClientName.setText(sale.getClient().getName());
			txtClientEmail.setText(sale.getClient().getEmail());
			txtClientCpf.setText(sale.getClient().getCpfCnpj());

			txtCodeSale.setText(sale.getCodeControll());
			txtSellerCode.setText(sale.getSaller());
		}

		cbShowDialog.setChecked(showAgainDialog);
		cbShowDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (!b)
					SimpleToast.info(activity, activity.getString(R.string.txt_no_show_dialog_new_sale_alert));

				PreferencesRepository.setShowDialogNewSale(b);
			}
		});
		this.builder.onNegative(new MaterialDialog.SingleButtonCallback() {
			@Override
			public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
				dialog.dismiss();
			}
		});

		this.builder.onPositive(new MaterialDialog.SingleButtonCallback() {
			@Override
			public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
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
					sale.getClient().setName(txtClientName.getText().toString());
					sale.getClient().setEmail(txtClientEmail.getText().toString());
					sale.getClient().setCpfCnpj(txtClientCpf.getText().toString());
					sale.setCodeControll(txtCodeSale.getText().toString());
					sale.setSaller(txtSellerCode.getText().toString());
					mOwner.onChangeInfoSale(sale);
				}
				dialog.dismiss();
			}
		});
	}
}
