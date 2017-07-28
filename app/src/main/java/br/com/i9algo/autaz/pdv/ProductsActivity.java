package br.com.i9algo.autaz.pdv;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.ProductsRealmRepository;
import br.com.i9algo.autaz.pdv.domain.interfaces.ProductControllerInterface;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.ui.adapters.ProductsListAdapter;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;
import br.com.i9algo.autaz.pdv.ui.dialog.ProductEditDialog;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import faranjit.currency.edittext.CurrencyEditText;
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
