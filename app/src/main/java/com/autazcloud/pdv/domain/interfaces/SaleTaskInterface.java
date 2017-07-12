package com.autazcloud.pdv.domain.interfaces;

import com.autazcloud.pdv.domain.models.SaleModel;

import java.util.List;

public interface SaleTaskInterface {
	public void onCompleteLoadSales(List<SaleModel> saleList);
	public void onChangeListViewAdapter(SaleModel item);
}
