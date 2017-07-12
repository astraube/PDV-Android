package com.autazcloud.pdv.helpers;

import android.content.Context;

import com.autazcloud.pdv.data.local.PreferencesRepository;
import com.autazcloud.pdv.domain.constants.Constants;

import java.text.DecimalFormat;

/**
 * C�digo de pedido de venda
 * @author andre
 *
 */
public class OrderCodeUtil {

	public static final String getCurrentCode(final Context context) {
		int contSales = PreferencesRepository.getCountSales();
		int lengthCode = PreferencesRepository.getLengthOrderCode();
		
		return (contSales + "" + Constants.CODE_PDV);
	}
	
	public static final String getCurrentCodeFormat(final Context context) {
		//int leng = PreferencesUtil.getLengthOrderCode(context);
		int leng = 3;
		StringBuilder format = new StringBuilder("");
		for (int i = 0; i < leng; i++) {
			format.append("0");
		}
		String str = new DecimalFormat(format.toString()).format(Long.parseLong(getCurrentCode(context)));
		return (str);
	}
	
	public static final String nextCode(final Context context) {
		int contSales = PreferencesRepository.getCountSales(true);
		int lengthCode = PreferencesRepository.getLengthOrderCode();
		
		//return (contSales + "" + Constants.CODE_PDV); // TODO
		return (contSales + "");
	}
}
