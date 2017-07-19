package com.autazcloud.pdv.domain.models;

/**
 * Created by aStraube on 15/07/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.realm.RealmObject;

public class PaymentSale extends RealmObject {

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("value_paid")
    @Expose
    private String valuePaid;
    @SerializedName("sale_id")
    @Expose
    private long saleId;
    @SerializedName("method_id")
    @Expose
    private long methodId;
    @SerializedName("method")
    @Expose
    private PaymentMethod method;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getValuePaid() {
        return valuePaid;
    }

    public void setValuePaid(String valuePaid) {
        this.valuePaid = valuePaid;
    }

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public long getMethodId() {
        return methodId;
    }

    public void setMethodId(long methodId) {
        this.methodId = methodId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}