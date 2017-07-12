package com.autazcloud.pdv.helpers.defaults;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class MainThreadBus extends Bus {
    private static Bus _bus;
    private Handler _handler = new Handler(Looper.getMainLooper());

    public MainThreadBus() {
        if (_bus == null) {
            _bus = new Bus();
        }
    }

    @Override
    public void register(Object obj) {
        _bus.register(obj);
    }

    @Override
    public void unregister(Object obj) {
        _bus.unregister(obj);
    }

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            _bus.post(event);
        } else {
            _handler.post(new Runnable() {
                @Override
                public void run() {
                    _bus.post(event);
                }
            });
        }
    }
}