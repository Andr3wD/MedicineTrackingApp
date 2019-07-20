package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    public static IndividualMedicineManager medicineManager;
    //TODO add dark theme
    //TODO add reason to specific page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<IndividualMedicine> temp = null;
        medicineManager = new IndividualMedicineManager();
        try {
            FileInputStream fis = this.openFileInput("testFile");
            ObjectInputStream is = new ObjectInputStream(fis);
            temp = (ArrayList<IndividualMedicine>) is.readObject();//TODO make lastChckForUpdate save and load
            is.close();
            fis.close();
            Log.i("test", "save loaded");

            medicineManager.medicineHistoryList = temp;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "No save found");
        }
        //medicineManager.medicineHistoryList = new ArrayList<>();
        medicineManager.fileContext = this;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicineHistoryPage()).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



}
