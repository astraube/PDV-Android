package com.autazcloud.pdv.executor.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.interfaces.SaleControllerInterface;
import com.autazcloud.pdv.domain.models.Product;
import com.autazcloud.pdv.helpers.FormatUtil;
import com.autazcloud.pdv.data.local.PreferencesRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ShowToast")
public class ProductsGridAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Product> mList;
    private List<Product> mOrigList; // Serve para utilizar o campo de busca
    private SaleControllerInterface mSaleAdap;

    public ProductsGridAdapter(final Context context, final SaleControllerInterface saleAdap) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	this.mContext = context;
        this.mSaleAdap = saleAdap;
    }

    public void setData(List<Product> details) {
        this.mList = details;
        try {
            final Product product = mList.get(0);
            if (PreferencesRepository.isVisibleProductJoker()) { // Verifica se Produto Coringa "Diversos" esta habilitado
                mList.add(0, Product.createJockerProduct());
            } else {
                // Verifica se e um Produto Coringa "Diversos"
                if (product.isJockerProduct())
                    mList.remove(0);
            }
        } catch (Exception e) {
            //Toast.makeText(context, R.string.txt_products_empty, Toast.LENGTH_LONG);
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
    	if (mList != null)
    		return mList.size();
    	else
    		return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View currentView, final ViewGroup parent) {
    	final Product product = (Product)mList.get(position);
    	ViewHolder holder;

    	if (currentView == null) {
    		currentView = inflater.inflate(R.layout.layout_product_grid_item, null);

            holder = new ViewHolder(currentView);

			currentView.setClickable(true);
			currentView.setTag(holder);
			currentView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//ViewHolder item = (ViewHolder) v.getTag();
                    //Log.v("Product", "---> clicou produto - " + item.product.getRequestPass());

					if (mSaleAdap != null)
						mSaleAdap.onIncrementProduct(mList.get(position));
				}
		    });

			holder.btDecrementItem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//Log.v("Product", "---> clicou decrement produto");
					//ViewHolder item = (ViewHolder) v.getTag();

					if (mSaleAdap != null)
						mSaleAdap.onDecrementProduct(mList.get(position));
				}
		    });

			holder.btIncrementItem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//Log.v("Product", "---> clicou increment produto");
					//ViewHolder item = (ViewHolder) v.getTag();

					if (mSaleAdap != null)
						mSaleAdap.onIncrementProduct(mList.get(position));
				}
		    });

    	} else {
    		holder = (ViewHolder) currentView.getTag();
    	}

    	holder.nomeView.setText(product.getName());

        product.getModelImpl().setStockCurrent(100); // TODO - insere estoque para teste

        // Troca o Background caso tenha pouco ou nenhum deste produto em estoque
        if (!product.getModelImpl().inStock()) {
            currentView.setBackgroundResource(R.drawable.bg_button_product_not_stock);
        } else if (product.getModelImpl().inStockLimit()) {
    		currentView.setBackgroundResource(R.drawable.bg_button_product_low_stock);
    	}

    	// TODO - deve ter uma opcao no SheredPreference para definir se o produto deve ser bloqueado
    	// Background vermelho caso nao tenha o produto em estoque
    	//currentView.setEnabled(product.inStock());
        //holder.btDecrementItem.setEnabled(product.inStock());
        //holder.btIncrementItem.setEnabled(product.inStock());


    	/**
    	 * TextView de desconto
    	 */
    	double priceFinal = product.getPriceResale();
    	if (product.getDiscount() > 0) {
    		holder.priceInitView.setText(FormatUtil.toMoneyFormat(product.getPriceResale()));
    		priceFinal = product.getModelImpl().getPriceDiscount();
    	}
    	holder.priceFinalView.setText(FormatUtil.toMoneyFormat(priceFinal));

        return currentView;
    }
    
	@Override
	public Filter getFilter() {
		//Log.w("ProductsAdapter", "getFilter()");
		
		return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
        		//Log.v("ProductsAdapter", "Filter performFiltering() - " + constraint.toString());
        		
                final FilterResults oReturn = new FilterResults();
                final List<Product> results = new ArrayList<Product>();
                if (mOrigList == null)
                	mOrigList = mList;
                
                if (constraint != null) {
                	final String query = constraint.toString().toLowerCase().trim(); // String digitada na caixa de busca

                    ((Activity)ProductsGridAdapter.this.mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!query.isEmpty() && mOrigList != null && mOrigList.size() > 0) {
                                for (final Product p : mOrigList) {

                                    if (!p.getStringSearch().isEmpty() && p.getStringSearch().toLowerCase().contains(query))
                                        results.add(p);
                                }
                            }
                        }//public void run() {
                    });
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
        		//Log.v("ProductsAdapter", "publishResults()");

                ArrayList<Product> l = (ArrayList<Product>) results.values;
            	mList = (l.size() > 0) ? l : mOrigList;

                notifyDataSetChanged();
            }
        };
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		//Log.w("ProductsAdapter", "notifyDataSetChanged()");
	}

	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
		//Log.w("ProductsAdapter", "notifyDataSetInvalidated()");
	}


    public static class ViewHolder {
        @BindView(R.id.txtNome) TextView nomeView;
        @BindView(R.id.txtPriceInit) TextView priceInitView;
        @BindView(R.id.txtPriceFinal) TextView priceFinalView;
        @BindView(R.id.btDecrementItem) Button btDecrementItem;
        @BindView(R.id.btIncrementItem) Button btIncrementItem;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
