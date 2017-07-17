package com.autazcloud.pdv;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.autazcloud.pdv.data.local.SalesRealmRepository;
import com.autazcloud.pdv.domain.constants.DateFormats;
import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.enums.SaleStatusEnum;
import com.autazcloud.pdv.domain.interfaces.SaleTaskInterface;
import com.autazcloud.pdv.domain.models.Payment;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.ui.adapters.SalesAllListAdapter;
import com.autazcloud.pdv.helpers.FormatUtil;
import com.autazcloud.pdv.ui.base.BaseActivity;
import com.autazcloud.pdv.ui.dialog.CalendarDialog;
import com.autazcloud.pdv.ui.dialog.CalendarDialog.CalendarDialogInterface;
import com.autazcloud.pdv.ui.views.TableRowTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesActivity extends BaseActivity implements SaleTaskInterface, CalendarDialogInterface, OnItemSelectedListener {
	
	private final String TAG = SalesActivity.class.getSimpleName();

	@BindView(R.id.tableLayoutDetails) TableLayout tableLayoutDetails;
	@BindView(R.id.textViewTotal) TextView textViewTotal;
	@BindView(R.id.textViewDateSaled) TextView textViewDateSaled;
	@BindView(R.id.containerResume) LinearLayout containerResume;
	@BindView(R.id.spinnerTipoRelatorio) Spinner spinnerTipoRelatorio;
	@BindView(R.id.listView) ListView salesLstView;

	private List<SaleModel> mSaleList;
	private TableRowTextView rowMoney, rowDebt, rowCredit, rowVoucher, rowOpen, rowCanceled;
	private Date mInitDate, mFinalDate;
	private double mTotalDay = 0;
	private double mTotalOpen = 0;
	private double mTotalCanceled = 0;
	private Map<PaymentMethodEnum, Double> mTotalPayMethods = null;
	private SalesAllListAdapter mSalesListAdapter;
	
	LinearLayout.OnClickListener mOnClickChangeDate = 
		new LinearLayout.OnClickListener() {
		@Override
		public void onClick(View v) {
			new CalendarDialog(SalesActivity.this, SalesActivity.this);
		}
	};
	
	public SalesActivity() {
		super();
	}
	
	private void loadSales() {
		mTotalDay = 0;
		mTotalOpen = 0;
		mTotalCanceled = 0;
		mTotalPayMethods = new HashMap<PaymentMethodEnum, Double>();
		textViewTotal.setText(FormatUtil.toMoneyFormat(mTotalDay));
		
		tableLayoutDetails.removeAllViews();
		
		/*new SaleSQLiteListTask(this, this).execute(	DateFormats.formatDate(this.mInitDate, DateFormats.getDateGlobal()),
													DateFormats.formatDate(this.mFinalDate, DateFormats.getDateGlobal()));*/

		String initDate = DateFormats.formatDate(this.mInitDate, DateFormats.getDateGlobal());
		String finalDate = DateFormats.formatDate(this.mFinalDate, DateFormats.getDateGlobal());

		onCompleteLoadSales(SalesRealmRepository.getSalesDate(initDate, finalDate));
	}
	
	public void changeValuesTotal() {
		if (mSaleList == null || mSaleList.size() <= 0)
			return;
		
		tableLayoutDetails.removeAllViews();
		
		for (SaleModel sale : mSaleList) {
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
			
			List<Payment> payments = sale.getPaymentList();
			if (payments != null && payments.size() > 0) {
				for (Payment p : payments) {
					double value = p.getAmountPaid();
					
					if (mTotalPayMethods.get(p.getPaymentMethod()) != null) {
						value +=  mTotalPayMethods.get(p.getPaymentMethod());
						
						//mTotalPayMethods.remove(p.getPaymentMethod());
					}
					mTotalPayMethods.put(PaymentMethodEnum.valueOf(p.getPaymentMethod()), value);
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
			rowMoney.setText1(getString(R.string.txt_payments_with, getString(R.string.sale_pay_type_money)));
			rowMoney.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.MONEY)));
		}
		
		if (mTotalPayMethods.get(PaymentMethodEnum.DEBT) != null) {
			tableLayoutDetails.addView(rowDebt);
			rowDebt.setText1(getString(R.string.txt_payments_with, getString(R.string.sale_pay_type_debt)));
			rowDebt.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.DEBT)));
		}
		
		if (mTotalPayMethods.get(PaymentMethodEnum.CREDIT) != null) {
			tableLayoutDetails.addView(rowCredit);
			rowCredit.setText1(getString(R.string.txt_payments_with, getString(R.string.sale_pay_type_credit)));
			rowCredit.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.CREDIT)));
		}

		if (mTotalPayMethods.get(PaymentMethodEnum.VOUCHER) != null) {
			tableLayoutDetails.addView(rowVoucher);
			rowVoucher.setText1(getString(R.string.txt_payments_with, getString(R.string.sale_pay_type_voucher)));
			rowVoucher.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.VOUCHER)));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fullScreen();
		setContentView(R.layout.activity_sales_sync);
		ButterKnife.bind(this);
		
		this.mInitDate = new Date();
		//this.mInitDate.setDate(15); // TODO teste
		this.mFinalDate = new Date();
		//this.mFinalDate.setDate(17); // TODO teste

		rowOpen = new TableRowTextView(this, "", "", 30);
		rowCanceled = new TableRowTextView(this, "", "", 30);
		rowMoney = new TableRowTextView(this, "", "", 30);
		rowDebt = new TableRowTextView(this, "", "", 30);
		rowCredit = new TableRowTextView(this, "", "", 30);
		rowVoucher = new TableRowTextView(this, "", "", 30);

		containerResume.setOnClickListener(mOnClickChangeDate);
		spinnerTipoRelatorio.setOnItemSelectedListener(this);
		
		loadSales();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		super.finish();
	}


	@Override
	public void onCompleteLoadSales(List<SaleModel> saleList) {
		Log.v(TAG, "onCompleteLoadSales ------------");
		mSaleList = saleList;
		
		// Cria adapter para o GridView
		this.mSalesListAdapter = new SalesAllListAdapter(this, this);
		this.mSalesListAdapter.setData(mSaleList);
		salesLstView.setAdapter(mSalesListAdapter);
		
		String s = "";
		
		if (this.mInitDate.getTime() < this.mFinalDate.getTime()) {
			// O usuario selecionou um RANGE de duas datas
			s = getString(R.string.txt_total_saled_range, 
					DateFormats.formatDate(this.mInitDate, DateFormats.getDateLocale("/")),
					DateFormats.formatDate(this.mFinalDate, DateFormats.getDateLocale("/")));
		} else {
			// O usuario selecionou apenas um dia
			s = getString(R.string.txt_total_saled, DateFormats.formatDate(this.mInitDate, DateFormats.getDateLocale("/")));
		}
		
		textViewDateSaled.setText(s);
		
		changeValuesTotal();
	}
	
	@Override
	public void onChangeListViewAdapter(SaleModel sale) {
        Log.v(TAG, "onChangeListViewAdapter ------------");

		/*if (sale.getStatus() == SaleStatusEnum.CANCELED)
			return;
		
		// Valor total recebido. Vendas canceladas nao contam
		mTotalDay += sale.getTotalProducts();
		textViewTotal.setText(FormatUtil.toMoneyFormat(mTotalDay));
		
		//Log.v("SalesActivity", "getStatus: " + item.getStatus() + "\ngetPaymentList: " + item.getPaymentList());
		
		List<Payment> payments = sale.getPaymentList();
		if (payments != null && payments.size() > 0) {
			for (Payment p : payments) {
				double value = p.getAmountPaid();
				
				if (mTotalPayMethods.get(p.getPaymentMethod()) != null) {
					value +=  mTotalPayMethods.get(p.getPaymentMethod());
					
					//mTotalPayMethods.remove(p.getPaymentMethod());
				}
				mTotalPayMethods.put(p.getPaymentMethod(), value);
			}
			// Valores de pagamento dinheir/credito/...
			if (mTotalPayMethods.get(PaymentMethodEnum.MONEY) != null) {
				tableLayoutDetails.addView(rowMoney);
				rowMoney.setText1(getString(R.string.txt_payments_with, getString(R.string.sale_pay_type_money)));
				rowMoney.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.MONEY)));
			}

			if (mTotalPayMethods.get(PaymentMethodEnum.DEBT) != null) {
				tableLayoutDetails.addView(rowDebt);
				rowDebt.setText1(getString(R.string.txt_payments_with, getString(R.string.sale_pay_type_debt)));
				rowDebt.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.DEBT)));
			}

			if (mTotalPayMethods.get(PaymentMethodEnum.CREDIT) != null) {
				tableLayoutDetails.addView(rowCredit);
				rowCredit.setText1(getString(R.string.txt_payments_with, getString(R.string.sale_pay_type_credit)));
				rowCredit.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.CREDIT)));
			}

			if (mTotalPayMethods.get(PaymentMethodEnum.VOUCHER) != null) {
				tableLayoutDetails.addView(rowVoucher);
				rowVoucher.setText1(getString(R.string.txt_payments_with, getString(R.string.sale_pay_type_voucher)));
				rowVoucher.setText2(FormatUtil.toMoneyFormat(mTotalPayMethods.get(PaymentMethodEnum.VOUCHER)));
			}
		}*/
	}
	
	@Override
	public void onChangeDate (Calendar calendar) {
		this.onChangeDate(calendar, calendar);
	}
	
	@Override
	public void onChangeDate (Calendar initCalendar, Calendar finalCalendar) {
		onChangeDate (initCalendar.getTime(), finalCalendar.getTime());
	}
	
	@Override
	public void onChangeDate (Date initCalendar, Date finalCalendar) {
		if (!finalCalendar.after(initCalendar)) {
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
