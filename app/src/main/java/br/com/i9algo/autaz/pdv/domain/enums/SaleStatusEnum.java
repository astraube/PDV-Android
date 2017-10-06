package br.com.i9algo.autaz.pdv.domain.enums;

import br.com.i9algo.autaz.pdv.R;

/**
 * Tipo de status que a venda pode assumir
 * Cuidado ao alterar, essas informacoes sao salvas no BD interno e BD externo
 * @author andre
 *
 */
public enum SaleStatusEnum {

    OPEN ("Em aberto", R.color.dark_gray_press),
    PAID ("Pagamento total", R.color.green),
    PAID_PARTIAL ("Pagamento parcial", R.color.orange),
    PAID_DISCOUNT ("Pagamento com desconto", R.color.yellow),
    CANCELED ("Cancelada", R.color.red);

    private final String _label;
    private final int _color;

    SaleStatusEnum(String label, int resID) {
        this._label = label;
        this._color = resID;
    }

    public String getLabel() { return _label; }
    public int getColor() { return _color; }
}
