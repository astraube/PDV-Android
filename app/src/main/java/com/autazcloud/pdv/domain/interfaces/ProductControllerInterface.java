package com.autazcloud.pdv.domain.interfaces;

import android.content.Context;

import com.autazcloud.pdv.domain.models.Product;

public interface ProductControllerInterface {
	public Context getContext();
	public void insertProduct(Product product);
	public void updateProduct(Product product);
	public void removeProduct(Product product);
}
