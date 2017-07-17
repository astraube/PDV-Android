package com.autazcloud.pdv.domain.models;

/**
 * Created by aStraube on 13/07/2017.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Sale extends RealmObject {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento de versao


    @Since(0.1)
    @SerializedName("id")
    @Expose
    private long id;

    @Since(0.1)
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @Since(0.1)
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @Since(0.1)
    @SerializedName("public_token")
    @Expose
    private String publicToken;

    @Since(0.1)
    @SerializedName("annotations")
    @Expose
    private String annotations;

    @Since(0.1)
    @SerializedName("corporate_id")
    @Expose
    private long corporateId;

    @Since(0.1)
    @SerializedName("client_id")
    @Expose
    private long clientId;

    @Since(0.1)
    @SerializedName("id_bank_account")
    @Expose
    private long idBankAccount;

    @Since(0.1)
    @SerializedName("user_id")
    @Expose
    private long userId;

    @Since(0.1)
    @SerializedName("account_id")
    @Expose
    private long accountId;

    @Since(0.1)
    @SerializedName("value_total")
    @Expose
    private String valueTotal;

    @Since(0.1)
    @SerializedName("value_total_paid")
    @Expose
    private String valueTotalPaid;

    @Since(0.1)
    @SerializedName("payment_sale")
    @Expose
    private RealmList<PaymentSale> paymentSale = null;

    @Since(0.1)
    @SerializedName("client")
    @Expose
    private Client client;

    @Since(0.1)
    @SerializedName("products")
    @Expose
    private RealmList<Product> products = null;


    public Sale() {

    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getPublicToken() { return publicToken; }
    public void setPublicToken(String publicToken) { this.publicToken = publicToken; }

    public String getAnnotations() { return annotations; }
    public void setAnnotations(String annotations) { this.annotations = annotations; }

    public long getCorporateId() { return corporateId; }
    public void setCorporateId(long corporateId) { this.corporateId = corporateId; }

    public long getClientId() { return clientId; }
    public void setClientId(long clientId) { this.clientId = clientId; }

    public long getIdBankAccount() { return idBankAccount; }
    public void setIdBankAccount(long idBankAccount) { this.idBankAccount = idBankAccount; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }

    public String getValueTotal() { return valueTotal; }
    public void setValueTotal(String valueTotal) { this.valueTotal = valueTotal; }

    public String getValueTotalPaid() { return valueTotalPaid; }
    public void setValueTotalPaid(String valueTotalPaid) { this.valueTotalPaid = valueTotalPaid; }

    public RealmList<PaymentSale> getPaymentSale() {return paymentSale;}
    public void setPaymentSale(RealmList<PaymentSale> paymentSale) {this.paymentSale = paymentSale;}

    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}

    public RealmList<Product> getProducts() { return products; }
    public void setProducts(RealmList<Product> products) { this.products = products; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}