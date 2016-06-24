package com.facebook.kshia.nytsearch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kshia on 6/21/16.
 */
public class DatePickerFragment extends DialogFragment {//implements DatePickerDialog.OnDateSetListener{

    //private int datePickerId;
//    public static final int FLAG_BEGIN_DATE = 0;
//    public static final int FLAG_END_DATE = 1;
    DatePickerDialog myDatePickerDialog;
   // public int flag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();

        // Create a new instance of TimePickerDialog and return it
        myDatePickerDialog = new DatePickerDialog(getActivity(), listener, year, month, day);

        return myDatePickerDialog;
    }

//    public void setFlag(int myFlag) {
//        flag = myFlag;
//    }

//    // handle the date selected
//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        // store the values selected into a Calendar instance
//        final Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, monthOfYear);
//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//        Log.d("CALENDAR", "" + c.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
//        String newDate = df.format(c.getTime());
//
//
//
//    }

}
