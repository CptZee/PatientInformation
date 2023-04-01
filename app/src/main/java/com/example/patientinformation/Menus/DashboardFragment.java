package com.example.patientinformation.Menus;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.patientinformation.MainActivity;
import com.example.patientinformation.Menus.Registration.RegistrationFragment;
import com.example.patientinformation.R;

public class DashboardFragment extends Fragment {
    public DashboardFragment() {
        super(R.layout.dashboard_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity activity = ((MainActivity) getActivity());
        activity.toggleNavBar(true);
        activity.getNavBar().setSelectedItemId(R.id.home);


        CardView add = view.findViewById(R.id.dashboard_add);
        CardView records = view.findViewById(R.id.dashboard_records);

        add.setOnClickListener(v->{
            activity.getNavBar().setSelectedItemId(R.id.add);
            RegistrationFragment fragment = new RegistrationFragment();
            Bundle args = new Bundle();
            args.putBoolean("newRecord", true);
            fragment.setArguments(args);
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commitNow();
        });

        records.setOnClickListener(v->{
            activity.getNavBar().setSelectedItemId(R.id.records);
        });
    }
}
