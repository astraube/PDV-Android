package br.com.i9algo.autaz.pdv.ui.adapters.item_view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductGridView extends FrameLayout
{
    @BindView(R.id.main_card_view)
    public CardView maiCardView;

    @BindView(R.id.containerLayout)
    public LinearLayout containerLayout;

    @BindView(R.id.txtNome)
    public TextView nomeView;

    @BindView(R.id.txtPriceInit)
    public TextView priceInitView;

    @BindView(R.id.txtPriceFinal)
    public TextView priceFinalView;

    @BindView(R.id.btDecrementItem)
    public Button btDecrementItem;

    @BindView(R.id.btIncrementItem)
    public Button btIncrementItem;


    public ProductGridView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_product_grid_item, this);
        ButterKnife.bind(this);
    }

    public void bind(Product product) {
        nomeView.setText(product.getName());

        product.getModelImpl().setStockCurrent(100); // TODO - insere estoque para teste


        // Troca o Background caso tenha pouco ou nenhum deste produto em estoque
        if (!product.getModelImpl().inStock()) {
            setBackgroundResource(R.drawable.bg_button_product_not_stock);
        } else if (product.getModelImpl().inStockLimit()) {
            setBackgroundResource(R.drawable.bg_button_product_low_stock);
        }

        // TODO - deve ter uma opcao no SheredPreference para definir se o produto deve ser bloqueado
        // Background vermelho caso nao tenha o produto em estoque
        //currentView.setEnabled(product.inStock());
        //btDecrementItem.setEnabled(product.inStock());
        //btIncrementItem.setEnabled(product.inStock());


        /**
         * TextView de desconto
         */
        double priceFinal = product.getPriceResale();
        if (product.getDiscount() > 0) {
            priceInitView.setText(FormatUtil.toMoneyFormat(product.getPriceResale()));
            priceFinal = product.getModelImpl().getPriceDiscount();
        }
        priceFinalView.setText(FormatUtil.toMoneyFormat(priceFinal));
    }
}
