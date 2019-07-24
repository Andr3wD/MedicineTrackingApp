package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static IndividualMedicineManager medicineManager;
    public static IndividualCustomMedicineManager customMedicineManager;
    //TODO add dark theme --low priority
    //TODO add calendar with medicines on it for easier viewing --medium priority
    //TODO add pill count left to remembered medicines and count down when used
    //TODO add reminder when low on remembered medicines pills
    //TODO add button to scanner that turns on flashlight
    //TODO make it so you can add a new remembered medicine if barcode scan doesn't identify current remembered medicine
    //TODO deal with if multiple meds have the same barcode
    //TODO add date separators for medicines taken
    //TODO make settings page
    //TODO encrypt everything and don't send it anywhere over the internet (no google sheets integration or anything)
    //TODO restrict or prevent the amount of data that is decrypted at a time.
    //TODO add password or fingerprint login, etc...
    //TODO add code tamper checking and preventing
    //TODO figure out why MedicineHistoryDetailPage doesn't update immediately after editing medicine and fix --low priority
    //TODO add session timeout
    //TODO follow https://www.nowsecure.com/blog/2017/03/23/5-vital-tips-developing-hipaa-compliant-mobile-apps-checklist/
    //TODO add way to export and load data
    //TODO sort IndividualCustomMedicine on date added
    //TODO deal with attempting to add med using custom med if medsleft = 0

    //TODO add pages so can split up decryption of data to each page
    //TODO switch add custom medicine tab to FAB on custom medicine list page
    //TODO add obfuscation name to custom meds and individual meds, then let user obfuscate data for private use in public
    //TODO add delete button for custom meds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ((NavigationView) findViewById(R.id.navigation_pullout)).setNavigationItemSelectedListener(this);


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
        } else if (item.getTitle().toString().equals("Remember Medicine List")) {
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_remembermedicinelist: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RememberMedicinePage()).addToBackStack(null).commit();
            }
            break;
            case R.id.action_addremembermedicine: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RememberMedicineInputPage()).addToBackStack(null).commit();
            }
            break;
            case R.id.action_settings: {

            }
            break;
        }
        Log.i("clicked", menuItem.getTitle().toString());
        return true;
    }
}
