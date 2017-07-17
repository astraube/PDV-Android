package com.autazcloud.pdv;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.autazcloud.pdv.controllers.MaterialIntroController;
import com.autazcloud.pdv.controllers.SaleController;
import com.autazcloud.pdv.data.local.ProductsRealmRepository;
import com.autazcloud.pdv.data.local.SalesRealmRepository;
import com.autazcloud.pdv.data.remote.repositoryes.AuthRepository;
import com.autazcloud.pdv.data.remote.repositoryes.ProductsRepository;
import com.autazcloud.pdv.data.remote.subscribers.SubscriberInterface;
import com.autazcloud.pdv.domain.enums.SaleStatusEnum;
import com.autazcloud.pdv.domain.models.CallbackModel;
import com.autazcloud.pdv.domain.models.Client;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.ui.adapters.SalesGridAdapter;
import com.autazcloud.pdv.ui.base.BaseActivity;
import com.autazcloud.pdv.ui.dialog.DialogUtil;
import com.autazcloud.pdv.ui.dialog.SaleNewDialog;
import com.autazcloud.pdv.ui.views.FloatUserButtonView;
import com.madx.updatechecker.lib.UpdateRunnable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.android.segmented.SegmentedGroup;


public class SalesGridActivity extends BaseActivity implements SubscriberInterface {
	
	private final String TAG;
	private SaleCtrl mSaleCtrl;
	private SalesGridAdapter mSalesGridAdapter;
	private SaleStatusEnum currentStatus;
	private List<SaleModel> mCurrentSaleList;
	private MaterialIntroController mIntroCtrl;
	private ProductsRepository mProductsRepo;
	private AuthRepository mAuthRepo;

	@BindView(R.id.btNewSale)
	Button btNewSale;

	@BindView(R.id.gridView)
	GridView gridView;

	@BindView(R.id.searchView)
	SearchView searchView;

	@BindView(R.id.segmentedButons)
    SegmentedGroup segmentedButons;

	@BindView(R.id.layoutScreen)
	RelativeLayout layoutScreen;

	@BindView(R.id.btOperator)
	FloatUserButtonView btOperator;

	@BindView(R.id.btTeste)
	Button btTeste;

	public SalesGridActivity () {
		super();
		this.TAG = getClass().getSimpleName();
	}

	// color chooser dialog
	private int primaryPreselect;
	// UTILITY METHODS
	private int accentPreselect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fullScreen();
		setContentView(R.layout.activity_sales_grid);
        ButterKnife.bind(this);
		//Log.v(TAG, "onCreate");
		
		this.mCurrentSaleList = new ArrayList<SaleModel>();

		getApp().startAlarmReceiver();

		mSaleCtrl = new SaleCtrl(this);

		mSalesGridAdapter = new SalesGridAdapter(this, mSaleCtrl);
		gridView.setAdapter(mSalesGridAdapter);

		btNewSale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                new SaleNewDialog(SalesGridActivity.this, mSaleCtrl, null);
			}
		});

		segmentedButons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
				RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
				setCurrentStatus(SaleStatusEnum.valueOf(rb.getTag().toString()));
			}
		});
		RadioButton rbt = (RadioButton) findViewById(R.id.OPEN);
		rbt.setSelected(true);
		setCurrentStatus(SaleStatusEnum.OPEN);

		this.mProductsRepo = new ProductsRepository(this);
		this.mAuthRepo = new AuthRepository(this);

		this.mAuthRepo.onValidateCredentialsUser(this);

		new UpdateRunnable(this, new Handler(), 10 * 1000).start();
	}

	private void setCurrentStatus(SaleStatusEnum statusSale) {
		this.currentStatus = statusSale;
		List<SaleModel> salesDb = SalesRealmRepository.getSalesByStatus(this.currentStatus);

		if (salesDb != null) {
			this.mCurrentSaleList = salesDb;
			this.mSalesGridAdapter.setData(this.mCurrentSaleList);
		}
	}

	private void startIntro() {
		this.mIntroCtrl = new MaterialIntroController(this);
		this.mIntroCtrl.createIntro(btNewSale, "Clique aqui para criar uma nova venda.", "bt_new_sale");
		//mIntroCtrl.createIntro(searchView, "Busque uma venda específica, a partir do nome do cliente, código da mesa ou do pedido, ou até mesmo digitando o valor da venda.", "search_view");
		this.mIntroCtrl.createIntro(btTeste, "Aqui você tem a lista dos seus produtos, relatório básico de vendas, e configurações.", "btTeste");
		this.mIntroCtrl.start();

		/*new TapTargetSequence(this)
				.targets(
						TapTarget.forView(btNewSale, "fdgdfgdfgfdg", "fgdfgdfg")
								.dimColor(R.color.dark_gray_press)
								.outerCircleColor(R.color.green)
								.textColor(R.color.white)
								.tintTarget(false)
								.transparentTarget(false)
								.cancelable(false),
						TapTarget.forView(btOperator, "You", "Up")
                                .dimColor(R.color.dark_gray_press)
                                .outerCircleColor(R.color.green)
								.targetCircleColor(R.color.red_dark)
								.textColor(R.color.white)
                                .tintTarget(false)
                                .transparentTarget(false)
								.cancelable(false))
				.listener(new TapTargetSequence.Listener() {
					// This listener will tell us when interesting(tm) events happen in regards
					// to the sequence
					@Override
					public void onSequenceFinish() {
						// Yay
					}

					@Override
					public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

					}

					@Override
					public void onSequenceCanceled(TapTarget lastTarget) {
						// Boo
					}
				}).start();*/
	}

	@Override
	public void onStart() {
		super.onStart();

		if (ProductsRealmRepository.count() == 0)
			this.mProductsRepo.onLoadProducts();

		/*
		// Cria adapter para o GridView e cria o botao "Nova Venda"
		int numSales = (getApp().getSalesListCache() != null) ? getApp().getSalesListCache().size() : 0;
		List<SaleModel> list = new ArrayList<SaleModel>();
		list.add(null); // Botao 'Nova Venda'
		for (int i = 0; i < numSales; i++) {
			list.add(getApp().getSalesListCache().get(i));
		}
		mSalesGridAdapter.setData(list);
		*/

		this.setCurrentStatus(this.currentStatus);

		this.startIntro();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Log.v(TAG, "onWindowFocusChanged - " + hasFocus);

		if (hasFocus) {
			this.mIntroCtrl.start();
		} else {
			this.mIntroCtrl.stop();
		}
	}
	
	@Override
    protected void onPause() {
        super.onPause();

		// Serve para remover o focus do serchView
		searchView.clearFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();

		// Serve para remover o focus do serchView
		searchView.clearFocus();
    }
    
    @Override 
    public void onBackPressed() {
        //super.onBackPressed();
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
		}
	}

	public void openCtrlSaleActivity() {
		Intent intent = getIntent();
		intent = new Intent(SalesGridActivity.this, SaleControllActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	class SaleCtrl extends SaleController {

		public SaleCtrl(Context context) {
			super(context);
		}

		@Override
		public void onAddNewSale(Client client, String codeSale, String saller) {
			super.onAddNewSale(client, codeSale, saller);
			//Log.w(TAG, "addNewSale(" + nameSale + ", " + codeSale + ", " + saller + ")");

            openCtrlSaleActivity();
		}

		@Override
		public void onOpenSale(SaleModel sale) {
			super.onOpenSale(sale);
			//Log.w(TAG, "openSale(" + sale.getClientName() + ")");

            openCtrlSaleActivity();
		}
	}
}
