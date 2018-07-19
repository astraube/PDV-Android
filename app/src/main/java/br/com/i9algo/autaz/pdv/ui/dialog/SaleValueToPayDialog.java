package br.com.i9algo.autaz.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.text.ParseException;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.enums.PaymentMethodEnum;
import br.com.i9algo.autaz.pdv.domain.interfaces.SaleControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import br.com.i9algo.autaz.pdv.ui.components.CurrencyEditText;

public class SaleValueToPayDialog extends AlertDialog.Builder {
	
	private AlertDialog mDialog;
	
	public SaleValueToPayDialog(Activity activity, final SaleControllerInterface saleAdapter, final Sale sale, final PaymentMethodEnum method) {
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

		/**
		 * Caso o pagamento seja em dinheiro
		 * Exibe as cedulas de dinheiro
		 */
		final TableLayout bts_money = (TableLayout) layout.findViewById(R.id.bts_money);
		if (method == PaymentMethodEnum.MONEY) {
			bts_money.setVisibility(View.VISIBLE);

			((View) layout.findViewById(R.id.bt_money_2)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					txtMoney.setCurrencyDouble(20.0);
				}
			});
			((View) layout.findViewById(R.id.bt_money_5)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					txtMoney.setCurrencyDouble(50.0);
				}
			});
			((View) layout.findViewById(R.id.bt_money_10)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					txtMoney.setCurrencyDouble(100.0);
				}
			});
			((View) layout.findViewById(R.id.bt_money_20)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					txtMoney.setCurrencyDouble(200.0);
				}
			});
			((View) layout.findViewById(R.id.bt_money_50)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					txtMoney.setCurrencyDouble(500.0);
				}
			});
			((View) layout.findViewById(R.id.bt_money_100)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					txtMoney.setCurrencyDouble(1000.0);
				}
			});
		}


        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter; 
        this.mDialog.show();
	}
}
