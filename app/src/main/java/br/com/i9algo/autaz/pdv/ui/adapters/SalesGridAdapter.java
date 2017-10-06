package br.com.i9algo.autaz.pdv.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.constants.DateFormats;
import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.interfaces.SaleControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.domain.models.Sale;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesGridAdapter extends BaseAdapter {
	
    private Context mContext;
    private List<Sale> listItems, filterList;
    private SaleControllerInterface mSaleCtrl;

    public SalesGridAdapter(Context context, SaleControllerInterface saleCtrl) {
    	this.mContext = context;
        this.mSaleCtrl = saleCtrl;
    }

	public void setData(List<Sale> list) {
		//Log.v(getClass().getSimpleName(), "setData - " + list.toString());
		this.listItems = list;
		this.notifyDataSetChanged();

		this.filterList = new ArrayList<Sale>();
		// we copy the original list to the filter list and use it for setting row values
		this.filterList.addAll(this.listItems);
	}
    
    @Override
    public int getCount() {
    	if (filterList != null)
    		return filterList.size();
    	else
    		return 0;
    }
    
    @Override
    public Object getItem(int position) {
        return filterList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
    	final Sale sale = filterList.get(position);
    	ViewHolder holder;
    	
    	if (convertView == null) {
    		LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		convertView = inflater.inflate(R.layout.layout_sale_grid_item, null);
            
            holder = new ViewHolder(convertView);
			
			convertView.setClickable(true);
			convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}

		String date = DateFormats.formatDateCommercial(sale.getCreatedAt());

		holder.txtDate.setText(date);
        holder.txtNameSaleView.setText(sale.getClient().getName());

		if (!sale.getCodeControll().isEmpty())
        	holder.txtCodeSaleView.setText(convertView.getResources().getString(R.string.txt_bt_sale_code_controll, sale.getCodeControll()));
		else
			holder.txtCodeSaleView.setText("");

		if (sale.existsOrderCodeWait())
			holder.txtOrderCodeWait.setText(parent.getContext().getString(R.string.code_wait, sale.getOrderCodeWait()));
		else
			holder.txtOrderCodeWait.setText("");


		int pCount = sale.getCountProducts();
		if (pCount == 1)
        	holder.txtQtdItemsView.setText(convertView.getResources().getString(R.string.txt_bt_sale_num_item, String.valueOf(pCount)));
		else if (pCount > 1)
			holder.txtQtdItemsView.setText(convertView.getResources().getString(R.string.txt_bt_sale_num_items, String.valueOf(pCount)));


        if (sale.getStatus().equals(SaleStatusEnum.OPEN.toString())) {
            convertView.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.bg_button_sale_grid));

        } else if (sale.getStatus().equals(SaleStatusEnum.CANCELED.toString())) {

            convertView.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.bg_button_sale_grid_canceled));

        } else if (sale.getStatus().equals(SaleStatusEnum.PAID.toString())) {

            convertView.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.bg_button_sale_grid_paid));

		} else if (sale.getStatus().equals(SaleStatusEnum.PAID_PARTIAL.toString())) {

            convertView.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.bg_button_sale_grid_partial_paid));
		}

        /*
        // Para o caso do primeiro botao da lista ser o botao de "Nova Venda"

    	if (sale != null) {
    		holder.txtNameSaleView.setText(sale.getClient().getName());
    		holder.txtCodeSaleView.setText(convertView.getResources().getString(R.string.txt_bt_sale_code_controll, sale.getCodeControll()));
    		holder.txtQtdItemsView.setText(convertView.getResources().getString(R.string.txt_bt_sale_num_itens, String.valueOf(sale.getCountProducts())));
    		
    		if (sale.getStatus().equals(SaleStatusEnum.CANCELED.toString())) {
        		convertView.setBackground(parent.getResources().getDrawable(R.drawable.bg_button_sale_cancel));
        	} else if (sale.getStatus().equals(SaleStatusEnum.PAID.toString())) {
        		convertView.setBackground(parent.getResources().getDrawable(R.drawable.bg_button_sale_grid_paid));
        	} else if (sale.getStatus().equals(SaleStatusEnum.PAID_PARTIAL.toString())) {
        		convertView.setBackground(parent.getResources().getDrawable(R.drawable.bg_button_sale_grid_partial_paid));
        	}
    	} else {
    		// Botao Nova Venda
    		holder.txtNameSaleView.setText(R.string.action_sale_new);
    		convertView.setBackground(parent.getResources().getDrawable(R.drawable.bg_button_sale_grid_new_sale));
    	}*/
    	
    	convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                mSaleCtrl.onOpenSale(sale);
                /*
                // Para o caso do primeiro botao da lista ser o botao de "Nova Venda"

				if (sale != null) {
					mSaleCtrl.onOpenSale(sale);
				} else {
					mSaleCtrl.onAddNewSale(null, true);
				}
				*/
			}
	    });
        return convertView;
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
				for (Sale item : listItems) {

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

	public static class ViewHolder {
		@BindView(R.id.txtNomeVenda) TextView txtNameSaleView;
		@BindView(R.id.txtCode) TextView txtCodeSaleView;
		@BindView(R.id.txtQtdItems) TextView txtQtdItemsView;
		@BindView(R.id.txtDate) TextView txtDate;
		@BindView(R.id.txtOrderCodeWait) TextView txtOrderCodeWait;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
