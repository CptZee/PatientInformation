package com.example.patientinformation.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.patientinformation.Data.Helper.HistoryHelper;
import com.example.patientinformation.Data.Helper.PatientHelper;
import com.example.patientinformation.Data.Helper.RecordHelper;
import com.example.patientinformation.Data.History;
import com.example.patientinformation.Data.Patient;
import com.example.patientinformation.Data.Record;
import com.example.patientinformation.MainActivity;
import com.example.patientinformation.Menus.Registration.RegistrationFragment;
import com.example.patientinformation.Menus.ViewFragment;
import com.example.patientinformation.R;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<Record> list;
    private MainActivity activity;

    public RecordAdapter(List<Record> list, MainActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.patient_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        RecordHelper recordHelper = new RecordHelper(activity);
        PatientHelper helper = new PatientHelper(activity);
        Record record = list.get(position);
        Patient patient = helper.get(record.getPatientID());

        String fullName = patient.getFirstName() + " " + patient.getMiddleName() + " " + patient.getLastName();
        viewHolder.getFullName().setText(fullName);
        viewHolder.getId().setText("Patient-"+patient.getID());
        viewHolder.getRemove().setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Are you sure you want to remove record with the ID of " + viewHolder.getId().getText().toString())
                    .setPositiveButton("Yes", (dialog, id) -> {
                        recordHelper.remove(record);
                        Toast.makeText(activity, "Successfully removed the record!", Toast.LENGTH_SHORT);
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        //Nothing cause swag
                    })
                    .create().show();
        });
        viewHolder.getEdit().setOnClickListener(v->{
            RegistrationFragment fragment = new RegistrationFragment();
            Bundle args = new Bundle();
            args.putInt("recordID", record.getID());
            args.putBoolean("newRecord", false);
            fragment.setArguments(args);
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        });
        viewHolder.getView().setOnClickListener(v->{
            ViewFragment fragment = new ViewFragment();
            Bundle args = new Bundle();
            args.putInt("recordID", record.getID());
            fragment.setArguments(args);
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fullName, id;
        private Button viewBtn, edit, remove;
        public ViewHolder(View view) {
            super(view);
            fullName = view.findViewById(R.id.card_full_name);
            id = view.findViewById(R.id.card_patient_id);
            viewBtn = view.findViewById(R.id.card_view);
            edit = view.findViewById(R.id.card_edit);
            remove = view.findViewById(R.id.card_remove);
        }

        public TextView getFullName() {
            return fullName;
        }

        public TextView getId() {
            return id;
        }

        public Button getView() {
            return viewBtn;
        }

        public Button getEdit() {
            return edit;
        }

        public Button getRemove() {
            return remove;
        }
    }

}

