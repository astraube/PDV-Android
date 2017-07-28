package br.com.i9algo.autaz.pdv.controllers.printer2;

import android.content.Context;

import br.com.i9algo.autaz.pdv.R;
import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Epos2Exception;
import com.github.pierry.simpletoast.SimpleToast;

public class ShowMsg {
    public static void showException(Exception e, String method, Context context) {
        String msg = getExceptionString(e, method, context);
        showError(msg, context);
    }
    public static String getExceptionString(Exception e, String method, Context context) {
        String msg = context.getString(R.string.errmsg_text_informe) + "\n\n";

        if (e instanceof Epos2Exception) {
            msg += String.format(
                    "%s\n\t%s\n%s\n\t%s",
                    context.getString(R.string.title_err_code),
                    getEposExceptionText(((Epos2Exception) e).getErrorStatus()),
                    context.getString(R.string.title_err_method),
                    method);
        }
        else {
            msg += e.toString();
        }
        return msg;
    }

    public static void showResult(int code, String errMsg, Context context) {
        String msg = getResultString(code, errMsg, context);
        showInfo(msg, context);
    }

    public static String getResultString(int code, String errMsg, Context context) {
        String msg = context.getString(R.string.errmsg_text_informe) + "\n\n";
        if (errMsg.isEmpty()) {
            msg += String.format(
                    "\t%s\n\t%s\n",
                    context.getString(R.string.title_msg_result),
                    getCodeText(code));
        }
        else {
            msg += String.format(
                    "\t%s\n\t%s\n\n\t%s\n\t%s\n",
                    context.getString(R.string.title_msg_result),
                    getCodeText(code),
                    context.getString(R.string.title_msg_description),
                    errMsg);
        }
        return msg;
    }

    public static void showMsg(String msg, Context context) {
        showInfo(msg, context);
    }

    private static void showError(String msg, Context context) {
        SimpleToast.error(context, msg);

        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return ;
            }
        });
        alertDialog.create();
        alertDialog.show();*/
    }

    private static void showInfo(String msg, Context context) {
        SimpleToast.muted(context, msg);
    }

    private static String getEposExceptionText(int state) {
        String return_text = "";
        switch (state) {
            case    Epos2Exception.ERR_PARAM:
                return_text = "ERR_PARAM";
                break;
            case    Epos2Exception.ERR_CONNECT:
                return_text = "ERR_CONNECT";
                break;
            case    Epos2Exception.ERR_TIMEOUT:
                return_text = "ERR_TIMEOUT";
                break;
            case    Epos2Exception.ERR_MEMORY:
                return_text = "ERR_MEMORY";
                break;
            case    Epos2Exception.ERR_ILLEGAL:
                return_text = "ERR_ILLEGAL";
                break;
            case    Epos2Exception.ERR_PROCESSING:
                return_text = "ERR_PROCESSING";
                break;
            case    Epos2Exception.ERR_NOT_FOUND:
                return_text = "ERR_NOT_FOUND";
                break;
            case    Epos2Exception.ERR_IN_USE:
                return_text = "ERR_IN_USE";
                break;
            case    Epos2Exception.ERR_TYPE_INVALID:
                return_text = "ERR_TYPE_INVALID";
                break;
            case    Epos2Exception.ERR_DISCONNECT:
                return_text = "ERR_DISCONNECT";
                break;
            case    Epos2Exception.ERR_ALREADY_OPENED:
                return_text = "ERR_ALREADY_OPENED";
                break;
            case    Epos2Exception.ERR_ALREADY_USED:
                return_text = "ERR_ALREADY_USED";
                break;
            case    Epos2Exception.ERR_BOX_COUNT_OVER:
                return_text = "ERR_BOX_COUNT_OVER";
                break;
            case    Epos2Exception.ERR_BOX_CLIENT_OVER:
                return_text = "ERR_BOX_CLIENT_OVER";
                break;
            case    Epos2Exception.ERR_UNSUPPORTED:
                return_text = "ERR_UNSUPPORTED";
                break;
            case    Epos2Exception.ERR_FAILURE:
                return_text = "ERR_FAILURE";
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }

    public static String getCodeText(int state) {
        String return_text = "";
        switch (state) {
            case Epos2CallbackCode.CODE_SUCCESS:
                return_text = "PRINT_SUCCESS";
                break;
            case Epos2CallbackCode.CODE_PRINTING:
                return_text = "PRINTING";
                break;
            case Epos2CallbackCode.CODE_ERR_AUTORECOVER:
                return_text = "ERR_AUTORECOVER";
                break;
            case Epos2CallbackCode.CODE_ERR_COVER_OPEN:
                return_text = "ERR_COVER_OPEN";
                break;
            case Epos2CallbackCode.CODE_ERR_CUTTER:
                return_text = "ERR_CUTTER";
                break;
            case Epos2CallbackCode.CODE_ERR_MECHANICAL:
                return_text = "ERR_MECHANICAL";
                break;
            case Epos2CallbackCode.CODE_ERR_EMPTY:
                return_text = "ERR_EMPTY";
                break;
            case Epos2CallbackCode.CODE_ERR_UNRECOVERABLE:
                return_text = "ERR_UNRECOVERABLE";
                break;
            case Epos2CallbackCode.CODE_ERR_FAILURE:
                return_text = "ERR_FAILURE";
                break;
            case Epos2CallbackCode.CODE_ERR_NOT_FOUND:
                return_text = "ERR_NOT_FOUND";
                break;
            case Epos2CallbackCode.CODE_ERR_SYSTEM:
                return_text = "ERR_SYSTEM";
                break;
            case Epos2CallbackCode.CODE_ERR_PORT:
                return_text = "ERR_PORT";
                break;
            case Epos2CallbackCode.CODE_ERR_TIMEOUT:
                return_text = "ERR_TIMEOUT";
                break;
            case Epos2CallbackCode.CODE_ERR_JOB_NOT_FOUND:
                return_text = "ERR_JOB_NOT_FOUND";
                break;
            case Epos2CallbackCode.CODE_ERR_SPOOLER:
                return_text = "ERR_SPOOLER";
                break;
            case Epos2CallbackCode.CODE_ERR_BATTERY_LOW:
                return_text = "ERR_BATTERY_LOW";
                break;
            case Epos2CallbackCode.CODE_ERR_TOO_MANY_REQUESTS:
                return_text = "ERR_TOO_MANY_REQUESTS";
                break;
            case Epos2CallbackCode.CODE_ERR_REQUEST_ENTITY_TOO_LARGE:
                return_text = "ERR_REQUEST_ENTITY_TOO_LARGE";
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }

    public static void showBatteryStatusChangeEvent(String deviceName, int battery, Context context){
        String msg;
        msg = String.format(
                "%s\n\t%s\n%s\n\t0x%04X",
                context.getString(R.string.statusmsg_ipaddress),
                deviceName,
                context.getString(R.string.statusmsg_batterystatus),
                battery);
        showError(msg, context);
    }
}
