package br.com.i9algo.autaz.pdv.helpers;

import android.util.Log;

import br.com.i9algo.autaz.pdv.BuildConfig;


/**
 * This class defines the Logger
 */
public final class Logger {

    private static final String TAG = "STRAUCORP";
    private static final boolean LOG_ENABLE = BuildConfig.DEBUG;
    private static final boolean DETAIL_ENABLE = true;

    private Logger() {
    }

    private static String buildMsg(String msg) {
        return Logger.buildMsg(msg, DETAIL_ENABLE);
    }
    private static String buildMsg(String msg, boolean detail) {
        StringBuilder buffer = new StringBuilder();

        if (detail) {
            final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];

            buffer.append("[ ");
            buffer.append(Thread.currentThread().getName());
            buffer.append(": ");
            buffer.append(stackTraceElement.getFileName());
            buffer.append(": ");
            buffer.append(stackTraceElement.getLineNumber());
            buffer.append(": ");
            buffer.append(stackTraceElement.getMethodName());
        }

        buffer.append("() ] _____ ");
        buffer.append(msg);
        buffer.append("\n");

        return buffer.toString();
    }


    public static void v(String msg) {
        v(TAG, msg);
    }
    public static void v(String tag, String msg) {
        //if (LOG_ENABLE && Log.isLoggable(tag, Log.VERBOSE)) {
        if (LOG_ENABLE) {
            Log.v(tag, buildMsg(msg));
        }
    }


    public static void d(String msg) {
        d(TAG, msg);
    }
    public static void d(String tag, String msg) {
        //if (LOG_ENABLE && Log.isLoggable(tag, Log.DEBUG)) {
        if (LOG_ENABLE) {
            Log.d(tag, buildMsg(msg));
        }
    }


    public static void i(String msg) {
        i(TAG, msg);
    }
    public static void i(String tag, String msg) {
        //if (LOG_ENABLE && Log.isLoggable(tag, Log.INFO)) {
        if (LOG_ENABLE) {
            Log.i(tag, buildMsg(msg));
        }
    }


    public static void w(String msg) {
        w(TAG, msg);
    }
    public static void w(String tag, String msg) {
        //if (LOG_ENABLE && Log.isLoggable(tag, Log.WARN)) {
        if (LOG_ENABLE) {
            Log.w(tag, buildMsg(msg));
        }
    }


    public static void w(String msg, Exception e) {
        w(TAG, msg, e);
    }
    public static void w(String tag, String msg, Exception e) {
        //if (LOG_ENABLE && Log.isLoggable(tag, Log.WARN)) {
        if (LOG_ENABLE) {
            Log.w(tag, buildMsg(msg), e);
        }
    }


    public static void e(String msg) {
        e(TAG, msg);
    }
    public static void e(String tag, String msg) {
        //if (LOG_ENABLE && Log.isLoggable(tag, Log.ERROR)) {
        if (LOG_ENABLE) {
            Log.e(tag, buildMsg(msg));
        }
    }

    public static void e(String msg, boolean detail) {
        e(TAG, msg, detail);
    }
    public static void e(String tag, String msg, boolean detail) {
        //if (LOG_ENABLE && Log.isLoggable(tag, Log.ERROR)) {
        if (LOG_ENABLE) {
            Log.e(tag, buildMsg(msg, detail));
        }
    }

    public static void e(String msg, Throwable tr) {
        e(TAG, msg, tr);
    }
    public static void e(String tag, String msg, Throwable tr) {
        //if (LOG_ENABLE && Log.isLoggable(tag, Log.ERROR)) {
        if (LOG_ENABLE) {
            Log.e(tag, buildMsg(msg), tr);
        }
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }
}