package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import datastuff.MedicineDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static MedicineDatabase medicineDatabase;
    //TODO add dark theme --low priority
    //TODO add calendar with medicines on it for easier viewing --medium priority
    //TODO add reminder when low on remembered medicines pills
    //TODO add button to scanner that turns on flashlight
    //TODO make it so you can add a new remembered medicine if barcode scan doesn't identify current remembered medicine
    //TODO deal with if multiple meds have the same barcode
    //TODO add date separators for medicines taken
    //TODO make settings page
    //TODO encrypt everything and don't send it anywhere over the internet (no google sheets integration or anything)
    //TODO add code tamper checking and preventing --no good way to do this
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

    //I will come back to encryption later.
    //TODO add saving and sorting of data in database
    //TODO deal with what primary id should be. need to be able to access entries regardless of primary id in a sorted order

    //TODO deal with switching custom medicine preset when editing and changing data inside both edit and input page for quantity

    //TODO add infinite scroll to customMedicinePage (maybe, might not be enough entries to add infinite scrolling)
    //TODO make buttons not add to back stack over and over infinitely

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //set notification items to be black instead of white
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ((NavigationView) findViewById(R.id.navigation_pullout)).setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AuthenticatePage()).commit();

        /*IndividualMedicineEntity i = new IndividualMedicineEntity();
        i.name = "TestName2";
        i.dose = 1;
        i.quantity = 2;
        //i.position = 2;

        db.individualMedicineDao().insertAll(i);*/
        //Log.i("test", db.individualMedicineDao().findByPosition(1).name + " " + db.individualMedicineDao().findByPosition(2).name); //it's saved automatically
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomMedicineInputPage()).addToBackStack(null).commit();
        } else if (item.getTitle().toString().equals("Remember Medicine List")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomMedicinePage()).addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_remembermedicinelist: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomMedicinePage()).addToBackStack(null).commit();
            }
            break;
            case R.id.action_addremembermedicine: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomMedicineInputPage()).addToBackStack(null).commit();
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
