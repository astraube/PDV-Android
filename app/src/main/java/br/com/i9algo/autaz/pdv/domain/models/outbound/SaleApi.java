package br.com.i9algo.autaz.pdv.domain.models.outbound;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import java.util.Date;

import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.models.Client;
import br.com.i9algo.autaz.pdv.domain.models.PaymentSale;
import br.com.i9algo.autaz.pdv.domain.models.ProductSale;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import io.realm.RealmList;

/**
 * Created by aStraube on 05/10/2017.
 */

public class SaleApi {

    @Since(0.1)
    @SerializedName("public_token")
    @Expose
    private String publicToken;

    @Since(0.1)
    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @Since(0.1)
    @SerializedName("updated_at")
    @Expose
    private Date updatedAt;

    @Since(0.1)
    @SerializedName("annotations")
    @Expose
    private String annotations;

    @Since(0.1)
    @SerializedName("corporate_id")
    @Expose
    private long corporateId;

    @Since(0.1)
    @SerializedName("value_total_paid")
    @Expose
    private double valueTotalPaid;

    @Since(0.1)
    @SerializedName("discount")
    @Expose
    private double discount = 0;

    @Since(0.1)
    @SerializedName("status")
    @Expose
    private String status = SaleStatusEnum.OPEN.toString();

    @Since(0.1)
    @SerializedName("payment_sale")
    @Expose
    private RealmList<PaymentSale> paymentSale = null;

    @Since(0.1)
    @SerializedName("code_controll")
    @Expose
    private String codeControll = "";

    @Since(0.1)
    @SerializedName("saller")
    @Expose
    private String saller;

    @Since(0.1)
    @SerializedName("client")
    @Expose
    private Client client = null;

    @Since(0.1)
    @SerializedName("products")
    @Expose
    private RealmList<ProductSale> products = null;

    @Since(0.1)
    @SerializedName("order_code_wait")
    @Expose
    private String orderCodeWait;


    public SaleApi() {
    }
    public SaleApi(Sale sale) {
        super();
        this.publicToken = sale.getPublicToken();
        this.createdAt = sale.getCreatedAt();
        this.updatedAt = sale.getUpdatedAt();
        this.corporateId = sale.getCorporateId();
        this.valueTotalPaid = sale.getValueTotalPaid();
        this.annotations = sale.getAnnotations();
        this.discount = sale.getDiscount();
        this.status = sale.getStatus();
        //this.paymentSale = sale.getPaymentSale();
        this.codeControll = sale.getCodeControll();
        this.saller = sale.getSaller();
        //this.client = sale.getClient();
        //this.products = sale.getProducts();
        this.orderCodeWait = sale.getOrderCodeWait();
    }

    public Date getCreatedAt() { return createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPublicToken() { return publicToken; }
    private void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public double getValueTotalPaid() { return valueTotalPaid; }
    public void setValueTotalPaid(double valueTotalPaid) {
        this.valueTotalPaid = valueTotalPaid;
    }
}
