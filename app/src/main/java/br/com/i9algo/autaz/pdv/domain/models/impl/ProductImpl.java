package br.com.i9algo.autaz.pdv.domain.models.impl;

import br.com.i9algo.autaz.pdv.domain.models.Category;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.helpers.MathUtils;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by aStraube on 09/07/2017.
 * Foi criada essa camada de implementacao pois o Realm não permite editar os parametros diretamente na Model
 */
public class ProductImpl {

    private Product _product;
    
    public ProductImpl (Product product) {
        this._product = product;
    }

    public void setProduct(Product product) { this._product = product; }
    public Product getProduct() { return this._product; }
    
    public void setCreatedAt(String createdAt) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setCreatedAt(createdAt);

        _realm.commitTransaction();
    }

    public void setUpdatedAt(String updatedAt) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setUpdatedAt(updatedAt);

        _realm.commitTransaction();
    }

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

    public void setPriceCost(double priceCost) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setPriceCost(priceCost);

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

    public boolean inStock() {
        return (this._product.getStockCurrent() >= 1);
    }
    public boolean inStockLimit() {
        return (this._product.getStockCurrent() <= this._product.getStockMin());
    }

    public void setStockCurrent(int stockCurrent) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setStockCurrent(stockCurrent);

        _realm.commitTransaction();
    }

    public void setStockMin(int stockMin) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setStockMin(stockMin);

        _realm.commitTransaction();
    }

    public void setStockMax(int stockMax) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setStockMax(stockMax);

        _realm.commitTransaction();
    }

    public void setCategoryId(long categoryId) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setCategoryId(categoryId);

        _realm.commitTransaction();
    }

    public void setValues(RealmList<RealmObject> values) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        //this._product.setValues(values);

        _realm.commitTransaction();
    }

    public void setCategory(Category category) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._product.setCategory(category);

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
}
