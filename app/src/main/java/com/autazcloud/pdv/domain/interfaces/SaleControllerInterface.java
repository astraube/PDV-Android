package com.autazcloud.pdv.domain.interfaces;

import android.view.View;

import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.models.Client;
import com.autazcloud.pdv.domain.models.SaleItemModel;
import com.autazcloud.pdv.domain.models.SaleModel;

public interface SaleControllerInterface {
	public void onSaleItemClick(SaleItemModel saleItem);
	public void onAddNewSale(SaleModel sale);
	public void onAddNewSale(Client client, String codeSale, String saller);
	public void onSyncSale(SaleModel sale);
	public SaleModel getCurrentSale();
	public void onChangeInfoSale(SaleModel sale);
	public void onOpenSale(SaleModel sale);
	public void onCleanListSale(SaleModel sale);
	public void onPrintCupom(View v);
	public void onCancelSale(SaleModel sale);
	public void onPayTotalSale(SaleModel sale, PaymentMethodEnum method);
	public void onPayPartiallySale(SaleModel sale, double amountPaid, PaymentMethodEnum method);
	public void onDecrementProduct(String idProduct);
	public void onDecrementProduct(ProductInterface product);
	public void onIncrementProduct(String idProduct);
	public void onIncrementProduct(ProductInterface product);
	public void onPaymentMethod(SaleModel sale, PaymentMethodEnum method, double value);
}
