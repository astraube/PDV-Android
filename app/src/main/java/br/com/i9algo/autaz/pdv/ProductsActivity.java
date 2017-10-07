package br.com.i9algo.autaz.pdv;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.util.List;

import br.com.i9algo.autaz.pdv.data.local.ProductsRealmRepository;
import br.com.i9algo.autaz.pdv.domain.interfaces.ProductControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.ui.adapters.ProductsListAdapter;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;
import br.com.i9algo.autaz.pdv.ui.dialog.ProductEditDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ProductsActivity extends BaseActivity implements ProductControllerInterface {
	
	private final String TAG = getClass().getSimpleName();

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;

	@BindView(R.id.btNewProduct)
	Button btNewProduct;

	private ProductsListAdapter mAdapter;
	private Realm realm;

	public List<Product> productsList;
	public ProductsActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        startMixPanelApi(this);
		fullScreen();
		setContentView(R.layout.activity_products_sync);
		ButterKnife.bind(this);

		this.productsList = ProductsRealmRepository.getAll();

		realm = Realm.getDefaultInstance();

		// Cria adapter para o GridView
		mAdapter = new ProductsListAdapter(this);
		mAdapter.setData(productsList);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(mAdapter);
	}

	@OnClick(R.id.btNewProduct)
	public void onClickBtNewProduct() {
		new ProductEditDialog(ProductsActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (realm != null) {
			realm.close();
			realm = null;
		}
	}

	@Override
	public Activity getContext() {
		return ProductsActivity.this;
	}

	@Override
	public void insertProduct(Product product) {
		ProductsRealmRepository.addItem(product);

		this.productsList = ProductsRealmRepository.getAll();
		mAdapter.setData(this.productsList);
	}

	@Override
	public void updateProduct(Product product) {
		ProductsRealmRepository.syncItem(product);

		this.productsList = ProductsRealmRepository.getAll();
		mAdapter.setData(this.productsList);
	}

	@Override
	public void removeProduct(Product product) {
		try {
			ProductsRealmRepository.removeItem(product);

			this.productsList = ProductsRealmRepository.getAll();
			mAdapter.setData(this.productsList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
