package com.example.ana.cityfeels.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.DataSource;
import com.example.ana.cityfeels.R;

public class Menu extends Fragment {

    private CityFeels application;

    public Menu() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity activity = getActivity();
        this.application = (CityFeels) activity.getApplication();

        RadioButton ouvirRadioButton = (RadioButton) activity.findViewById(R.id.ouvirRadioButton);
        RadioButton silenciarRadioButton = (RadioButton) activity.findViewById(R.id.silenciarRadioButton);

        ouvirRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton view = (RadioButton)v;
                view.setChecked(true);
                application.getTextToSpeechModule().enable();
            }
        });

        silenciarRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton view = (RadioButton) v;
                view.setChecked(true);
                application.getTextToSpeechModule().disable();
            }
        });

        ImageButton repeatButton = (ImageButton) activity.findViewById(R.id.repeatButtonImage);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.getTextToSpeechModule().repeat();
            }
        });

        setLayerButtonsClickListeners();

        this.setState(this.application);
    }

    private void setLayerButtonsClickListeners() {
        Activity activity = getActivity();
        final Button basicLayerButton = (Button) activity.findViewById(R.id.basicButton);
        final Button localLayerButton = (Button) activity.findViewById(R.id.localButton);
        final Button detailedLayerButton = (Button) activity.findViewById(R.id.detailedButton);
        final Button anotherLayerButton = (Button) activity.findViewById(R.id.anotherButton);

        basicLayerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) return true;

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) return false;

                localLayerButton.setPressed(false);
                detailedLayerButton.setPressed(false);
                anotherLayerButton.setPressed(false);
                application.setCurrentDataLayer(DataSource.DataLayer.Basic);
                v.setPressed(true);

                return true;
            }
        });

        localLayerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) return true;

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) return false;

                basicLayerButton.setPressed(false);
                detailedLayerButton.setPressed(false);
                anotherLayerButton.setPressed(false);
                application.setCurrentDataLayer(DataSource.DataLayer.Local);
                v.setPressed(true);

                return true;
            }
        });

        detailedLayerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) return true;

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) return false;

                localLayerButton.setPressed(false);
                basicLayerButton.setPressed(false);
                anotherLayerButton.setPressed(false);
                application.setCurrentDataLayer(DataSource.DataLayer.Detailed);
                v.setPressed(true);

                return true;
            }
        });

        anotherLayerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) return true;

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) return false;

                localLayerButton.setPressed(false);
                basicLayerButton.setPressed(false);
                detailedLayerButton.setPressed(false);
                application.setCurrentDataLayer(DataSource.DataLayer.Another);
                v.setPressed(true);

                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setState(this.application);
    }

    private void setState(CityFeels app) {
        Activity activity = getActivity();
        DataSource.DataLayer layer = app.getCurrentDataLayer();
        switch(layer) {
            case Another:
                activity.findViewById(R.id.anotherButton).setPressed(true);
                break;

            case Detailed:
                activity.findViewById(R.id.detailedButton).setPressed(true);
                break;

            case Local:
                activity.findViewById(R.id.localButton).setPressed(true);
                break;

            default:
                activity.findViewById(R.id.basicButton).setPressed(true);
        }

        if(app.getTextToSpeechModule().isEnabled())
            ((RadioButton)activity.findViewById(R.id.ouvirRadioButton)).setChecked(true);
        else
            ((RadioButton)activity.findViewById(R.id.silenciarRadioButton)).setChecked(true);
    }

}
