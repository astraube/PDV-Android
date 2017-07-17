package com.autazcloud.pdv.domain.models;

import com.autazcloud.pdv.domain.interfaces.SaleProductItemInterface;
import com.autazcloud.pdv.helpers.MathUtils;
import com.autazcloud.pdv.helpers.ObjectUtil;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class SaleItemModel extends RealmObject implements SaleProductItemInterface {

	@Ignore private volatile SaleItemModel clone;
	@Ignore private volatile double priceDiscount;
	@Ignore private volatile double discountPercent;
	@Ignore private volatile double itemTotal;
	@Ignore private volatile String stringSearch;
	@Ignore private volatile boolean clear;
	@Ignore private volatile boolean addItem;
	@Ignore private volatile boolean removeItem;
	@Ignore private volatile int countSaleItem;
	@Ignore private volatile boolean jockerProduct = false;

	@PrimaryKey
	private String id;

	private String name;
	private String codeCorporate;
	private String codeEan;
	private long categoryId;
	private double priceResale = 0;
	private double discount = 0; // Desconto em valor monet�rio - R$
	private int stockCurrent;
	private int requestPass = 0;

	private int quantityItem = 1;
	private String annotation = "";

	//@LinkingObjects("products")
	//private final RealmResults<SaleModel> owners;
	
	
	public static SaleItemModel newSaleItemFromJson(String jsonObject) {
		SaleItemModel model = (SaleItemModel) ObjectUtil.JsonToObject(jsonObject, SaleItemModel.class);
		return model;
	}
	
	public SaleItemModel () {
	}

	public SaleItemModel (SaleItemModel saleItemModel) {
		id = saleItemModel.getId();
		name = saleItemModel.getName();
		codeCorporate = saleItemModel.getCodeCorporate();
		codeEan = saleItemModel.getCodeEan();
		categoryId = saleItemModel.getCategoryId();
		priceResale = saleItemModel.getPriceResale();
		discount = saleItemModel.getDiscount();
		stockCurrent = saleItemModel.getStockCurrent();
		requestPass = saleItemModel.getRequestPass();
		quantityItem = saleItemModel.getQuantityItem();
		annotation = saleItemModel.getAnnotation();
		jockerProduct = saleItemModel.isJockerProduct();
	}

	public SaleItemModel (Product product) {
		id = product.getPublicToken();
		name = product.getName();
		codeCorporate = product.getCodeCorporate();
		codeEan = product.getCodeEan();
		categoryId = product.getCategoryId();
		priceResale = product.getPriceResale();
		discount = product.getDiscount();
		stockCurrent = product.getStockCurrent();
		requestPass = product.getRequestPass();
		jockerProduct = product.isJockerProduct();
	}

	public boolean isJockerProduct() { return jockerProduct; }
	public void setJockerProduct(boolean value) { this.jockerProduct = value; }


	public String getId() { return id; }
	public void setId(String value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.id = value;
		_realm.commitTransaction();
	}

	public String getName() { return name; }
	public void setName(String value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.name = value;
		_realm.commitTransaction();
	}

	public String getCodeCorporate() { return codeCorporate; }
	public void setCodeCorporate(String value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.codeCorporate = value;
		_realm.commitTransaction();
	}

	public String getCodeEan() { return  codeEan; }
	public void setCodeEan(String value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.codeEan = value;
		_realm.commitTransaction();
	}

	public long getCategoryId() { return categoryId; }
	public void setCategoryId(long value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.categoryId = value;
		_realm.commitTransaction();
	}

	public double getPriceResale() {
		return this.priceResale;
	}
	public void setPriceResale(double value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.priceResale = value;
		_realm.commitTransaction();
	}


	/**
	 * Valor de venda do produto (com desconto)
	 */
	public double getPriceDiscount() {
		if (this.priceResale > 0)
			return (this.priceResale - discount);
		else
			return (this.priceResale);
	}
	public void setPriceDiscount(double value) {
		Realm _realm = Realm.getDefaultInstance();
		//_realm.beginTransaction();

		//_realm.commitTransaction();

	}

	/**
	 * Retorna o desconto em R$
	 * @return
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * Altera o desconto a partir de um valor monet�rio - R$
	 */
	public void setDiscount(double value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		discount = value;
		_realm.commitTransaction();
	}

	/**
	 * Retorna o desconto em %
	 * @return
	 */
	public double getDiscountPercent() {
		if (priceResale > 0 && discount > 0)
			return MathUtils.valueToPercent(priceResale, discount);
		else
			return 0;
	}

	// Altera o desconto a partir de uma porcentagem
	public void setDiscountPercent(double value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		if (priceResale > 0 && value > 0)
			discount = MathUtils.percentToValue(priceResale, value);
		_realm.commitTransaction();
	}

	// Quantidade de itens no estoque
	public int getStockCurrent() { return stockCurrent; }
	public void setStockCurrent(int value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.stockCurrent = value;
		_realm.commitTransaction();
	}


	//Produto gera senha de espera
	public boolean isRequestPass() { return ((requestPass == 0) ? false : true); }
	public int getRequestPass() { return this.requestPass; }
	public void setRequestPass(int value) {
		this.requestPass = value;
	}
	public void setRequestPass(boolean value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.requestPass = ((!value) ? 0 : 1);
		_realm.commitTransaction();
	}

	public String getAnnotation() {
		return this.annotation;
	}
	public void setAnnotation(String value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.annotation = value;
		_realm.commitTransaction();
	}

	/**
	 * Valor de venda original do produto X quantidade
	 */
	public double getItemTotal() {
		return (getPriceResale() * this.quantityItem);
	}
	
	/**
	 * Quantidade do itens na venda
	 */
	@Override
	public int getQuantityItem() {
		return quantityItem;
	}
	@Override
	public void setQuantityItem(int value) {
		Realm _realm = Realm.getDefaultInstance();
		_realm.beginTransaction();
		this.quantityItem = value;
		_realm.commitTransaction();
	}
	
	/**
	 * Adicionar um item � quantidade
	 */
	@Override
	public void setAddItem() {
		this.setQuantityItem(this.getQuantityItem() + 1);
	}
	
	/**
	 * Adicionar um item � quantidade
	 */
	@Override
	public void setRemoveItem() {
		if (this.quantityItem > 0)
			this.setQuantityItem(this.getQuantityItem() - 1);
	}
	
	/*@Override
	public String toString() {
		return ObjectUtil.ObjectToJson(this);
	}*/
	
}
