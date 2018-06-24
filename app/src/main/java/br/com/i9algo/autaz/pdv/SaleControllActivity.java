package br.com.i9algo.autaz.pdv;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import br.com.i9algo.autaz.pdv.events.OnCallbackEvent;
import br.com.i9algo.autaz.pdv.domain.constants.Constants;
import br.com.i9algo.autaz.pdv.domain.enums.PaymentMethodEnum;
import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.domain.models.ProductSale;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import br.com.i9algo.autaz.pdv.helpers.Logger;
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


	private final String LOG_TAG = PrinterEpson.class.getSimpleName();


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
	@BindView(R.id.btnPrint) ImageButton btnPrint; // Botao Imprimir cupom
	@BindView(R.id.btnPaySale) Button btnPaySale; // Botao Pagar compra
	@BindView(R.id.btnCancelSale) Button btnCancelSale;
	@BindView(R.id.btnCleanSale) Button btnCleanSale;
	@BindView(R.id.listCartSale) ListView listCartSale;

	@BindView(R.id.lineTxtTotalInit) View lineTxtTotalInit;


	private Realm realm;
	private SaleCtrl mSaleCtrl;
	private ProductsGridAdapter mProductsAdapter;
	private SaleItensAdapter saleListAdapter;
	private List<Product> productsList;

	private PrinterEpson mPrinterEpson;

	private boolean isAutoClose = true;



	public static Intent createIntent(Context context) {
		return new Intent(context, SaleControllActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
				.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	}
	public static void startActivityIfDiff(Activity activity) {
		if (!activity.getClass().getSimpleName().equals(SaleControllActivity.class.getSimpleName())){
			activity.startActivity(createIntent(activity));
		}
	}


	RealmChangeListener<Sale> listenerSaleModel = new RealmChangeListener<Sale>() {
		@Override
		public void onChange(Sale saleModel) {
			//Log.w(TAG, "RealmChangeListener onChange");
			//mSaleCtrl.onSyncSale(saleModel);
			changeListSale();
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        startMixPanelApi(this);
		fullScreen();
		setContentView(R.layout.activity_sale_controller);

		ButterKnife.bind(this);

		this.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);

		this.realm = Realm.getDefaultInstance();

		this.mPrinterEpson = new PrinterEpson(SaleControllActivity.this); // TODO - trabalhar com injectors

		this.mSaleCtrl = new SaleCtrl(this);

		updateButtonState(true);

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
					//Log.w(LOG_TAG, "onQueryTextSubmit(" + query + ")");
					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					//Log.w(LOG_TAG, "onQueryTextChange(" + newText + ")");

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
		Log.v(LOG_TAG, "SaleControllActivity onResume - Restaurar CurrentSaleModel");

		try {
			mSaleCtrl.getCurrentSale().addChangeListener(listenerSaleModel);
		} catch (Exception e) {}
	}

	@Override
	public void finish() {
		super.finish();
		Log.v(LOG_TAG, "SaleControllActivity finish");

		try {
			if (mSaleCtrl.getCurrentSale() != null)
				mSaleCtrl.getCurrentSale().removeAllChangeListeners();
		} catch (Exception e) {}
	}


    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "SaleControllActivity onPause");

        mSaleCtrl.onSyncSale(mSaleCtrl.getCurrentSale());
    }

	@Override
	public void onStop() {
		super.onStop();
		Log.v(LOG_TAG, "SaleControllActivity onStop - Salvar CurrentSaleModel");

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
		Log.w(LOG_TAG, "SaleControllActivity onDestroy");

		try {
			if (mSaleCtrl.getCurrentSale() != null)
				mSaleCtrl.getCurrentSale().removeAllChangeListeners();
		} catch (Exception e) {}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.v(LOG_TAG, "SaleControllActivity onBackPressed");

		boolean cancelSale = getIntent().getBooleanExtra(Constants.EXTRA_CANCEL_SALE, false);
		if (cancelSale && (mSaleCtrl.getCurrentSale().getStatus().equals(SaleStatusEnum.OPEN.toString())))
			mSaleCtrl.onCancelSale(mSaleCtrl.getCurrentSale());
	}

	@OnClick(R.id.btnPaySale)
	public void onClickBtnPaySale() {
		new SalePayDialog(SaleControllActivity.this, mSaleCtrl, mSaleCtrl.getCurrentSale());
	}

	@OnClick(R.id.btnCancelSale)
	public void onClickBtnCancelSale() {
		OnCallbackEvent callback = new OnCallbackEvent(SaleControllActivity.this, "onCallBackCancelSale");
		DialogUtil.showActionDialog(SaleControllActivity.this, R.string.txt_cancel_sale_title, R.string.txt_cancel_sale_text, callback, true);
	}

	@OnClick(R.id.btnCleanSale)
	public void onClickBtnCleanSale() {
		OnCallbackEvent callback = new OnCallbackEvent(SaleControllActivity.this, "onCallBackCleanSale");
		DialogUtil.showActionDialog(SaleControllActivity.this, R.string.txt_clean_sale_title, R.string.txt_clean_sale_text, callback, true);
	}

	@OnClick(R.id.btnPrint)
	public void onClickBtnPrintSale(final View v) {

		setSweetDialog(new SweetAlertDialog(SaleControllActivity.this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText(getResources().getString(R.string.dialog_print_title))
				.setContentText(getResources().getString(R.string.dialog_print_msg))
				.setConfirmText(getResources().getString(R.string.action_yes_print))
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						// Imprime o Cupom sem aparecer o troco
						isAutoClose = false;
						mSaleCtrl.onPrintCupom(null, 0, 0);

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
        Log.i(LOG_TAG, "onConfigurationChanged(" + newConfig.toString() + ")");

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            DialogUtil.showMessageDialog(
                    this,
                    R.string.txt_physical_device_conected,
                    R.string.txt_disable_physical_keyboard,
                    0,
                    new OnCallbackEvent(getApp(), "callBack"),
                    false);

            newConfig.keyboard = Configuration.HARDKEYBOARDHIDDEN_NO;
            newConfig.hardKeyboardHidden = Configuration.HARDKEYBOARDHIDDEN_YES;
            showTheKeyboardWhenQWERTY(this, mSearchView);

        } else if (newConfig.hardKeyboardHidden > Configuration.HARDKEYBOARDHIDDEN_NO) {

            showTheKeyboardWhenQWERTY(this, mSearchView);
        }
    }


	private void updateButtonState(boolean state) {
		if (mPrinterEpson.isEnablePrinter())
			btnPrint.setEnabled(state);
		else {
			btnPrint.setEnabled(false);
			btnPrint.setVisibility(View.GONE);
		}

		btnPaySale.setEnabled(state);
	}

	/**
	 * Model Sale controller
	 *
	 * TODO - separar isso, trabalhar com injectors
	 */
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
			//Log.w(LOG_TAG, saleItem.getName());

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

			Log.v(LOG_TAG, "---> onDecrementProduct");
			//Log.v(LOG_TAG, "---> onDecrementProduct - " + product.toString());

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

			Log.v(LOG_TAG, "---> onIncrementProduct");
			//Log.v(LOG_TAG, "---> onIncrementProduct - " + product.toString());

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
				Log.v(LOG_TAG, "OrderCodeWait --> " + getCurrentSale().getOrderCodeWait());
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

		/**
		 * Imprime Cupom
		 *
		 * @param method
		 * @param value
		 * @param troco
		 */
		@Override
		public void onPrintCupom(final PaymentMethodEnum method, final double value, final double troco) {

			// Desativar temporariamente este botao
			updateButtonState(false);

			// TODO - Criar esquema de cupom com (Head, Body, Footer)
			// Imprime Cupom
			Cupom cupom = createCupom(method, value, troco);

			//if (mPrinterEpson.isEnablePrinter() && !mPrinterEpson.runPrintReceiptSequence(cupom)) {
			if (!mPrinterEpson.runPrintReceiptSequence(cupom)) {
				updateButtonState(true);

                SimpleToast.error(SaleControllActivity.this, "Não foi possivel imprimir o cupom");

				new SweetAlertDialog(SaleControllActivity.this, SweetAlertDialog.ERROR_TYPE)
						.setTitleText( getString(R.string.err_oops) )
						.setContentText( getString(R.string.err_printer_cupom) )
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								SaleControllActivity.this.finish();
							}
						})
						.show();

			}
		}

		/**
		 * Criar Cupom
		 * TODO - Criar esquema de cupom com (Head, Body, Footer)
		 * @return
		 */
		private Cupom createCupom(PaymentMethodEnum method, double value, double troco) {
			Cupom cupom = new Cupom();
			cupom.setSale(getCurrentSale());
			cupom.setMethod(method);
			cupom.setAmountPaid(value);
			cupom.setTroco(troco);
			cupom.setCorporateName("Witt Burger"); // TODO - isso deve ser dinamico
			cupom.setCorporatePhone("(41) 99847-0357"); // TODO - isso deve ser dinamico
			cupom.setCorporateSocialmedia("#WittBurger"); // TODO - isso deve ser dinamico
			//cupom.setCorporateImage(getResources(), R.drawable.ic_action_add_alarm);

			return cupom;
		}
		
		@Override
		public void onPaymentMethod(Sale sale, final PaymentMethodEnum method, final double value) {
			Log.w(LOG_TAG, "TIPO PAGAMENTO - " + method.name());
			Log.w(LOG_TAG, "Valor - " + value);

			isAutoClose = true;

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
							new OnCallbackEvent(SaleControllActivity.this, "finish"),
							true);*/

					new SweetAlertDialog(SaleControllActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
							.setTitleText("Troco!")
							.setContentText("Devolver " + FormatUtil.toMoneyFormat(troco))
							.setCustomImage(R.drawable.money)
							.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									// Imprime Cupom
									onPrintCupom(method, value, troco);
								}
							})
							.show();

				} else {
					// Imprime Cupom
					onPrintCupom(method, value, 0);
				}
				
			} else {
				// Pagamento Parcial
				super.onPayPartiallySale(getCurrentSale(), value, method);
				
				// Imprime Cupom
				onPrintCupom(method, value, 0);
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

				String msg = mPrinterEpson.makeErrorMessage(status);

				if (!TextUtils.isEmpty(msg)) {
					Logger.e(msg);
					ShowMsg.showResult(code, msg, getActivity());
				}
				mPrinterEpson.dispPrinterWarnings(status);

				updateButtonState(true);

				new Thread(new Runnable() {
					@Override
					public void run() {
						mPrinterEpson.disconnectPrinter();

						if (isAutoClose)
							SaleControllActivity.this.finish();
					}
				}).start();
			}
		});
	}
}
