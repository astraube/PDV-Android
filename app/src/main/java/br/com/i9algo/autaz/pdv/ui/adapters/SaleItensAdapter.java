package br.com.i9algo.autaz.pdv.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.controllers.SaleController;
import br.com.i9algo.autaz.pdv.domain.models.ProductSale;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaleItensAdapter extends BaseAdapter {
	
    private Context mContext;
    private Sale mSale;
    private SaleController mListener;
	private LayoutInflater inflater;

    public SaleItensAdapter(Context context, SaleController listener) {
    	this.mContext = context;
        this.mListener = listener;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	public void setData(Sale sale) {
		this.mSale = sale;
		this.notifyDataSetChanged();
	}

    @Override
    public int getCount() {
		if (mSale == null) {
			return 0;
		}
		return mSale.getProducts().size();
    }

    @Override
    public Object getItem(int position) {
		List<ProductSale> l = mSale.getProducts();
		if (l == null) {
			return null;
		}
		return l.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	final ProductSale item = mSale.get(position);
    	ViewHolder holder;

    	if (convertView == null) {
    		convertView = this.inflater.inflate(R.layout.layout_product_cart_item, null);

            holder = new ViewHolder(convertView);

			convertView.setTag(holder);
			convertView.setClickable(true);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onSaleItemClick(item);
				}
			});

    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}

    	try {
	    	// Background alternado
	    	int background = (position % 2 == 0) ? R.drawable.listview_selector_even : R.drawable.listview_selector_odd;
	    	convertView.setBackgroundResource(background);

			//item.setDiscount(0.5);

            holder.txtNome.setText(item.getName());
	    	holder.txtQuantity.setText("x " + item.getQuantity() + " und"); // TODO - deve mostrar o tipo da unidade de medida
			holder.txtPriceUnit.setText(FormatUtil.toMoneyFormat(item.getPriceDiscount()));

            // TextView de desconto
	    	if (item.getDiscount() > 0) {
	    		holder.txtPriceInit.setText(FormatUtil.toMoneyFormat(item.getPriceResale()));
	    		holder.txtPriceInit.setVisibility(View.VISIBLE);
				holder.txtPriceInit.setPaintFlags(holder.txtPriceInit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	    	}

	    	//holder.priceFinalView.setText(FormatUtil.toMoneyFormat(item.getItemTotal()));
			holder.txtPriceFinal.setAmount((float)item.getItemTotal());
			holder.txtPriceFinal.invalidate();
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
        return convertView;
    }

	public class ViewHolder {
		@BindView(R.id.txtNome) TextView txtNome;
		@BindView(R.id.txtQuantity) TextView txtQuantity;
		@BindView(R.id.txtPriceUnit) TextView txtPriceUnit;
		@BindView(R.id.txtPriceInit) TextView txtPriceInit;
		@BindView(R.id.txtPriceFinal) MoneyTextView txtPriceFinal;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
