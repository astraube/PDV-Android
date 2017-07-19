package com.autazcloud.pdv.domain.models;

import com.autazcloud.pdv.domain.constants.Constants;
import com.autazcloud.pdv.domain.constants.DateFormats;
import com.autazcloud.pdv.domain.enums.SaleStatusEnum;
import com.autazcloud.pdv.helpers.MathUtils;
import com.autazcloud.pdv.helpers.ObjectUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class SaleModel extends RealmObject {
	
	private static volatile String TAG = "SaleModel";

	@Ignore private volatile double priceDiscount;
	@Ignore private volatile double discountPercent;
	@Ignore private volatile double totalSale;
	@Ignore private volatile double totalRestPay;
	@Ignore private volatile double totalProducts;
	@Ignore private volatile String paymentJson;
	@Ignore private volatile boolean increment;
	@Ignore private volatile boolean decrement;
	@Ignore private volatile boolean payment;
	@Ignore private volatile int countProducts;
	@Ignore private volatile boolean existsProduct;
	@Ignore private volatile boolean addPayment;
	@Ignore private volatile boolean clear;

	@Index
	@PrimaryKey
	private String id; // Codigo numerico de identificacao

	//@Ignore
	//private HashMap<String, SaleItemModel> itemListMap;
	private RealmList<SaleItemModel> itemList;
	private String clientName = "";
	private String clientEmail = "";
	private String clientCpf = "";
	private String codeControll = ""; // Codigo da venda para controle particular do estabelecimento. EX: Numero da mesa
	private String saller; // Vendedor
	private String status = SaleStatusEnum.OPEN.toString();
	private double discount = 0;
	private RealmList<Payment> paymentList = null;
	private double totalPaid = 0;
	private Date dateCreated;
	private Date dateUpdated;

	private String orderCodeWait; // Codigo de espera do pedido

	public SaleModel () {
		itemList = new RealmList<SaleItemModel>();
		dateCreated = new Date();
		dateUpdated = new Date();
		id = generateCodeSale();
		setStatus(SaleStatusEnum.OPEN);
	}
	
	/*public static SaleModel fromCursor(Cursor cursor) {
		SaleModel model = new SaleModel();
		model.setClientName(cursor.getString(cursor.getColumnIndex(SalesTable.COL_NAME)));
		model.setClientEmail(cursor.getString(cursor.getColumnIndex(SalesTable.COL_CLIENT_EMAIL)));
		model.setClientCpf(cursor.getString(cursor.getColumnIndex(SalesTable.COL_CLIENT_CPF)));
		//model.setTotalPaid(cursor.getDouble(cursor.getColumnIndex(SalesTable.COL_TOTAL_PAID)));
		model.setDiscount(cursor.getDouble(cursor.getColumnIndex(SalesTable.COL_DISCOUNT)));
		model.setId(cursor.getString(cursor.getColumnIndex(SalesTable.COL_CODE_BD)));
		model.setCodeControll(cursor.getString(cursor.getColumnIndex(SalesTable.COL_CODE_CONTROLL)));
		model.setSaller(cursor.getString(cursor.getColumnIndex(SalesTable.COL_SALLER)));
		model.setStatus(SaleStatusEnum.valueOf(cursor.getString(cursor.getColumnIndex(SalesTable.COL_STATUS))));
		model.setDateCreated(cursor.getString(cursor.getColumnIndex(SalesTable.COL_DATE_CREATED)));
		
		String productsSale = cursor.getString(cursor.getColumnIndex(SalesTable.COL_PRODUCTS_SALE));
		String payments = cursor.getString(cursor.getColumnIndex(SalesTable.COL_PAYMENTS));
		
		//Log.i(TAG, "getDateCreated: " + model.getDateCreated());
		//Log.i(TAG, "COL_PRODUCTS_SALE: " + productsSale);
		//Log.i(TAG, "COL_PAYMENTS: " + payments);
		
		try {
			
			// Produtos na lista de venda
			if (productsSale != null && !productsSale.isEmpty()) {
				
				JSONObject jsonProducts = new JSONObject(productsSale);
				
				Log.v(TAG, "productsSale: " + jsonProducts.toString());
				
				Iterator<String> temp = jsonProducts.keys();
		        while (temp.hasNext()) {
		            String key = temp.next();
		            String value = jsonProducts.getString(key);
		            
		            SaleItemModel prod = SaleItemModel.newSaleItemFromJson(value);
					model.setIncrement(prod);
		        }
			}
			
			// Pagamentos
			if (payments != null && !payments.isEmpty()) {
				JSONArray jsonPayments = new JSONArray(payments);
				for (int i = 0; i < jsonPayments.length(); i++) {
					Payment pay = Payment.newPaymentFromJson(jsonPayments.getString(i));
					model.setAddPayment(pay);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "---->Erro aqui: " + e.getMessage());
		}
		
		return model;
	}*/

	public static String getRouteKeyName()
	{
		return "id";
	}

	// Deve ser codigo numerico APENAS
	public String getId() { return this.id; }
	public void setId(String id) { this.id = id; }

	public List<SaleItemModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleItemModel> itemList) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.itemList = (RealmList)itemList;
		_realm.commitTransaction();
	}

	public String getClientName() { return clientName; }
	public void setClientName(String value) {

		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.clientName = value;
		_realm.commitTransaction();
	}

	public String getClientEmail() { return clientEmail; }
	public void setClientEmail(String value) {

		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.clientEmail = value;
		_realm.commitTransaction();
	}

	public String getClientCpf() { return clientCpf; }
	public void setClientCpf(String value) {

		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.clientCpf = value;
		_realm.commitTransaction();
	}
	
	/**
	 * Codigo de controle particular do estabelecimento. EX: Numero da mesa
	 * @return
	 */
	public String getCodeControll() { return codeControll; }
	public void setCodeControll(String saleCodeControll) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.codeControll = saleCodeControll;
		_realm.commitTransaction();
	}
	
	public String getSaller() { return saller; }
	public void setSaller(String saller) {

		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.saller = saller;
		_realm.commitTransaction();
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(SaleStatusEnum status) {

		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();

		this.status = status.toString();

		_realm.commitTransaction();
	}
	public void setStatus(String status) {

		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.status = status;
		_realm.commitTransaction();
	}

	/**
	 *  desconto da venda em R$
	 */
	public double getDiscount() { return discount; }
	public void setDiscount(double value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		discount = value;
		_realm.commitTransaction();
	}
	
	/**
	 * Retorna o desconto da venda em %
	 * @return
	 */
	public double getDiscountPercent() {
		double total = getTotalProducts();
		if (total > 0 && discount > 0)
			return MathUtils.valueToPercent(total, discount);
		else
			return 0;
	}
	
	/**
	 * Altera o desconto da venda a partir de uma porcentagem
	 */
	public void setDiscountPercent(double value) {

		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();

		double total = getTotalProducts();
		if (total > 0 && value > 0)
			discount = MathUtils.percentToValue(total, value);

		_realm.commitTransaction();
	}
	

	public Date getDateCreated() { return this.dateCreated; }
	public void setDateCreated(String date) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.dateCreated = DateFormats.parseDate(date, DateFormats.getDateTimeGlobal());
		_realm.commitTransaction();
	}

	public Date getDateUpdated() { return dateUpdated; }
	public void setDateUpdated(String date) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.dateUpdated = DateFormats.parseDate(date, DateFormats.getDateTimeGlobal());
		_realm.commitTransaction();
	}

	public List<Payment> getPaymentList() { return paymentList; }
	public String getPaymentJson() {
		if (paymentList != null) {
			String retorno = ObjectUtil.ObjectToJson(paymentList);
			//Log.v(TAG, "getPaymentJson" + retorno);
			return retorno;
		} else
			return "";
	}
	public void setAddPayment(Payment payment) {
		if (paymentList == null) {
			paymentList = new RealmList<Payment>();
		}
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();

		paymentList.add(payment);
		totalPaid += payment.getAmountPaid();

		_realm.commitTransaction();
	}
	
	/**
	 * Valor total pago. pago parcialmente, com desconto, pago total da venda. 
	 * @return
	 */
	public double getTotalPaid() { return totalPaid; }
	//public void setTotalPaid(double value) { totalPaid = value; }
	
	/**
	 * Retorna o valor total dos produtos com desconto
	 * @return
	 */
	public double getTotalProducts() {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();

		double d = 0;
		for (SaleItemModel item : itemList) {
		
			d +=(item.getPriceDiscount() * item.getQuantityItem());
		}
		_realm.commitTransaction();
		return d;
	}
	
	/**
	 * Retorna o valor total da venda com desconto
	 * @return
	 */
	public double getTotalSale() {
		double total = getTotalProducts();
		double desc = discount;

		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();

		if (total > 0 && total > desc)
			total = (total - desc);

		_realm.commitTransaction();

		return (total);
	}
	
	/**
	 * Retorna o valor restante a pagar da venda
	 * @return
	 */
	public double getTotalRestPay() {
		if (getTotalSale() > totalPaid) {
			return getTotalSale() - totalPaid;
		} else {
			return 0;
		}
	}
	
	/**
	 * Retorna um item a partir de um incice
	 * @param index
	 * @return
	 */
	public SaleItemModel get(int index) {
		//ArrayList<SaleItemModel> keys = new ArrayList(itemListMap.values());
    	final SaleItemModel item = itemList.get(index);
		return item;
	}
	
	/**
	 * Retorna o tamanho da lista, a quantidade de produtos diferentes 
	 */
	/*@Override
	public int size() {
		Log.v("@@@", "@@@ size - ");
		return this.size();
	}*/
	
	/**
	 * Retorna a quantidade total de produtos
	 * @return
	 */
	public int getCountProducts() {
		int aux = 0;
		for (SaleItemModel item : itemList) {
			aux += item.getQuantityItem();
		}
		return aux;
	}

	public boolean isExistsProduct(SaleItemModel saleItem) {
		for (SaleItemModel i : itemList) {
			if (i.getId().equals(saleItem.getId())) {
				return true;
			}
		}
		return false;
	}

	public SaleItemModel getExistsProduct(SaleItemModel saleItem) {
		for (SaleItemModel i : itemList) {
			if (i.getId().equals(saleItem.getId())) {
				return i;
			}
		}
		return null;
	}

	public boolean existsOrderCodeWait() {
		return (this.orderCodeWait != null && !this.orderCodeWait.isEmpty());
	}
	public String getOrderCodeWait() { return this.orderCodeWait; }
	public void setOrderCodeWait(String orderCodeWait) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.orderCodeWait = orderCodeWait;
		_realm.commitTransaction();
	}
	
	
	private static String generateCodeSale() {
		Calendar c = Calendar.getInstance();
		String code = "" + 	String.format("%02d", c.get(Calendar.DAY_OF_MONTH)) + 
							String.format("%02d", c.get(Calendar.MONTH) + 1) + 
							String.format("%04d", c.get(Calendar.YEAR)) + 
							String.format("%02d", c.get(Calendar.HOUR)) + 
							String.format("%02d", c.get(Calendar.MINUTE)) + 
							String.format("%02d", c.get(Calendar.SECOND)) +
							String.format("%04d", Constants.CODE_OP);
		
		return code;
	}

	/**
	 * Adicionar um novo item a lista de compra
	 * @param product
	 * @return
	 */
	public SaleItemModel setIncrement(Product product) {
		return setIncrement(new SaleItemModel(product));
	}
	public SaleItemModel setIncrement(SaleItemModel saleItem) {
		SaleItemModel item = this.getExistsProduct(saleItem);

		if (item == null) {
			Realm _realm = Realm.getDefaultInstance();
			_realm.beginTransaction();
			itemList.add(saleItem);
			_realm.commitTransaction();

			return saleItem;
		} else {
			if (!saleItem.isJockerProduct()) {
				item.setAddItem();
			} else {
				double newPrice = saleItem.getPriceResale();
				double curPrice = item.getPriceResale();
				item.setPriceResale(curPrice + newPrice);
			}
			return item;
		}
	}
	
	/**
	 * Remover um item da lista de compra
	 * @param product
	 */
	public void setDecrement(Product product) {
		setDecrement(new SaleItemModel(product));
	}
	public void setDecrement(SaleItemModel saleItem) {
		SaleItemModel item = this.getExistsProduct(saleItem);

		if (item != null) {
			//Log.v(TAG, "decrement: - " + item.getName());

			item.setRemoveItem();

			Realm _realm = Realm.getDefaultInstance();
			_realm.beginTransaction();
			if (item.getQuantityItem() < 1) {
				itemList.remove(item);

				// Caso for um produto que precisa de senha de pedido, verifica se pode remover a senha atual
				if (item.isRequestPass()) {
					String currentOrderCodeWait = getOrderCodeWait();
					for (SaleItemModel si : itemList) {
						currentOrderCodeWait = null;
						if (si.isRequestPass()) {
							currentOrderCodeWait = getOrderCodeWait();
							break;
						}
					}
					orderCodeWait = currentOrderCodeWait;
				}
			}
			_realm.commitTransaction();
			//Log.v("@@@", "decrement: unidades - " + item.getQuantityItem());
		}
	}

	public void setClear() {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		itemList.clear();
		_realm.commitTransaction();
	}
}
