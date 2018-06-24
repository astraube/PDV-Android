package br.com.i9algo.autaz.pdv.domain.interfaces;

import android.view.View;

import br.com.i9algo.autaz.pdv.domain.enums.PaymentMethodEnum;
import br.com.i9algo.autaz.pdv.domain.models.Client;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.domain.models.ProductSale;
import br.com.i9algo.autaz.pdv.domain.models.Sale;

public interface SaleControllerInterface {
	public void onSaleItemClick(ProductSale saleItem);
	public void onAddNewSale(Sale sale);
	public void onAddNewSale(Client client, String codeSale, String saller);
	public void onSyncSale(Sale sale);
	public Sale getCurrentSale();
	public void onChangeInfoSale(Sale sale);
	public void onOpenSale(Sale sale);
	public void onCleanListSale(Sale sale);
	public void onPrintCupom(final PaymentMethodEnum method, final double value, final double troco);
	public void onCancelSale(Sale sale);
	public void onPayTotalSale(Sale sale, PaymentMethodEnum method);
	public void onPayPartiallySale(Sale sale, double amountPaid, PaymentMethodEnum method);
	public void onDecrementProduct(String idProduct);
	public void onDecrementProduct(Product product);
	public void onIncrementProduct(String idProduct);
	public void onIncrementProduct(Product product);
	public void onPaymentMethod(Sale sale, PaymentMethodEnum method, double value);
}
