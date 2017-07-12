package com.autazcloud.pdv.executor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.domain.constants.DateFormats;
import com.autazcloud.pdv.domain.interfaces.SaleTaskInterface;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.helpers.FormatUtil;
import com.autazcloud.pdv.helpers.SaleStatusUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesAllListAdapter extends BaseAdapter {

	private Context mContext;
	private SaleTaskInterface mOwner;
    private List<SaleModel> mList;
	private LayoutInflater layoutInflater;
	
	public SalesAllListAdapter(Context context, SaleTaskInterface owner) {
    	this.mContext = context;
    	this.mOwner = owner;
		this.layoutInflater = LayoutInflater.from(context);
    }

	public void setData(List<SaleModel> details) {
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
    	SaleModel item = mList.get(position);
    	ViewHolder holder;
    	
    	if (convertView == null) {
    		convertView = layoutInflater.inflate(R.layout.layout_sale_list_item, null);
    		
    		holder = new ViewHolder(convertView);
			
			convertView.setClickable(true);
			convertView.setTag(holder);
			
			if (this.mOwner != null)
				this.mOwner.onChangeListViewAdapter(item);
			
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}
    	
    	// Background alternado
    	int background = (position % 2 == 0) ? R.drawable.listview_selector_even : R.drawable.listview_selector_odd;
    	convertView.setBackgroundResource(background);
    	
    	holder.txtClient.setText(item.getClientName());
		holder.txtTotal.setText(FormatUtil.toMoneyFormat(item.getTotalProducts()));
		holder.txtStatus.setText(SaleStatusUtil.getLabel(mContext, item));
		holder.txtStatus.setTextColor(SaleStatusUtil.getColor(mContext, item));
		holder.txtDate.setText(DateFormats.formatDateDivider(item.getDateCreated(), "/"));
    	return convertView;
    }
    
    /*private TextView newTextView(View convertView) {
    	Resources res = convertView.getResources();
    	
    	TextView t = new TextView(convertView.getContext());
    	LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	int vPadding = res.getDimensionPixelOffset(R.dimen.vertical_padding_textView);
    	int hPadding = res.getDimensionPixelOffset(R.dimen.horizontal_padding_textView);
        
    	lp.gravity = Gravity.CENTER;
    	t.setLayoutParams(lp);
    	t.setGravity(Gravity.CENTER_HORIZONTAL);
    	t.setPadding(hPadding, vPadding, vPadding, hPadding);
		t.setSingleLine(true);
		t.setTextColor(res.getColor(R.color.black));
		t.setTextAppearance(convertView.getContext(), R.style.textProductMedium);
    	return t;
    }*/

	public static class ViewHolder {
		@BindView(R.id.txt1) TextView txtClient;
		@BindView(R.id.txt2) TextView txtTotal;
		@BindView(R.id.txt3) TextView txtStatus;
		@BindView(R.id.txtDate) TextView txtDate;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
