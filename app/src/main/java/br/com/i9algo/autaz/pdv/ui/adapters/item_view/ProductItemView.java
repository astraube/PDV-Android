package br.com.i9algo.autaz.pdv.ui.adapters.item_view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductItemView extends RelativeLayout {

    @BindView(R.id.txt1)
    TextView nomeView;

    @BindView(R.id.txt2)
    TextView priceView;

    @BindView(R.id.txt3)
    TextView categoryView;

    public ProductItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_product_list_item, this);
        ButterKnife.bind(this);
    }

    public void bind(Product item) {
        nomeView.setText(item.getName());
        priceView.setText(FormatUtil.toMoneyFormat(item.getPriceResale()));

        if (item.getCategory() != null)
            categoryView.setText(item.getCategory().getName());
    }
}
