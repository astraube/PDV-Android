package com.autazcloud.pdv;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.autazcloud.pdv.data.local.ProductsRealmRepository;
import com.autazcloud.pdv.domain.interfaces.ProductControllerInterface;
import com.autazcloud.pdv.domain.models.Product;
import com.autazcloud.pdv.ui.adapters.ProductsListAdapter;
import com.autazcloud.pdv.ui.base.BaseActivity;
import com.autazcloud.pdv.ui.dialog.ProductEditDialog;
import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ProductsActivity extends BaseActivity implements ProductControllerInterface {
	
	private final String TAG = getClass().getSimpleName();

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;

	@BindView(R.id.fabButton)
	FloatingActionButton fabButton;

	private ProductsListAdapter mAdapter;
	private Realm realm;

	public List<Product> productsList;
	public ProductsActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		
		fabButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				new ProductEditDialog(ProductsActivity.this);
			}
		});
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
