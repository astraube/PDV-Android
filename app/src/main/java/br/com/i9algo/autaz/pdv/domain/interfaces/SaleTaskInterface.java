package br.com.i9algo.autaz.pdv.domain.interfaces;

import java.util.List;

import br.com.i9algo.autaz.pdv.domain.models.Sale;

public interface SaleTaskInterface {
	public void onCompleteLoadSales(List<Sale> saleList);
}
