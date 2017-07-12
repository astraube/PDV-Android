package com.autazcloud.pdv.executor.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.controllers.SaleController;
import com.autazcloud.pdv.domain.models.SaleItemModel;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.helpers.FormatUtil;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaleItensAdapter extends BaseAdapter {
	
    private Context mContext;
    private SaleModel mSaleModel;
    private SaleController mListener;
	private LayoutInflater inflater;

    public SaleItensAdapter(Context context, SaleController listener) {
    	this.mContext = context;
        this.mListener = listener;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	public void setData(SaleModel saleModel) {
		this.mSaleModel = saleModel;
		this.notifyDataSetChanged();
	}

    @Override
    public int getCount() {
		if (mSaleModel == null) {
			return 0;
		}
		return mSaleModel.getItemList().size();
    }

    @Override
    public Object getItem(int position) {
		List<SaleItemModel> l = mSaleModel.getItemList();
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
    	final SaleItemModel item = mSaleModel.get(position);
    	ViewHolder holder;

    	if (convertView == null) {
    		convertView = this.inflater.inflate(R.layout.layout_sale_product_item, null);

            holder = new ViewHolder(convertView);

			convertView.setClickable(true);
			convertView.setTag(holder);

    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}

    	try {
	    	// Background alternado
	    	int background = (position % 2 == 0) ? R.drawable.listview_selector_even : R.drawable.listview_selector_odd;
	    	convertView.setBackgroundResource(background);

            holder.nomeView.setText(item.getName());
	    	holder.qtdView.setText(" x " + item.getQuantityItem() + " und");
			holder.txtPriceOriginal.setAmount((float)item.getPriceResale());
			holder.txtPriceOriginal.invalidate();

            // TextView de desconto
	    	double priceFinal = item.getItemTotal();
	    	if (item.getDiscount() > 0) {
	    		holder.priceInitView.setText(FormatUtil.toMoneyFormat(item.getItemTotal()));
	    		holder.priceInitView.setVisibility(View.VISIBLE);
				holder.priceInitView.setPaintFlags(holder.priceInitView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	    		priceFinal = item.getPriceDiscount();
	    	}

	    	//holder.priceFinalView.setText(FormatUtil.toMoneyFormat(priceFinal));
			holder.priceFinalView.setAmount((float)priceFinal);
			holder.priceFinalView.invalidate();

	    	convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onSaleItemClick(item);
				}
		    });
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
        return convertView;
    }

	public static class ViewHolder {
		@BindView(R.id.txtNome) TextView nomeView;
		@BindView(R.id.txtQuantity) TextView qtdView;
		@BindView(R.id.txtPriceOriginal) MoneyTextView txtPriceOriginal;
		@BindView(R.id.txtPriceInit) TextView priceInitView;
		@BindView(R.id.txtPriceFinal) MoneyTextView priceFinalView;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
