package br.com.i9algo.autaz.pdv.domain.models;


import br.com.i9algo.autaz.pdv.domain.models.impl.ProductImpl;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Caso seja adicionado ou removido algum parametro, editar a implementacao
 */
public class Product extends RealmObject {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento de versao

    public static final String JOCKER_NAME = "Diversos";
    public static final String JOCKER_ID = "0";
    public static final long JOCKER_PRICE = 0;


    @Ignore private volatile String stringSearch;
    @Ignore private volatile ProductImpl modelImpl; // Implementacao da model
    @Ignore private volatile boolean jockerProduct = false;

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
    @SerializedName("price_cost")
    @Expose
    private double priceCost;

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
    @SerializedName("stock_current")
    @Expose
    private int stockCurrent = 0; // Estoque atual

    @Since(0.1)
    @SerializedName("stock_min")
    @Expose
    private int stockMin = 0;

    @Since(0.1)
    @SerializedName("stock_max")
    @Expose
    private int stockMax = 0;

    @Since(0.1)
    @SerializedName("category_id")
    @Expose
    private long categoryId;

    /*@Since(0.1)
    @SerializedName("values")
    @Expose
    private RealmList<RealmObject> values = null;*/

    @Since(0.1)
    @SerializedName("category")
    @Expose
    private Category category;

    /**
     * No args constructor for use in serialization
     *
     */
    public Product() {
        this.modelImpl = new ProductImpl(this);
    }

    public Product (Product product) {
        super();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
        this.publicToken = product.getPublicToken();
        this.name = product.getName();
        this.description = product.getDescription();
        this.codeCorporate = product.getCodeCorporate();
        this.codeEan = product.getCodeEan();
        this.priceCost = product.getPriceCost();
        this.priceResale = product.getPriceResale();
        this.discount = product.getDiscount();
        this.requestPass = product.getRequestPass();
        this.stockCurrent = product.getStockCurrent();
        this.stockMin = product.getStockMin();
        this.stockMax = product.getStockMax();
        this.categoryId = product.getCategoryId();
        //this.values = product.getValues();
        this.category = product.getCategory();
    }

    public static Product createJockerProduct() {
        Product model = new Product();
        model.jockerProduct = true;
        model.modelImpl.setName(JOCKER_NAME);
        model.modelImpl.setStockCurrent(100);
        model.modelImpl.setPriceResale(JOCKER_PRICE);
        model.modelImpl.setPublicToken(JOCKER_ID);
        model.modelImpl.setCodeCorporate(JOCKER_ID);
        model.modelImpl.setCodeEan(JOCKER_ID);
        model.modelImpl.setRequestPass(0);

        return model;
    }
    public boolean isJockerProduct() { return jockerProduct; }
    public void setJockerProduct(boolean value) { this.jockerProduct = value; }


    public static String getRouteKeyName()
    {
        return "publicToken";
    }

    /**
     * Implementacao da model
     * @return
     */
    public ProductImpl getModelImpl() {
        if (this.modelImpl == null)
            this.modelImpl = new ProductImpl(this);

        return this.modelImpl;
    }


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

    public double getPriceCost() {
        return priceCost;
    }
    public void setPriceCost(double priceCost) {
        this.priceCost = priceCost;
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

    public int getStockCurrent() {
        return stockCurrent;
    }
    public void setStockCurrent(int stockCurrent) {
        this.stockCurrent = stockCurrent;
    }

    public int getStockMin() {
        return stockMin;
    }
    public void setStockMin(int stockMin) {
        this.stockMin = stockMin;
    }

    public int getStockMax() {
        return stockMax;
    }
    public void setStockMax(int stockMax) {
        this.stockMax = stockMax;
    }

    public long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /*public RealmList<RealmObject> getValues() {
        return values;
    }
    public void setValues(RealmList<RealmObject> values) {
        this.values = values;
    }*/

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    /*@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }*/

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