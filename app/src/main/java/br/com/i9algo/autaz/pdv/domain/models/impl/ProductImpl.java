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

    private Product _model;
    
    public ProductImpl (Product model) {
        this._model = model;
    }

    public void setModel(Product model) { this._model = model; }
    public Product getModel() { return this._model; }

    // TODO - remover! o token deve ser criado pelo sistema WEB
    public void setPublicToken(String publicToken) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setPublicToken(publicToken);

        _realm.commitTransaction();
    }

    public void setName(String name) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setName(name);

        _realm.commitTransaction();
    }

    public void setDescription(String description) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setDescription(description);

        _realm.commitTransaction();
    }

    public void setCodeCorporate(String codeCorporate) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setCodeCorporate(codeCorporate);

        _realm.commitTransaction();
    }

    public void setCodeEan(String codeEan) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setCodeEan(codeEan);

        _realm.commitTransaction();
    }

    public void setPriceCost(double priceCost) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setPriceCost(priceCost);

        _realm.commitTransaction();
    }

    public void setPriceResale(double priceResale) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setPriceResale(priceResale);

        _realm.commitTransaction();
    }

    public void setDiscount(double value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setDiscount(value);

        _realm.commitTransaction();
    }

    public void setRequestPass(int requestPass) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setRequestPass(requestPass);

        _realm.commitTransaction();
    }

    public void setRequestPass(boolean value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setRequestPass((!value) ? 0 : 1);

        _realm.commitTransaction();
    }

    public boolean inStock() {
        return (this._model.getStockCurrent() >= 1);
    }
    public boolean inStockLimit() {
        return (this._model.getStockCurrent() <= this._model.getStockMin());
    }

    public void setStockCurrent(int stockCurrent) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setStockCurrent(stockCurrent);

        _realm.commitTransaction();
    }

    public void setStockMin(int stockMin) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setStockMin(stockMin);

        _realm.commitTransaction();
    }

    public void setStockMax(int stockMax) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setStockMax(stockMax);

        _realm.commitTransaction();
    }

    public void setCategoryId(long categoryId) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setCategoryId(categoryId);

        _realm.commitTransaction();
    }

    public void setValues(RealmList<RealmObject> values) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        //this._model.setValues(values);

        _realm.commitTransaction();
    }

    public void setCategory(Category category) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        this._model.setCategory(category);

        _realm.commitTransaction();
    }





    /**
     * Valor de venda do produto (com desconto)
     */
    public double getPriceDiscount() {
        double price = this._model.getPriceResale();

        if (price > 0)
            price = (price - this._model.getDiscount());

        return (price);
    }
    public void setPriceDiscount(double value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        double price = this._model.getPriceResale();

        if (value < price)
            this._model.setDiscount(price - value);

        this._model.setPriceResale(value);

        _realm.commitTransaction();
    }

    /**
     * Retorna o desconto em %
     * @return
     */
    public double getDiscountPercent() {
        if (this._model.getPriceResale() > 0 && this._model.getDiscount() > 0)
            return MathUtils.valueToPercent(this._model.getPriceResale(), this._model.getDiscount());
        else
            return 0;
    }

    // Altera o desconto a partir de uma porcentagem
    public void setDiscountPercent(double value) {
        Realm _realm = Realm.getDefaultInstance();
        _realm.beginTransaction();

        double price = this._model.getPriceResale();

        if (price > 0 && value > 0) {
            double discount = MathUtils.percentToValue(price, value);
            this._model.setDiscount(discount);
        }

        _realm.commitTransaction();
    }
}
