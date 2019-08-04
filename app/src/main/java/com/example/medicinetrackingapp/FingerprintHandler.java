package com.example.medicinetrackingapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context c;

    public FingerprintHandler() {

    }

    public void auth(FingerprintManager fM) {

        CancellationSignal cS = new CancellationSignal();
        fM.authenticate(null, cS, 0, this, null);
    }

}
