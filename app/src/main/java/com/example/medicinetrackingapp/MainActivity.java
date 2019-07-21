package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static IndividualMedicineManager medicineManager;
    public static IndividualCustomMedicineManager customMedicineManager;
    //TODO add dark theme --low priority
    //TODO add reason to specific page --medium priority
    //TODO add calendar with medicines on it for easier viewing --medium priority
    //TODO add remembered medicines --medium priority
    //TODO add page for barcode reading --really want to do priority
    //TODO add pill count left to remembered medicines and count down when used
    //TODO add page for all remembered medicines
    //TODO add reminder when low on remembered medicines pills
    //TODO add button to scanner that turns on flashlight
    //TODO make it so you can add a new remembered medicine if barcode scan doesn't identify current remembered medicine
    //TODO deal with if multiple meds have the same barcode
    //TODO add date separators for medicines taken


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        medicineManager = new IndividualMedicineManager();
        customMedicineManager = new IndividualCustomMedicineManager();
        loadSavedData();

        //medicineManager.medicineHistoryList = new ArrayList<>();
        medicineManager.fileContext = this;
        customMedicineManager.fileContext = this;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals("Add Remember Medicine")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RememberMedicineInputPage()).addToBackStack(null).commit();
        } else if(item.getTitle().toString().equals("Remember Medicine List")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RememberMedicinePage()).addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadSavedData() {
        try {
            FileInputStream fis = this.openFileInput("testFileIndividualMedicine");
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<IndividualMedicine> temp = (ArrayList<IndividualMedicine>) is.readObject();//TODO make lastChckForUpdate save and load
            is.close();
            fis.close();
            Log.i("test", "save loaded for IndividualMedicine");

            medicineManager.medicineHistoryList = temp;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "No save found for IndividualMedicine");
        }

        try {
            FileInputStream fis = this.openFileInput("testFileIndividualCustomMedicine");
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<IndividualCustomMedicine> temp = (ArrayList<IndividualCustomMedicine>) is.readObject();//TODO make lastChckForUpdate save and load
            is.close();
            fis.close();
            Log.i("test", "save loaded for IndividualCustomMedicine");

            customMedicineManager.rememberedMedicineList = temp;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "No save found for IndividualCustomMedicine");
        }

    }


}
