package br.com.i9algo.autaz.pdv;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Trace;
import android.support.annotation.IdRes;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.RatingEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.controllers.MaterialIntroController;
import br.com.i9algo.autaz.pdv.controllers.SaleController;
import br.com.i9algo.autaz.pdv.data.local.ProductsRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.SalesRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.SignaturePlanRealmRepository;
import br.com.i9algo.autaz.pdv.data.remote.repositoryes.AuthRepository;
import br.com.i9algo.autaz.pdv.data.remote.repositoryes.ProductsRepository;
import br.com.i9algo.autaz.pdv.data.remote.repositoryes.SaleRepository;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.models.CallbackModel;
import br.com.i9algo.autaz.pdv.domain.models.Client;
import br.com.i9algo.autaz.pdv.domain.models.Product;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.domain.models.SignaturePlan;
import br.com.i9algo.autaz.pdv.domain.models.outbound.SaleApi;
import br.com.i9algo.autaz.pdv.ui.adapters.SalesGridAdapter;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;
import br.com.i9algo.autaz.pdv.ui.dialog.DialogUtil;
import br.com.i9algo.autaz.pdv.ui.dialog.SaleNewDialog;
import br.com.i9algo.autaz.pdv.ui.views.FloatUserButtonView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.fabric.sdk.android.Fabric;
import io.realm.RealmResults;


public class SalesGridActivity extends BaseActivity implements SubscriberInterface {
	
	private final String TAG;
	private SaleCtrl mSaleCtrl;
	private SalesGridAdapter mSalesGridAdapter;
	private SaleStatusEnum currentStatus;
	private List<Sale> mCurrentSaleList;
	private MaterialIntroController mIntroCtrl;
	private ProductsRepository mProductsRepo;
	private AuthRepository mAuthRepo;

	@BindView(R.id.btNewSale)
	Button btNewSale;

	@BindView(R.id.gridView)
	GridView gridView;

	@BindView(R.id.searchView)
	SearchView mSearchView;

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

    /**************************************************************************/
    private static final int PHOTO_WAS_PICKED = 2;

    public void setBackgroundImage(final View view) {
        final Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_WAS_PICKED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (PHOTO_WAS_PICKED == requestCode && null != data) {
            final Uri imageUri = data.getData();
            if (null != imageUri) {
                // AsyncTask, please...
                final ContentResolver contentResolver = getContentResolver();
                try {
                    final InputStream imageStream = contentResolver.openInputStream(imageUri);
                    System.out.println("DRAWING IMAGE FROM URI " + imageUri);
                    final Bitmap background = BitmapFactory.decodeStream(imageStream);
                    getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), background));
                } catch (final FileNotFoundException e) {
                    Log.e(TAG, "Image apparently has gone away", e);
                }
            }
        }
    }
    /**************************************************************************/


	
	@Override
	//@AddTrace(name = "onCreateTrace", enabled = true)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startMixPanelApi(this);
		fullScreen();
		setContentView(R.layout.activity_sales_grid);
		//Log.v(TAG, "onCreate");
		ButterKnife.bind(this);

		// Firebase performance
		//Trace myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
		//onCreateTrace.start();

		// API WEB - Repository "Auth"
		//this.mAuthRepo = new AuthRepository(this);

		// API WEB - Verifica as credenciais do usuario
		//this.mAuthRepo.onValidateCredentialsUser(this);

		// API WEB - Repository "Products"
		this.mProductsRepo = new ProductsRepository(this);

		
		this.mCurrentSaleList = new ArrayList<Sale>();

		getApp().startAlarmReceiver();

		mSaleCtrl = new SaleCtrl(this);

		mSalesGridAdapter = new SalesGridAdapter(this, mSaleCtrl);
		gridView.setAdapter(mSalesGridAdapter);

		segmentedButons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
				mSearchView.onActionViewCollapsed();
				RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
				setCurrentStatus(SaleStatusEnum.valueOf(rb.getTag().toString()));
			}
		});
		RadioButton rbt = (RadioButton) findViewById(R.id.OPEN);
		rbt.setSelected(true);
		setCurrentStatus(SaleStatusEnum.OPEN);

		// SearchView
		setupSearchView();


		// TODO teste
		List<Sale> sales = SalesRealmRepository.getAllNotSynchronized();

		if (sales != null && sales.size() > 0) {
			for (Sale s : sales) {
				Log.v(TAG, "TESTES - Sales getValueTotalPaid - " + s.getAccountId());
				Log.v(TAG, "TESTES - Sales getUserId - " + s.getUserId());
				Log.v(TAG, "TESTES - Sales getValueTotalPaid - " + s.getValueTotalPaid());
				Log.v(TAG, "TESTES - Sales getTotalProducts- " + s.getTotalProducts());
				Log.v(TAG, "TESTES - Sales getTotalSale- " + s.getTotalSale());
				Log.v(TAG, "TESTES - Sales isSyncronized- " + s.isSyncronized());
			}

			SaleRepository repoSale = new SaleRepository(this);
			//Sale s = new Sale(sales.get(0));
			SaleApi s = new SaleApi(sales.get(0));
			repoSale.storeSale(s);
		}

	}


	// fabric.io
	/*public void onKeyMetric() {
		Answers.getInstance().logLogin(new LoginEvent()
				.putMethod("Digits")
				.putSuccess(true));

		Answers.getInstance().logCustom(new CustomEvent("Video Played")
				.putCustomAttribute("Category", "Comedy")
				.putCustomAttribute("Length", 350));

		Answers.getInstance().logPurchase(new PurchaseEvent()
			  .putItemPrice(BigDecimal.valueOf(13.50))
			  .putCurrency(Currency.getInstance("BRL"))
			  .putItemName("Answers Shirt")
			  .putItemType("Apparel")
			  .putItemId("sku-350")
			  .putSuccess(true));

		Answers.getInstance().logContentView(new ContentViewEvent()
				.putContentName("Answers setup process super easy!")
				.putContentType("Technical documentation")
				.putContentId("article-350"));

		Answers.getInstance().logShare(new ShareEvent()
				.putMethod("Twitter")
				.putContentName("Answers named #2 in Mobile Analytics")
				.putContentType("tweet")
				.putContentId("601072000245858305"));

		Answers.getInstance().logRating(new RatingEvent()
				.putRating(6)
				.putContentName("Answers setup process super easy!")
				.putContentType("Technical documentation")
				.putContentId("article-350"));
	}*/


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

					mSalesGridAdapter.filter(newText);

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

	@OnClick(R.id.btNewSale)
	public void onClickBtNewSale() {
		mSearchView.onActionViewCollapsed();
		new SaleNewDialog(SalesGridActivity.this, mSaleCtrl, null);
	}

	@OnClick(R.id.searchView)
	public void onClickSearchView() {
		mSearchView.onActionViewExpanded();
		//searchView.performClick();
		//searchView.requestFocus();
		showTheKeyboardWhenQWERTY(SalesGridActivity.this, mSearchView);
	}

	private void setCurrentStatus(SaleStatusEnum statusSale) {
		this.currentStatus = statusSale;
		List<Sale> salesDb = SalesRealmRepository.getByStatus(this.currentStatus);

		if (salesDb != null) {
			this.mCurrentSaleList = salesDb;
			this.mSalesGridAdapter.setData(this.mCurrentSaleList);
		}
	}

	private void startIntro() {
		this.mIntroCtrl = new MaterialIntroController(this);
		this.mIntroCtrl.createIntro(btNewSale, getString(R.string.intro_new_sale), "intro_new_sale");
		//mIntroCtrl.createIntro(mSearchView, getString(R.string.intro_search_sale), "intro_search_sale");
		//this.mIntroCtrl.createIntro(btTeste, getString(R.string.intro_menu_button), "intro_menu_button");
		this.mIntroCtrl.start();
	}

	@Override
	public void onStart() {
		super.onStart();

		// Aqui só vai baixar os produtos se ainda nao sincronizou nenhum
		// A sincronizacao convencional fica na classe "SampleSchedulingService"
		if (ProductsRealmRepository.count() == 0)
			this.mProductsRepo.onLoadProducts("");

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
		mSearchView.clearFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();

		// Serve para remover o focus do serchView
		mSearchView.clearFocus();
    }
    
    @Override 
    public void onBackPressed() {
        //super.onBackPressed();
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();

		} else {
			mSearchView.onActionViewExpanded();
			//mSearchView.performClick();
			//mSearchView.requestFocus();
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
		public void onOpenSale(Sale sale) {
			super.onOpenSale(sale);
			//Log.w(TAG, "openSale(" + sale.getClient().getName() + ")");

            openCtrlSaleActivity();
		}
	}
}
