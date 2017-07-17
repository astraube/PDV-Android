package com.autazcloud.pdv;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.autazcloud.pdv.controllers.SaleController;
import com.autazcloud.pdv.controllers.printer.Cupom;
import com.autazcloud.pdv.data.local.ProductsRealmRepository;
import com.autazcloud.pdv.domain.constants.Constants;
import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.enums.SaleStatusEnum;
import com.autazcloud.pdv.domain.interfaces.ProductInterface;
import com.autazcloud.pdv.domain.models.CallbackModel;
import com.autazcloud.pdv.domain.models.Product;
import com.autazcloud.pdv.domain.models.SaleItemModel;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.ui.adapters.ProductsGridAdapter;
import com.autazcloud.pdv.ui.adapters.SaleItensAdapter;
import com.autazcloud.pdv.helpers.FormatUtil;
import com.autazcloud.pdv.ui.base.BaseActivity;
import com.autazcloud.pdv.ui.dialog.DialogUtil;
import com.autazcloud.pdv.ui.dialog.ProductChangePriceDialog;
import com.autazcloud.pdv.ui.dialog.SaleCloseDialog;
import com.autazcloud.pdv.ui.dialog.SaleItemEditDialog;
import com.autazcloud.pdv.ui.dialog.SaleNewDialog;
import com.autazcloud.pdv.ui.dialog.SalePayDialog;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class SaleControllActivity extends BaseActivity {

	private final String TAG;


	@BindView(R.id.gridView)
	RecyclerView gridViewProducts;
	@BindView(R.id.txtTotalInit) TextView txtTotalInit;
	@BindView(R.id.txtTotalFinal) MoneyTextView txtTotalFinal;
	@BindView(R.id.txtSaleName) TextView txtSaleName;
	@BindView(R.id.txtSaleCode) TextView txtSaleCode;
	@BindView(R.id.txtTotalPaidFinal) TextView txtTotalPaidFinal;
	@BindView(R.id.txtTotalToPayFinal) TextView txtTotalToPayFinal;
	@BindView(R.id.containerInfoSale) LinearLayout containerInfoSale;
	@BindView(R.id.containerTotalPaid) RelativeLayout containerTotalPaid;
	@BindView(R.id.containerTotalToPay) RelativeLayout containerTotalToPay;
	@BindView(R.id.searchView) SearchView mSearchView;
	@BindView(R.id.viewRight) RelativeLayout viewRight;
	@BindView(R.id.btnPaySale) Button btnPaySale;
	@BindView(R.id.btnPrint) ImageButton btnPrint;
	@BindView(R.id.btnCancelSale) Button btnCancelSale;
	@BindView(R.id.btnCleanSale) Button btnCleanSale;
	@BindView(R.id.listCartSale) ListView listCartSale;

	@BindView(R.id.lineTxtTotalInit) View lineTxtTotalInit;


	private Realm realm;
	private SaleCtrl mSaleCtrl;
	private ProductsGridAdapter mProductsAdapter;
	private SaleItensAdapter saleListAdapter;
	private List<Product> productsList;


	RealmChangeListener<SaleModel> listenerSaleModel = new RealmChangeListener<SaleModel>() {
		@Override
		public void onChange(SaleModel saleModel) {
			//Log.w(TAG, "RealmChangeListener onChange");
			//mSaleCtrl.onSyncSale(saleModel);
			changeListSale();
		}
	};

	public SaleControllActivity () {
		super();
		this.TAG = getClass().getSimpleName();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fullScreen();
		setContentView(R.layout.activity_sale_controller);

		ButterKnife.bind(this);

		this.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);

        realm = Realm.getDefaultInstance();

		mSaleCtrl = new SaleCtrl(this);

        // Buscar Produtos no DB Realm
		this.productsList = ProductsRealmRepository.getAll();


		// Inicia Adapter para o GridView produto
		mProductsAdapter = new ProductsGridAdapter(this, mSaleCtrl);
        mProductsAdapter.setData(this.productsList);
        gridViewProducts.setHasFixedSize(true);
        gridViewProducts.setLayoutManager(new GridLayoutManager(this, 4));
        gridViewProducts.setAdapter(mProductsAdapter);


		//Inicia total a pagar em 0
		txtTotalFinal.setAmount((float)0);
		txtTotalFinal.invalidate();

		containerInfoSale.setOnClickListener(mInfoSaleViewOnClickListener);
		txtSaleName.setText(mSaleCtrl.getCurrentSale().getClientName());
		txtSaleCode.setText(getResources().getString(R.string.txt_bt_sale_code_controll, mSaleCtrl.getCurrentSale().getCodeControll()));

		if (mSaleCtrl.getCurrentSale() != null) {
			changeListSale();
		}

	    // Inicia lista de produtos vendidos
		saleListAdapter = new SaleItensAdapter(this, mSaleCtrl);
		saleListAdapter.setData(mSaleCtrl.getCurrentSale());
	    listCartSale.setAdapter(saleListAdapter);

	    // Evento dos botoes
	    btnPaySale.setOnClickListener(mPayOnClickListener);
	    btnCancelSale.setOnClickListener(mCancelOnClickListener);
	    btnCleanSale.setOnClickListener(mCleanOnClickListener);
        btnPrint.setOnClickListener(mPrintOnClickListener);

	    // SearchView
	    setupSearchView();

	    // Desabilita algumas funcoes, de acordo com o status da venda
		SaleStatusEnum status = SaleStatusEnum.valueOf(mSaleCtrl.getCurrentSale().getStatus());
	    if (status.equals(SaleStatusEnum.PAID) || status.equals(SaleStatusEnum.CANCELED)) {
	    	btnPaySale.setVisibility(View.GONE);
	    	btnCancelSale.setVisibility(View.GONE);
	    	btnCleanSale.setVisibility(View.GONE);
	    	containerInfoSale.setOnClickListener(null);
	    	listCartSale.setEnabled(false);
	    	gridViewProducts.setAdapter(null);

	    } else if (status.equals(SaleStatusEnum.PAID_PARTIAL)) {
	    	//containerInfoSale.setOnClickListener(null);
	    	btnCancelSale.setVisibility(View.GONE);
	    	btnCleanSale.setVisibility(View.GONE);
	    }
	}

	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupSearchView() {
		mSearchView.setOnClickListener(mSearchViewOnClickListener);
	    mSearchView.setQueryHint("Buscar produto...");
	    mSearchView.setIconifiedByDefault(false);
	    mSearchView.setSubmitButtonEnabled(true);
	    
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    
	        if (searchManager != null) {
	        	// Try to use the "applications" global search provider
	        	SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
	        	
	            mSearchView.setSearchableInfo(info);
	        }
			mSearchView.setOnQueryTextListener(new SearchListener());
	    }
		mSearchView.onActionViewCollapsed();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.v(TAG, "SaleControllActivity onResume - Restaurar CurrentSaleModel");

		try {
			mSaleCtrl.getCurrentSale().addChangeListener(listenerSaleModel);
		} catch (Exception e) {}
	}

	@Override
	public void finish() {
		super.finish();
		Log.v(TAG, "SaleControllActivity finish");

		try {
			if (mSaleCtrl.getCurrentSale() != null)
				mSaleCtrl.getCurrentSale().removeAllChangeListeners();
		} catch (Exception e) {}
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG, "SaleControllActivity onStop - Salvar CurrentSaleModel");

		mSaleCtrl.onSyncSale(mSaleCtrl.getCurrentSale());

		try {
			if (mSaleCtrl.getCurrentSale() != null)
				mSaleCtrl.getCurrentSale().removeAllChangeListeners();
		} catch (Exception e) {}

		/*if (transaction != null && !transaction.isCancelled()) {
			transaction.cancel();
		}*/
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.w(TAG, "SaleControllActivity onDestroy");

		try {
			if (mSaleCtrl.getCurrentSale() != null)
				mSaleCtrl.getCurrentSale().removeAllChangeListeners();
		} catch (Exception e) {}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.v(TAG, "SaleControllActivity onBackPressed");

		boolean cancelSale = getIntent().getBooleanExtra(Constants.STRING_EXTRA_CANCEL_SALE, false);
		if (cancelSale)
			mSaleCtrl.onCancelSale(mSaleCtrl.getCurrentSale());
	}
	
	Button.OnClickListener mPayOnClickListener = 
			new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				new SalePayDialog(SaleControllActivity.this, mSaleCtrl, mSaleCtrl.getCurrentSale());
			}
		};
    Button.OnClickListener mCancelOnClickListener =
        new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogUtil.showActionDialog(SaleControllActivity.this, R.string.txt_cancel_sale_title, R.string.txt_cancel_sale_text, new CallbackModel(SaleControllActivity.this, "onCallBackCancelSale"), true);
        }
    };

    Button.OnClickListener mCleanOnClickListener =
        new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogUtil.showActionDialog(SaleControllActivity.this, R.string.txt_clean_sale_title, R.string.txt_clean_sale_text, new CallbackModel(SaleControllActivity.this, "onCallBackCleanSale"), true);
        }
    };

    public void onCallBackCancelSale() {
        mSaleCtrl.onCancelSale(mSaleCtrl.getCurrentSale());
    }
    public void onCallBackCleanSale() {
        mSaleCtrl.onCleanListSale(mSaleCtrl.getCurrentSale());
    }


    Button.OnClickListener mPrintOnClickListener =
        new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleControllActivity.this.mSaleCtrl.onClickPrintCupom();
            }
        };

    View.OnClickListener mSearchViewOnClickListener =
        new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSearchView.onActionViewExpanded();
            //searchView.performClick();
            //searchView.requestFocus();
            showTheKeyboardWhenQWERTY(SaleControllActivity.this, mSearchView);
        }
    };

    View.OnClickListener mInfoSaleViewOnClickListener =
        new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SaleNewDialog(SaleControllActivity.this, mSaleCtrl, mSaleCtrl.getCurrentSale());
        }
    };

    public void onBackPressedButton(View v) {
        onBack();
    }

    public void onBack() {
        if (mSaleCtrl.getCurrentSale() == null || mSaleCtrl.getCurrentSale().getItemList().size() <= 0)
            new SaleCloseDialog(this);
        else
            onBackPressed();
    }

    public void changeListSale() {
        if (mSaleCtrl.getCurrentSale().getItemList().size() > 0)
            btnPaySale.setEnabled(true);
        else
            btnPaySale.setEnabled(false);

		listCartSale.invalidateViews();
		listCartSale.invalidate();

        // Mostrar valor da venda com desconto (se existe desconto)
        txtTotalFinal.setAmount((float)mSaleCtrl.getCurrentSale().getTotalSale());
		txtTotalFinal.invalidate();

        // Mostrar valor da venda sem desconto (se existe desconto)
        if (mSaleCtrl.getCurrentSale().getTotalSale() < mSaleCtrl.getCurrentSale().getTotalProducts()) {
            txtTotalInit.setText(FormatUtil.toMoneyFormat(mSaleCtrl.getCurrentSale().getTotalProducts()));
            lineTxtTotalInit.setVisibility(View.VISIBLE);
        }

        // Mostrar valor da venda nos campos "valor Pago" e "Total a pagar"
        if (mSaleCtrl.getCurrentSale().getTotalPaid() > 0) {
            containerTotalPaid.setVisibility(View.VISIBLE);

            // Valor Pago
            txtTotalPaidFinal.setText(FormatUtil.toMoneyFormat(mSaleCtrl.getCurrentSale().getTotalPaid()));

            // Valor a Pagar
            double toPay = mSaleCtrl.getCurrentSale().getTotalSale() - mSaleCtrl.getCurrentSale().getTotalPaid();
            if (toPay > 0) {
                containerTotalToPay.setVisibility(View.VISIBLE);
                txtTotalToPayFinal.setText(FormatUtil.toMoneyFormat(toPay));
            }
        }
    }
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
        	onBack();
			
		} else {
			mSearchView.onActionViewExpanded();
			//searchView.performClick();
		    //searchView.requestFocus();
		}
		return false;
	}
	
	@Override 
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.i(TAG, "onConfigurationChanged(" + newConfig.toString() + ")");
		
		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
			DialogUtil.showMessageDialog(
					this, 
					R.string.txt_physical_device_conected, 
					R.string.txt_disable_physical_keyboard, 
					0,
					new CallbackModel(getApp(), "callBack"),
					false);
			
			newConfig.keyboard = Configuration.HARDKEYBOARDHIDDEN_NO;
			newConfig.hardKeyboardHidden = Configuration.HARDKEYBOARDHIDDEN_YES;
			showTheKeyboardWhenQWERTY(this, mSearchView);
			
		} else if (newConfig.hardKeyboardHidden > Configuration.HARDKEYBOARDHIDDEN_NO) {
			
			showTheKeyboardWhenQWERTY(this, mSearchView);
		}
	}
	
	/**
     * Method for showing the Keyboard
     * @param context The context of the activity
     * @param editText The edit text for which we want to show the keyboard
     */
    public void showTheKeyboard(Context context, SearchView editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
 
    /**
     * Method for showing the Keyboard when a QWERTY (physical keyboard is enabled)
     * @param context The context of the activity
     * @param editText The edit text for which we want to show the keyboard
     */
    public void showTheKeyboardWhenQWERTY(Context context, SearchView editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, 0);
    }


	
	class SearchListener implements  SearchView.OnQueryTextListener
	{
	    @Override
	    public boolean onQueryTextSubmit(String query) {
			//Log.w(TAG, "onQueryTextSubmit(" + query + ")");
	        return false;
	    }

	    @Override
	    public boolean onQueryTextChange(String newText) {
			//Log.w(TAG, "onQueryTextChange(" + newText + ")");

			mProductsAdapter.filter(newText);

	        /*if (TextUtils.isEmpty(newText)) {
	        	gridViewProducts.clearTextFilter();
	        } else {
	        	gridViewProducts.setFilterText(newText);
	        }*/
	        return true;
	    }
	};


	class SaleCtrl extends SaleController {

		public SaleCtrl(Context context) {
			super(context);
		}

		@Override
		public void onSaleItemClick(SaleItemModel saleItem) {
			SaleStatusEnum status = SaleStatusEnum.valueOf(getCurrentSale().getStatus());
			if (status.equals(SaleStatusEnum.PAID) || status.equals(SaleStatusEnum.CANCELED)) {
				return;
			}
			super.onSaleItemClick(saleItem);
			//Log.w(TAG, saleItem.getName());

			new SaleItemEditDialog(SaleControllActivity.this, mSaleCtrl, saleItem);
		}

		@Override
		public void onChangeInfoSale(SaleModel sale) {
			txtSaleName.setText(getCurrentSale().getClientName());
			txtSaleCode.setText(getResources().getString(R.string.txt_bt_sale_code_controll, getCurrentSale().getCodeControll()));
			super.onChangeInfoSale(sale);
		}

		@Override
		public void onDecrementProduct(ProductInterface product) {
			super.onDecrementProduct(product);

			Log.v(TAG, "---> onDecrementProduct");
			//Log.v(TAG, "---> onDecrementProduct - " + product.toString());
			
			//gridViewProducts.clearTextFilter();
			mSearchView.onActionViewCollapsed();
			getCurrentSale().setDecrement((Product) product);

			//mSaleCtrl.onSyncSale(getCurrentSale());
			listenerSaleModel.onChange(getCurrentSale());
		}

		@Override
		public void onIncrementProduct(ProductInterface product) {
			super.onIncrementProduct(product);

			Log.v(TAG, "---> onIncrementProduct");
			//Log.v(TAG, "---> onIncrementProduct - " + product.toString());

			if (product.getPriceResale() <= 0) {
				Product joquer = new Product((Product) product);
				joquer.setJockerProduct(true);

				new ProductChangePriceDialog(
						SaleControllActivity.this,
						joquer,
						this);
				return;
			}
			//gridViewProducts.clearTextFilter();
			mSearchView.onActionViewCollapsed();
			getCurrentSale().setIncrement((Product) product);

			//mSaleCtrl.onSyncSale(getCurrentSale());
			listenerSaleModel.onChange(getCurrentSale());
		}
		
		@Override
		public void onCancelSale(SaleModel sale) {
			super.onCancelSale(sale);
			
			finish();
		}
		
		@Override
		public void onCleanListSale(SaleModel sale) {
			super.onCleanListSale(sale);
		}

		public void onClickPrintCupom() {
            setSweetDialog(new SweetAlertDialog(SaleControllActivity.this, SweetAlertDialog.WARNING_TYPE)
					.setTitleText(getResources().getString(R.string.dialog_print_title))
					.setContentText(getResources().getString(R.string.dialog_print_msg))
					.setConfirmText(getResources().getString(R.string.action_yes_print))
					.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sDialog) {
							SaleCtrl.this.onPrintCupom();
							sDialog.dismissWithAnimation();
						}
					}));

        }
		
		@Override
		public void onPaymentMethod(SaleModel sale, PaymentMethodEnum method, double value) {
			Log.w(TAG, "TIPO PAGAMENTO - " + method.name());
			Log.w(TAG, "Valor - " + value);

			double troco = 0;

			// Pagamento total
			if (value >= getCurrentSale().getTotalRestPay()) {
				troco = value - getCurrentSale().getTotalRestPay();
				
				super.onPayTotalSale(getCurrentSale(), method);

				this.onPrintCupom();

				// Se existe troco, aparece na tela o valor.
				// Apenas para pagamento em dinheiro
				if (method == PaymentMethodEnum.MONEY && troco > 0) {
					/*DialogUtil.showMessageDialog(
							SaleControllActivity.this, 
							getString(R.string.sale_pay_spare_money), 
							getString(R.string.sale_pay_spare_money) + ": " + troco, 
							R.drawable.money, 
							new CallbackModel(SaleControllActivity.this, "finish"),
							true);*/

					new SweetAlertDialog(SaleControllActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
							.setTitleText("Troco!")
							.setContentText("Devolver " + FormatUtil.toMoneyFormat(troco))
							.setCustomImage(R.drawable.money)
							.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									finish();
								}
							})
							.show();

				} else {
					finish();
				}
				
			} else {
				// Pagamento Parcial
				super.onPayPartiallySale(getCurrentSale(), value, method);
				
				// Imprime o Cupom
				Cupom.getInstance(getContext()).runPrintSequence(sale, method, value, 0);
				
				finish();
			}
		}
	}
}
