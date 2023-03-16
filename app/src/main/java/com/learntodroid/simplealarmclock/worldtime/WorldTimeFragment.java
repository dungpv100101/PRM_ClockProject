package com.learntodroid.simplealarmclock.worldtime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import com.learntodroid.simplealarmclock.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorldTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorldTimeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Calendar current;

    private Spinner spinner;
    private TextView timezone;
    private TextView txtCurrentTime;
    private TextView txtTimeZoneTime;
    private TextClock textClockWorld;

    long miliSeconds;
    ArrayAdapter<String> idAdapter;
    SimpleDateFormat simpleDateFormat;
    Date resultDate;

    public WorldTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorldTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorldTimeFragment newInstance(String param1, String param2) {
        WorldTimeFragment fragment = new WorldTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        timezone = (TextView) view.findViewById(R.id.timezone);
        txtCurrentTime = (TextView) view.findViewById(R.id.txtCurrentTime);
        txtTimeZoneTime = (TextView) view.findViewById(R.id.txtTimeZoneTime);
        textClockWorld = (TextClock) view.findViewById(R.id.textClockWorld);

        String[] idArray = TimeZone.getAvailableIDs();
        simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        idAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, idArray);
        spinner.setAdapter(idAdapter);

        getGMTtime();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                getGMTtime();
                String selectId = (String) (parent.getItemAtPosition(position));

                TimeZone timeZone =  TimeZone.getTimeZone(selectId);

                String timeZoneName = timeZone.getDisplayName();

                textClockWorld.setTimeZone(selectId);

                int timeZoneOffSet = timeZone.getRawOffset() / (60 * 100);

                int hrs = timeZoneOffSet / 600;
                int mins = timeZoneOffSet % 60;

                miliSeconds = miliSeconds + timeZone.getRawOffset();
                resultDate = new Date(miliSeconds);

                System.out.println(simpleDateFormat.format(resultDate));

                timezone.setText(timeZoneName + ": GMT " + hrs + ":" + mins);
                txtTimeZoneTime.setText("" + simpleDateFormat.format(resultDate));
                miliSeconds = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getGMTtime() {
        current = Calendar.getInstance();

        txtCurrentTime.setText("" + current.getTime());

        miliSeconds = current.getTimeInMillis();

        TimeZone txCurrent = current.getTimeZone();
        int offSet = txCurrent.getRawOffset();

        if (txCurrent.inDaylightTime(new Date())) {
            offSet += txCurrent.getDSTSavings();
        }

        miliSeconds -= offSet;

        resultDate = new Date(miliSeconds);
        System.out.println(simpleDateFormat.format(resultDate));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_world_time, container, false);
    }
}