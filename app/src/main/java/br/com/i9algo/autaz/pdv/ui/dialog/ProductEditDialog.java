package br.com.i9algo.autaz.pdv.ui.dialog;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.interfaces.ProductControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;

import java.text.ParseException;
import java.util.Date;

import faranjit.currency.edittext.CurrencyEditText;

public class ProductEditDialog extends MaterialDialog {
	
	private ProductControllerInterface mController;
	
	public ProductEditDialog(final ProductControllerInterface controller) {
		this(controller, null);
	}
	public ProductEditDialog(final ProductControllerInterface controller, final Product product) {
		super(new MaterialDialog.Builder((BaseActivity) controller.getContext()));
		mController = controller;

		this.builder.cancelable(false);
		this.builder.autoDismiss(false);
		this.builder.title(R.string.title_dialog_product);
		this.builder.customView(R.layout.dialog_layout_product, true);
		this.builder.positiveText(R.string.action_save);
		this.builder.negativeText(R.string.action_cancel);
		this.builder.iconRes(R.drawable.ic_mode_edit_black_36dp);
		this.builder.show();

		View view = getCustomView();

		final EditText txtName = (EditText) view.findViewById(R.id.txtName);
		final CurrencyEditText txtPrice = (CurrencyEditText) view.findViewById(R.id.txtPrice);
		final TextView txtMsg = (TextView) view.findViewById(R.id.txtMsg);
		
		if (product != null) {
			txtName.setText(product.getName());
			txtPrice.setText(FormatUtil.toMoneyFormat(product.getPriceResale(), false));
		}

		this.builder.onNegative(new MaterialDialog.SingleButtonCallback() {
			@Override
			public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
				dialog.dismiss();
			}
		});

		this.builder.onPositive(new MaterialDialog.SingleButtonCallback() {
			@Override
			public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
				//View view = dialog.getCustomView();

				if (TextUtils.isEmpty(txtName.getText()) || TextUtils.isEmpty(txtPrice.getText())) {
					txtMsg.setText(R.string.txt_form_fill);
					return;
				}
				Date d = new Date();

				try {
					double value = txtPrice.getCurrencyDouble();

					if (product == null) {
						Product product = new Product();
						product.getModelImpl().setPublicToken(d.getDay() + d.getMonth() + d.getYear()+ d.getMinutes() + d.getSeconds()+"");
						product.getModelImpl().setName(txtName.getText().toString());
						product.getModelImpl().setPriceResale(value);

						mController.insertProduct(product);

					} else {
						product.getModelImpl().setName(txtName.getText().toString());
						product.getModelImpl().setPriceResale(value);

						mController.updateProduct(product);
					}
					dialog.dismiss();

				} catch (ParseException ne) {
					ne.printStackTrace();
				}
			}
		});
	}
}