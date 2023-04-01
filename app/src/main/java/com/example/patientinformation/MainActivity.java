package com.example.patientinformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.patientinformation.Data.Helper.HistoryHelper;
import com.example.patientinformation.Data.Helper.PatientHelper;
import com.example.patientinformation.Data.Helper.RecordHelper;
import com.example.patientinformation.Menus.DashboardFragment;
import com.example.patientinformation.Menus.RecordsFragment;
import com.example.patientinformation.Menus.Registration.RegistrationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            Bundle args = new Bundle();
            switch (item.getItemId()) {
                case R.id.add:
                    fragment = new RegistrationFragment();
                    args.putBoolean("newRecord", true);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, fragment)
                            .commitNow();
                    return true;
                case R.id.records:
                    fragment = new RecordsFragment();
                    break;
                case R.id.home:
                    fragment = new DashboardFragment();
                    break;
            }
            if (fragment != null && !fragment.getClass().getName().equals(getSupportFragmentManager().findFragmentById(R.id.main_container).getClass().getName())) {
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, fragment)
                        .commitNow();
                return true;
            }
            return false;
        });

        HistoryHelper historyHelper = new HistoryHelper(this);
        PatientHelper patientHelper = new PatientHelper(this);
        RecordHelper recordHelper = new RecordHelper(this);

        historyHelper.onCreate(historyHelper.getWritableDatabase());
        patientHelper.onCreate(patientHelper.getWritableDatabase());
        recordHelper.onCreate(recordHelper.getWritableDatabase());
        Log.i("FormLogger", "Current ID: " + getNavBar().getSelectedItemId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new SplashFragment())
                .commitNow();
    }

    public void toggleNavBar(boolean enabled){
        BottomNavigationView nav = this.findViewById(R.id.bottom_nav);
        if(enabled)
            nav.setVisibility(View.VISIBLE);
        else
            nav.setVisibility(View.INVISIBLE);
    }

    public BottomNavigationView getNavBar(){
        return this.findViewById(R.id.bottom_nav);
    }

    @Override
    public void onBackPressed(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if(currentFragment instanceof DashboardFragment) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        this.finish();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        //Nothing cause swag
                    })
                    .create().show();
            return;
        } else if(!currentFragment.getArguments().getBoolean("newRecord") && currentFragment instanceof RegistrationFragment){
            nav.setSelectedItemId(R.id.records);
            return;
        }else if(currentFragment instanceof RegistrationFragment || currentFragment instanceof RecordsFragment) {
            nav.setSelectedItemId(R.id.home);
            return;
        }
        super.onBackPressed();
    }
}