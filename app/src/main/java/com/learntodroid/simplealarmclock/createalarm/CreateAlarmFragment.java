package com.learntodroid.simplealarmclock.createalarm;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.alarmslist.AlarmsListViewModel;
import com.learntodroid.simplealarmclock.data.Alarm;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAlarmFragment extends Fragment {
    @BindView(R.id.fragment_createalarm_timePicker) TimePicker timePicker;
    @BindView(R.id.fragment_createalarm_title) EditText title;
    @BindView(R.id.fragment_createalarm_scheduleAlarm)
    FloatingActionButton scheduleAlarm;
    @BindView(R.id.fragment_createalarm_recurring) CheckBox recurring;
    @BindView(R.id.fragment_createalarm_isSendMail) CheckBox isSendMail;
    @BindView(R.id.fragment_createalarm_checkMon) CheckBox mon;
    @BindView(R.id.fragment_createalarm_checkTue) CheckBox tue;
    @BindView(R.id.fragment_createalarm_checkWed) CheckBox wed;
    @BindView(R.id.fragment_createalarm_checkThu) CheckBox thu;
    @BindView(R.id.fragment_createalarm_checkFri) CheckBox fri;
    @BindView(R.id.fragment_createalarm_checkSat) CheckBox sat;
    @BindView(R.id.fragment_createalarm_checkSun) CheckBox sun;
    @BindView(R.id.fragment_createalarm_mailTo) EditText mailTo;
    @BindView(R.id.fragment_createalarm_mailTitle) EditText mailTitle;
    @BindView(R.id.fragment_createalarm_mailContent) EditText mailContent;
    @BindView(R.id.fragment_createalarm_recurring_options) LinearLayout recurringOptions;
    @BindView(R.id.fragment_createalarm_mail) LinearLayout mailOptions;
    @BindView(R.id.delete_Alarm) FloatingActionButton deleteAlarm;

    private CreateAlarmViewModel createAlarmViewModel;
    private AlarmsListViewModel alarmsListViewModel;

    public static Alarm alarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);
        alarmsListViewModel  = ViewModelProviders.of(this).get(AlarmsListViewModel.class);
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createalarm, container, false);

        ButterKnife.bind(this, view);

        deleteAlarm.setVisibility( alarm!=null ? View.VISIBLE : View.INVISIBLE);

        if(alarm!=null){
            viewAlarm();
        }else {
            scheduleAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scheduleAlarm();
                    Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
                }
            });
        }

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOptions.setVisibility(View.VISIBLE);
                } else {
                    recurringOptions.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = getAlarmFromFragment();

        alarm.setAlarmId(alarmId);

        createAlarmViewModel.insert(alarm);

        alarm.schedule(getContext());
    }

    private void viewAlarm(){
        title.setText((alarm.getTitle()!=null && !alarm.getTitle().isEmpty()) ? alarm.getTitle() : "OK");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(alarm.getHour());
            timePicker.setMinute(alarm.getMinute());
        }

        if(alarm.isStarted()){
            recurringOptions.setVisibility(View.VISIBLE);
            recurring.setChecked(alarm.isStarted());
            mon.setChecked(alarm.isMonday());
            fri.setChecked(alarm.isFriday());
            tue.setChecked(alarm.isTuesday());
            wed.setChecked(alarm.isWednesday());
            thu.setChecked(alarm.isThursday());
            fri.setChecked(alarm.isFriday());
            sat.setChecked(alarm.isSaturday());
            sun.setChecked(alarm.isSunday());
        }

        isSendMail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mailOptions.setVisibility(View.VISIBLE);
                } else {
                    mailOptions.setVisibility(View.GONE);
                }
            }
        });

        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm alarmNew = getAlarmFromFragment();
                alarmNew.setAlarmId(alarm.getAlarmId());
                updateAlarm(alarmNew);
                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlarm(alarm);
                Navigation.findNavController(view).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

    }

    private void updateAlarm(Alarm alarm){
        alarmsListViewModel.update(alarm);
    }

    private void deleteAlarm(Alarm alarm){
        createAlarmViewModel.delete(alarm);
    }

    private Alarm getAlarmFromFragment(){
        return
                new Alarm(
                0,
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                title.getText().toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked(),
                mailTo.getText().toString(),
                mailTitle.getText().toString(),
                mailContent.getText().toString()
        );
    }

}
