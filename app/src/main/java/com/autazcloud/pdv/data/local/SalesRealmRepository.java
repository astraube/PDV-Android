package com.autazcloud.pdv.data.local;

import com.autazcloud.pdv.domain.enums.SaleStatusEnum;
import com.autazcloud.pdv.domain.models.SaleModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SalesRealmRepository {

	private static final String TAG = SalesRealmRepository.class.getSimpleName();
	
	public static boolean isSaleExisting(String token) {
		Realm realm = Realm.getDefaultInstance();
		SaleModel result = realm.where(SaleModel.class).equalTo(SaleModel.getRouteKeyName(), token).findFirst();

		return (result != null);
	}

	public static int getSaleCount() {
		Realm realm = Realm.getDefaultInstance();
		return realm.where(SaleModel.class).findAll().size();
	}

	public static SaleModel getSaleById(String token) {
		Realm realm = Realm.getDefaultInstance();
		SaleModel result = realm.where(SaleModel.class).equalTo(SaleModel.getRouteKeyName(), token).findFirst();
		return result;
	}
	
	public static List<SaleModel> getSalesByStatus(SaleStatusEnum status) {
		//Log.w(TAG, "getSalesByStatus()");

		Realm realm = Realm.getDefaultInstance();
		RealmResults<SaleModel> result = realm.where(SaleModel.class).equalTo("status", status.toString()).findAll();
		result = result.sort("dateUpdated"); // Sort ascending

		//Log.w(TAG, "getSalesByStatus - " + result.toString());

		return new ArrayList<SaleModel>(result);
	}
	
	public static List<SaleModel> getSalesDate(String initDate, String finalDate) {
		//Log.w(TAG, "getSalesAll( " + initDate + ", " + finalDate + ")");

		Realm realm = Realm.getDefaultInstance();
		RealmResults<SaleModel> result = realm.where(SaleModel.class).findAll();
		result = result.sort("dateUpdated"); // Sort ascending

		//Log.w(TAG, "getSalesAll - " + result.size() + " vendas");

		return new ArrayList<SaleModel>(result);
	}
	
	public static synchronized void syncSale(final SaleModel sale) {
		Realm realm = Realm.getDefaultInstance();
		try {
			realm.beginTransaction();
			realm.insertOrUpdate(sale); // insere ou atualiza, sem esperar um retorno
			realm.commitTransaction();
		} finally {
			realm.close();
		}
	}
	
	public static synchronized void syncSaleList(final List<SaleModel> saleList) {
		Realm realm = Realm.getDefaultInstance();
		try {
			realm.beginTransaction();
			List<SaleModel> pp = new ArrayList<SaleModel>((Collection<? extends SaleModel>) saleList);
			realm.insertOrUpdate(pp);
			realm.commitTransaction();
		} finally {
			realm.close();
		}
	}

	public static synchronized void addSale(SaleModel sale) {
		Realm realm = Realm.getDefaultInstance();
		try {
			realm.beginTransaction();
			realm.insert(sale);
			realm.commitTransaction();
		} finally {
			realm.close();
		}
	}

	public static synchronized void addSaleList(final List<SaleModel> saleList) {
		Realm realm = Realm.getDefaultInstance();
		try {
			realm.beginTransaction();
			List<SaleModel> pp = new ArrayList<SaleModel>((Collection<? extends SaleModel>) saleList);
			realm.insert(pp);
			realm.commitTransaction();
		} finally {
			realm.close();
		}
	}
}
