package com.example.patientinformation.Menus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;

import com.example.patientinformation.Data.Helper.HistoryHelper;
import com.example.patientinformation.Data.Helper.PatientHelper;
import com.example.patientinformation.Data.Helper.RecordHelper;
import com.example.patientinformation.Data.History;
import com.example.patientinformation.Data.Patient;
import com.example.patientinformation.Data.Record;
import com.example.patientinformation.R;

public class ViewFragment extends Fragment {
    public ViewFragment() {
        super(R.layout.view_record_fragment);
    }

    private TextView view_id, view_fullname, view_age, view_gender, view_smoking, view_heart, view_asthma;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button button = view.findViewById(R.id.view_closed);
        view_id = view.findViewById(R.id.view_id);
        view_fullname = view.findViewById(R.id.view_fullname);
        view_age = view.findViewById(R.id.view_age);
        view_gender = view.findViewById(R.id.view_gender);
        view_smoking = view.findViewById(R.id.view_smoking);
        view_heart = view.findViewById(R.id.view_heart);
        view_asthma = view.findViewById(R.id.view_asthma);

        new Init().execute();

        button.setOnClickListener(v->{
            Toast.makeText(getContext(), "Closed patient information", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new RecordsFragment())
                    .commitNow();
        });
    }

    public class Init extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {RecordHelper recordHelper = new RecordHelper(getContext());
            PatientHelper patientHelper = new PatientHelper(getContext());
            HistoryHelper historyHelper = new HistoryHelper(getContext());

            Record record = recordHelper.get(getArguments().getInt("recordID"));
            Patient patient = patientHelper.get(record.getPatientID());
            History history = historyHelper.get(record.getHistoryID());

            view_id.setText("Patient-" + record.getID());
            view_fullname.setText(patient.getFirstName() + " " + patient.getMiddleName() + " " + patient.getLastName());
            view_age.setText("" + patient.getAge());
            view_gender.setText(patient.getSex());

            if(history.isSmoker())
                view_smoking.setText("Yes");
            else
                view_smoking.setText("No");

            if(history.hasHeartCondition())
                view_heart.setText("Yes");
            else
                view_heart.setText("No");

            if(history.hasAsthma())
                view_asthma.setText("Yes");
            else
                view_asthma.setText("No");
            return null;
        }

    }
}
