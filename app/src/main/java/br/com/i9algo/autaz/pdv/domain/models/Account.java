package br.com.i9algo.autaz.pdv.domain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aStraube on 20/09/2017.
 */

public class Account extends RealmObject {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento da versao atual da model na API WEB


    @Since(0.1)
    @SerializedName("public_token")
    @Expose
    @PrimaryKey
    private String publicToken;

    @Since(0.1)
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @Since(0.1)
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @Since(0.1)
    @SerializedName("client_id")
    @Expose
    private String clientId;

    @Since(0.1)
    @SerializedName("signature_plan")
    @Expose
    private SignaturePlan signaturePlan;


    public Account() {
    }

    public Account(Account account) {
        super();
        this.publicToken = account.getPublicToken();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
        this.clientId = account.getClientId();
        this.signaturePlan = account.getSignaturePlan();
    }

    public static String getRouteKeyName()
    {
        return "publicToken";
    }


    public String getPublicToken() {
        return publicToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getClientId() {
        return clientId;
    }

    public SignaturePlan getSignaturePlan() {
        return signaturePlan;
    }
    public void setSignaturePlan(SignaturePlan plan) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.signaturePlan = plan;

        _realm.commitTransaction();
    }
}