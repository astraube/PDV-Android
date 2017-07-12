package com.autazcloud.pdv.ui.base.listeners;

/**
 * Created by aStraube on 11/07/2017.
 */

public interface ProgressView {
    void showProgress(String message);
    void showProgress(String title, String message);

    void hideProgress();
}
