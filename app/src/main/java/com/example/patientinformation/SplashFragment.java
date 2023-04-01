package com.example.patientinformation;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.patientinformation.Menus.DashboardFragment;

public class SplashFragment extends Fragment {
    public SplashFragment() {
        super(R.layout.splash_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        activity.toggleNavBar(false);
        new Handler().postDelayed(()->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new DashboardFragment())
                    .commit();
        }, 1000);
    }
}
