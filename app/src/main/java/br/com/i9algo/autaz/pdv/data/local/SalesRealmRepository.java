package br.com.i9algo.autaz.pdv.data.local;

import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.models.Sale;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SalesRealmRepository {

	private static final String TAG = SalesRealmRepository.class.getSimpleName();
	
	public static boolean isSaleExisting(String token) {
		Realm realm = Realm.getDefaultInstance();
		Sale result = realm.where(Sale.class).equalTo(Sale.getRouteKeyName(), token).findFirst();

		return (result != null);
	}

	public static int getCount() {
		Realm realm = Realm.getDefaultInstance();
		return realm.where(Sale.class).findAll().size();
	}

	public static List<Sale> getAll() {
		Realm realm = Realm.getDefaultInstance();
		RealmResults<Sale> result = realm.where(Sale.class).findAll();
		result = result.sort("updatedAt"); // Sort ascending
		return new ArrayList<Sale>(result);
	}

	public static List<Sale> getAllNotSynchronized() {
		Realm realm = Realm.getDefaultInstance();
		RealmResults<Sale> result = realm.where(Sale.class).equalTo("syncronized", false).findAll();
		result = result.sort("updatedAt"); // Sort ascending
		return new ArrayList<Sale>(result);
	}

	public static Sale getById(String token) {
		Realm realm = Realm.getDefaultInstance();
		Sale result = realm.where(Sale.class).equalTo(Sale.getRouteKeyName(), token).findFirst();
		return result;
	}
	
	public static List<Sale> getByStatus(SaleStatusEnum status) {
		//Log.w(TAG, "getSalesByStatus()");

		Realm realm = Realm.getDefaultInstance();
		RealmResults<Sale> result = realm.where(Sale.class).equalTo("status", status.toString()).findAll();
		result = result.sort("updatedAt"); // Sort ascending

		//Log.w(TAG, "getSalesByStatus - " + result.toString());

		return new ArrayList<Sale>(result);
	}

	public static List<Sale> getOpenedAndPartialPaid(SaleStatusEnum status) {
		//Log.w(TAG, "getSalesByStatus()");

		Realm realm = Realm.getDefaultInstance();
		RealmResults<Sale> result = realm.where(Sale.class).equalTo("status", SaleStatusEnum.OPEN.toString()).equalTo("status", SaleStatusEnum.PAID_PARTIAL.toString()).findAll();
		result = result.sort("updatedAt"); // Sort ascending

		//Log.w(TAG, "getSalesByStatus - " + result.toString());

		return new ArrayList<Sale>(result);
	}
	
	public static List<Sale> getByDate(Date initDate, Date finalDate) {
		//Log.w(TAG, "getSalesAll( " + initDate + ", " + finalDate + ")");

		Realm realm = Realm.getDefaultInstance();
		RealmResults<Sale> result = realm.where(Sale.class).between("createdAt", initDate, finalDate).findAll();
		result = result.sort("updatedAt"); // Sort ascending

		//Log.w(TAG, "getSalesAll - " + result.size() + " vendas");

		return new ArrayList<Sale>(result);
	}
	
	public static synchronized void syncSale(final Sale sale) {
		Realm realm = Realm.getDefaultInstance();
		try {
			realm.beginTransaction();
			realm.insertOrUpdate(sale); // insere ou atualiza, sem esperar um retorno
			realm.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();

		}  finally {
			realm.close();
		}
	}
	
	public static synchronized void syncSaleList(final List<Sale> saleList) {
		Realm realm = Realm.getDefaultInstance();
		try {
			realm.beginTransaction();
			List<Sale> pp = new ArrayList<Sale>((Collection<? extends Sale>) saleList);
			realm.insertOrUpdate(pp);
			realm.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();

		}  finally {
			realm.close();
		}
	}

	public static synchronized void addSale(Sale sale) {
		Realm realm = Realm.getDefaultInstance();
		try {
			realm.beginTransaction();
			realm.insert(sale);
			realm.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();

		}  finally {
			realm.close();
		}
	}

	public static synchronized void addSaleList(final List<Sale> saleList) {
		Realm realm = Realm.getDefaultInstance();
		try {
			realm.beginTransaction();
			List<Sale> pp = new ArrayList<Sale>((Collection<? extends Sale>) saleList);
			realm.insert(pp);
			realm.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();

		}  finally {
			realm.close();
		}
	}
}
