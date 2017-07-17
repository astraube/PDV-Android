package com.autazcloud.pdv.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autazcloud.pdv.data.local.PreferencesRepository;
import com.autazcloud.pdv.domain.interfaces.SaleControllerInterface;
import com.autazcloud.pdv.domain.models.Product;
import com.autazcloud.pdv.ui.adapters.item_view.ProductGridView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

@SuppressLint("ShowToast")
public class ProductsGridAdapter extends RecyclerView.Adapter<ProductsGridAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Product> listItems, filterList;
    private SaleControllerInterface mSaleAdap;
    private Realm realm;

    public ProductsGridAdapter(final Context context, final SaleControllerInterface saleAdap) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	this.mContext = context;
        this.mSaleAdap = saleAdap;
        this.realm = Realm.getDefaultInstance();
    }

    public void setData(List<Product> details) {
        this.listItems = details;
        try {
            final Product product = listItems.get(0);
            if (PreferencesRepository.isVisibleProductJoker()) { // Verifica se Produto Coringa "Diversos" esta habilitado
                listItems.add(0, Product.createJockerProduct());
            } else {
                // Verifica se e um Produto Coringa "Diversos"
                if (product.isJockerProduct())
                    listItems.remove(0);
            }
        } catch (Exception e) {
            //Toast.makeText(context, R.string.txt_products_empty, Toast.LENGTH_LONG);
            e.printStackTrace();
        }
        this.notifyDataSetChanged();


        this.filterList = new ArrayList<Product>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.listItems);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(new ProductGridView(parent.getContext()));
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Product item = this.filterList.get(position);

        viewHolder.itemView.bind(item);
    }

    @Override
    public int getItemCount() {
        if(filterList != null && !filterList.isEmpty())
            return filterList.size();
        else
            return 0;
    }

    public Product getItem(int id) {
        return filterList.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ProductGridView itemView;

        public ViewHolder(ProductGridView productGridView) {
            super(productGridView);
            this.itemView = productGridView;
            itemView.setOnClickListener(this);
            itemView.containerLayout.setOnClickListener(this);
            itemView.containerLayout.setOnLongClickListener(this);

            this.itemView.btDecrementItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSaleAdap != null)
                        mSaleAdap.onDecrementProduct(filterList.get(getAdapterPosition()));
                }
            });

            this.itemView.btIncrementItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSaleAdap != null)
                        mSaleAdap.onIncrementProduct(filterList.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {
            //Log.v(getClass().getSimpleName(), "onClick() - 2");

            if (mSaleAdap != null)
                mSaleAdap.onIncrementProduct(filterList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            Log.v(getClass().getSimpleName(), "onLongClick() - 2");
            return false;
        }
    }


    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        /*new Thread(new Runnable() {
            @Override
            public void run() {*/

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(listItems);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (Product item : listItems) {

                        if (item.getStringSearch().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            /*}
        }).start();*/

    }
}
