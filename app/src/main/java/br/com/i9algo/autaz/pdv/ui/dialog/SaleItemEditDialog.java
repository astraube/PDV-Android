package br.com.i9algo.autaz.pdv.ui.dialog;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.SaleControllActivity;
import br.com.i9algo.autaz.pdv.domain.interfaces.SaleControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.ProductSale;

public class SaleItemEditDialog extends AlertDialog.Builder {

	TextView txtObs;
	Button btSaveObs;
	Button btDecrease;
	Button btIncrease;

	private AlertDialog mDialog;
	private final SaleControllActivity mActivity;
	private final SaleControllerInterface mSaleAdapter;
	private final ProductSale mProductSale;

	public SaleItemEditDialog(SaleControllActivity activity, final SaleControllerInterface saleAdapter, final ProductSale si) {
		super(activity);

		mActivity = activity;
		mSaleAdapter = saleAdapter;
		mProductSale = si;
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.dialog_layout_sale_item_edit, null);

		txtObs = (TextView)layout.findViewById(R.id.txtObs);
		btSaveObs = (Button)layout.findViewById(R.id.btSaveObs);
		btDecrease = (Button)layout.findViewById(R.id.btDecrease);
		btIncrease = (Button)layout.findViewById(R.id.btIncrease);

		if (!si.getAnnotation().isEmpty()) {
			txtObs.setText(si.getAnnotation());
		}

		btSaveObs.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!txtObs.getText().toString().isEmpty()) {
					si.getModelImpl().setAnnotation(txtObs.getText().toString());
					mDialog.dismiss();
				} else {

				}
			}
		});

		btDecrease.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.v("Product", "---> decrement produto - " + si.toString());
				saleAdapter.onDecrementProduct(si.getPublicToken());
			}
		});

		btIncrease.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.v("Product", "---> increment produto - " + si.toString());
				saleAdapter.onIncrementProduct(si.getPublicToken());
			}
		});

		
		//setIcon(R.drawable.money);
        setView(layout);
        //setTitle(R.string.title_dialog_sale_pay_type);
        setCancelable(true);
        this.mDialog = this.create();
        this.mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationCenter; 
        this.mDialog.show();
	}
}
