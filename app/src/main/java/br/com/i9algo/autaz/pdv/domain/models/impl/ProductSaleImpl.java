package br.com.i9algo.autaz.pdv.domain.models.impl;

import br.com.i9algo.autaz.pdv.domain.models.ProductSale;
import br.com.i9algo.autaz.pdv.helpers.MathUtils;
import io.realm.Realm;

/**
 * Created by aStraube on 09/07/2017.
 * Foi criada essa camada de implementacao pois o Realm não permite editar os parametros diretamente na Model
 */
public class ProductSaleImpl {

    private ProductSale _product;

    public ProductSaleImpl(ProductSale product) {
        this._product = product;
    }

    public void setProduct(ProductSale product) { this._product = product; }
    public ProductSale getProduct() { return this._product; }

    public void setPublicToken(String publicToken) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setPublicToken(publicToken);

        _realm.commitTransaction();
    }

    public void setName(String name) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setName(name);

        _realm.commitTransaction();
    }

    public void setDescription(String description) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setDescription(description);

        _realm.commitTransaction();
    }

    public void setCodeCorporate(String codeCorporate) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setCodeCorporate(codeCorporate);

        _realm.commitTransaction();
    }

    public void setCodeEan(String codeEan) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setCodeEan(codeEan);

        _realm.commitTransaction();
    }

    public void setPriceResale(double priceResale) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setPriceResale(priceResale);

        _realm.commitTransaction();
    }

    public void setDiscount(double value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setDiscount(value);

        _realm.commitTransaction();
    }

    public void setRequestPass(int requestPass) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setRequestPass(requestPass);

        _realm.commitTransaction();
    }

    public void setRequestPass(boolean value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setRequestPass((!value) ? 0 : 1);

        _realm.commitTransaction();
    }

    public void setQuantity(long quantity) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setQuantity(quantity);

        _realm.commitTransaction();
    }

    public void setAnnotation(String annotation) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setAnnotation(annotation);

        _realm.commitTransaction();
    }


    /**
     * Valor de venda do produto (com desconto)
     */
    public double getPriceDiscount() {
        double price = this._product.getPriceResale();

        if (price > 0)
            price = (price - this._product.getDiscount());

        return (price);
    }
    public void setPriceDiscount(double value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        double price = this._product.getPriceResale();

        if (value < price)
            this._product.setDiscount(price - value);

        this._product.setPriceResale(value);

        _realm.commitTransaction();
    }

    /**
     * Retorna o desconto em %
     * @return
     */
    public double getDiscountPercent() {
        if (this._product.getPriceResale() > 0 && this._product.getDiscount() > 0)
            return MathUtils.valueToPercent(this._product.getPriceResale(), this._product.getDiscount());
        else
            return 0;
    }

    // Altera o desconto a partir de uma porcentagem
    public void setDiscountPercent(double value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        double price = this._product.getPriceResale();

        if (price > 0 && value > 0) {
            double discount = MathUtils.percentToValue(price, value);
            this._product.setDiscount(discount);
        }

        _realm.commitTransaction();
    }

    public void setAddItem() {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setQuantity(this._product.getQuantity() + 1);

        _realm.commitTransaction();
    }

    public void setRemoveItem() {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        if (this._product.getQuantity() > 0)
            this._product.setQuantity(this._product.getQuantity() - 1);

        _realm.commitTransaction();
    }

    /**
     * Valor de venda (com desconto) do produto X quantidade
     */
    public double getItemTotal() {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        double total = (getPriceDiscount() * this._product.getQuantity());

        _realm.commitTransaction();

        return total;
    }
}
