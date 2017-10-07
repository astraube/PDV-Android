package br.com.i9algo.autaz.pdv.domain.models;

/**
 * Created by aStraube on 13/07/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Calendar;
import java.util.Date;

import br.com.i9algo.autaz.pdv.domain.constants.Constants;
import br.com.i9algo.autaz.pdv.domain.constants.DateFormats;
import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Sale extends RealmObject /*implements Parcelable*/ {

    public static final double CURRENT_VERSION = 0.1; // Apenas para conhecimento de versao

    @Ignore
    private volatile double totalSale;
    @Ignore private volatile double totalRestPay;
    @Ignore private volatile double totalProducts;
    @Ignore private volatile boolean increment;
    @Ignore private volatile boolean decrement;
    @Ignore private volatile int countProducts;
    @Ignore private volatile boolean existsProduct;
    @Ignore private volatile boolean addPayment;
    @Ignore private volatile boolean clear;
    @Ignore private volatile String stringSearch;



    private boolean syncronized = false; // indica que foi sincronizado com a API WEB

    public boolean isSyncronized() { return this.syncronized; }
    public void setSyncronized(boolean sync) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.syncronized = sync;

        _realm.commitTransaction();
    }

    @Index
    @PrimaryKey
    private String id;

    @Since(0.1)
    @SerializedName("public_token")
    @Expose
    private String publicToken;

    @Since(0.1)
    @SerializedName("user_id")
    @Expose
    private long userId;

    @Since(0.1)
    @SerializedName("account_id")
    @Expose
    private long accountId;

    @Since(0.1)
    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @Since(0.1)
    @SerializedName("updated_at")
    @Expose
    private Date updatedAt;

    @Since(0.1)
    @SerializedName("corporate_id")
    @Expose
    private long corporateId;

    @Since(0.1)
    @SerializedName("id_bank_account")
    @Expose
    private long idBankAccount;

    //@Since(0.1)
    //@SerializedName("value_total")
    //@Expose
    //private double valueTotal;

    @Since(0.1)
    @SerializedName("value_total_paid")
    @Expose
    private double valueTotalPaid;

    @Since(0.1)
    @SerializedName("annotations")
    @Expose
    private String annotations;

    @Since(0.1)
    @SerializedName("discount")
    @Expose
    private double discount = 0; // Desconto em toda a venda

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
    private String codeControll = ""; // Codigo da venda para controle particular do estabelecimento. EX: Numero da mesa

    @Since(0.1)
    @SerializedName("saller")
    @Expose
    private String saller; // Vendedor TODO - Melhorar forma de cadastro de vendedor

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
    private String orderCodeWait; // Codigo de espera do pedido


    public Sale() {
    }
    public Sale(Sale sale) {
        super();
        this.id = sale.getId();
        this.publicToken = sale.getPublicToken();
        this.userId = sale.getUserId();
        this.accountId = sale.getAccountId();
        this.createdAt = sale.getCreatedAt();
        this.updatedAt = sale.getUpdatedAt();
        this.corporateId = sale.getCorporateId();
        this.idBankAccount = sale.getIdBankAccount();
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
    public Sale (Client client) {
        products = new RealmList<ProductSale>();
        createdAt = new Date();
        createdAt = new Date();
        id = generateCodeSale();
        setStatus(SaleStatusEnum.OPEN);

        setClient(client);
    }


    public static String getRouteKeyName()
    {
        return "id";
    }

    public String getId() { return id; }
    public void setId(String id) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.id = id;

        _realm.commitTransaction();
    }

    public Date getCreatedAt() { return createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.updatedAt = updatedAt;

        _realm.commitTransaction();
    }

    public String getPublicToken() { return publicToken; }
    private void setPublicToken(String publicToken) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.publicToken = publicToken;

        _realm.commitTransaction();
    }

    public long getUserId() { return userId; }

    public long getAccountId() { return accountId; }

    public String getAnnotations() { return annotations; }
    public void setAnnotations(String annotations) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.annotations = annotations;

        _realm.commitTransaction();
    }

    public long getCorporateId() { return corporateId; }
    /*public void setCorporateId(long corporateId) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.corporateId = corporateId;

        _realm.commitTransaction();
    }*/

    public long getIdBankAccount() { return idBankAccount; }
    /*public void setIdBankAccount(long idBankAccount) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.idBankAccount = idBankAccount;

        _realm.commitTransaction();
    }*/

    /*public double getValueTotal() { return valueTotal; }
    public void setValueTotal(double valueTotal) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.valueTotal = valueTotal;

        _realm.commitTransaction();
    }*/

    public double getValueTotalPaid() { return valueTotalPaid; }
    public void setValueTotalPaid(double valueTotalPaid) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.valueTotalPaid = valueTotalPaid;

        _realm.commitTransaction();
    }

    /**
     *  desconto da venda em R$
     */
    public double getDiscount() { return discount; }
    public void setDiscount(double value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        discount = value;

        _realm.commitTransaction();
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(SaleStatusEnum status) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.status = status.toString();

        _realm.commitTransaction();
    }
    public void setStatus(String status) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();
        this.status = status;
        _realm.commitTransaction();
    }

    public RealmList<PaymentSale> getPaymentSale() {
        return paymentSale;
    }
    public void setAddPayment(RealmList<PaymentSale> paymentSale) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.paymentSale = paymentSale;

        _realm.commitTransaction();
    }
    public void setAddPayment(PaymentSale payment) {
        if (this.paymentSale == null) {
            this.paymentSale = new RealmList<PaymentSale>();
        }
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.paymentSale.add(payment);

        _realm.commitTransaction();

        this.setValueTotalPaid(payment.getAmountPaid());
    }

    public String getCodeControll() { return codeControll; }
    public void setCodeControll(String saleCodeControll) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();
        this.codeControll = saleCodeControll;
        _realm.commitTransaction();
    }

    public String getSaller() { return saller; }
    public void setSaller(String saller) {

        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();
        this.saller = saller;
        _realm.commitTransaction();
    }

    public Client getClient() {return client;}
    public void setClient(Client client) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.client = client;

        _realm.commitTransaction();
    }

    public RealmList<ProductSale> getProducts() { return products; }
    public void setProducts(RealmList<ProductSale> products) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.products = products;

        _realm.commitTransaction();
    }

    public boolean existsOrderCodeWait() {
        return (this.orderCodeWait != null && !this.orderCodeWait.isEmpty());
    }
    public String getOrderCodeWait() { return orderCodeWait; }
    public void setOrderCodeWait(String orderCodeWait) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this.orderCodeWait = orderCodeWait;

        _realm.commitTransaction();
    }




    /**
     * Retorna o valor total dos produtos com desconto
     * @return
     */
    public double getTotalProducts() {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        double d = 0;
        for (ProductSale item : products) {

            d +=(item.getPriceDiscount() * item.getQuantity());
        }
        _realm.commitTransaction();
        return d;
    }

    /**
     * Retorna o valor total da venda com desconto
     * @return
     */
    public double getTotalSale() {
        double total = getTotalProducts();
        double desc = discount;

        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        if (total > 0 && total > desc)
            total = (total - desc);

        _realm.commitTransaction();

        return (total);
    }

    /**
     * Retorna o valor restante a pagar da venda
     * @return
     */
    public double getTotalRestPay() {
        if (getTotalSale() > getValueTotalPaid()) {
            return getTotalSale() - getValueTotalPaid();
        } else {
            return 0;
        }
    }

    /**
     * Retorna um item a partir de um incice
     * @param index
     * @return
     */
    public ProductSale get(int index) {
        final ProductSale item = products.get(index);
        return item;
    }

    /**
     * Retorna o tamanho da lista, a quantidade de produtos diferentes
     */
	/*@Override
	public int size() {
		Log.v("@@@", "@@@ size - ");
		return this.size();
	}*/

    /**
     * Retorna a quantidade total de produtos
     * @return
     */
    public int getCountProducts() {
        int aux = 0;
        for (ProductSale item : products) {
            aux += item.getQuantity();
        }
        return aux;
    }

    public boolean isExistsProduct(ProductSale saleItem) {
        for (ProductSale i : products) {
            if (i.getPublicToken().equals(saleItem.getPublicToken())) {
                return true;
            }
        }
        return false;
    }

    public ProductSale getExistsProduct(ProductSale saleItem) {
        for (ProductSale i : products) {
            if (i.getPublicToken().equals(saleItem.getPublicToken())) {
                return i;
            }
        }
        return null;
    }

    private static String generateCodeSale() {
        Calendar c = Calendar.getInstance();
        String code = "" + 	String.format("%02d", c.get(Calendar.DAY_OF_MONTH)) +
                String.format("%02d", c.get(Calendar.MONTH) + 1) +
                String.format("%04d", c.get(Calendar.YEAR)) +
                String.format("%02d", c.get(Calendar.HOUR)) +
                String.format("%02d", c.get(Calendar.MINUTE)) +
                String.format("%02d", c.get(Calendar.SECOND)) +
                String.format("%04d", Constants.CODE_OP);

        return code;
    }

    /**
     * Adicionar um novo item a lista de compra
     * @param product
     * @return
     */
    public ProductSale setIncrement(Product product) {
        ProductSale ps = new ProductSale(product);
        return this.setIncrement(ps);
    }
    public ProductSale setIncrement(ProductSale saleItem) {
        ProductSale item = this.getExistsProduct(saleItem);

        if (item == null) {
            Realm _realm = Realm.getDefaultInstance();
            _realm.beginTransaction();
            products.add(saleItem);
            _realm.commitTransaction();

            return saleItem;
        } else {
            if (!saleItem.isJockerProduct()) {
                item.getModelImpl().setAddItem();
            } else {
                double newPrice = saleItem.getPriceResale();
                double curPrice = item.getPriceResale();
                item.getModelImpl().setPriceResale(curPrice + newPrice);
            }
            return item;
        }
    }

    /**
     * Remover um item da lista de compra
     * @param product
     */
    public boolean setDecrement(Product product) {
        return setDecrement(new ProductSale(product));
    }
    public boolean setDecrement(ProductSale saleItem) {
        ProductSale item = this.getExistsProduct(saleItem);

        if (item != null) {
            //Log.v(TAG, "decrement: - " + item.getName());

            item.getModelImpl().setRemoveItem();

            Realm _realm = Realm.getDefaultInstance();
            _realm.beginTransaction();
            if (item.getQuantity() < 1) {
                products.remove(item);

                // Caso for um produto que precisa de senha de pedido, verifica se pode remover a senha atual
                if (item.isRequestPass()) {
                    String currentOrderCodeWait = null;
                    for (ProductSale si : products) {
                        if (si.isRequestPass()) {
                            currentOrderCodeWait = getOrderCodeWait();
                            break;
                        }
                    }
                    orderCodeWait = currentOrderCodeWait;
                }
            }
            _realm.commitTransaction();
            //Log.v("@@@", "decrement: unidades - " + item.getQuantityItem());

            return true;
        }
        return false;
    }

    public void setClear() {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();
        products.clear();
        orderCodeWait = null;
        //valueTotal = 0; // TODO - valueTotal desabilitado
        valueTotalPaid = 0;
        discount = 0;
        _realm.commitTransaction();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Retorna uma string para busca organica no objeto
     * @return
     */
    public String getStringSearch() {
        String code = "" +
                getOrderCodeWait() +
                getTotalSale() +
                getId() +
                getSaller() +
                DateFormats.formatDateCommercial(getCreatedAt());

        if (getClient() != null) {
            code += getClient().getName() +
                    getClient().getCode() +
                    getClient().getCpfCnpj() +
                    getClient().getEmail() +
                    getClient().getPhone();
        }

        return code;
        //return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_FIELD_NAMES_STYLE);
    }



    /// PARCELABLE
    /*public Sale(Parcel parcel) {
        setPublicToken(parcel.readString());
        setValueTotalPaid(parcel.readDouble());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getPublicToken());
        dest.writeDouble(getValueTotalPaid());
    }
    public static final Parcelable.Creator<Sale> CREATOR = new Parcelable.Creator<Sale>(){
        @Override
        public Sale createFromParcel(Parcel source) {
            return new Sale(source);
        }
        @Override
        public Sale[] newArray(int size) {
            return new Sale[size];
        }
    };*/
}