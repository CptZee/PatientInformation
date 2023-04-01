package com.example.patientinformation.Menus.Registration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.patientinformation.Data.Helper.PatientHelper;
import com.example.patientinformation.Data.Helper.RecordHelper;
import com.example.patientinformation.Data.History;
import com.example.patientinformation.Data.Patient;
import com.example.patientinformation.Data.Record;
import com.example.patientinformation.MainActivity;
import com.example.patientinformation.R;

public class RegistrationFragment extends Fragment {
    public RegistrationFragment() {
        super(R.layout.add_fragment);
    }

    private EditText firstName, middleName, lastName, age;
    private TextView textView3;
    private RadioButton male, female;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle args = getArguments();
        Button button = view.findViewById(R.id.register_next_button);
        firstName = view.findViewById(R.id.register_fist_name);
        middleName = view.findViewById(R.id.register_middle_name);
        lastName = view.findViewById(R.id.register_last_name);
        age = view.findViewById(R.id.register_age);
        male = view.findViewById(R.id.register_male);
        female = view.findViewById(R.id.register_female);
        textView3 = view.findViewById(R.id.textView3);

        if(!args.getBoolean("newRecord"))
            initModifyData();

        button.setOnClickListener(v->{
            args.putString("firstName", firstName.getText().toString());
            args.putString("middleName", middleName.getText().toString());
            args.putString("lastName", lastName.getText().toString());
            args.putInt("age", Integer.valueOf(age.getText().toString()));
            args.putBoolean("male", male.isChecked());
            args.putBoolean("female", female.isChecked());
            HistoryFragment fragment = new HistoryFragment();
            fragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void initModifyData(){
        RecordHelper recordHelper = new RecordHelper(getContext());
        PatientHelper patientHelper = new PatientHelper(getContext());
        Record record = recordHelper.get(getArguments().getInt("recordID"));
        Patient patient = patientHelper.get(record.getPatientID());
        textView3.setText("UPDATE PATIENT");
        firstName.setText(patient.getFirstName());
        middleName.setText(patient.getMiddleName());
        lastName.setText(patient.getLastName());
        age.setText("" + patient.getAge());
        if(patient.getSex().equals("Male"))
            male.setChecked(true);
        else
            female.setChecked(true);

    }
}
