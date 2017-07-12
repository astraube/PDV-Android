package com.autazcloud.pdv.domain.constants;

import android.content.Context;

import com.autazcloud.pdv.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Formatos de data
 * Datas devem ser armazenadas em ingles.
 */
public class DateFormats {

	public static final String FORMAT_DATE_GLOBAL = "yyyy-MM-dd";
	public static final String FORMAT_DATE_TIME_GLOBAL = "yyyy-MM-dd hh:mm:ss";

	static volatile Context applicationContext;

	public static synchronized void init(Context context) {
		if (DateFormats.applicationContext == null) {
			if (context == null) {
				throw new IllegalArgumentException("Non-null context required.");
			}
			DateFormats.applicationContext = context;
		}
	}


	/**
	 * Retorna o formato de data universal
	 * @return
	 */
	public static final String getDateGlobal() {
		return FORMAT_DATE_GLOBAL;
	}
	public static final String getDateGlobal(String divider) {
		return "yyyy" + divider + "MM" + divider + "dd";
	}
	public static final String getDateTimeGlobal() {
		return FORMAT_DATE_TIME_GLOBAL;
	}
	public static final String getDateTimeGlobal(String divider) {
        return "yyyy" + divider + "MM" + divider + "dd hh:mm:ss";
	}



	/**
	 * UTILIZADO APENAS PARA EXIBIR A DATA AO USUARIO
	 * Retorna o formato de data de acordo com a localização do usuario
	 */
	public static final String getDateLocale() {
		return applicationContext.getString(R.string.format_date);
	}
	public static final String getDateLocale(String divider) {
		return applicationContext.getString(R.string.format_date_divider, divider);
	}
	public static final String getDateTimeLocale() {
		return applicationContext.getString(R.string.format_date_time);
	}
	public static final String getDateTimeLocale(String divider) {
		return applicationContext.getString(R.string.format_date_time_divider, divider);
	}
	public static final String getDateTimeComercial(String divider) {
		return applicationContext.getString(R.string.format_date_time_divider_comercial, divider);
	}


    /**
     * String -> Date
     * @param date
     * @param dateFormat
     * @return
     */
    public static Date parseDate(String date, String dateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.getDefault());
        df.setTimeZone(TimeZone.getDefault());
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Date parseDate(String date) {
        String format = getDateTimeLocale();
        return parseDate(date, format);
    }
    public static Date parseDateDivider(String date, String divider) {
        String format = getDateTimeLocale(divider);
        return parseDate(date, format);
    }

	/**
	 * Date -> String
	 * @param dateFormat DateFormats.getDateGlobal()
	 * @return
	 */
	public static String formatDate(Date date, String dateFormat) {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.getDefault());
		df.setTimeZone(TimeZone.getDefault());
		return df.format(date);
	}

	/**
     * Date -> String
	 * Converte uma variavel Date para String, formatando a partir da data padrao da lingua atual
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		String format = getDateTimeLocale();
		return formatDate(date, format);
	}
	public static String formatDateDivider(Date date, String divider) {
		String format = getDateTimeLocale(divider);
        return formatDate(date, format);
	}

	public static String formatDateCommercial(Date date) {
		String format = getDateTimeComercial("/");
		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		df.setTimeZone(TimeZone.getDefault());
		return df.format(date);
	}
}
