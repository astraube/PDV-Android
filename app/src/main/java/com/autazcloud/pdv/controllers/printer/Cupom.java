package com.autazcloud.pdv.controllers.printer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.models.SaleModel;

public class Cupom {

    private String _corporateName = "";
    private Bitmap _corporateImage = null;
    private String _corporatePhone = "";
    private String _corporateSocialmedia = "";
    private SaleModel _sale = null;
    private PaymentMethodEnum _method = null;
    private double _amountPaid = 0;
    private double _troco = 0;


    public Cupom() {

    }
	public Cupom(String corporateName, Bitmap corporateImage, SaleModel sale, PaymentMethodEnum method, double amountPaid, final double troco) {
        _corporateName = corporateName;
        _corporateImage = corporateImage;
        _sale = sale;
        _method = method;
        _amountPaid = amountPaid;
        _troco = troco;
	}


    public String getCorporateName() {
        return _corporateName;
    }
    public void setCorporateName(String sale) {
        this._corporateName = sale;
    }

    public Bitmap getCorporateImage() {
        return _corporateImage;
    }
    public void setCorporateImage(Bitmap image) {
        this._corporateImage = image;
    }
    public void setCorporateImage(Resources res, int id) {
        Bitmap logoData = BitmapFactory.decodeResource(res, id);
        this._corporateImage = logoData;
    }

    public String getCorporatePhone() {
        return _corporatePhone;
    }
    public void setCorporatePhone(String phone) {
        this._corporatePhone = phone;
    }

    public String getCorporateSocialmedia() {
        return _corporateSocialmedia;
    }
    public void setCorporateSocialmedia(String social) {
        this._corporateSocialmedia = social;
    }

    public SaleModel getSale() {
        return _sale;
    }
    public void setSale(SaleModel sale) {
        this._sale = sale;
    }

    public PaymentMethodEnum getMethod() {
        return _method;
    }
    public void setMethod(PaymentMethodEnum method) {
        this._method = method;
    }

    public double getAmountPaid() {
        return _amountPaid;
    }
    public void setAmountPaid(double amountPaid) {
        this._amountPaid = amountPaid;
    }

    public double getTroco() {
        return _troco;
    }
    public void setTroco(double troco) {
        this._troco = troco;
    }
}
