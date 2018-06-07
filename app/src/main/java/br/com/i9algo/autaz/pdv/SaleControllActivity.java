package br.com.i9algo.autaz.pdv;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.github.pierry.simpletoast.SimpleToast;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.List;

import br.com.i9algo.autaz.pdv.controllers.SaleController;
import br.com.i9algo.autaz.pdv.controllers.printer2.Cupom;
import br.com.i9algo.autaz.pdv.controllers.printer2.EpsonReceiveListener;
import br.com.i9algo.autaz.pdv.controllers.printer2.PrinterEpson;
import br.com.i9algo.autaz.pdv.controllers.printer2.ShowMsg;
import br.com.i9algo.autaz.pdv.data.local.ProductsRealmRepository;
import br.com.i9algo.autaz.pdv.domain.constants.Constants;
import br.com.i9algo.autaz.pdv.domain.enums.PaymentMethodEnum;
import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.models.CallbackModel;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.domain.models.ProductSale;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import br.com.i9algo.autaz.pdv.helpers.OrderCodeUtil;
import br.com.i9algo.autaz.pdv.ui.adapters.ProductsGridAdapter;
import br.com.i9algo.autaz.pdv.ui.adapters.SaleItensAdapter;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;
import br.com.i9algo.autaz.pdv.ui.dialog.DialogUtil;
import br.com.i9algo.autaz.pdv.ui.dialog.ProductChangePriceDialog;
import br.com.i9algo.autaz.pdv.ui.dialog.SaleCloseDialog;
import br.com.i9algo.autaz.pdv.ui.dialog.SaleItemEditDialog;
import br.com.i9algo.autaz.pdv.ui.dialog.SaleNewDialog;
import br.com.i9algo.autaz.pdv.ui.dialog.SalePayDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class SaleControllActivity extends BaseActivity implements EpsonReceiveListener {

	private final String TAG;


	@BindView(R.id.gridView)
	RecyclerView gridViewProducts;
	@BindView(R.id.txtTotalInit) TextView txtTotalInit;
	@BindView(R.id.txtTotalFinal) MoneyTextView txtTotalFinal;
	@BindView(R.id.txtSaleName) TextView txtSaleName;
	@BindView(R.id.txtSaleControllCode) TextView txtSaleControllCode;
	@BindView(R.id.txtSaleCode) TextView txtSaleCode;
	@BindView(R.id.txtTotalPaidFinal) TextView txtTotalPaidFinal;
	@BindView(R.id.txtTotalToPayFinal) TextView txtTotalToPayFinal;
	@BindView(R.id.containerInfoSale) RelativeLayout containerInfoSale;
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


	RealmChangeListener<Sale> listenerSaleModel = new RealmChangeListener<Sale>() {
		@Override
		public void onChange(Sale saleModel) {
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
        startMixPanelApi(this);
		fullScreen();
		setContentView(R.layout.activity_sale_controller);

		ButterKnife.bind(this);

		this.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);

		this.realm = Realm.getDefaultInstance();

		this.mSaleCtrl = new SaleCtrl(this);

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

		txtSaleName.setText(mSaleCtrl.getCurrentSale().getClient().getName());

		if (!mSaleCtrl.getCurrentSale().getCodeControll().isEmpty())
			txtSaleControllCode.setText(getResources().getString(R.string.txt_bt_sale_code_controll, mSaleCtrl.getCurrentSale().getCodeControll()));


		txtSaleCode.setText(mSaleCtrl.getCurrentSale().getId());

		if (mSaleCtrl.getCurrentSale() != null) {
			changeListSale();
		}

	    // Inicia lista de produtos vendidos
		saleListAdapter = new SaleItensAdapter(this, mSaleCtrl);
		saleListAdapter.setData(mSaleCtrl.getCurrentSale());
	    listCartSale.setAdapter(saleListAdapter);

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
			mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
			});
	    }
		mSearchView.onActionViewCollapsed();
	}

	@Override
	public void onStart() {
		super.onStart();

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
    public void onPause() {
        super.onPause();
        Log.v(TAG, "SaleControllActivity onPause");

        mSaleCtrl.onSyncSale(mSaleCtrl.getCurrentSale());
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
		if (cancelSale && (mSaleCtrl.getCurrentSale().getStatus().equals(SaleStatusEnum.OPEN.toString())))
			mSaleCtrl.onCancelSale(mSaleCtrl.getCurrentSale());
	}

	@OnClick(R.id.btnPaySale)
	public void onClickBtnPaySale() {
		new SalePayDialog(SaleControllActivity.this, mSaleCtrl, mSaleCtrl.getCurrentSale());
	}

	@OnClick(R.id.btnCancelSale)
	public void onClickBtnCancelSale() {
		CallbackModel callback = new CallbackModel(SaleControllActivity.this, "onCallBackCancelSale");
		DialogUtil.showActionDialog(SaleControllActivity.this, R.string.txt_cancel_sale_title, R.string.txt_cancel_sale_text, callback, true);
	}

	@OnClick(R.id.btnCleanSale)
	public void onClickBtnCleanSale() {
		CallbackModel callback = new CallbackModel(SaleControllActivity.this, "onCallBackCleanSale");
		DialogUtil.showActionDialog(SaleControllActivity.this, R.string.txt_clean_sale_title, R.string.txt_clean_sale_text, callback, true);
	}

	@OnClick(R.id.btnPrint)
	public void onClickBtnPrintSale() {

		setSweetDialog(new SweetAlertDialog(SaleControllActivity.this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText(getResources().getString(R.string.dialog_print_title))
				.setContentText(getResources().getString(R.string.dialog_print_msg))
				.setConfirmText(getResources().getString(R.string.action_yes_print))
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						mSaleCtrl.onPrintCupom(null);
						sDialog.dismissWithAnimation();
					}
				}));
	}

    public void onCallBackCancelSale() {
        mSaleCtrl.onCancelSale(mSaleCtrl.getCurrentSale());
    }
    public void onCallBackCleanSale() {
        mSaleCtrl.onCleanListSale(mSaleCtrl.getCurrentSale());
    }


	@OnClick(R.id.searchView)
	public void onClickSearchView() {
		mSearchView.onActionViewExpanded();
		//searchView.performClick();
		//searchView.requestFocus();
		showTheKeyboardWhenQWERTY(SaleControllActivity.this, mSearchView);
	}

	@OnClick(R.id.containerInfoSale)
	public void onClickInfoSaleView() {
		new SaleNewDialog(SaleControllActivity.this, mSaleCtrl, mSaleCtrl.getCurrentSale());
	}

    public void onBackPressedButton(View v) {
        onBack();
    }

    public void onBack() {
        if (mSaleCtrl.getCurrentSale() == null || mSaleCtrl.getCurrentSale().getProducts().size() <= 0)
            new SaleCloseDialog(this);
        else
            onBackPressed();
    }

    public void changeListSale() {
        if (mSaleCtrl.getCurrentSale().getProducts().size() > 0)
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
        if (mSaleCtrl.getCurrentSale().getValueTotalPaid() > 0) {
            containerTotalPaid.setVisibility(View.VISIBLE);

            // Valor Pago
            txtTotalPaidFinal.setText(FormatUtil.toMoneyFormat(mSaleCtrl.getCurrentSale().getValueTotalPaid()));

            // Valor a Pagar
            double toPay = mSaleCtrl.getCurrentSale().getTotalSale() - mSaleCtrl.getCurrentSale().getValueTotalPaid();
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


	class SaleCtrl extends SaleController {

		public SaleCtrl(Context context) {
			super(context);
		}

		@Override
		public void onSaleItemClick(ProductSale saleItem) {
			SaleStatusEnum status = SaleStatusEnum.valueOf(getCurrentSale().getStatus());
			if (status.equals(SaleStatusEnum.PAID) || status.equals(SaleStatusEnum.CANCELED)) {
				return;
			}
			super.onSaleItemClick(saleItem);
			//Log.w(TAG, saleItem.getName());

			new SaleItemEditDialog(SaleControllActivity.this, mSaleCtrl, saleItem);
		}

		@Override
		public void onChangeInfoSale(Sale sale) {
			txtSaleName.setText(getCurrentSale().getClient().getName());

			if (!mSaleCtrl.getCurrentSale().getCodeControll().isEmpty())
				txtSaleControllCode.setText(getResources().getString(R.string.txt_bt_sale_code_controll, getCurrentSale().getCodeControll()));

			txtSaleCode.setText(mSaleCtrl.getCurrentSale().getId());

			super.onChangeInfoSale(sale);
		}

		@Override
		public void onDecrementProduct(Product product) {
			super.onDecrementProduct(product);

			Log.v(TAG, "---> onDecrementProduct");
			//Log.v(TAG, "---> onDecrementProduct - " + product.toString());

			//gridViewProducts.clearTextFilter();
			mSearchView.onActionViewCollapsed();
			if (getCurrentSale().setDecrement((Product) product)) {
                animateDecrementProduct();
            }

			//mSaleCtrl.onSyncSale(getCurrentSale());
			listenerSaleModel.onChange(getCurrentSale());
		}

		@Override
		public void onIncrementProduct(Product product) {
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

			// Gerar senha de pedido
			if (!getCurrentSale().existsOrderCodeWait() && product.isRequestPass()) {
				String code = OrderCodeUtil.nextCode(getContext());
				getCurrentSale().setOrderCodeWait(code);
				Log.v(TAG, "OrderCodeWait --> " + getCurrentSale().getOrderCodeWait());
			}

			//gridViewProducts.clearTextFilter();
			mSearchView.onActionViewCollapsed();
			getCurrentSale().setIncrement((Product) product);

            animateIncrementProduct();

			//mSaleCtrl.onSyncSale(getCurrentSale());
			listenerSaleModel.onChange(getCurrentSale());
		}

		private void animateImageView(@NonNull int resID) {
			final ImageView imageView = (ImageView) findViewById(R.id.imageView);
			imageView.setImageResource(resID);

			imageView.setVisibility(View.VISIBLE);

            if (imageView.getAnimation() != null)
            	//imageView.getAnimation().cancel();
                imageView.getAnimation().reset();
            else {
                imageView.animate()
                        .scaleX(6)
                        .scaleY(6)
                        .alpha(1.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                imageView.animate()
                                        .alpha(0.0f)
                                        .setDuration(300)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                imageView.setScaleY(0);
                                                imageView.setScaleX(0);
                                                imageView.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        });
            }
		}
		private void animateIncrementProduct() {
			animateImageView(R.drawable.ic_add_white_48dp);
		}

		private void animateDecrementProduct() {
			animateImageView(R.drawable.ic_remove_white_48dp);
		}
		
		@Override
		public void onCancelSale(Sale sale) {
			super.onCancelSale(sale);
			
			finish();
		}
		
		@Override
		public void onCleanListSale(Sale sale) {
			super.onCleanListSale(sale);

			animateDecrementProduct();
		}

		@Override
		public void onPrintCupom(final View v) {
			// Imprime o Cupom sem aparecer o troco

			// Desativar temporariamente este botao
			/*if (v != null) {
				SaleControllActivity.this.printerEpson.updateViewState(v);
			}*/

			// Imprime Cupom
			onPrintCupom2(null, 0, 0);
		}

		private void onPrintCupom2(final PaymentMethodEnum method, final double value, final double troco) {
			// Imprime Cupom
			Cupom cupom = new Cupom();
			cupom.setSale(getCurrentSale());
			cupom.setMethod(method);
			cupom.setAmountPaid(value);
			cupom.setTroco(troco);
			cupom.setCorporateName("Witt Burger"); // TODO - isso deve ser dinamico
			//cupom.setCorporatePhone("(41) 988558596"); // TODO - isso deve ser dinamico
			cupom.setCorporateSocialmedia("#WittBurger"); // TODO - isso deve ser dinamico
			//cupom.setCorporateImage(getResources(), R.drawable.ic_action_add_alarm);

			final PrinterEpson printerEpson = PrinterEpson.getInstance(SaleControllActivity.this);

			if (PrinterEpson.PRINTER_TARGET != null && !printerEpson.runPrintReceiptSequence(cupom)) {
                SimpleToast.error(SaleControllActivity.this, "Não foi possivel imprimir o cupom");
			}
			finish();
		}
		
		@Override
		public void onPaymentMethod(Sale sale, final PaymentMethodEnum method, final double value) {
			Log.w(TAG, "TIPO PAGAMENTO - " + method.name());
			Log.w(TAG, "Valor - " + value);


			// Pagamento total
			if (value >= getCurrentSale().getTotalRestPay()) {
				final double troco = value - getCurrentSale().getTotalRestPay();
				
				super.onPayTotalSale(getCurrentSale(), method);

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
									// Imprime Cupom
									onPrintCupom2(method, value, troco);
								}
							})
							.show();

				} else {
					// Imprime Cupom
					onPrintCupom2(method, value, 0);
				}
				
			} else {
				// Pagamento Parcial
				super.onPayPartiallySale(getCurrentSale(), value, method);
				
				// Imprime Cupom
				onPrintCupom2(method, value, 0);
			}
		}
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
		runOnUiThread(new Runnable() {
			@Override
			public synchronized void run() {

				if (PrinterEpson.PRINTER_TARGET != null) {
					final PrinterEpson printerEpson = PrinterEpson.getInstance(SaleControllActivity.this);

					String msg = printerEpson.makeErrorMessage(status);
					ShowMsg.showResult(code, "oi"+msg, getActivity());

					printerEpson.dispPrinterWarnings(status);

					new Thread(new Runnable() {
						@Override
						public void run() {
							printerEpson.disconnectPrinter();
						}
					}).start();
				}
			}
		});
	}
}
