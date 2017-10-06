package br.com.i9algo.autaz.pdv.domain.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import br.com.i9algo.autaz.pdv.domain.constants.DateFormats;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by aStraube on 04/10/2017.
 *
 * Os dados do plano selecionado sao mantidos localmente para fazer uma verificacao da expiracao da conta
 */
public class SignaturePlan extends RealmObject {

    /*@SerializedName("signature_plan_id")
    @Expose
    private int signaturePlanId;*/


    @Ignore
    private volatile Date expireDate = null; // Apenas para economizar processamento...


    @SerializedName("expire_at")
    @Expose
    private String expireAt;

    @SerializedName("signature_at")
    @Expose
    private String signatureAt;

    @SerializedName("current_price")
    @Expose
    private String currentPrice;

    @SerializedName("annotations")
    @Expose
    private String annotations;



    public SignaturePlan() {
    }

    public SignaturePlan(SignaturePlan plan) {
        super();
        //this.signaturePlanId = plan.getSignaturePlanId();
        this.expireAt = plan.getExpireAt();
        this.signatureAt = plan.getSignatureAt();
        this.currentPrice = plan.getCurrentPrice();
        this.annotations = plan.getAnnotations();
    }


    /*public int getSignaturePlanId() {
        return signaturePlanId;
    }*/

    public String getExpireAt() {
        return expireAt;
    }
    public Date getExpireDate() {
        if (expireAt != null && expireDate == null)
            expireDate = DateFormats.parseDate(expireAt, DateFormats.FORMAT_DATE_GLOBAL);

        return expireDate;
    }
    public boolean hasExpired() {
        Date today = new Date();
        Date expireDate = getExpireDate();

        return (expireDate != null && (today.after(expireDate)));
    }

    public String getSignatureAt() {
        return signatureAt;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public String getAnnotations() {
        return annotations;
    }
}