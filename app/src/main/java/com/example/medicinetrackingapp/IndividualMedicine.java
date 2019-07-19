package com.example.medicinetrackingapp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class IndividualMedicine implements Serializable, Comparable<IndividualMedicine> {
    public String name;
    public Calendar takenDateTime;
    public final Calendar inputTimeDate;
    public Long quantity;
    public String reason;
    public int dose;

    IndividualMedicine() {
        inputTimeDate = Calendar.getInstance();
        takenDateTime = Calendar.getInstance();
    }

    @Override
    public int compareTo(IndividualMedicine o) {
        if (takenDateTime.get(Calendar.YEAR) > o.takenDateTime.get(Calendar.YEAR) || takenDateTime.get(Calendar.MONTH) > o.takenDateTime.get(Calendar.MONTH) || takenDateTime.get(Calendar.DAY_OF_MONTH) > o.takenDateTime.get(Calendar.DAY_OF_MONTH)) {
            return 1;
        } else if (takenDateTime.get(Calendar.YEAR) == o.takenDateTime.get(Calendar.YEAR) && takenDateTime.get(Calendar.MONTH) == o.takenDateTime.get(Calendar.MONTH) && takenDateTime.get(Calendar.DAY_OF_MONTH) == o.takenDateTime.get(Calendar.DAY_OF_MONTH)) {
            return 0;
        } else {
            return -1;
        }
    }

}
