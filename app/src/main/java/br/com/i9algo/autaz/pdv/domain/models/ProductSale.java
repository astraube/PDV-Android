package br.com.i9algo.autaz.pdv.domain.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import br.com.i9algo.autaz.pdv.domain.models.impl.ProductSaleImpl;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Caso seja adicionado ou removido algum parametro, editar a implementacao
 */
public class ProductSale extends RealmObject {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento de versao


    @Ignore private volatile double priceDiscount;
    @Ignore private volatile double itemTotal;
    @Ignore private volatile String stringSearch;
    @Ignore private volatile ProductSaleImpl modelImpl; // Implementacao da model
    @Ignore private volatile boolean jockerProduct = false;

    @Since(0.1)
    @SerializedName("public_token")
    @Expose
    private String publicToken;

    @Since(0.1)
    @SerializedName("name")
    @Expose
    private String name;

    @Since(0.1)
    @SerializedName("description")
    @Expose
    private String description;

    @Since(0.1)
    @SerializedName("code_corporate")
    @Expose
    private String codeCorporate;

    @Since(0.1)
    @SerializedName("code_ean")
    @Expose
    private String codeEan;

    @Since(0.1)
    @SerializedName("price_resale")
    @Expose
    private double priceResale;

    @Since(0.1)
    @SerializedName("discount")
    @Expose
    private double discount = 0; // Desconto em valor monetario. Valor cadastrado na plataforma Web

    @Since(0.1)
    @SerializedName("is_request_pass")
    @Expose
    private int requestPass = 0;

    @Since(0.1)
    @SerializedName("quantity")
    @Expose
    private long quantity = 1;

    @Since(0.1)
    @SerializedName("annotation")
    @Expose
    private String annotation = "";

    /**
     * No args constructor for use in serialization
     *
     */
    public ProductSale() {
        this.modelImpl = new ProductSaleImpl(this);
    }

    public ProductSale (ProductSale productSale) {
        this.publicToken = productSale.getPublicToken();
        this.name = productSale.getName();
        this.description = productSale.getDescription();
        this.codeCorporate = productSale.getCodeCorporate();
        this.codeEan = productSale.getCodeEan();
        this.priceResale = productSale.getPriceResale();
        this.discount = productSale.getDiscount();
        this.requestPass = productSale.getRequestPass();
        this.jockerProduct = productSale.isJockerProduct();

        this.quantity = productSale.getQuantity();
        this.annotation = productSale.getAnnotation();

        this.modelImpl = new ProductSaleImpl(this);
    }

    public ProductSale (Product product) {
        this.publicToken = product.getPublicToken();
        this.name = product.getName();
        this.description = product.getDescription();
        this.codeCorporate = product.getCodeCorporate();
        this.codeEan = product.getCodeEan();
        this.priceResale = product.getPriceResale();
        this.discount = product.getDiscount();
        this.requestPass = product.getRequestPass();
        this.jockerProduct = product.isJockerProduct();

        this.modelImpl = new ProductSaleImpl(this);
    }


    public static String getRouteKeyName()
    {
        return "publicToken";
    }


    public boolean isJockerProduct() { return jockerProduct; }
    public void setJockerProduct(boolean value) { this.jockerProduct = value; }

    /**
     * Implementacao da model
     * @return
     */
    public ProductSaleImpl getModelImpl() {
        if (this.modelImpl == null)
            this.modelImpl = new ProductSaleImpl(this);

        return this.modelImpl;
    }


    public String getPublicToken() {
        return publicToken;
    }
    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeCorporate() {
        return codeCorporate;
    }
    public void setCodeCorporate(String codeCorporate) {
        this.codeCorporate = codeCorporate;
    }

    public String getCodeEan() {
        return codeEan;
    }
    public void setCodeEan(String codeEan) {
        this.codeEan = codeEan;
    }

    public double getPriceResale() {
        return priceResale;
    }
    public void setPriceResale(double priceResale) {
        this.priceResale = priceResale;
    }

    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double value) {
        discount = value;
    }

    public boolean isRequestPass() {
        return ((requestPass == 0) ? false : true);
    }
    public int getRequestPass() {
        return requestPass;
    }
    public void setRequestPass(int requestPass) {
        this.requestPass = requestPass;
    }

    public long getQuantity() { return quantity; }
    public void setQuantity(long quantity) { this.quantity = quantity; }

    public String getAnnotation() { return annotation; }
    public void setAnnotation(String annotation) { this.annotation = annotation; }


    public double getPriceDiscount() {
        double price = this.getPriceResale();

        if (price > 0)
            price = (price - this.getDiscount());

        return (price);
    }
    public double getItemTotal() {
        double total = (getPriceDiscount() * this.getQuantity());

        return total;
    }

    /**
     * Retorna uma string para busca organica no objeto
     * @return
     */
    public String getStringSearch() {
        String code = "" +
                getName() +
                getDescription() +
                getPriceResale() +
                getCodeCorporate() +
                getCodeEan();

        return code;
        //return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_FIELD_NAMES_STYLE);
    }

}