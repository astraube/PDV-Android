package com.autazcloud.pdv.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.data.local.ProductsRealmRepository;
import com.autazcloud.pdv.data.local.SalesRealmRepository;
import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.enums.SaleStatusEnum;
import com.autazcloud.pdv.domain.interfaces.ProductInterface;
import com.autazcloud.pdv.domain.interfaces.SaleControllerInterface;
import com.autazcloud.pdv.domain.models.Client;
import com.autazcloud.pdv.domain.models.Payment;
import com.autazcloud.pdv.domain.models.Product;
import com.autazcloud.pdv.domain.models.SaleItemModel;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.ui.base.CustomApplication;

public abstract class SaleController implements SaleControllerInterface {

	private Context mContext;
	private CustomApplication app;
	private static SaleModel _saleModel;
	
	public SaleController (Context context) {
		mContext = context;
		app = ((CustomApplication) mContext.getApplicationContext());
	}
	
	public Context getContext() {
		return mContext;
	}
	
	@Override
	public void onAddNewSale(SaleModel sale) {
		// Code Default
		if (sale != null) {
			sale.setStatus(SaleStatusEnum.OPEN);

			_saleModel = sale;

			onSyncSale(sale);
		}
	}

	@Override
	public void onAddNewSale(Client client, String codeSale, String saller) {
		if (client == null) {
			client = new Client();
		}
		if (client.getName() == null || client.getName().isEmpty()) {
			int count  = SalesRealmRepository.getSaleCount();
			client.setName(mContext.getResources().getString(R.string.txt_bt_sale_init_name, (count + 1)+""));
		}

		// TODO - incluir no SaleModel apenas o model Client
		SaleModel newSale = new SaleModel();
		newSale.setClientName(client.getName());
		newSale.setClientEmail(client.getEmail());
		newSale.setClientCpf(client.getCpfCnpj());
		newSale.setSaller(saller);
		newSale.setCodeControll(codeSale);

		_saleModel = newSale;

		onSyncSale(newSale);
	}

	@Override
	public void onSaleItemClick(SaleItemModel saleItem) {
		// Code Default
	}

	@Override
	public void onPrintCupom(View v) {
		// Code Default
	}
	
	@Override
	public void onChangeInfoSale(SaleModel sale) {
		// Code Default
		onSyncSale(sale);
	}

	@Override
	public SaleModel getCurrentSale() {
		// Code Default
		return _saleModel;
	}
	
	@Override
	public void onOpenSale(SaleModel sale) {
		// Code Default
		_saleModel = sale;
	}

	@Override
	public void onCleanListSale(SaleModel sale) {
		// Code Default
		sale.setClear();
		onSyncSale(sale);
	}

	@Override
	public void onCancelSale(SaleModel sale) {
		// Code Default
		Log.w("SaleController", "onCancelSale(" + sale.getClientName() + ")");
		
		sale.setStatus(SaleStatusEnum.CANCELED);
	}

	@Override
	public void onSyncSale(SaleModel sale) {
		// Code Default
		Log.v("SaleController", "SaleControllActivity - onSyncSale");

		if (sale != null) {
			try {
				SalesRealmRepository.syncSale(sale);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Pagamento total da venda
	 */
	@Override
	public void onPayTotalSale(SaleModel sale, PaymentMethodEnum method) {
		// Code Default
		Log.w("SaleController", "SaleControll onPaySale() : " + method.toString());
		
		sale.setStatus(SaleStatusEnum.PAID);
		sale.setAddPayment(new Payment(method, sale.getTotalSale()));
		onSyncSale(sale);
	}
	
	
	/**
	 * Pagamento parcial da venda
	 */
	@Override
	public void onPayPartiallySale(SaleModel sale, double amountPaid, PaymentMethodEnum method) {
		// Code Default
		Log.w("SaleController", "SaleControll onPayPartiallySale() valor: " + amountPaid + " - metodo: " + method.toString());
		
		sale.setStatus(SaleStatusEnum.PAID_PARTIAL);
		sale.setAddPayment(new Payment(method, amountPaid));
		onSyncSale(sale);
	}

	@Override
	public void onDecrementProduct(String idProduct) {
		// Code Default
		Product p = ProductsRealmRepository.getById(idProduct);
		if (p != null)
			onDecrementProduct(p);
	}

	@Override
	public void onDecrementProduct(ProductInterface product) {
		// Code Default
	}

	@Override
	public void onIncrementProduct(String idProduct) {
		// Code Default
		Product p = ProductsRealmRepository.getById(idProduct);
		if (p != null)
			onIncrementProduct(p);
	}

	@Override
	public void onIncrementProduct(ProductInterface product) {
		// Code Default
	}

    @Override
    public void onPaymentMethod(SaleModel sale, PaymentMethodEnum method, double value) {
        // Code Default
    }
}
