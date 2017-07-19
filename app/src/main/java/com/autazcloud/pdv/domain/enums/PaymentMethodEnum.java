package com.autazcloud.pdv.domain.enums;

import com.autazcloud.pdv.R;

/**
 * Curdado ao alterar, essas informacoes sao salvas no SQLite e BD externo
 * @author andre
 *
 */
public enum PaymentMethodEnum {
    MONEY(R.string.sale_pay_type_money),
    DEBT(R.string.sale_pay_type_money),
    CREDIT(R.string.sale_pay_type_money),
    VOUCHER(R.string.sale_pay_type_money);

    private final int _resId;

    PaymentMethodEnum(int resId) {
        this._resId = resId;
    }

    public int getResourceId() {
        return _resId;
    }
}
