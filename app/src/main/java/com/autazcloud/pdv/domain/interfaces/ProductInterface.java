package com.autazcloud.pdv.domain.interfaces;

public interface ProductInterface {

	public String getPublicToken();
	public void setPublicToken(String value);

	public String getName();
	public void setName(String value);

	public String getCodeCorporate();
	public void setCodeCorporate(String value);

	public String getCodeEan();
	public void setCodeEan(String value);

	public long getCategoryId();
	public void setCategoryId(long value);

	public double getPriceResale();
	public void setPriceResale(double value);
	
	public double getDiscount();
	public void setDiscount(double value);

	public void setStockCurrent(int value);
	public int getStockCurrent();

	public int getRequestPass();
	public void setRequestPass(int value);
}
