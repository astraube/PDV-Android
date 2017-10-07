package br.com.i9algo.autaz.pdv.controllers;

import android.app.Activity;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.prefs.PreferencesManager;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;

/**
 * Created by aStraube on 11/07/2017.
 */

interface MaterialIntroControllerListener {
    /** Called when there are no more tap targets to display */
    void onSequenceFinish();
}

public class MaterialIntroController implements MaterialIntroListener, MaterialIntroControllerListener {

    private Activity _activity;
    private PreferencesManager _preferencesManager;
    private String _prefix;
    private final Queue<MaterialIntroView.Builder> _targets;
    private boolean _started;


    public MaterialIntroController(Activity activity) {
        this._activity = activity;
        this._preferencesManager = new PreferencesManager(activity);
        this._prefix = PreferencesRepository.getUsername();
        this._targets = new LinkedList<>();
    }

    public void createIntro(View view, String text, String uniqueId) {
        String id = this._prefix + "_" + uniqueId;

        if(!this._preferencesManager.isDisplayed(id)) {

            MaterialIntroView.Builder intro = new MaterialIntroView.Builder(_activity)
                    .enableDotAnimation(true)
                    .setFocusGravity(FocusGravity.CENTER)
                    .setFocusType(Focus.MINIMUM)
                    .setDelayMillis(500)
                    .enableFadeAnimation(true)
                    .performClick(true)
                    //.setColorTextViewInfo(R.color.gray)
                    .setInfoText(text)
                    .setTarget(view)
                    .setListener(this)
                    .setUsageId(id);

            this.addTarget(intro);
        }
    }

    public MaterialIntroController targets(List<MaterialIntroView.Builder> targets) {
        this._targets.addAll(targets);
        return this;
    }

    public MaterialIntroController addTarget(MaterialIntroView.Builder target) {
        this._targets.add(target);
        return this;
    }

    public void start() {
        if (_targets.isEmpty() || _started) {
            return;
        }
        _started = true;
        showNext();
    }

    public void stop() {
        _started = false;
    }

    private void showNext() {
        try {
            if (_started) {
                MaterialIntroView.Builder t = _targets.remove();
                t.show();
            }
        } catch (NoSuchElementException e) {
            onSequenceFinish();
        }
    }

    @Override
    public void onUserClicked(String introViewId) {
        this.showNext();
    }

    @Override
    public void onSequenceFinish() {

    }
}