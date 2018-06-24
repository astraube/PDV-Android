package br.com.i9algo.autaz.pdv.controllers.printer2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.Log;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.github.pierry.simpletoast.SimpleToast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.i9algo.autaz.pdv.BuildConfig;
import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.domain.constants.DateFormats;
import br.com.i9algo.autaz.pdv.domain.enums.PaymentMethodEnum;
import br.com.i9algo.autaz.pdv.domain.models.ProductSale;
import br.com.i9algo.autaz.pdv.helpers.FormatUtil;
import br.com.i9algo.autaz.pdv.helpers.Logger;


public class PrinterEpson {

	private final String LOG_TAG = PrinterEpson.class.getSimpleName();

	public static final String TARGET = "Target";

	public static String PRINTER_TARGET = null;
	public static int PRINTER_SERIES = -1;
	public int PRINTER_LANG = Printer.MODEL_ANK;
	
	private Printer mPrinter = null;


	private EpsonReceiveListener mOwner = null;
	public int lang = Printer.MODEL_ANK;


	public static String[] getSelectSeries(final Activity activity) {
		return activity.getResources().getStringArray(R.array.arr_printer_series);
	}
	
	public PrinterEpson (EpsonReceiveListener owner) {
		this.mOwner = owner;

		try {
			Log.setLogSettings(this.mOwner.getActivity(), com.epson.epos2.Log.PERIOD_TEMPORARY, Log.OUTPUT_STORAGE, null, 0, 1, Log.LOGLEVEL_LOW);
		} catch (Exception e) {
			Logger.e(LOG_TAG, e);
			ShowMsg.showException(e, "setLogSettings", this.mOwner.getActivity());
		}
	}

	public boolean isEnablePrinter() {
		return !TextUtils.isEmpty(PRINTER_TARGET);
	}

	public boolean runPrintReceiptSequence(final Cupom cupom, Date date, Map<PaymentMethodEnum, Double> reportPayMethods) {
		if (!initializeObject()) {
			return false;
		}

		if (!createReceiptDataSale(cupom, date, reportPayMethods)) {
			finalizeObject();
			return false;
		}

		if (!printData()) {
			finalizeObject();
			return false;
		}

		return true;
	}


	public boolean runPrintReceiptSequence(final Cupom cupom) {
		if (!initializeObject()) {
			return false;
		}

		if (!createReceiptDataSale(cupom)) {
		//if(!createReceiptData()) { // Teste
			finalizeObject();
			return false;
		}

		if (!printData()) {
			finalizeObject();
			return false;
		}

		return true;
	}

	private boolean createReceiptData() {
		String method = "";
		StringBuilder textData = new StringBuilder();
		final int barcodeWidth = 2;
		final int barcodeHeight = 100;

		if (mPrinter == null) {
			return false;
		}

		try {
			method = "addTextAlign";
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);

			method = "addFeedLine";
			mPrinter.addFeedLine(1);
			textData.append("THE STORE 123 (555) 555 – 5555\n");
			textData.append("STORE DIRECTOR – John Smith\n");
			textData.append("\n");
			textData.append("7/01/07 16:58 6153 05 0191 134\n");
			textData.append("ST# 21 OP# 001 TE# 01 TR# 747\n");
			textData.append("------------------------------\n");
			method = "addText";
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());

			textData.append("400 OHEIDA 3PK SPRINGF  9.99 R\n");
			textData.append("410 3 CUP BLK TEAPOT    9.99 R\n");
			textData.append("445 EMERIL GRIDDLE/PAN 17.99 R\n");
			textData.append("438 CANDYMAKER ASSORT   4.99 R\n");
			textData.append("474 TRIPOD              8.99 R\n");
			textData.append("433 BLK LOGO PRNTED ZO  7.99 R\n");
			textData.append("458 AQUA MICROTERRY SC  6.99 R\n");
			textData.append("493 30L BLK FF DRESS   16.99 R\n");
			textData.append("407 LEVITATING DESKTOP  7.99 R\n");
			textData.append("441 **Blue Overprint P  2.99 R\n");
			textData.append("476 REPOSE 4PCPM CHOC   5.49 R\n");
			textData.append("461 WESTGATE BLACK 25  59.99 R\n");
			textData.append("------------------------------\n");
			method = "addText";
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());

			textData.append("SUBTOTAL                160.38\n");
			textData.append("TAX                      14.43\n");
			method = "addText";
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());

			method = "addTextSize";
			mPrinter.addTextSize(2, 2);
			method = "addText";
			mPrinter.addText("TOTAL    174.81\n");
			method = "addTextSize";
			mPrinter.addTextSize(1, 1);
			method = "addFeedLine";
			mPrinter.addFeedLine(1);

			textData.append("CASH                    200.00\n");
			textData.append("CHANGE                   25.19\n");
			textData.append("------------------------------\n");
			method = "addText";
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());

			textData.append("Purchased item total number\n");
			textData.append("Sign Up and Save !\n");
			textData.append("With Preferred Saving Card\n");
			method = "addText";
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());
			method = "addFeedLine";
			mPrinter.addFeedLine(2);

			method = "addBarcode";
			mPrinter.addBarcode("01209457",
					Printer.BARCODE_CODE39,
					Printer.HRI_BELOW,
					Printer.FONT_A,
					barcodeWidth,
					barcodeHeight);

			method = "addCut";
			mPrinter.addCut(Printer.CUT_FEED);
		}
		catch (Exception e) {
			return false;
		}

		textData = null;

		return true;
	}

	private boolean createReceiptDataSale(final Cupom cupom, Date d, Map<PaymentMethodEnum, Double> reportPayMethods) {
		String method = "";
		StringBuilder textData = new StringBuilder();
		final int barcodeWidth = 2;
		final int barcodeHeight = 70;
		final String date = DateFormats.formatDateDivider(d, "/");

		if (mPrinter == null) {
			return false;
		}

		try {

			method = "addTextAlign";
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addTextFont(Printer.FONT_B);

			if (cupom.getCorporateImage() != null) {
				method = "addImage";
				mPrinter.addImage(cupom.getCorporateImage(), 0, 0,
						cupom.getCorporateImage().getWidth(),
						cupom.getCorporateImage().getHeight(),
						Printer.COLOR_1,
						Printer.MODE_MONO,
						Printer.HALFTONE_DITHER,
						Printer.PARAM_DEFAULT,
						Printer.COMPRESS_AUTO);
			}

			method = "addCorporateInfo";
			/********************* CABEÇALHO ************************/
			mPrinter.addFeedLine(1);
			method = "addCorporateInfo.Name";
			if (!cupom.getCorporateName().isEmpty()) {
				mPrinter.addTextSize(2, 2);
				mPrinter.addText(cupom.getCorporateName());
				mPrinter.addTextSize(1, 1);
				mPrinter.addFeedLine(2);
			}

			method = "addCorporateInfo.Phone";
			if (!cupom.getCorporatePhone().isEmpty()) {
                mPrinter.addTextAlign(Printer.ALIGN_CENTER);
				mPrinter.addText(cupom.getCorporatePhone());
			}

			method = "addCorporateInfo.Socialmedia";
			if (!cupom.getCorporateSocialmedia().isEmpty()) {
                mPrinter.addTextAlign(Printer.ALIGN_CENTER);
				mPrinter.addText(cupom.getCorporateSocialmedia());
			}

			mPrinter.addFeedLine(2);
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);
			textData.append(date + "\n");

			//textData.append("7/01/07 16:58 6153 05 0191 134\n");
			//mPrinter.addText("ST# 21 OP# 001 TE# 01 TR# 747\n");

			textData.append(this.mOwner.getActivity().getString(R.string.divider));
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());
			/********************************************************/


			/********************* REPORTS ************************/
			method = "addReports";

			mPrinter.addTextAlign(mPrinter.ALIGN_LEFT);
            mPrinter.addTextFont(Printer.FONT_B);
			mPrinter.addTextSize(1, 1);

			// Valores de pagamento dinheir/credito/...
			if (reportPayMethods.get(PaymentMethodEnum.MONEY) != null) {
				String label = mOwner.getActivity().getString(R.string.txt_payments_with, mOwner.getActivity().getString(PaymentMethodEnum.MONEY.getResourceId()));
				String value = FormatUtil.toMoneyFormat(reportPayMethods.get(PaymentMethodEnum.MONEY));

				mPrinter.addHPosition(100);
				mPrinter.addText(label);

				mPrinter.addHPosition(400);
				mPrinter.addText(value);
				mPrinter.addFeedLine(1);
				/*
				mPrinter.addFeedLine(1);
				mPrinter.addHPosition(100);
				mPrinter.addText(label);

				mPrinter.addHPosition(270);
				mPrinter.addText("");

				mPrinter.addHPosition(400);
				mPrinter.addText(value);
				mPrinter.addFeedLine(1);*/
			}

			if (reportPayMethods.get(PaymentMethodEnum.DEBT) != null) {
				String label = mOwner.getActivity().getString(R.string.txt_payments_with, mOwner.getActivity().getString(PaymentMethodEnum.DEBT.getResourceId()));
				String value = FormatUtil.toMoneyFormat(reportPayMethods.get(PaymentMethodEnum.DEBT));

				mPrinter.addHPosition(100);
				mPrinter.addText(label);

				mPrinter.addHPosition(400);
				mPrinter.addText(value);
				mPrinter.addFeedLine(1);
				/*
				mPrinter.addFeedLine(1);
				mPrinter.addHPosition(100);
				mPrinter.addText(label);

				mPrinter.addHPosition(270);
				mPrinter.addText("");

				mPrinter.addHPosition(400);
				mPrinter.addText(value);
				mPrinter.addFeedLine(1);*/
			}

			if (reportPayMethods.get(PaymentMethodEnum.CREDIT) != null) {
				String label = mOwner.getActivity().getString(R.string.txt_payments_with, mOwner.getActivity().getString(PaymentMethodEnum.CREDIT.getResourceId()));
				String value = FormatUtil.toMoneyFormat(reportPayMethods.get(PaymentMethodEnum.CREDIT));

				mPrinter.addHPosition(100);
				mPrinter.addText(label);

				mPrinter.addHPosition(400);
				mPrinter.addText(value);
				mPrinter.addFeedLine(1);
				/*
				mPrinter.addFeedLine(1);
				mPrinter.addHPosition(100);
				mPrinter.addText(label);

				mPrinter.addHPosition(270);
				mPrinter.addText("");

				mPrinter.addHPosition(400);
				mPrinter.addText(value);
				mPrinter.addFeedLine(1);*/
			}

			if (reportPayMethods.get(PaymentMethodEnum.VOUCHER) != null) {
				String label = mOwner.getActivity().getString(R.string.txt_payments_with, mOwner.getActivity().getString(PaymentMethodEnum.VOUCHER.getResourceId()));
				String value = FormatUtil.toMoneyFormat(reportPayMethods.get(PaymentMethodEnum.VOUCHER));

				mPrinter.addHPosition(100);
				mPrinter.addText(label);

				mPrinter.addHPosition(400);
				mPrinter.addText(value);
				mPrinter.addFeedLine(1);
				/*
				mPrinter.addFeedLine(1);
				mPrinter.addHPosition(100);
				mPrinter.addText(label);

				mPrinter.addHPosition(270);
				mPrinter.addText("");

				mPrinter.addHPosition(400);
				mPrinter.addText(value);
				mPrinter.addFeedLine(1);*/
			}

			mPrinter.addTextAlign(mPrinter.ALIGN_CENTER);
			/********************************************************/

			method = "addFooter";
			mPrinter.addTextFont(Printer.FONT_A);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
			mPrinter.addFeedUnit(5);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.app_catchphrase));
			mPrinter.addFeedUnit(5);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.app_site));
			mPrinter.addFeedLine(2);

			method = "addCut";
			mPrinter.addCut(Printer.CUT_FEED);

		} catch (Exception e) {
			Logger.e(LOG_TAG, e);

			ShowMsg.showException(e, method, this.mOwner.getActivity());

			return false;
		}

		textData = null;

		return true;
	}

	private boolean createReceiptDataSale(final Cupom cupom) {
		String method = "";
		StringBuilder textData = new StringBuilder();
		final int barcodeWidth = 2;
		final int barcodeHeight = 70;
		final String date = DateFormats.formatDateDivider(cupom.getSale().getCreatedAt(), "/");

		if (mPrinter == null) {
			return false;
		}

		try {

			method = "addTextAlign";
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);
			mPrinter.addTextFont(Printer.FONT_B);

			if (cupom.getCorporateImage() != null) {
				method = "addImage";
				mPrinter.addImage(cupom.getCorporateImage(), 0, 0,
						cupom.getCorporateImage().getWidth(),
						cupom.getCorporateImage().getHeight(),
						Printer.COLOR_1,
						Printer.MODE_MONO,
						Printer.HALFTONE_DITHER,
						Printer.PARAM_DEFAULT,
						Printer.COMPRESS_AUTO);
			}

			method = "addCorporateInfo";
			/********************* CABEÇALHO ************************/
			mPrinter.addFeedLine(1);
			method = "addCorporateInfo.Name";
			if (!cupom.getCorporateName().isEmpty()) {
				mPrinter.addTextSize(2, 2);
				mPrinter.addText(cupom.getCorporateName());
				mPrinter.addTextSize(1, 1);
				mPrinter.addFeedLine(2);
			}

			method = "addCorporateInfo.Phone";
            if (!cupom.getCorporatePhone().isEmpty()) {
                mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                mPrinter.addText(cupom.getCorporatePhone());
            }

            method = "addCorporateInfo.Socialmedia";
            if (!cupom.getCorporateSocialmedia().isEmpty()) {
                mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                mPrinter.addText(cupom.getCorporateSocialmedia());
            }

			mPrinter.addFeedLine(2);
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);
			textData.append(date + "\n");

			//textData.append("7/01/07 16:58 6153 05 0191 134\n");
			//mPrinter.addText("ST# 21 OP# 001 TE# 01 TR# 747\n");

			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());

			mPrinter.addTextFont(Printer.FONT_A);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
			mPrinter.addTextFont(Printer.FONT_B);
			/********************************************************/


			/********************* PRODUTOS ************************/
			method = "addProducts";
			mPrinter.addTextSize(1, 1);
			mPrinter.addTextFont(Printer.FONT_B);

			printProductsSale(cupom);

			mPrinter.addTextFont(Printer.FONT_A);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
			mPrinter.addTextFont(Printer.FONT_B);
			/********************************************************/


			/********************* TOTAL / SUBTOTAL ************************/
			method = "Subtotal";
			if (cupom.getSale().getDiscount() > 0) {
				textData.append("SUBTOTAL             " + FormatUtil.toMoneyFormat(cupom.getSale().getTotalProducts()) + "\n");
				textData.append("DESCONTO             " + FormatUtil.toMoneyFormat(cupom.getSale().getDiscount()) + "\n");
				mPrinter.addText(textData.toString());
				textData.delete(0, textData.length());
			}
			/*
			method = "addTextSize";
			mPrinter.addTextSize(2, 2);
			method = "addTotal";
			mPrinter.addText("TOTAL   " + FormatUtil.toMoneyFormat(cupom.getSale().getTotalSale()) + "\n");
			method = "addTextSize";
			mPrinter.addTextSize(1, 1);
			method = "addFeedLine";
			mPrinter.addFeedLine(1);

			textData.append("VALOR RECEBIDO           " + FormatUtil.toMoneyFormat(cupom.getAmountPaid()) + "\n");
			textData.append("TROCO                    " + FormatUtil.toMoneyFormat(cupom.getTroco()) + "\n");
			*/
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());


			mPrinter.addTextAlign(Printer.ALIGN_LEFT);

			method = "Total";
			mPrinter.addTextSize(2, 2);
			mPrinter.addHPosition(100);
			mPrinter.addText("TOTAL");
			mPrinter.addHPosition(300);
			mPrinter.addText( FormatUtil.toMoneyFormat(cupom.getSale().getTotalSale()) );
			mPrinter.addTextSize(1, 1);
			mPrinter.addFeedLine(2);

			method = "Valor Recebido";
			mPrinter.addHPosition(100);
			mPrinter.addText("VALOR RECEBIDO");
			mPrinter.addHPosition(350);
			mPrinter.addText( FormatUtil.toMoneyFormat(cupom.getAmountPaid()) );
			mPrinter.addFeedLine(1);

			method = "Troco";
			mPrinter.addHPosition(100);
			mPrinter.addText("TROCO");
			mPrinter.addHPosition(350);
			mPrinter.addText( FormatUtil.toMoneyFormat(cupom.getTroco()) );

			mPrinter.addFeedLine(1);
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);
			/********************************************************/


			/********************* METODO DE PAGAMENTO ************************/
			if (cupom.getMethod() != null) {
				method = "addMethodPayment";
				String m = this.mOwner.getActivity().getString(cupom.getMethod().getResourceId());
				if (m != null) {
					mPrinter.addTextFont(Printer.FONT_A);
					mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
					mPrinter.addTextFont(Printer.FONT_B);

					mPrinter.addText( this.mOwner.getActivity().getString(R.string.txt_payment_with, m.toLowerCase()) + "\n" );
				}
			}
			/********************************************************/


			/********************* SENHA DE ESPERA ************************/
			method = "addOrderCodeWait";
            if (cupom.getSale().existsOrderCodeWait()) {

				method = "addDivider";
				mPrinter.addTextFont(Printer.FONT_A);
				mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
				mPrinter.addTextFont(Printer.FONT_B);

				method = "addTextSize";
				mPrinter.addTextSize(2, 2);
				method = "addOrderCodeWait";

                mPrinter.addTextFont(Printer.FONT_B);
                mPrinter.addText(this.mOwner.getActivity().getString(R.string.code_wait, cupom.getSale().getOrderCodeWait()).toUpperCase() + "\n");
                mPrinter.addTextFont(Printer.FONT_A);

				method = "addTextSize";
				mPrinter.addTextSize(1, 1);

				method = "addText.SenhaEspera";
				mPrinter.addText(textData.toString());
				textData.delete(0, textData.length());
            }
			/********************************************************/

			method = "addCupomNaoFiscalMessage";
			mPrinter.addTextFont(Printer.FONT_A);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
			mPrinter.addTextFont(Printer.FONT_B);

			mPrinter.addText(this.mOwner.getActivity().getString(R.string.cupom_not_fiscal));

			mPrinter.addTextFont(Printer.FONT_A);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
			mPrinter.addTextFont(Printer.FONT_B);
			mPrinter.addFeedLine(1);


			if (!cupom.getSale().getId().isEmpty() && cupom.getSale().existsOrderCodeWait()) {
				method = "addBarcodeSaleCode";
				// Add barcode data to command buffer
				mPrinter.addBarcode(cupom.getSale().getId(),
						Printer.BARCODE_CODE93,
						Printer.HRI_BELOW,
						Printer.FONT_B,
						barcodeWidth,
						barcodeHeight);
			}

			/********************************************** PARTE DESTACADA **********************************************/

			mPrinter.addTextFont(Printer.FONT_B);

			/********************* SENHA DE ESPERA ************************/
			if (cupom.getSale().existsOrderCodeWait()) {
				method = "addCut";
				mPrinter.addCut(Printer.CUT_FEED);

				method = "addOrderCodeWait";
				mPrinter.addText(date);
				mPrinter.addFeedLine(2);
				mPrinter.addTextSize(2, 2);
				mPrinter.addText(this.mOwner.getActivity().getString(R.string.code_wait, cupom.getSale().getOrderCodeWait()).toUpperCase() + "\n");
				mPrinter.addTextSize(1, 1);

                mPrinter.addTextFont(Printer.FONT_A);
				mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
                mPrinter.addTextFont(Printer.FONT_B);

				/********************* PRODUTOS ************************/
				method = "addProductsPreparation";
				printProductsSalePreparation(cupom);

                mPrinter.addTextFont(Printer.FONT_A);
                mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
                mPrinter.addTextFont(Printer.FONT_B);
				/********************************************************/
			}

			if (!cupom.getSale().getId().isEmpty()) {
				//textData.append(this.mOwner.getActivity().getString(R.string.sale_code));
				method = "addBarcodeSaleCode";
				mPrinter.addBarcode(cupom.getSale().getId(),
						Printer.BARCODE_CODE93,
						Printer.HRI_BELOW,
						Printer.FONT_A,
						barcodeWidth,
						barcodeHeight);

				mPrinter.addFeedUnit(20);
				//mPrinter.addText("* " + sale.getId() + " *");
				mPrinter.addFeedUnit(5);

				if (BuildConfig.BACKEND_STATUS) {
					mPrinter.addText(this.mOwner.getActivity().getString(R.string.sale_detail, cupom.getSale().getId()));
					mPrinter.addFeedUnit(10);
				}
			}
			/********************* PARTE DESTACADA ************************/

			method = "addFooter";
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.divider));
			mPrinter.addFeedUnit(5);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.app_catchphrase));
			mPrinter.addFeedUnit(5);
			mPrinter.addText(this.mOwner.getActivity().getString(R.string.app_site));
			mPrinter.addFeedLine(2);

			method = "addCut";
			mPrinter.addCut(Printer.CUT_FEED);


			/*if (cupom.getMethod() != null && cupom.getMethod().equals(PaymentMethodEnum.MONEY)) {
				// Cash drawer
				mPrinter.addPulse(Printer.DRAWER_5PIN, mPrinter.PULSE_100);
			}*/
		} catch (Exception e) {
			Logger.e(LOG_TAG, e);

			ShowMsg.showException(e, method, this.mOwner.getActivity());

			return false;
		}

		textData = null;

		return true;
	}

	private void printProductsSale(Cupom cupom) throws Exception {
		mPrinter.addFeedUnit(1);
		mPrinter.addTextAlign(Printer.ALIGN_LEFT);
		mPrinter.addHPosition(100);
		mPrinter.addText("Item");
		mPrinter.addHPosition(270);
		mPrinter.addText("Unit.");
		mPrinter.addHPosition(400);
		mPrinter.addText("Total");
		mPrinter.addFeedUnit(1);

		List<ProductSale> list = cupom.getSale().getProducts();
		for (ProductSale saleItem : list) {

			String productAnnotation = saleItem.getAnnotation();
			String productName = saleItem.getName();

			/*if (productName.length() > 10) {
				try {
					productName = productName.substring(0, 10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/

			mPrinter.addFeedLine(1);
			mPrinter.addHPosition(100);
			mPrinter.addText(productName);

			mPrinter.addFeedLine(1);
			mPrinter.addHPosition(100);
			mPrinter.addText(String.valueOf(saleItem.getQuantity()) + " X UN"); // TODO - deve mostrar o tipo da unidade de medida

			mPrinter.addHPosition(270);
			mPrinter.addText(FormatUtil.toMoneyFormat(saleItem.getPriceResale(), false));

			mPrinter.addHPosition(400);
			mPrinter.addText(FormatUtil.toMoneyFormat(saleItem.getItemTotal(), false));

			if (!productAnnotation.isEmpty()) {
				mPrinter.addFeedLine(1);
				mPrinter.addHPosition(100);
				mPrinter.addText("--> " + productAnnotation);
			}
		}
		mPrinter.addFeedUnit(1);
		mPrinter.addTextAlign(mPrinter.ALIGN_CENTER);
	}

	/**
	 * Imprimir os produtos que serao preparados na cozinha
	 * @param cupom
	 * @throws Exception
	 */
	private void printProductsSalePreparation(Cupom cupom) throws Exception {
		mPrinter.addTextAlign(Printer.ALIGN_LEFT);

		List<ProductSale> list = cupom.getSale().getProducts();
		for (ProductSale saleItem : list) {

			String productAnnotation = saleItem.getAnnotation();
			String productName = saleItem.getName();

			mPrinter.addHPosition(100);
			mPrinter.addText(productName);

			mPrinter.addHPosition(400);
			mPrinter.addText(String.valueOf(saleItem.getQuantity()) + " X UN"); // TODO - deve mostrar o tipo da unidade de medida
			mPrinter.addFeedLine(1);

			if (!productAnnotation.isEmpty()) {
				mPrinter.addFeedLine(1);
				mPrinter.addHPosition(100);
				mPrinter.addText("--> " + productAnnotation);
			}
		}
		mPrinter.addTextAlign(mPrinter.ALIGN_CENTER);
	}

	private boolean printData() {
		if (mPrinter == null) {
			return false;
		}

		if (!connectPrinter()) {
			return false;
		}

		PrinterStatusInfo status = mPrinter.getStatus();

		dispPrinterWarnings(status);

		if (!isPrintable()) {
			//ShowMsg.showMsg(makeErrorMessage(status), this.mOwner.getActivity());
			try {
				mPrinter.disconnect();
			} catch (Exception e) {
				Logger.e(LOG_TAG, e);
			}
			return false;
		}

		try {
			int cupoms = PreferencesRepository.getCuponsPrint();

			// TODO - Talvez isso esteja dando pau
			for (int i = 0; i < cupoms; i++) {
				mPrinter.sendData(Printer.PARAM_DEFAULT);
			}

		} catch (Exception e) {
			Logger.e(LOG_TAG, e);

			//ShowMsg.showException(e, "sendData", this.mOwner.getActivity());

			try {
				mPrinter.disconnect();
			} catch (Exception ex) {
				Logger.e(LOG_TAG, ex);
			}
			return false;
		}

		return true;
	}

	/**
	 * Inicializa o objeto Printer das impressoras Epson
	 * @return
	 */
	private boolean initializeObject() {
		try {
			mPrinter = new Printer(PRINTER_SERIES, PRINTER_LANG, this.mOwner.getActivity());

		} catch (Exception e) {
			Logger.e(LOG_TAG, e);

			ShowMsg.showException(e, "Printer", this.mOwner.getActivity());

			return false;
		}
		mPrinter.setReceiveEventListener(this.mOwner);

		return true;
	}

	private void finalizeObject() {
		if (mPrinter == null) {
			return;
		}

		mPrinter.clearCommandBuffer();

		mPrinter.setReceiveEventListener(null);

		mPrinter = null;
	}

	public boolean connectPrinter() {
		boolean isBeginTransaction = false;

		if (mPrinter == null) {
			return false;
		}

		try {
			Logger.v(LOG_TAG, "PRINTER_TARGET: " + PRINTER_TARGET);
			mPrinter.connect(PRINTER_TARGET, Printer.PARAM_DEFAULT);
		} catch (Exception e) {
			Logger.e(LOG_TAG, e);

			ShowMsg.showException(e, "connect", this.mOwner.getActivity());

			return false;
		}

		try {
			mPrinter.beginTransaction();
			isBeginTransaction = true;
		} catch (Exception e) {
			Logger.e(LOG_TAG, e);
			ShowMsg.showException(e, "beginTransaction", this.mOwner.getActivity());
		}

		if (isBeginTransaction == false) {
			try {
				mPrinter.disconnect();
			} catch (Epos2Exception e) {
				Logger.e(LOG_TAG, e);
				// Do nothing
				return false;
			}
		}

		return true;
	}

	public void disconnectPrinter() {
		if (mPrinter == null) {
			return;
		}

		try {
			mPrinter.endTransaction();
		} catch (final Exception e) {
			Logger.e(LOG_TAG, e);

			ShowMsg.showException(e, "endTransaction", this.mOwner.getActivity());

			/*runOnUiThread(new Runnable() {
				@Override
				public synchronized void run() {
					ShowMsg.showException(e, "endTransaction", this.mOwner.getActivity());
				}
			});*/
		}

		try {
			mPrinter.disconnect();
		} catch (final Exception e) {
			Logger.e(LOG_TAG, e);
			ShowMsg.showException(e, "disconnect", this.mOwner.getActivity());

			/*runOnUiThread(new Runnable() {
				@Override
				public synchronized void run() {
					ShowMsg.showException(e, "disconnect", this.mOwner.getActivity());
				}
			});*/
		}

		finalizeObject();
	}

	public PrinterStatusInfo getStatus() {
		PrinterStatusInfo status = null;
		if (mPrinter != null) {
			status = mPrinter.getStatus();
		}
		return status;
	}

	public boolean isPrintable() {
		PrinterStatusInfo status = this.getStatus();

		if (status == null) {
			return false;
		}

		if (status.getConnection() == Printer.FALSE) {
			return false;
		}
		else if (status.getOnline() == Printer.FALSE) {
			return false;
		}
		else {
			;//print available
		}

		return true;
	}

	public String makeErrorMessage(PrinterStatusInfo status) {
		String msg = "";

		if (status.getOnline() == Printer.FALSE) {
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_offline);
		}
		if (status.getConnection() == Printer.FALSE) {
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_no_response);
		}
		if (status.getCoverOpen() == Printer.TRUE) {
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_cover_open);
		}
		if (status.getPaper() == Printer.PAPER_EMPTY) {
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_receipt_end);
		}
		if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_paper_feed);
		}
		if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_autocutter);
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_need_recover);
		}
		if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_unrecover);
		}
		if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
			if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
				msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_overheat);
				msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_head);
			}
			if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
				msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_overheat);
				msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_motor);
			}
			if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
				msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_overheat);
				msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_battery);
			}
			if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
				msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_wrong_paper);
			}
		}
		if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
			msg += this.mOwner.getActivity().getString(R.string.handlingmsg_err_battery_real_end);
		}

		return msg;
	}

	public void dispPrinterWarnings(PrinterStatusInfo status) {
		String warningsMsg = "";

		if (status == null) {
			return;
		}

		if (status.getPaper() == Printer.PAPER_NEAR_END) {
			warningsMsg += this.mOwner.getActivity().getString(R.string.handlingmsg_warn_receipt_near_end);
		}

		if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
			warningsMsg += this.mOwner.getActivity().getString(R.string.handlingmsg_warn_battery_near_end);
		}

		android.util.Log.e("Error Printer", warningsMsg);
	}

	public void updateViewState(final View view) {
		view.setEnabled(false);

		new Handler().postDelayed(
				new Runnable() {
					@Override
					public void run() {
						view.setEnabled(true);
					}
				}, 1000);
	}
}
