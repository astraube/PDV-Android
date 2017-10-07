package br.com.i9algo.autaz.pdv.domain.models;

/**
 * Created by aStraube on 15/07/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

import br.com.i9algo.autaz.pdv.domain.enums.PaymentMethodEnum;
import io.realm.RealmObject;

public class PaymentSale extends RealmObject {

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    private Date updatedAt;

    @SerializedName("value_paid")
    @Expose
    private double amountPaid;

    @SerializedName("sale_id")
    @Expose
    private long saleId;
    /*
    @SerializedName("method_id")
    @Expose
    private long methodId;
    @SerializedName("method")
    @Expose
    private PaymentMethod method;*/

    private String paymentMethod;


    public PaymentSale() {}

    public PaymentSale(String method, double amountPaid) {
        setMethod(method);
        setAmountPaid(amountPaid);
        setCreatedAt(new Date());
    }
    public PaymentSale(PaymentMethodEnum method, double amountPaid) {
        this(method.toString(), amountPaid);
    }


    public Date getCreatedAt() {
        return createdAt;
    }
    private void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getAmountPaid() {
        return amountPaid;
    }
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;

        setUpdatedAt(new Date());
    }

    public long getSaleId() {
        return saleId;
    }
    public void setSaleId(long saleId) {
        this.saleId = saleId;

        setUpdatedAt(new Date());
    }

    /*public long getMethodId() {
        return methodId;
    }
    public void setMethodId(long methodId) {
        this.methodId = methodId;

        setUpdatedAt(DateFormats.formatDate(new Date(), DateFormats.getDateTimeGlobal()));
    }

    public PaymentMethod getMethod() {
        return method;
    }
    public void setMethod(PaymentMethod method) {
        this.method = method;

        setUpdatedAt(DateFormats.formatDate(new Date(), DateFormats.getDateTimeGlobal()));
    }*/

    public String getMethod() { return paymentMethod; }
    public void setMethod(String value) {
        this.paymentMethod = value;

        setUpdatedAt(new Date());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}