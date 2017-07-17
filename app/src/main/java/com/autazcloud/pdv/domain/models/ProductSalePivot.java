package com.autazcloud.pdv.domain.models;

/**
 * Created by aStraube on 13/07/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.realm.RealmObject;

public class ProductSalePivot extends RealmObject {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento de versao

    @Since(0.1)
    @SerializedName("sale_id")
    @Expose
    private long saleId;

    @Since(0.1)
    @SerializedName("product_id")
    @Expose
    private long productId;

    @Since(0.1)
    @SerializedName("current_name")
    @Expose
    private String currentName;

    @Since(0.1)
    @SerializedName("current_price")
    @Expose
    private String currentPrice;

    @Since(0.1)
    @SerializedName("quantity")
    @Expose
    private long quantity;


    public ProductSalePivot() {

    }

    public long getSaleId() { return saleId; }
    public void setSaleId(long saleId) { this.saleId = saleId; }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public String getCurrentName() { return currentName; }
    public void setCurrentName(String currentName) { this.currentName = currentName; }

    public String getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(String currentPrice) { this.currentPrice = currentPrice; }

    public long getQuantity() { return quantity; }
    public void setQuantity(long quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}