package br.com.i9algo.autaz.pdv.domain.models.inbound;

import br.com.i9algo.autaz.pdv.domain.models.Product;

public class ProductWrapper {

    public Object error;
    public Object success;
    public Product data;

    public Product getProduct() { return data; }
    public void setProduct(Product data) { this.data = data; }
}
