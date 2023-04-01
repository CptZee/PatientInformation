package com.example.patientinformation.Menus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientinformation.Adapter.RecordAdapter;
import com.example.patientinformation.Data.Helper.PatientHelper;
import com.example.patientinformation.Data.Helper.RecordHelper;
import com.example.patientinformation.Data.Patient;
import com.example.patientinformation.Data.Record;
import com.example.patientinformation.MainActivity;
import com.example.patientinformation.R;

import java.util.List;

public class RecordsFragment extends Fragment {
    public RecordsFragment() {
        super(R.layout.records_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        EditText records_search = view.findViewById(R.id.records_search);
        Button records_search_button = view.findViewById(R.id.records_search_button);
        RecyclerView records_list = view.findViewById(R.id.records_list);
        TextView records_no_list = view.findViewById(R.id.records_no_list);
        RecordHelper helper = new RecordHelper(getContext());
        List<Record> list = helper.get();
        List<Record> proxyList = helper.get();
        Log.i("DATABASEHELPER", "Data to show is: " + list.size());
        if(list.size() < 1){
            records_no_list.setVisibility(View.VISIBLE);
            records_list.setVisibility(View.INVISIBLE);
        }else{
            records_no_list.setVisibility(View.INVISIBLE);
            records_list.setVisibility(View.VISIBLE);
        }
        RecordAdapter adapter = new RecordAdapter(list, (MainActivity) getActivity());
        records_list.setLayoutManager(new LinearLayoutManager(getContext()));
        records_list.setAdapter(adapter);

        records_search_button.setOnClickListener(v->{
            String name = records_search.getText().toString();
            if(name.isEmpty()){
                RecordAdapter adapter2 = new RecordAdapter(proxyList, (MainActivity) getActivity());
                records_list.setAdapter(adapter2);
                return;
            }
            list.clear();
            for(Record r : proxyList){
                PatientHelper patientHelper = new PatientHelper(getContext());
                Patient patient = patientHelper.get(r.getPatientID());
                String fullName = patient.getFirstName() + " " + patient.getMiddleName() + " " + patient.getLastName();
                if(fullName.toLowerCase().contains(name.toLowerCase()))
                    list.add(r);
            }
            RecordAdapter adapter3 = new RecordAdapter(list, (MainActivity) getActivity());
            records_list.setAdapter(adapter3);
        });
    }
}