package br.com.i9algo.autaz.pdv.helpers;

import android.content.Context;

import java.text.DecimalFormat;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.constants.DateFormats;

public class FormatUtil {

	static volatile Context applicationContext;

	public static synchronized void init(Context context) {
		if (FormatUtil.applicationContext == null) {
			if (context == null) {
				throw new IllegalArgumentException("Non-null context required.");
			}
			FormatUtil.applicationContext = context;
		}
		DateFormats.init(context);
	}


	public static final String getLocaleFormatMoney() {
		return applicationContext.getString(R.string.format_money);
	}
	public static final String getLocaleFormatMoneySymbol() {
		return applicationContext.getString(R.string.format_money_with_symbol);
	}




	public static final String toMoneyFormat(double value) {
		String formattedPrice = new DecimalFormat(getLocaleFormatMoneySymbol()).format(value);
		return (formattedPrice);
	}
	
	public static final String toMoneyFormat(String value) {
		String formattedPrice = new DecimalFormat(getLocaleFormatMoneySymbol()).format(value);
		return (formattedPrice);
	}

	public static final String toMoneyFormat(double value, boolean symbol) {
		String format = (symbol) ? getLocaleFormatMoneySymbol() : getLocaleFormatMoney();
		String formattedPrice = new DecimalFormat(format).format(value);
		return (formattedPrice);
	}

	public static final String toMoneyFormat(String value, boolean symbol) {
		String format = (symbol) ? getLocaleFormatMoneySymbol() : getLocaleFormatMoney();
		String formattedPrice = new DecimalFormat(format).format(value);
		return (formattedPrice);
	}
}
