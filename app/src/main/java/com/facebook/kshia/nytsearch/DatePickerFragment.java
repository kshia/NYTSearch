package com.facebook.kshia.nytsearch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import com.facebook.kshia.nytsearch.activities.SearchActivity;
import java.util.Calendar;

/**
 * Created by kshia on 6/21/16.
 */
public class DatePickerFragment extends DialogFragment {

    DatePickerDialog myDatePickerDialog;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SearchActivity activity = (SearchActivity) getActivity();
        if (activity.getFilterSettings().getYear() != 0) {
            year = activity.getFilterSettings().getYear();
            month = activity.getFilterSettings().getMonth();
            day = activity.getFilterSettings().getDay();
        }

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();

        // Create a new instance of TimePickerDialog and return it
        myDatePickerDialog = new DatePickerDialog(getActivity(), listener, year, month, day);

        return myDatePickerDialog;
    }


}
