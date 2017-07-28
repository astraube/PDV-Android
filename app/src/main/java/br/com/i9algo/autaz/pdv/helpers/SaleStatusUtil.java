package br.com.i9algo.autaz.pdv.helpers;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.models.Sale;

/**
 * Created by aStraube on 04/07/2017.
 */

public class SaleStatusUtil {

    public static String getLabel(Context c, Sale s) {
        String str = "";
        switch (SaleStatusEnum.valueOf(s.getStatus())) {
            case OPEN:
                str = c.getString(R.string.txt_sale_open);
                break;
            case PAID:
                str = c.getString(R.string.txt_sale_paid);
                break;
            case PAID_PARTIAL:
                str = c.getString(R.string.txt_sale_paid_partial);
                break;
            case CANCELED:
                str = c.getString(R.string.txt_sale_canceled);
                break;
        }
        return str;
    }

    public static int getColor(Context c, Sale s) {
        int color = ResourcesCompat.getColor(c.getResources(), R.color.black, null);
        switch (SaleStatusEnum.valueOf(s.getStatus())) {
            case OPEN:
                color = ResourcesCompat.getColor(c.getResources(), R.color.gray, null);
                break;
            case PAID:
                color = ResourcesCompat.getColor(c.getResources(), R.color.green, null);
                break;
            case PAID_PARTIAL:
                color = ResourcesCompat.getColor(c.getResources(), R.color.orange, null);
                break;
            case CANCELED:
                color = ResourcesCompat.getColor(c.getResources(), R.color.red, null);
                break;
        }
        return color;
    }
}
