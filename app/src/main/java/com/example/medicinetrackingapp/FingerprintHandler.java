package com.example.medicinetrackingapp;

import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.EditText;

import androidx.room.Room;

import com.commonsware.cwac.saferoom.SafeHelperFactory;

import datastuff.MedicineDatabase;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private MainActivity context;

    public FingerprintHandler(MainActivity context) {
        this.context = context;
    }

    public void auth(FingerprintManager fM) {
        CancellationSignal cS = new CancellationSignal();
        fM.authenticate(null, cS, 0, this, null);
        Log.i("test", "auth created");
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Log.i("test", "auth err");
        super.onAuthenticationError(errorCode, errString);
    }

    @Override
    public void onAuthenticationFailed() {
        Log.i("test", "auth fail");
        super.onAuthenticationFailed();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Log.i("test", "auth help");
        super.onAuthenticationHelp(helpCode, helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        EditText text = context.findViewById(R.id.authenticate_password);
        SafeHelperFactory f = SafeHelperFactory.fromUser(text.getText());
        MainActivity.medicineDatabase = Room.databaseBuilder(context, MedicineDatabase.class, "medicine_database").openHelperFactory(f).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicineHistoryPage()).commit();
        Log.i("test", "auth success");
        super.onAuthenticationSucceeded(result);
    }
}
