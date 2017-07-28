package br.com.i9algo.autaz.pdv.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.interfaces.ItemAdapterInterface;
import br.com.i9algo.autaz.pdv.domain.models.LayoutModel;

import java.util.List;

public class LayoutsGridAdapter extends BaseAdapter {
	
    private Context mContext;
    private List<LayoutModel> mList;
    private ItemAdapterInterface mOwner;

    public LayoutsGridAdapter(final Context context, final List<LayoutModel> list, final ItemAdapterInterface owner) {
    	this.mContext = context;
        this.mList = list;
        this.mOwner = owner;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
    	final LayoutModel model = mList.get(position);
    	ViewHolder holder;
    	
    	if (convertView == null) {
    		LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		convertView = inflater.inflate(R.layout.layout_grid_item, null);
            
            holder = new ViewHolder();
			holder.nomeView = (TextView) convertView.findViewById(R.id.txtNome);
			holder.priceInitView = (ImageView) convertView.findViewById(R.id.image);
			holder.descriptionView = (TextView) convertView.findViewById(R.id.txtDescription);
			
			convertView.setClickable(true);
			convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}

    	holder.nomeView.setText(model.mName);
    	holder.descriptionView.setText(model.mDescription);
    	holder.priceInitView.setImageResource(model.mImage);
    	
    	convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mOwner.onItemClick(v, position);
			}
		});
    	
        return convertView;
    }
    
    class ViewHolder {
		TextView nomeView;
    	TextView descriptionView;
		ImageView priceInitView;
    }
}
