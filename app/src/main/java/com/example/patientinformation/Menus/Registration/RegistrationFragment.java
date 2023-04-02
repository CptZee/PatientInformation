package com.example.patientinformation.Menus.Registration;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button button;
    private TextView textView3, loading;
    private RadioButton male, female;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle args = getArguments();
        button = view.findViewById(R.id.register_next_button);
        firstName = view.findViewById(R.id.register_fist_name);
        middleName = view.findViewById(R.id.register_middle_name);
        lastName = view.findViewById(R.id.register_last_name);
        age = view.findViewById(R.id.register_age);
        male = view.findViewById(R.id.register_male);
        female = view.findViewById(R.id.register_female);
        textView3 = view.findViewById(R.id.textView3);
        loading = view.findViewById(R.id.register_loading);

        button.setVisibility(View.INVISIBLE);
        firstName.setVisibility(View.INVISIBLE);
        middleName.setVisibility(View.INVISIBLE);
        lastName.setVisibility(View.INVISIBLE);
        age.setVisibility(View.INVISIBLE);
        male.setVisibility(View.INVISIBLE);
        female.setVisibility(View.INVISIBLE);
        textView3.setVisibility(View.INVISIBLE);

        if(!args.getBoolean("newRecord")){
            textView3.setText("UPDATE PATIENT");
            new initModifyData().execute(getArguments().getInt("recordID"));
        }else
            showFields();
        button.setOnClickListener(v->{
            if(areFieldsEmpty())
                return;

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

    private void showFields(){
        button.setVisibility(View.VISIBLE);
        firstName.setVisibility(View.VISIBLE);
        middleName.setVisibility(View.VISIBLE);
        lastName.setVisibility(View.VISIBLE);
        age.setVisibility(View.VISIBLE);
        male.setVisibility(View.VISIBLE);
        female.setVisibility(View.VISIBLE);
        textView3.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);
    }

    private boolean areFieldsEmpty(){
        boolean isEmpty = false;
        if(firstName.getText().toString().isEmpty()){
            firstName.setError("Field is required!");
            isEmpty = true;
        }
        if(lastName.getText().toString().isEmpty()){
            lastName.setError("Field is required!");
            isEmpty = true;
        }
        if(age.getText().toString().isEmpty()){
            age.setError("Field is required!");
            isEmpty = true;
        }
        if(!male.isChecked() && !female.isChecked()){
            Toast.makeText(getContext(), "Please select your sex!", Toast.LENGTH_SHORT).show();
            isEmpty = true;
        }
        return isEmpty;
    }


    private class initModifyData extends AsyncTask<Integer, Void, Patient>{
        private Record record;
        @Override
        protected Patient doInBackground(Integer... recordIds) {
            RecordHelper recordHelper = new RecordHelper(getContext());
            PatientHelper patientHelper = new PatientHelper(getContext());
            record = recordHelper.get(recordIds[0]);
            return patientHelper.get(record.getPatientID());
        }
        @Override
        protected void onPostExecute(Patient patient) {
            super.onPostExecute(patient);
            firstName.setText(patient.getFirstName());
            middleName.setText(patient.getMiddleName());
            lastName.setText(patient.getLastName());
            age.setText("" + patient.getAge());
            if(patient.getSex().equals("Male"))
                male.setChecked(true);
            else
                female.setChecked(true);
            showFields();
        }
    }
}
