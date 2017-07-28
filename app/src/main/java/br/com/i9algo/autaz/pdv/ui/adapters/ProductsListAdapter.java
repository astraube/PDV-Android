package br.com.i9algo.autaz.pdv.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.interfaces.ProductControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.ui.adapters.item_view.ProductItemView;
import br.com.i9algo.autaz.pdv.ui.dialog.ProductClickedDialog;

import java.util.List;

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ViewHolder> {

	private ProductControllerInterface _context;
    private List<Product> _list = null;

    public ProductsListAdapter(ProductControllerInterface context) {
        this(context, null);
    }
	public ProductsListAdapter(ProductControllerInterface context, List<Product> list) {
		super();
        this._context = context;
        this._list = list;
	}

    public void setData(List<Product> list) {
        this._list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder vh = new ViewHolder(new ProductItemView(viewGroup.getContext()));
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Product item = this._list.get(position);

        viewHolder.itemView.bind(item);

        // Background alternado
        int background = (position % 2 == 0) ? R.drawable.listview_selector_even : R.drawable.listview_selector_odd;
        viewHolder.itemView.setBackgroundResource(background);
        viewHolder.itemView.setClickable(true);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setSelected(true);

                        new ProductClickedDialog(_context, item);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        if(_list != null && !_list.isEmpty())
            return _list.size();
        else
            return 0;
    }
    
	/*Button.OnClickListener mClickEdit =
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	};
	
	Button.OnClickListener mClickRemove = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	};*/


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ProductItemView itemView;

        public ViewHolder(ProductItemView productItemView) {
            super(productItemView);
            this.itemView = productItemView;
        }
    }
}
