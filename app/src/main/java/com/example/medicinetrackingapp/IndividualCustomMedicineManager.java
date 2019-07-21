package com.example.medicinetrackingapp;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class IndividualCustomMedicineManager {

    ArrayList<IndividualCustomMedicine> rememberedMedicineList;
    Context fileContext;

    public IndividualCustomMedicineManager() {
        rememberedMedicineList = new ArrayList<>();
    }

    public void add(IndividualCustomMedicine entry) {
        rememberedMedicineList.add(entry);
        save();
    }

    public void remove(int index) {

    }

    /**
     * @param barcode
     * @return IndividualCustomMedicine of IndividualCustomMedicine that has that barcode
     */
    public IndividualCustomMedicine exists(String barcode) {
        for (IndividualCustomMedicine i : rememberedMedicineList) {
            for (int v = 0; v < i.barcodes.size(); v++) {
                if (i.barcodes.get(v).equals(barcode)) {
                    return i;
                }
            }
        }
        return null;
    }

    public void save() {
        ObjectOutputStream os = null;
        FileOutputStream fos = null;
        try {
            fos = fileContext.openFileOutput("testFileIndividualCustomMedicine", Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(rememberedMedicineList);
            os.close();
            fos.close();
            Log.i("test", "Saving data");
        } catch (Exception e) {
            Log.i("test", "Unable to save data");
            e.printStackTrace();
        }

    }

}
