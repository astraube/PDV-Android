package com.autazcloud.pdv.controllers.printer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.autazcloud.pdv.R;
import com.autazcloud.pdv.data.local.PreferencesRepository;
import com.autazcloud.pdv.domain.constants.DateFormats;
import com.autazcloud.pdv.domain.enums.PaymentMethodEnum;
import com.autazcloud.pdv.domain.models.SaleItemModel;
import com.autazcloud.pdv.domain.models.SaleModel;
import com.autazcloud.pdv.helpers.FormatUtil;
import com.autazcloud.pdv.helpers.OrderCodeUtil;
import com.autazcloud.pdv.helpers.ShowMsg;
import com.epson.eposprint.BatteryStatusChangeEventListener;
import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.epson.eposprint.Print;
import com.epson.eposprint.StatusChangeEventListener;

import java.util.List;

public class Cupom implements StatusChangeEventListener, BatteryStatusChangeEventListener  {

	private static Cupom instance = null;
	
	private Context mContext;
	
	public static Cupom getInstance(Context context) {
		if (instance == null)
			return (new Cupom(context));
		
		return (instance);
	}
	
	public Cupom(Context context) {
		instance = this;
		mContext = context;
	}
	
	
	public synchronized void runPrintSequence(final SaleModel sale, final PaymentMethodEnum method, final double amountPaid, final double troco) {
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	Result result = new Result();
		        Builder builder = null;
		        
		        try {
                    Log.v("Cupom", "---> getPrinterStatus - " + result.getPrinterStatus());
                    Log.v("Cupom", "---> getBatteryStatus - " + result.getBatteryStatus());
                    Log.v("Cupom", "---> getEposException - " + result.getEposException());
                    Log.v("Cupom", "---> getEpsonIoException - " + result.getEpsonIoException());

                    builder = createReceiptData(result, sale, method, amountPaid, troco);
			
			        if (result.getEposException() == null) {
			            print(builder, result);
			        }
			
			        displayMsg(result);
			
			        // Clear objects 
			        if (builder != null) {
			            builder.clearCommandBuffer();
			        }
		        } catch (Exception e) {
		        	
		        }
		        builder = null;
		        result = null;
		    }
		}).start();
    }


    // Senha de espera
    private boolean requestPassWait = false;


    private Builder createReceiptData(Result result, SaleModel sale, PaymentMethodEnum method, double amountPaid, final double troco) {
        Builder builder = null;

        // Top logo data 
        Bitmap logoData = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

        // Text buffer 
        StringBuilder textData = new StringBuilder();

        // addBarcode API settings 
        final int barcodeWidth = 2;
        final int barcodeHeight = 100;
        final String date = DateFormats.formatDateDivider(sale.getDateCreated(), "/");

        // Null check
        if (result == null) {
            return null;
        }

        // init result
        result.setPrinterStatus(0);
        result.setBatteryStatus(0);
        result.setEposException(null);
        result.setEpsonIoException(null);

        try {
            // Create builder 
            builder = new Builder(PrinterEpson.mDeviceName, Builder.LANG_EN);

            /*builder.addTextAlign(Builder.ALIGN_LEFT);
            builder.addFeedLine(1);
            builder.addTextPosition(0);
            builder.addText("aaaaaa");
            builder.addFeedLine(1);

            builder.addTextPosition(100);
            builder.addText("bbbbbbbb");
            builder.addFeedLine(1);

            builder.addTextPosition(250);
            builder.addText("ccccccc");
            builder.addFeedLine(1);

            builder.addTextPosition(300);
            builder.addText("dddddd");
            builder.addFeedLine(1);

            builder.addTextPosition(400);
            builder.addText("dddddd");
            builder.addFeedLine(1);

            builder.addTextPosition(507);
            builder.addText("eeeeeee");
            builder.addFeedLine(1);*/


            // Set alignment to center
            builder.addTextAlign(Builder.ALIGN_CENTER);
            //builder.addTextLineSpace(30);

            /*
            // Add top logo to command buffer
            builder.addImage(logoData, 0, 0,
                             logoData.getWidth(),
                             logoData.getHeight(),
                             Builder.COLOR_1,
                             Builder.MODE_MONO,
                             Builder.HALFTONE_DITHER,
                             Builder.PARAM_DEFAULT,
                             getCompress(PrinterEpson.mDeviceType));

            // Add receipt text to command buffer
            */

            /********************* CABEÇALHO ************************/
            builder.addFeedLine(1);
            builder.addTextDouble(Builder.TRUE, Builder.TRUE);
            builder.addText("Arca do Sabor");
            //builder.addText("Redes Sociais - #WittBurger");
            builder.addTextDouble(Builder.FALSE, Builder.FALSE);
            textData.append("\n");
            textData.append(date + "\n" );
            //textData.append("ST# 21 OP# 001 TE# 01 TR# 747\n");
            builder.addText(textData.toString());
            textData.delete(0, textData.length());
            /********************************************************/


            /********************* PRODUTOS ************************/
            printProductsSale(result, builder, sale);
            /********************************************************/


            /********************* TOTAL / SUBTOTAL ************************/
            if (sale.getDiscount() > 0) {
            	textData.append("SUBTOTAL             " + FormatUtil.toMoneyFormat(sale.getTotalProducts()) + "\n");
            	textData.append("DESCONTO             " + FormatUtil.toMoneyFormat(sale.getDiscount()) + "\n");
                builder.addText(textData.toString());
                textData.delete(0, textData.length());
            }
            builder.addTextDouble(Builder.TRUE, Builder.TRUE);
            builder.addText("TOTAL   " + FormatUtil.toMoneyFormat(sale.getTotalSale()) + "\n");
            builder.addTextDouble(Builder.FALSE, Builder.FALSE);
            builder.addFeedLine(1);

            textData.append("VALOR PAGO            " + FormatUtil.toMoneyFormat(amountPaid) + "\n");
            
            if (troco > 0) {
				textData.append("TROCO                 " + FormatUtil.toMoneyFormat(troco) + "\n");
            }
            builder.addText(textData.toString());
            textData.delete(0, textData.length());
            builder.addFeedLine(1);
            /********************************************************/

            /********************* METODO DE PAGAMENTO ************************/
            String m = "";
            // TODO deixar mais dinamico
            if ((method != null) && method.equals(PaymentMethodEnum.MONEY)) {
            	m = this.mContext.getString(R.string.sale_pay_type_money);
            } else if ((method != null) && method.equals(PaymentMethodEnum.DEBT)) {
                m = this.mContext.getString(R.string.sale_pay_type_debt);
            } else if ((method != null) && method.equals(PaymentMethodEnum.CREDIT)) {
                m = this.mContext.getString(R.string.sale_pay_type_credit);
            } else if ((method != null) && method.equals(PaymentMethodEnum.VOUCHER)) {
                m = this.mContext.getString(R.string.sale_pay_type_voucher);
            }
            textData.append(this.mContext.getString(R.string.txt_payment_with, m.toLowerCase()) + "\n");
            builder.addText(textData.toString());
            textData.delete(0, textData.length());
            /********************************************************/


            /********************* SENHA DE ESPERA ************************/
            String orderCodeWait = OrderCodeUtil.nextCode(mContext);
            if (requestPassWait) {
                builder.addText(this.mContext.getString(R.string.divider));
                builder.addFeedLine(1);
                builder.addTextDouble(Builder.TRUE, Builder.FALSE);
                builder.addText("Senha: " + orderCodeWait);
                builder.addTextDouble(Builder.FALSE, Builder.FALSE);
                builder.addFeedLine(2);
            }
            /********************************************************/

            builder.addText(this.mContext.getString(R.string.divider));
            builder.addText(this.mContext.getString(R.string.cupom_not_fiscal));
            builder.addText(this.mContext.getString(R.string.divider));
            builder.addFeedLine(1);


            if (!sale.getId().isEmpty() && requestPassWait) {
                // Add barcode data to command buffer
                builder.addBarcode(sale.getId(),
                        Builder.BARCODE_CODE93,
                        Builder.HRI_BELOW,
                        Builder.FONT_B,
                        barcodeWidth,
                        70);
            }

            builder.addTextFont(Builder.FONT_B);


            if (requestPassWait) {
                builder.addCut(Builder.CUT_FEED);

                builder.addText(date);
                builder.addFeedLine(2);
                builder.addTextDouble(Builder.TRUE, Builder.TRUE);
                builder.addText(this.mContext.getString(R.string.code_wait, orderCodeWait));
                builder.addTextDouble(Builder.FALSE, Builder.FALSE);
                builder.addFeedLine(2);
                builder.addText(this.mContext.getString(R.string.divider));
            }

            if (!sale.getId().isEmpty()) {
                //textData.append(this.mContext.getString(R.string.sale_code));

                // Add barcode data to command buffer
                builder.addBarcode(sale.getId(),
                        Builder.BARCODE_CODE93,
                        Builder.HRI_BELOW,
                        Builder.FONT_B,
                        barcodeWidth,
                        70);

                builder.addFeedUnit(20);
                //builder.addText("* " + sale.getId() + " *");
                builder.addFeedUnit(5);
                builder.addText(this.mContext.getString(R.string.sale_detail, sale.getId()));
                builder.addFeedUnit(10);
            }
            builder.addText(this.mContext.getString(R.string.divider));
            builder.addFeedUnit(5);
            builder.addText(this.mContext.getString(R.string.app_catchphrase));
            builder.addFeedUnit(5);
            builder.addText(this.mContext.getString(R.string.app_site));
            builder.addFeedLine(2);


            // Add command to cut receipt to command buffer
            builder.addCut(Builder.CUT_FEED);

            if (method.equals(PaymentMethodEnum.MONEY)) {
                // Cash drawer
                builder.addPulse(builder.DRAWER_1, builder.PULSE_100);
            }

        }
        catch (EposException e) {
            result.setEposException(e);
        }

        // Discard text buffer 
        textData = null;

        return builder;
    }

    private void printProductsSale(Result result, Builder builder, SaleModel sale) {
        try {
            builder.addText(this.mContext.getString(R.string.divider));
            builder.addFeedUnit(1);
            builder.addTextAlign(Builder.ALIGN_LEFT);
            builder.addTextPosition(120);
            builder.addText("Item");
            //builder.addTextPosition(270);
            //builder.addText("Qtd");
            builder.addTextPosition(340);
            builder.addText("Unit. Total");
            builder.addFeedLine(2);

            List<SaleItemModel> list = sale.getItemList();
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

                if (saleItem.getRequestPass() == 1) {
                    requestPassWait = true;
                }

                builder.addFeedLine(1);
                builder.addTextPosition(100);
                builder.addText(productName);

                builder.addFeedLine(1);
                builder.addTextPosition(100);
                builder.addText(String.valueOf(saleItem.getQuantityItem()) + " X UN");

                builder.addTextPosition(270);
                builder.addText(FormatUtil.toMoneyFormat(saleItem.getPriceResale(), false));

                builder.addTextPosition(400);
                builder.addText(FormatUtil.toMoneyFormat(saleItem.getItemTotal(), false));

                if (!productAnnotation.isEmpty()) {
                    builder.addFeedLine(1);
                    builder.addTextPosition(100);
                    builder.addText("--> " + productAnnotation);
                }
            }
            builder.addFeedUnit(1);
            builder.addTextAlign(Builder.ALIGN_CENTER);
            builder.addText(this.mContext.getString(R.string.divider));
        }
        catch (EposException e) {
            result.setEposException(e);
        }
    }

    private void print(Builder builder, Result result) {
        int printerStatus[] = new int[1];
        int batteryStatus[] = new int[1];
        boolean isBeginTransaction = false;

        // sendData API timeout setting (10000 msec) 
        final int sendTimeout = 10000;
        
        Print printer = PrinterEpson.getPrinter(mContext);
        
        if(printer != null){
            printer.setStatusChangeEventCallback(this);
            printer.setBatteryStatusChangeEventCallback(this);
        }

        // Null check 
        if ((builder == null) || (result == null)) {
            return;
        }

        // init result 
        result.setPrinterStatus(0);
        result.setBatteryStatus(0);
        result.setEposException(null);
        result.setEpsonIoException(null);

        try {
            // Print data if printer is printable 
            printer.getStatus(printerStatus, batteryStatus);
            result.setPrinterStatus(printerStatus[0]);
            result.setBatteryStatus(batteryStatus[0]);

            if (isPrintable(result)) {
                printerStatus[0] = 0;
                batteryStatus[0] = 0;

                printer.beginTransaction();
                isBeginTransaction = true;
                
                int cupons = PreferencesRepository.getCuponsPrint();
                for (int i = 0; i < cupons; i++) {
                	//Log.i("Cupom", "-------------------> Imprimir cupom " + i);
                	synchronized (printer) {
                		printer.sendData(builder, sendTimeout, printerStatus, batteryStatus);
					}
                }
                    
                result.setPrinterStatus(printerStatus[0]);
                result.setBatteryStatus(batteryStatus[0]);
            }
        }
        catch (EposException e) {
            result.setEposException(e);
        }
        finally {
            if (isBeginTransaction) {
                try {
                    printer.endTransaction();
                }
                catch (EposException e) {
                    // Do nothing
                }
            }
        }

        try {
            printer.closePrinter();
        }
        catch (EposException e) {
            // Do nothing 
        }

        return;
    }

    // Display error messages and warning messages 
    private void displayMsg(Result result) {
        if (result == null) {
            return;
        }

        String errorMsg = MsgMaker.makeErrorMessage(mContext, result);
        String warningMsg = MsgMaker.makeWarningMessage(mContext, result);

        if (!errorMsg.isEmpty()) {
            ShowMsg.show(errorMsg, mContext);
        }

        return;
    }

    // Determine whether printer is printable 
    private boolean isPrintable(Result result) {
        if (result == null) {
            return false;
        }

        int status = result.getPrinterStatus();
        if ((status & Print.ST_OFF_LINE) == Print.ST_OFF_LINE) {
            return false;
        }

        if ((status & Print.ST_NO_RESPONSE) == Print.ST_NO_RESPONSE) {
            return false;
        }

        return true;
    }

    // Get Compress parameter of addImage API 
    private int getCompress(int connection) {
        if (connection == Print.DEVTYPE_BLUETOOTH) {
            return Builder.COMPRESS_DEFLATE;
        }
        else {
            return Builder.COMPRESS_NONE;
        }
    }
    
    
    /*********************************
	 * Eventos da impressora Epson
	 *********************************/
	
	@Override
	public void onStatusChangeEvent(final String deviceName, final int status) {
		((Activity) mContext).runOnUiThread(new Runnable() {
			@Override
			public synchronized void run() {
				ShowMsg.showStatusChangeEvent(deviceName, status, mContext);
			}
		});
	}

	@Override
	public void onBatteryStatusChangeEvent(final String deviceName, final int battery) {
		((Activity) mContext).runOnUiThread(new Runnable() {
			@Override
			public synchronized void run() {
				ShowMsg.showBatteryStatusChangeEvent(deviceName, battery, mContext);
			}
		});
	}
}
