package br.com.i9algo.autaz.pdv.domain.enums;

import br.com.i9algo.autaz.pdv.R;

/**
 * Curdado ao alterar, essas informacoes sao salvas no SQLite e BD externo
 * @author andre
 *
 */
public enum PaymentMethodEnum {
    MONEY(R.string.sale_pay_type_money),
    DEBT(R.string.sale_pay_type_debt),
    CREDIT(R.string.sale_pay_type_credit),
    VOUCHER(R.string.sale_pay_type_voucher);

    private final int _resId;

    PaymentMethodEnum(int resId) {
        this._resId = resId;
    }

    public int getResourceId() {
        return _resId;
    }
}
