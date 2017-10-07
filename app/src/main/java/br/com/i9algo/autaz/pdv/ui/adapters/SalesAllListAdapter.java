package br.com.i9algo.autaz.pdv.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.constants.DateFormats;
import br.com.i9algo.autaz.pdv.domain.interfaces.SaleTaskInterface;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import br.com.i9algo.autaz.pdv.helpers.SaleStatusUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesAllListAdapter extends BaseAdapter {

	private Context mContext;
	private SaleTaskInterface mOwner;
    private List<Sale> mList;
	private LayoutInflater layoutInflater;
	
	public SalesAllListAdapter(Context context, SaleTaskInterface owner) {
    	this.mContext = context;
    	this.mOwner = owner;
		this.layoutInflater = LayoutInflater.from(context);
    }

	public void setData(List<Sale> details) {
		this.mList = details;
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
    public View getView(int position, View convertView, ViewGroup parent) {
    	Sale item = mList.get(position);
    	ViewHolder holder;
    	
    	if (convertView == null) {
    		convertView = layoutInflater.inflate(R.layout.layout_sale_list_item, null);
    		
    		holder = new ViewHolder(convertView);
			
			convertView.setClickable(true);
			convertView.setTag(holder);
			
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}
    	
    	// Background alternado
    	int background = (position % 2 == 0) ? R.drawable.listview_selector_even : R.drawable.listview_selector_odd;
    	convertView.setBackgroundResource(background);
    	
    	holder.txtClient.setText(item.getClient().getName());
		holder.txtTotal.setText(FormatUtil.toMoneyFormat(item.getTotalProducts()));
		holder.txtStatus.setText(SaleStatusUtil.getLabel(mContext, item));
		holder.txtStatus.setTextColor(SaleStatusUtil.getColor(mContext, item));
		holder.txtDate.setText(DateFormats.formatDateDivider(item.getCreatedAt(), "/"));
		holder.txtCode.setText(item.getId());
    	return convertView;
    }

	public static class ViewHolder {
		@BindView(R.id.txtClient) TextView txtClient;
		@BindView(R.id.txtTotal) TextView txtTotal;
		@BindView(R.id.txtStatus) TextView txtStatus;
		@BindView(R.id.txtDate) TextView txtDate;
		@BindView(R.id.txtCode) TextView txtCode;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
