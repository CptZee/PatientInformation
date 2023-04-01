package com.example.patientinformation.Menus.Registration;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.patientinformation.Data.Helper.HistoryHelper;
import com.example.patientinformation.Data.Helper.PatientHelper;
import com.example.patientinformation.Data.Helper.RecordHelper;
import com.example.patientinformation.Data.History;
import com.example.patientinformation.Data.Patient;
import com.example.patientinformation.Data.Record;
import com.example.patientinformation.MainActivity;
import com.example.patientinformation.Menus.RecordsFragment;
import com.example.patientinformation.R;

public class HistoryFragment extends Fragment {
    public HistoryFragment() {
        super(R.layout.history_fragment);
    }
    private RadioButton smokerYes, smokerNo, heartYes, heartNo, asthmaYes, asthmaNo;
    private HistoryHelper historyHelper;
    private PatientHelper patientHelper;
    private RecordHelper recordHelper;
    private Bundle args;
    private Patient patient;
    private History history;
    private Record record;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        smokerYes = view.findViewById(R.id.history_smoking_yes);
        smokerNo = view.findViewById(R.id.history_smoking_no);
        heartYes = view.findViewById(R.id.history_heart_yes);
        heartNo = view.findViewById(R.id.history_heart_no);
        asthmaYes = view.findViewById(R.id.history_asthma_yes);
        asthmaNo = view.findViewById(R.id.history_asthma_no);
        Button button = view.findViewById(R.id.history_submit_button);
        historyHelper = new HistoryHelper(getContext());
        patientHelper = new PatientHelper(getContext());
        recordHelper = new RecordHelper(getContext());
        args = getArguments();
        if(!args.getBoolean("newRecord"))
            initModifyData();
        button.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are the details provided are correct?")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        if(args.getBoolean("newRecord"))
                            launchNew();
                        else
                            launchEdit();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        //Nothing cause swag
                    })
                    .create().show();
        });
    }
    private void initModifyData(){
        RecordHelper recordHelper = new RecordHelper(getContext());
        Record record = recordHelper.get(getArguments().getInt("recordID"));
        History history = historyHelper.get(record.getHistoryID());

        if(history.isSmoker())
            smokerYes.setChecked(true);
        else
            smokerNo.setChecked(true);
        if(history.hasHeartCondition())
            heartYes.setChecked(true);
        else
            heartNo.setChecked(true);
        if(history.hasAsthma())
            asthmaYes.setChecked(true);
        else
            asthmaNo.setChecked(true);
    }

    private void launchNew(){
        patient = new Patient();
        history = new History();
        record = new Record();

        patient.setID(patientHelper.getID());
        patient.setFirstName(args.getString("firstName"));
        patient.setMiddleName(args.getString("middleName"));
        patient.setLastName(args.getString("lastName"));
        patient.setAge(args.getInt("age"));
        if(args.getBoolean("male"))
            patient.setSex("Male");
        else
            patient.setSex("Female");

        history.setID(historyHelper.getID());

        if(smokerYes.isChecked())
            history.setSmoker(true);
        else
            history.setSmoker(false);
        if(heartYes.isChecked())
            history.setHeartCondition(true);
        else
            history.setHeartCondition(false);
        if(asthmaYes.isChecked())
            history.setAsthma(true);
        else
            history.setAsthma(false);
        record.setPatientID(patient.getID());
        record.setHistoryID(history.getID());
        recordHelper.insert(record);
        patientHelper.insert(patient);
        historyHelper.insert(history);
        Toast.makeText(getContext(), "Successfully added a new record!", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).getNavBar().setSelectedItemId(R.id.records);
        ((MainActivity) getActivity()).getNavBar().invalidate();
    }
    private void launchEdit(){
        record = recordHelper.get(args.getInt("recordID"));
        patient = patientHelper.get(record.getPatientID());
        history = historyHelper.get(record.getHistoryID());

        patient.setID(record.getPatientID());
        patient.setFirstName(args.getString("firstName"));
        patient.setMiddleName(args.getString("middleName"));
        patient.setLastName(args.getString("lastName"));
        patient.setAge(args.getInt("age"));
        if(args.getBoolean("male"))
            patient.setSex("Male");
        else
            patient.setSex("Female");

        history.setID(record.getID());

        if(smokerYes.isChecked())
            history.setSmoker(true);
        else
            history.setSmoker(false);
        if(heartYes.isChecked())
            history.setHeartCondition(true);
        else
            history.setHeartCondition(false);
        if(asthmaYes.isChecked())
            history.setAsthma(true);
        else
            history.setAsthma(false);

        recordHelper.update(record);
        patientHelper.update(patient);
        historyHelper.update(history);

        Toast.makeText(getContext(), "Successfully edited the record!", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).getNavBar().setSelectedItemId(R.id.records);
        ((MainActivity) getActivity()).getNavBar().invalidate();
    }
}
