package com.mvrt.superscout;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.Snacker;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    Spinner alliance;
    SharedPreferences prefs;

    FirebaseRemoteConfig mRemoteConfig;

    private static final int POS_ALLIANCE_BLUE = 1;
    private static final int POS_ALLIANCE_RED = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        mRemoteConfig = FirebaseRemoteConfig.getInstance();

        prefs = getActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME_SUPER, Activity.MODE_PRIVATE);

        alliance = (Spinner)view.findViewById(R.id.settings_alliance);

        CharSequence[] allianceArray = new CharSequence[2];
        allianceArray[POS_ALLIANCE_BLUE] = "Blue Alliance";
        allianceArray[POS_ALLIANCE_RED] = "Red Alliance";

        ArrayAdapter<CharSequence> alliances = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allianceArray);
        alliance.setAdapter(alliances);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.settings_save);
        fab.setOnClickListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        loadSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_save:
                saveSettings();
                break;
        }
    }

    public void loadSettings(){
        char alliance = (char)prefs.getInt(Constants.PREFS_ALLIANCE_KEY, (int)Constants.ALLIANCE_BLUE);
        int index = (alliance == Constants.ALLIANCE_BLUE)?POS_ALLIANCE_BLUE:POS_ALLIANCE_RED;
        this.alliance.setSelection(index);
    }

    public void saveSettings(){
        char al = (alliance.getSelectedItemPosition() == POS_ALLIANCE_BLUE)?Constants.ALLIANCE_BLUE:Constants.ALLIANCE_RED;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.PREFS_ALLIANCE_KEY, al);
        editor.commit();
        Snacker.snack("Settings Saved", getActivity(), Snackbar.LENGTH_SHORT);
        loadSettings();
    }


}
