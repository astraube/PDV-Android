package com.autazcloud.pdv.domain.enums;

/**
 * Tipo de status que a venda pode assumir
 * Cuidado ao alterar, essas informa��es s�o salvas no SQLite e BD externo
 * @author andre
 *
 */
public enum SaleStatusEnum {
    OPEN ("Em aberto"),
    PAID ("Pagamento toal"),
    PAID_PARTIAL ("Pagamento parcial"),
    CANCELED ("Cancelada");

    private final String _label;

    SaleStatusEnum(String label) {
        this._label = label;
    }

    public String getLabel() { return _label; }
}
