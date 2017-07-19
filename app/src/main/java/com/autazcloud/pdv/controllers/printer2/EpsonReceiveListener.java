package com.autazcloud.pdv.controllers.printer2;

import android.app.Activity;

import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;

/**
 * Created by aStraube on 18/07/2017.
 */

public interface EpsonReceiveListener extends com.epson.epos2.printer.ReceiveListener {
    Activity getActivity();
    void onPtrReceive(Printer var1, int var2, PrinterStatusInfo var3, String var4);
}
