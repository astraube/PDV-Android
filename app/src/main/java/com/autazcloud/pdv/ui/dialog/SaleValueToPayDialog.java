package com.autazcloud.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.interfaces.SaleControllerInterface;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.helpers.FormatUtil;

import java.text.ParseException;

import faranjit.currency.edittext.CurrencyEditText;

public class SaleValueToPayDialog extends AlertDialog.Builder {
	
	private AlertDialog mDialog;
	
	public SaleValueToPayDialog(Activity activity, final SaleControllerInterface saleAdapter, final SaleModel sale, final PaymentMethodEnum method) {
		super(activity);
		
		final Double total = sale.getTotalRestPay();
		final String totalStr = FormatUtil.toMoneyFormat(total, false);

		//Log.w("SaleControllActivity", "total - " + total);
		//Log.w("SaleControllActivity", "totalStr - " + totalStr);
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_layout_sale_value_to_pay, null);

		final CurrencyEditText txtMoney = (CurrencyEditText)layout.findViewById(R.id.txtMoney);
		txtMoney.setText(totalStr);
		txtMoney.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                	txtMoney.setText("");
                }
                return false;
            }
        });
		setIcon(R.drawable.money);
        setView(layout);
        setTitle(activity.getString(R.string.sale_pay_value_to_pay) + ": " +  FormatUtil.toMoneyFormat(total));
        setCancelable(true);
        setOnCancelListener(new AlertDialog.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mDialog.dismiss();
			}
		});
        setPositiveButton(R.string.action_sale_pay,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
    				try {
						double value = txtMoney.getCurrencyDouble();

						//Log.w("SaleControllActivity", "value - " + value);
    					if (saleAdapter != null)
    						saleAdapter.onPaymentMethod(sale, method, value);
    					
    				} catch (ParseException e) {
    					txtMoney.setText("");
    					return;
    				}
    				mDialog.dismiss();
                }
            }
        );
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter; 
        this.mDialog.show();
	}
}
