package br.com.i9algo.autaz.pdv.domain.interfaces;

import br.com.i9algo.autaz.pdv.domain.models.Sale;

import java.util.List;

public interface SaleTaskInterface {
	public void onCompleteLoadSales(List<Sale> saleList);
}
