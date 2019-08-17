package com.example.medicinetrackingapp;

import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.FINGERPRINT_SERVICE;

@TargetApi(Build.VERSION_CODES.M)
public class AuthenticatePage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.authenticate_main, container, false);
        getActivity().setTitle("Login");
        FingerprintHandler fH = new FingerprintHandler((MainActivity) getActivity());
        FingerprintManager fM = (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);
        fH.auth(fM);
        return v;
    }
}
