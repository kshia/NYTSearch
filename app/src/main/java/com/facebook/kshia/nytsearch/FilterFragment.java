package com.facebook.kshia.nytsearch;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.kshia.nytsearch.activities.SearchActivity;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Button btnBegin;
    OnFragmentInteractionListener mListener;
    FilterSettings filterSettings;
    Spinner sortSpinner;
    Spinner spinner;


    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);


        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SearchActivity activity = (SearchActivity) getActivity();
        filterSettings = activity.getFilterSettings();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        spinner = (Spinner) view.findViewById(R.id.news_desk_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.news_desk_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        int spinnerPosition = 0;
        if(filterSettings.getNews_desk() != null){
            spinnerPosition = adapter.getPosition(filterSettings.getNews_desk());
        }
        Log.d("Filters", "Current news_desk: " + filterSettings.getNews_desk() + " Spinner position: " + spinnerPosition);
        spinner.setSelection(spinnerPosition);

        sortSpinner = (Spinner) view.findViewById(R.id.sort_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.sort_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sortSpinner.setAdapter(sortAdapter);

        int sortSpinnerPosition = 0;
        if(filterSettings.getSort() != null){
            sortSpinnerPosition = sortAdapter.getPosition(filterSettings.getSort());
        }
        Log.d("Filters", "Current sort: " + filterSettings.getSort() + " Spinner position: " + sortSpinnerPosition);
        sortSpinner.setSelection(sortSpinnerPosition);

        btnBegin = (Button) view.findViewById(R.id.btnBegin);
        if (filterSettings.getButton_date() != null) {
            btnBegin.setText(filterSettings.getButton_date());
        }

        getDialog().setTitle("Select Filters");

        btnBegin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerFragment dpFragment = new DatePickerFragment();
                DialogFragment newFragment = (DialogFragment) dpFragment;

                newFragment.setTargetFragment(FilterFragment.this, 300);
                newFragment.show(getFragmentManager(), "beginPicker");

            }
        });

        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "onSaveClicked", Toast.LENGTH_SHORT).show();
                FilterFragmentListener listener = (FilterFragmentListener) getActivity();
                filterSettings.setSort(sortSpinner.getSelectedItem().toString());
                filterSettings.setNews_desk(spinner.getSelectedItem().toString());
                listener.onFilterFinished();
                dismiss();
            }
        });


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("Calendar", "onDateSet in FilterFragment triggered. DatePicker View id: " + view.getId());
//        if () {
//            Toast.makeText(getActivity(), "OMG IT WORKS!", Toast.LENGTH_SHORT).show();
//        }
//        else {
            final Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Toast.makeText(getActivity(),"YAY!", Toast.LENGTH_SHORT).show();

            filterSettings.setYear(c.get(Calendar.YEAR));
            filterSettings.setMonth(c.get(Calendar.MONTH));
            filterSettings.setDay(c.get(Calendar.DAY_OF_MONTH));


            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            String newDate = df.format(c.getTime());
            filterSettings.setButton_date(newDate);
            btnBegin.setText(filterSettings.getButton_date());
            Log.d("Filters", "Button Text: " + newDate);

            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyyMMdd");
            String beginDateAPI = apiFormat.format(c.getTime());
            filterSettings.setBegin_date(beginDateAPI);
            Log.d("Filters", "Setting: " + beginDateAPI);
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    // Defines the listener interface
    public interface FilterFragmentListener {
        void onFilterFinished();
    }


}
