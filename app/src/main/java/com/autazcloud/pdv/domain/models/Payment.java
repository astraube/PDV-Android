package com.autazcloud.pdv.domain.models;

import com.autazcloud.pdv.domain.constants.DateFormats;
import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;

public class Payment extends RealmObject {

	/*@Index
	@PrimaryKey
	private int id;*/

	private String paymentMethod; // Metodo de pagamento, dinheiro, cartao, debito...
	private double amountPaid = 0; // Valor Pago no pagamento
	private String datePayment; // Data do pagamento
	
	
	/*public static Payment newPaymentFromJson(String jsonObject) {
		Payment model = (Payment) ObjectUtil.JsonToObject(jsonObject, Payment.class);
		return model;
	}*/
	
	public Payment() {}
	
	public Payment(String method, double amountPaid) {
		setPaymentMethod(method);
		setAmountPaid(amountPaid);
		setDatePayment(DateFormats.formatDate(new Date(), DateFormats.getDateTimeGlobal()));
	}
    public Payment(PaymentMethodEnum method, double amountPaid) {
        this(method.toString(), amountPaid);
    }

	public static int getNextKey()
	{
		try {
			Realm realm = Realm.getDefaultInstance();
			return realm.where(Payment.class).max("id").intValue() + 1;
		} catch (ArrayIndexOutOfBoundsException e)
		{ return 0; }
	}

	//public int getId() { return this.id; }
	//public void setId(int id) { this.id = id; }

	public String getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(String value) { this.paymentMethod = value; }
	
	public double getAmountPaid() { return amountPaid; }
	public void setAmountPaid(double mAmountPaid) { this.amountPaid = mAmountPaid; }
	
	public String getDatePayment() { return datePayment.replace("_"," ").replace(".",":").replace("i","-"); }
	public void setDatePayment(String datePayment) { this.datePayment = datePayment; }

	/*@Override
	public String toString() {
		return ObjectUtil.ObjectToJson(this);
	}*/
}
