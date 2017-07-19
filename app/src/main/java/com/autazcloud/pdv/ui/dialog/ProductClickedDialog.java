package com.autazcloud.pdv.ui.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.interfaces.ProductControllerInterface;
import com.autazcloud.pdv.domain.models.Product;
import com.autazcloud.pdv.ui.base.BaseActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProductClickedDialog extends AlertDialog.Builder {
	
	private ProductControllerInterface mController;
	private AlertDialog mDialog;

	public ProductClickedDialog(ProductControllerInterface controller, final Product product) {
		super((BaseActivity) controller.getContext());
		mController = controller;

		LayoutInflater layoutInflater = LayoutInflater.from((BaseActivity) mController.getContext());
		final LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_layout_product_clicked, null);
		
		layout.findViewById(R.id.btnEdit).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				
				new ProductEditDialog(mController, product);
			}
		});
		
		layout.findViewById(R.id.btnRemove).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();

				SweetAlertDialog sad = new SweetAlertDialog(mController.getContext(), SweetAlertDialog.WARNING_TYPE)
						.setTitleText(getContext().getString(R.string.txt_title_remove_entry))
						.setContentText(getContext().getString(R.string.txt_confirm_remove_product))
						.setConfirmText(getContext().getString(R.string.action_yes_remove))
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								mController.removeProduct(product);
								sDialog.dismissWithAnimation();
							}
						});
				sad.setCancelable(false);

				((BaseActivity) mController.getContext()).setSweetDialog(sad);
			}
		});
		
		setIcon(R.mipmap.ic_launcher);
        setView(layout);
        setTitle(R.string.title_dialog_product_clicked);
        setCancelable(true);
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter;
        this.mDialog.show();
	}
}