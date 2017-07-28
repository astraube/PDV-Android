package br.com.i9algo.autaz.pdv.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.ProductsRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.SalesRealmRepository;
import br.com.i9algo.autaz.pdv.domain.enums.PaymentMethodEnum;
import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.interfaces.SaleControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Client;
import br.com.i9algo.autaz.pdv.domain.models.PaymentSale;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.domain.models.ProductSale;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.ui.base.CustomApplication;

public abstract class SaleController implements SaleControllerInterface {

	private Context mContext;
	private CustomApplication app;
	private static Sale _saleModel;
	
	public SaleController (Context context) {
		mContext = context;
		app = ((CustomApplication) mContext.getApplicationContext());
	}
	
	public Context getContext() {
		return mContext;
	}
	
	@Override
	public void onAddNewSale(Sale sale) {
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
		Sale newSale = new Sale(client);
		newSale.setSaller(saller);
		newSale.setCodeControll(codeSale);

		_saleModel = newSale;

		onSyncSale(newSale);
	}

	@Override
	public void onSaleItemClick(ProductSale saleItem) {
		// Code Default
	}

	@Override
	public void onPrintCupom(View v) {
		// Code Default
	}
	
	@Override
	public void onChangeInfoSale(Sale sale) {
		// Code Default
		onSyncSale(sale);
	}

	@Override
	public Sale getCurrentSale() {
		// Code Default
		return _saleModel;
	}
	
	@Override
	public void onOpenSale(Sale sale) {
		// Code Default
		_saleModel = sale;
	}

	@Override
	public void onCleanListSale(Sale sale) {
		// Code Default
		sale.setClear();
		onSyncSale(sale);
	}

	@Override
	public void onCancelSale(Sale sale) {
		// Code Default
		if (sale.getClient() != null)
			Log.w("SaleController", "onCancelSale(" + sale.getClient().getName() + ")");
		
		sale.setStatus(SaleStatusEnum.CANCELED);
	}

	@Override
	public void onSyncSale(Sale sale) {
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
	public void onPayTotalSale(Sale sale, PaymentMethodEnum method) {
		// Code Default
		Log.w("SaleController", "SaleControll onPaySale() : " + method.toString());
		
		sale.setStatus(SaleStatusEnum.PAID);
		sale.setAddPayment(new PaymentSale(method, sale.getTotalSale()));
		onSyncSale(sale);
	}
	
	
	/**
	 * Pagamento parcial da venda
	 */
	@Override
	public void onPayPartiallySale(Sale sale, double amountPaid, PaymentMethodEnum method) {
		// Code Default
		Log.w("SaleController", "SaleControll onPayPartiallySale() valor: " + amountPaid + " - metodo: " + method.toString());
		
		sale.setStatus(SaleStatusEnum.PAID_PARTIAL);
		sale.setAddPayment(new PaymentSale(method, amountPaid));
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
	public void onDecrementProduct(Product product) {
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
	public void onIncrementProduct(Product product) {
		// Code Default
	}

    @Override
    public void onPaymentMethod(Sale sale, PaymentMethodEnum method, double value) {
        // Code Default
    }
}
