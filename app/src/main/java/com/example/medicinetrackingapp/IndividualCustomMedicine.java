package com.example.medicinetrackingapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class IndividualCustomMedicine implements Serializable {
    public String name;
    public int quantity;
    public int dose;
    public String use;
    public String reason;
    public ArrayList<String> barcodes; //had to switch from SparseList to ArrayList because it isn't serializable
    private Calendar createdTimeDate;

    public IndividualCustomMedicine(){
        createdTimeDate = Calendar.getInstance();
        barcodes = new ArrayList<>();
    }

}
