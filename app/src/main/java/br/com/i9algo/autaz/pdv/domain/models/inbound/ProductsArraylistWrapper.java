package br.com.i9algo.autaz.pdv.domain.models.inbound;

import java.util.List;

import br.com.i9algo.autaz.pdv.domain.models.Product;

public class ProductsArraylistWrapper {

    public ResultStatusDefault error;
    public ResultStatusDefault success;
    public List<Product> data;

    public List<Product> getProducts() { return data; }
    public void setProducts(List<Product> data) { this.data = data; }
}
