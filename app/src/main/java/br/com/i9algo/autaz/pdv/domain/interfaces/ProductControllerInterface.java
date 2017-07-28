package br.com.i9algo.autaz.pdv.domain.interfaces;

import android.content.Context;

import br.com.i9algo.autaz.pdv.domain.models.Product;

public interface ProductControllerInterface {
	public Context getContext();
	public void insertProduct(Product product);
	public void updateProduct(Product product);
	public void removeProduct(Product product);
}
