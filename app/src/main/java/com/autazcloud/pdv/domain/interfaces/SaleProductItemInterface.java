package com.autazcloud.pdv.domain.interfaces;

public interface SaleProductItemInterface {
	
	public String getName();
	
	/**
	 * Quantidade do iten adicionado na venda
	 * @return
	 */
	public int getQuantityItem();
	public void setQuantityItem(int value);

	public int getRequestPass();
	public void setRequestPass(int value);
	
	public void setAddItem();
	public void setRemoveItem();
	
	/**
	 * Pre�o atual do produto
	 * @return
	 */
	public double getPriceResale();
	public void setPriceResale(double value);
}
