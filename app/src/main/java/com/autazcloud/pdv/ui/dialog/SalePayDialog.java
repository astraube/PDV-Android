package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.interfaces.SaleControllerInterface;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.ui.views.ImageTextView;

public class SalePayDialog extends AlertDialog.Builder implements View.OnClickListener {
	
	private AlertDialog mDialog;
	private final Activity mActivity;
	private final SaleControllerInterface mSaleAdapter;
	private final SaleModel mSale;
	
	public SalePayDialog(Activity activity, final SaleControllerInterface saleAdapter, final SaleModel sale) {
		super(activity);
		
		mActivity = activity;
		mSaleAdapter = saleAdapter;
		mSale = sale;
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_layout_sale_pay, null);
		
		createButtons(layout);
		
		setIcon(R.drawable.money);
        setView(layout);
        setTitle(R.string.title_dialog_sale_pay_type);
        setCancelable(true);
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter; 
        this.mDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		final double total = mSale.getTotalRestPay();
		PaymentMethodEnum method = null;
		
		switch (v.getId()) {
		case 0:
			// Dinheiro
			method = PaymentMethodEnum.MONEY;
			break;
		case 1:
			// Cartao Debito
			method = PaymentMethodEnum.DEBT;
			break;
		case 2:
			// Cartao Credito
			method = PaymentMethodEnum.CREDIT;
			break;
		case 3:
			// Voucher
			method = PaymentMethodEnum.VOUCHER;
			break;
		}
		
		if (method != null)
			new SaleValueToPayDialog(mActivity, mSaleAdapter, mSale, method);
		
		mDialog.dismiss();
	}
	
	private void createButtons(LinearLayout layout) {
		String [] menuItems = getContext().getResources().getStringArray(R.array.arr_items_payment_types);
		String[] menuItemsIcon = getContext().getResources().getStringArray(R.array.arr_icons_payment_types);
		
		for (int i = 0; i < menuItems.length; i++) {
			ImageTextView btn;
			String text;
			int id_icon;
			
			text = getContext().getString(getContext().getResources().getIdentifier(menuItems[i], "string", getContext().getPackageName()));
			id_icon = getContext().getResources().getIdentifier(menuItemsIcon[i], "drawable", getContext().getPackageName());
			btn = new ImageTextView(getContext(), 50);
			
			LayoutParams lp = new LayoutParams(100, LayoutParams.WRAP_CONTENT);
			lp.setMargins(10, 10, 10, 10);
			btn.setLayoutParams(lp);
			//btn.setImageResource(id_icon);
			btn.setText(text);
			btn.setOnClickListener(this);
			btn.setId(i);
			btn.setTextColor(ResourcesCompat.getColor(getContext().getResources(), android.R.color.black, null));
			btn.setBackgroundTextView(0, 10, 10);
			btn.setImageResource(id_icon);
			btn.setBackgroundResource(android.R.color.transparent);
			
			layout.addView(btn);
		}
	}
}
