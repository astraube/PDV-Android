package com.autazcloud.pdv.domain.models.inbound;

import com.autazcloud.pdv.data.remote.ResultStatusDefault;
import com.autazcloud.pdv.domain.models.Product;

import java.util.List;

public class ProductsArraylistWrapper {

    public ResultStatusDefault error;
    public ResultStatusDefault success;
    public List<Product> data;

    public List<Product> getProducts() { return data; }
    public void setProducts(List<Product> data) { this.data = data; }
}
