package br.com.i9algo.autaz.pdv;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;


import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.AccountRealmRepository;
import br.com.i9algo.autaz.pdv.data.local.SalesRealmRepository;
import br.com.i9algo.autaz.pdv.domain.constants.DateFormats;
import br.com.i9algo.autaz.pdv.domain.enums.PaymentMethodEnum;
import br.com.i9algo.autaz.pdv.domain.enums.SaleStatusEnum;
import br.com.i9algo.autaz.pdv.domain.interfaces.SaleTaskInterface;
import br.com.i9algo.autaz.pdv.domain.models.Account;
import br.com.i9algo.autaz.pdv.domain.models.PaymentSale;
import br.com.i9algo.autaz.pdv.domain.models.Sale;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import br.com.i9algo.autaz.pdv.ui.adapters.SalesAllListAdapter;
import br.com.i9algo.autaz.pdv.ui.base.BaseActivity;
import br.com.i9algo.autaz.pdv.ui.dialog.CalendarDialog;
import br.com.i9algo.autaz.pdv.ui.dialog.CalendarDialog.CalendarDialogInterface;
import br.com.i9algo.autaz.pdv.ui.views.TableRowTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SalesActivity extends BaseActivity implements SaleTaskInterface, CalendarDialogInterface, OnItemSelectedListener {
	
	private final String TAG = SalesActivity.class.getSimpleName();

	@BindView(R.id.tableLayoutDetails) TableLayout tableLayoutDetails;
	@BindView(R.id.textViewTotal) TextView textViewTotal;
	@BindView(R.id.textViewDateSaled) TextView textViewDateSaled;
	@BindView(R.id.containerResume) LinearLayout containerResume;
	@BindView(R.id.spinnerTipoRelatorio) Spinner spinnerTipoRelatorio;
	@BindView(R.id.listView) ListView salesLstView;
	@BindView(R.id.imageBlurBlockView) ImageView imageBlurBlockView;

	private List<Sale> mSaleList;
	private TableRowTextView rowMoney, rowDebt, rowCredit, rowVoucher, rowOpen, rowCanceled;
	private Calendar mInitDate;
	private Calendar mFinalDate;
	private double mTotalDay = 0;
	private double mTotalOpen = 0;
	private double mTotalCanceled = 0;
	private Map<PaymentMethodEnum, Double> mTotalPayMethods = null;
	private SalesAllListAdapter mSalesListAdapter;
	
	LinearLayout.OnClickListener mOnClickChangeDate = 
		new LinearLayout.OnClickListener() {
		@Override
		public void onClick(View v) {
			new CalendarDialog(SalesActivity.this, SalesActivity.this, mInitDate);
		}
	};
	
	public SalesActivity() {
		super();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        startMixPanelApi(this);
		fullScreen();
		setContentView(R.layout.activity_sales_sync);
		ButterKnife.bind(this);

		Account account = AccountRealmRepository.getFirst();

		if (account.getSignaturePlan() == null || !account.getSignaturePlan().hasExpired()) {

			imageBlurBlockView.setVisibility(View.GONE);

			this.mInitDate = (Calendar)Calendar.getInstance().clone();
			this.mFinalDate = (Calendar)Calendar.getInstance().clone();

			rowOpen = new TableRowTextView(this, "", "", 30);
			rowCanceled = new TableRowTextView(this, "", "", 30);
			rowMoney = new TableRowTextView(this, "", "", 30);
			rowDebt = new TableRowTextView(this, "", "", 30);
			rowCredit = new TableRowTextView(this, "", "", 30);
			rowVoucher = new TableRowTextView(this, "", "", 30);

			containerResume.setOnClickListener(mOnClickChangeDate);
			spinnerTipoRelatorio.setOnItemSelectedListener(this);
		} else {
			Log.v(TAG, "------------> Periodo de teste expirou");
			imageBlurBlockView.setVisibility(View.VISIBLE);

			SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
			pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
			pDialog.setTitle(getString(R.string.err_account_expired_title));
			pDialog.setTitleText(getString(R.string.err_account_expired_title));
			pDialog.setContentText(getString(R.string.err_account_expired_msg));
			pDialog.setCancelable(false);
			pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sweetAlertDialog) {
					sweetAlertDialog.dismiss();
					SalesActivity.this.onBackPressed();
				}
			});
			setSweetDialog(pDialog);
		}
	}

	private void loadSales() {
		mTotalDay = 0;
		mTotalOpen = 0;
		mTotalCanceled = 0;
		mTotalPayMethods = new HashMap<PaymentMethodEnum, Double>();
		textViewTotal.setText(FormatUtil.toMoneyFormat(mTotalDay));
		
		tableLayoutDetails.removeAllViews();
		tableLayoutDetails.invalidate();

		this.mInitDate.set(Calendar.HOUR_OF_DAY, 00);
		this.mInitDate.set(Calendar.MINUTE, 00);
		this.mInitDate.set(Calendar.SECOND, 00);
		Date dInit = new Date(this.mInitDate.getTime().getTime());

		this.mFinalDate.set(Calendar.HOUR_OF_DAY, 00);
		this.mFinalDate.set(Calendar.MINUTE, 00);
		this.mFinalDate.set(Calendar.SECOND, 00);
		this.mFinalDate.add(Calendar.DATE, 1);
		Date dfinal = new Date(this.mFinalDate.getTime().getTime());

		//Log.v("SalesAct", "---->init "+dInit);
		//Log.v("SalesAct", "---->final "+dfinal);

		onCompleteLoadSales(SalesRealmRepository.getByDate(dInit, dfinal));
	}
	
	public void changeValuesTotal() {
		if (mSaleList == null || mSaleList.size() <= 0)
			return;
		
		tableLayoutDetails.removeAllViews();
		tableLayoutDetails.invalidate();
		
		for (Sale sale : mSaleList) {
			SaleStatusEnum status = SaleStatusEnum.valueOf(sale.getStatus());
			if (status != SaleStatusEnum.CANCELED) {
				// Valor total recebido. Vendas canceladas nao contam
				mTotalDay += sale.getTotalProducts();
				textViewTotal.setText(FormatUtil.toMoneyFormat(mTotalDay));
				
				if (status != SaleStatusEnum.OPEN || status != SaleStatusEnum.PAID_PARTIAL) {
					// Valores em aberto
					mTotalOpen += sale.getTotalRestPay();
				}
			} else {
				// Vendas canceladas
				mTotalCanceled += sale.getTotalProducts();
			}
			
			List<PaymentSale> payments = sale.getPaymentSale();
			if (payments != null && payments.size() > 0) {
				for (PaymentSale p : payments) {
					double value = p.getAmountPaid();
					
					if (mTotalPayMethods.get(p.getMethod()) != null) {
						value +=  mTotalPayMethods.get(p.getMethod());
						
						//mTotalPayMethods.remove(p.getPaymentMethod());
					}
					mTotalPayMethods.put(PaymentMethodEnum.valueOf(p.getMethod()), value);
				}
			}
		}
		
		if (mTotalCanceled > 0) {
			tableLayoutDetails.addView(rowCanceled);
			rowCanceled.setText1(getString(R.string.txt_sales_canceled));
			rowCanceled.setText2(FormatUtil.toMoneyFormat(mTotalCanceled));
		}
		
		if (mTotalOpen > 0) {
			tableLayoutDetails.addView(rowOpen);
			rowOpen.setText1(getString(R.string.txt_sales_open));
			rowOpen.setText2(FormatUtil.toMoneyFormat(mTotalOpen));
		}
		
		// Valores de pagamento dinheir/credito/...
		if (mTotalPayMethods.get(PaymentMethodEnum.MONEY) != null) {
			tableLayoutDetails.addView(rowMoney);
			rowMoney.setText1(getString(R.string.txt_payments_with, getString(PaymentMethodEnum.MONEY.getResourceId())));
			rowMoney.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.MONEY)));
		}
		
		if (mTotalPayMethods.get(PaymentMethodEnum.DEBT) != null) {
			tableLayoutDetails.addView(rowDebt);
			rowDebt.setText1(getString(R.string.txt_payments_with, getString(PaymentMethodEnum.DEBT.getResourceId())));
			rowDebt.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.DEBT)));
		}
		
		if (mTotalPayMethods.get(PaymentMethodEnum.CREDIT) != null) {
			tableLayoutDetails.addView(rowCredit);
			rowCredit.setText1(getString(R.string.txt_payments_with, getString(PaymentMethodEnum.CREDIT.getResourceId())));
			rowCredit.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.CREDIT)));
		}

		if (mTotalPayMethods.get(PaymentMethodEnum.VOUCHER) != null) {
			tableLayoutDetails.addView(rowVoucher);
			rowVoucher.setText1(getString(R.string.txt_payments_with, getString(PaymentMethodEnum.VOUCHER.getResourceId())));
			rowVoucher.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.VOUCHER)));
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		super.finish();
	}


	@Override
	public void onCompleteLoadSales(List<Sale> saleList) {
		Log.v(TAG, "onCompleteLoadSales ------------");
		mSaleList = saleList;
		
		// Cria adapter para o GridView
		this.mSalesListAdapter = new SalesAllListAdapter(this, this);
		this.mSalesListAdapter.setData(mSaleList);
		salesLstView.setAdapter(mSalesListAdapter);
		
		String s = "";

		// TODo melhorar isso
		if (this.mInitDate.getTime().getTime() < this.mFinalDate.getTime().getTime()) {
			// O usuario selecionou um RANGE de duas datas
			s = getString(R.string.txt_total_saled_range, 
					DateFormats.formatDate(this.mInitDate.getTime(), DateFormats.getDateLocale("/")),
					DateFormats.formatDate(this.mFinalDate.getTime(), DateFormats.getDateLocale("/")));
		} else {
			// O usuario selecionou apenas um dia
			s = getString(R.string.txt_total_saled, DateFormats.formatDate(this.mInitDate.getTime(), DateFormats.getDateLocale("/")));
		}
		
		textViewDateSaled.setText(s);
		
		changeValuesTotal();
	}

	@Override
	public void onChangeDate(int year, int month, int dayOfMonth) {
		Log.e(TAG, "onChangeDate");
		this.mInitDate = (Calendar)Calendar.getInstance().clone();
		this.mInitDate.set(Calendar.YEAR, year);
		this.mInitDate.set(Calendar.MONTH, month);
		this.mInitDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		this.mFinalDate = (Calendar)Calendar.getInstance().clone();
		this.mFinalDate.set(Calendar.YEAR, year);
		this.mFinalDate.set(Calendar.MONTH, month);
		this.mFinalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		loadSales();
	}
	
	@Override
	public void onChangeDate (Calendar initCalendar, Calendar finalCalendar) {
		//onChangeDate (initCalendar.getTime(), finalCalendar.getTime());
		Log.e(TAG, "onChangeDate");
		if (!finalCalendar.after(initCalendar) && !finalCalendar.equals(initCalendar)) {
			Log.e(TAG, "A data inserida esta incorreta");
			finalCalendar = initCalendar;
		}
		this.mInitDate = initCalendar;
		this.mFinalDate = finalCalendar;

		loadSales();
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.v(TAG, "onItemSelected - pos: " + pos + " - id: " + id);
		Calendar cInit = (Calendar)Calendar.getInstance().clone();
		Calendar cFinal = (Calendar)Calendar.getInstance().clone();
		
		switch (pos) {
			case 0:
				break;
			case 1:
				cInit.set(Calendar.DAY_OF_WEEK, cInit.getActualMinimum(cInit.DAY_OF_WEEK));
				cFinal.set(Calendar.DAY_OF_WEEK, cFinal.getActualMaximum(cFinal.DAY_OF_WEEK));
				break;
			case 2:
				cInit.set(Calendar.DAY_OF_MONTH, cInit.getActualMinimum(cInit.DAY_OF_MONTH));
				cFinal.set(Calendar.DAY_OF_MONTH, cFinal.getActualMaximum(cFinal.DAY_OF_MONTH));
				break;
			case 3:
				cInit.set(Calendar.DAY_OF_YEAR, cInit.getActualMinimum(cInit.DAY_OF_YEAR));
				cFinal.set(Calendar.DAY_OF_YEAR, cFinal.getActualMaximum(cFinal.DAY_OF_YEAR));
				break;
		}
		
		this.onChangeDate(cInit, cFinal);
    }
    public void onNothingSelected(AdapterView<?> parent) {
        Log.v(TAG, "onNothingSelected");
    }
}
