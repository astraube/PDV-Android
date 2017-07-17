package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.interfaces.ProductControllerInterface;
import com.autazcloud.pdv.domain.models.Product;
import com.autazcloud.pdv.helpers.FormatUtil;
import com.autazcloud.pdv.ui.base.BaseActivity;

import java.text.ParseException;
import java.util.Date;


import cn.pedant.SweetAlert.SweetAlertDialog;
import faranjit.currency.edittext.CurrencyEditText;

public class ProductEditDialog extends AlertDialog.Builder {
	
	private ProductControllerInterface mController;
	private AlertDialog mDialog;
	
	public ProductEditDialog(final ProductControllerInterface controller) {
		this(controller, null);
	}
	public ProductEditDialog(final ProductControllerInterface controller, final Product product) {
		super((BaseActivity) controller.getContext());
		mController = controller;
		
		LayoutInflater layoutInflater = LayoutInflater.from(controller.getContext());
		final RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.dialog_layout_product, null);
		
		final EditText txtName = (EditText) layout.findViewById(R.id.txtName);
		final CurrencyEditText txtPrice = (CurrencyEditText) layout.findViewById(R.id.txtPrice);
		
		if (product != null) {
			txtName.setText(product.getName());
			txtPrice.setText(FormatUtil.toMoneyFormat(product.getPriceResale(), false));
		}
		
		layout.findViewById(R.id.btSend).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				
				Editable subject = txtName.getText();
				
				if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(txtPrice.getText())) {
					Date d = new Date();
					
					try {
						double value = txtPrice.getCurrencyDouble();

						if (product == null) {
							Product product = new Product();
							product.getModelImpl().setPublicToken(d.getDay() + d.getMonth() + d.getYear()+ d.getMinutes() + d.getSeconds()+"");
							product.getModelImpl().setName(subject.toString());
							product.getModelImpl().setPriceResale(value);
							
							mController.insertProduct(product);
							
						} else {
							product.getModelImpl().setName(subject.toString());
							product.getModelImpl().setPriceResale(value);
							
							mController.updateProduct(product);
						}
						
					} catch (ParseException ne) {
						ne.printStackTrace();
					}
					return;
				}

				SweetAlertDialog sad = new SweetAlertDialog(controller.getContext(), SweetAlertDialog.ERROR_TYPE)
						.setTitleText(getContext().getString(R.string.txt_form_incomplete))
						.setContentText(getContext().getString(R.string.txt_form_fill));
				sad.setCancelable(false);
				((BaseActivity) controller.getContext()).setSweetDialog(sad);
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
        setTitle(R.string.title_dialog_product);
        setCancelable(true);
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter;
        this.mDialog.show();
	}
}