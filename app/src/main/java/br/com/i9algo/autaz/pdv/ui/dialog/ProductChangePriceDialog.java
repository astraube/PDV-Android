package br.com.i9algo.autaz.pdv.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.interfaces.SaleControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Product;

import java.text.ParseException;

import faranjit.currency.edittext.CurrencyEditText;

public class ProductChangePriceDialog extends AlertDialog.Builder {
	
	private AlertDialog mDialog;
	
	public ProductChangePriceDialog(Activity activity, final Product product, final SaleControllerInterface saleAdap) {
		super(activity);
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_layout_product_change_price, null);

		final CurrencyEditText txtMoney = (CurrencyEditText) layout.findViewById(R.id.txtMoney);
		txtMoney.setText(String.valueOf(0));
        //txtMoney.setLocale(new Locale("pt", "BR"));

		final TextView txtProductName = (TextView)layout.findViewById(R.id.txtProductName);

		txtProductName.setText(product.getName());

		setIcon(R.drawable.money);
        setView(layout);
        setTitle(R.string.txt_change_price_product);
        setCancelable(true);
        setOnCancelListener(new AlertDialog.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mDialog.dismiss();
			}
		});
		setNegativeButton(R.string.action_cancel,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            }
        );
        setPositiveButton(android.R.string.ok,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
					try {
						double value = txtMoney.getCurrencyDouble();
						product.getModelImpl().setPriceResale(value);
						if (saleAdap != null)
							saleAdap.onIncrementProduct(product);

					} catch (ParseException ex) {
						txtMoney.setText("");
						return;
					}
    				/*try {
                        String value = txtMoney.getText().toString();
    					product.setPrice(Double.parseDouble(value));
    					if (saleAdap != null)
    						saleAdap.onIncrementProduct(product);
    					
    				} catch (NumberFormatException ex) {
    					txtMoney.setText("");
    					return;
    				}*/
                    dialog.dismiss();
                }
            }
        );
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter; 
        this.mDialog.show();
	}
}
