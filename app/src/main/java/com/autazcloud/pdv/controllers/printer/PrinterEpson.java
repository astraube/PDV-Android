package com.autazcloud.pdv.controllers.printer;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.controllers.printer2.EpsonReceiveListener;
import com.autazcloud.pdv.controllers.printer2.ShowMsg;
import com.autazcloud.pdv.domain.constants.DateFormats;
import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.models.SaleItemModel;
import com.autazcloud.pdv.helpers.FormatUtil;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.Log;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;

import java.util.List;


public class PrinterEpson {

	public static final String TARGET = "Target";

	public static String PRINTER_TARGET = null;
	private static Printer mPrinter = null;


	private Activity mActivity = null;
	private EpsonReceiveListener mOwner = null;
	public int lang = Printer.MODEL_ANK;


	public PrinterEpson (EpsonReceiveListener owner) {
		this.mOwner = owner;
		this.mActivity = owner.getActivity();

		try {
			Log.setLogSettings(this.mActivity, com.epson.epos2.Log.PERIOD_TEMPORARY, Log.OUTPUT_STORAGE, null, 0, 1, Log.LOGLEVEL_LOW);
		}
		catch (Exception e) {
			ShowMsg.showException(e, "setLogSettings", this.mActivity);
		}
	}


	public boolean runPrintReceiptSequence(final Cupom cupom) {
		if (!initializeObject()) {
			return false;
		}

		if (!createReceiptDataSale(cupom)) {
			finalizeObject();
			return false;
		}

		if (!printData()) {
			finalizeObject();
			return false;
		}

		return true;
	}

	private boolean createReceiptDataSale(final Cupom cupom) {
		String method = "";
		StringBuilder textData = new StringBuilder();
		final int barcodeWidth = 2;
		final int barcodeHeight = 70;
		final String date = DateFormats.formatDateDivider(cupom.getSale().getDateCreated(), "/");

		if (mPrinter == null) {
			return false;
		}

		try {
			method = "addTextAlign";
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);

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
				mPrinter.addFeedLine(1);
			}

			method = "addCorporateInfo.Phone";
			if (!cupom.getCorporatePhone().isEmpty()) {
				mPrinter.addTextAlign(Printer.ALIGN_LEFT);
				mPrinter.addHPosition(100);
				mPrinter.addText(cupom.getCorporatePhone());
				mPrinter.addTextAlign(Printer.ALIGN_CENTER);
			}

			method = "addCorporateInfo.Socialmedia";
			if (!cupom.getCorporateSocialmedia().isEmpty()) {
				mPrinter.addTextAlign(Printer.ALIGN_LEFT);
				if (cupom.getCorporatePhone().isEmpty()) {
					mPrinter.addHPosition(100);
				} else {
					mPrinter.addHPosition(300);
				}
				mPrinter.addText(cupom.getCorporateSocialmedia());
				mPrinter.addTextAlign(Printer.ALIGN_CENTER);
			}

			mPrinter.addFeedLine(1);
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);
			textData.append(date + "\n");

			//textData.append("7/01/07 16:58 6153 05 0191 134\n");
			//mPrinter.addText("ST# 21 OP# 001 TE# 01 TR# 747\n");

			textData.append(this.mActivity.getString(R.string.divider));
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());
			/********************************************************/


			/********************* PRODUTOS ************************/
			method = "addProducts";
			printProductsSale(cupom);
			/********************************************************/


			/********************* TOTAL / SUBTOTAL ************************/
			method = "addSubtotal";
			if (cupom.getSale().getDiscount() > 0) {
				textData.append("SUBTOTAL             " + FormatUtil.toMoneyFormat(cupom.getSale().getTotalProducts()) + "\n");
				textData.append("DESCONTO             " + FormatUtil.toMoneyFormat(cupom.getSale().getDiscount()) + "\n");
				mPrinter.addText(textData.toString());
				textData.delete(0, textData.length());
			}

			method = "addTextSize";
			mPrinter.addTextSize(2, 2);
			method = "addTotal";
			mPrinter.addText("TOTAL     " + FormatUtil.toMoneyFormat(cupom.getSale().getTotalSale()) + "\n");
			method = "addTextSize";
			mPrinter.addTextSize(1, 1);
			method = "addFeedLine";
			mPrinter.addFeedLine(1);

			textData.append("VALOR RECEBIDO           " + FormatUtil.toMoneyFormat(cupom.getAmountPaid()) + "\n");
			textData.append("TROCO                    " + FormatUtil.toMoneyFormat(cupom.getTroco()) + "\n");

			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());
			/********************************************************/


			/********************* METODO DE PAGAMENTO ************************/
			if (cupom.getMethod() != null) {
				method = "addMethodPayment";
				String m = this.mActivity.getString(cupom.getMethod().getResourceId());
				if (m != null) {
					textData.append(this.mActivity.getString(R.string.divider));
					textData.append(this.mActivity.getString(R.string.txt_payment_with, m.toLowerCase()) + "\n");
					mPrinter.addText(textData.toString());
					textData.delete(0, textData.length());
				}
			}
			/********************************************************/


			/********************* SENHA DE ESPERA ************************/
			method = "addOrderCodeWait";
            if (cupom.getSale().existsOrderCodeWait()) {

				method = "addDivider";
				textData.append(this.mActivity.getString(R.string.divider));
				mPrinter.addText(textData.toString());
				textData.delete(0, textData.length());

				method = "addTextSize";
				mPrinter.addTextSize(2, 2);
				method = "addOrderCodeWait";
				mPrinter.addText("Senha: " + cupom.getSale().getOrderCodeWait());
				method = "addTextSize";
				mPrinter.addTextSize(1, 1);
				method = "addFeedLine";
				mPrinter.addFeedLine(2);

				method = "addText.SenhaEspera";
				mPrinter.addText(textData.toString());
				textData.delete(0, textData.length());
            }
			/********************************************************/

			method = "addCupomNaoFiscal";
			textData.append(this.mActivity.getString(R.string.divider));
			textData.append(this.mActivity.getString(R.string.cupom_not_fiscal));
			textData.append(this.mActivity.getString(R.string.divider));
			mPrinter.addText(textData.toString());
			textData.delete(0, textData.length());
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

			mPrinter.addTextFont(Printer.FONT_B);

			if (cupom.getSale().existsOrderCodeWait()) {
				method = "addCut";
				mPrinter.addCut(Printer.CUT_FEED);

				method = "addOrderCodeWait";
				mPrinter.addText(date);
				mPrinter.addFeedLine(2);
				mPrinter.addTextSize(2, 2);
				mPrinter.addText(this.mActivity.getString(R.string.code_wait, cupom.getSale().getOrderCodeWait()));
				mPrinter.addTextSize(1, 1);
				mPrinter.addFeedLine(2);
				mPrinter.addText(this.mActivity.getString(R.string.divider));
			}


			if (!cupom.getSale().getId().isEmpty()) {
				//textData.append(this.mActivity.getString(R.string.sale_code));
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
				mPrinter.addText(this.mActivity.getString(R.string.sale_detail, cupom.getSale().getId()));
				mPrinter.addFeedUnit(10);
			}

			method = "addFooter";
			mPrinter.addText(this.mActivity.getString(R.string.divider));
			mPrinter.addFeedUnit(5);
			mPrinter.addText(this.mActivity.getString(R.string.app_catchphrase));
			mPrinter.addFeedUnit(5);
			mPrinter.addText(this.mActivity.getString(R.string.app_site));
			mPrinter.addFeedLine(2);

			method = "addCut";
			mPrinter.addCut(Printer.CUT_FEED);

			if (cupom.getMethod() != null && cupom.getMethod().equals(PaymentMethodEnum.MONEY)) {
				// Cash drawer
				mPrinter.addPulse(Printer.DRAWER_5PIN, mPrinter.PULSE_100);
			}
		}
		catch (Exception e) {
			ShowMsg.showException(e, method, this.mActivity);

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

		List<SaleItemModel> list = cupom.getSale().getItemList();
		for (SaleItemModel saleItem : list) {

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
			mPrinter.addText(String.valueOf(saleItem.getQuantityItem()) + " X UN");

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
		mPrinter.addText(this.mActivity.getString(R.string.divider));
	}

	/*private boolean createReceiptDataOriginal() {
		String method = "";
		Bitmap logoData = BitmapFactory.decodeResource(this.mActivity.getResources(), R.drawable.ic_launcher);
		StringBuilder textData = new StringBuilder();
		final int barcodeWidth = 2;
		final int barcodeHeight = 100;

		if (mPrinter == null) {
			return false;
		}

		try {
			method = "addTextAlign";
			mPrinter.addTextAlign(Printer.ALIGN_CENTER);

			method = "addImage";
			mPrinter.addImage(logoData, 0, 0,
					logoData.getWidth(),
					logoData.getHeight(),
					Printer.COLOR_1,
					Printer.MODE_MONO,
					Printer.HALFTONE_DITHER,
					Printer.PARAM_DEFAULT,
					Printer.COMPRESS_AUTO);

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
			ShowMsg.showException(e, method, this.mActivity);

			return false;
		}

		textData = null;

		return true;
	}*/

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
			ShowMsg.showMsg(makeErrorMessage(status), this.mActivity);
			try {
				mPrinter.disconnect();
			}
			catch (Exception ex) {
				// Do nothing
			}
			return false;
		}

		try {
			mPrinter.sendData(Printer.PARAM_DEFAULT);
		}
		catch (Exception e) {
			ShowMsg.showException(e, "sendData", this.mActivity);

			try {
				mPrinter.disconnect();
			}
			catch (Exception ex) {
				// Do nothing
			}
			return false;
		}

		return true;
	}

	private boolean initializeObject() {
		try {
			if (mPrinter == null)
				mPrinter = new Printer(Printer.TM_T20, Printer.MODEL_ANK, this.mActivity);
		}
		catch (Exception e) {
			ShowMsg.showException(e, "Printer", this.mActivity);

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

		PRINTER_TARGET = null;
	}

	public boolean connectPrinter() {
		boolean isBeginTransaction = false;

		if (mPrinter == null) {
			return false;
		}

		try {
			mPrinter.connect(PRINTER_TARGET, Printer.PARAM_DEFAULT);
		}
		catch (Exception e) {
			ShowMsg.showException(e, "connect", this.mActivity);

			return false;
		}

		try {
			mPrinter.beginTransaction();
			isBeginTransaction = true;
		}
		catch (Exception e) {
			ShowMsg.showException(e, "beginTransaction", this.mActivity);
		}

		if (isBeginTransaction == false) {
			try {
				mPrinter.disconnect();
			}
			catch (Epos2Exception e) {
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
		}
		catch (final Exception e) {
			ShowMsg.showException(e, "endTransaction", this.mActivity);

			/*runOnUiThread(new Runnable() {
				@Override
				public synchronized void run() {
					ShowMsg.showException(e, "endTransaction", this.mActivity);
				}
			});*/
		}

		try {
			mPrinter.disconnect();
		}
		catch (final Exception e) {
			ShowMsg.showException(e, "disconnect", this.mActivity);

			/*runOnUiThread(new Runnable() {
				@Override
				public synchronized void run() {
					ShowMsg.showException(e, "disconnect", this.mActivity);
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
			msg += this.mActivity.getString(R.string.handlingmsg_err_offline);
		}
		if (status.getConnection() == Printer.FALSE) {
			msg += this.mActivity.getString(R.string.handlingmsg_err_no_response);
		}
		if (status.getCoverOpen() == Printer.TRUE) {
			msg += this.mActivity.getString(R.string.handlingmsg_err_cover_open);
		}
		if (status.getPaper() == Printer.PAPER_EMPTY) {
			msg += this.mActivity.getString(R.string.handlingmsg_err_receipt_end);
		}
		if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
			msg += this.mActivity.getString(R.string.handlingmsg_err_paper_feed);
		}
		if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
			msg += this.mActivity.getString(R.string.handlingmsg_err_autocutter);
			msg += this.mActivity.getString(R.string.handlingmsg_err_need_recover);
		}
		if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
			msg += this.mActivity.getString(R.string.handlingmsg_err_unrecover);
		}
		if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
			if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
				msg += this.mActivity.getString(R.string.handlingmsg_err_overheat);
				msg += this.mActivity.getString(R.string.handlingmsg_err_head);
			}
			if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
				msg += this.mActivity.getString(R.string.handlingmsg_err_overheat);
				msg += this.mActivity.getString(R.string.handlingmsg_err_motor);
			}
			if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
				msg += this.mActivity.getString(R.string.handlingmsg_err_overheat);
				msg += this.mActivity.getString(R.string.handlingmsg_err_battery);
			}
			if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
				msg += this.mActivity.getString(R.string.handlingmsg_err_wrong_paper);
			}
		}
		if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
			msg += this.mActivity.getString(R.string.handlingmsg_err_battery_real_end);
		}

		return msg;
	}

	public void dispPrinterWarnings(PrinterStatusInfo status) {
		String warningsMsg = "";

		if (status == null) {
			return;
		}

		if (status.getPaper() == Printer.PAPER_NEAR_END) {
			warningsMsg += this.mActivity.getString(R.string.handlingmsg_warn_receipt_near_end);
		}

		if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
			warningsMsg += this.mActivity.getString(R.string.handlingmsg_warn_battery_near_end);
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
