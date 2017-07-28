package br.com.i9algo.autaz.pdv.helpers.defaults;

import android.text.Editable;
import android.text.TextWatcher;

public class DefaultTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // no-op by default
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // no-op by default
    }

    @Override
    public void afterTextChanged(Editable s) {
        // no-op by default
    }
}
