package com.example.reciteword;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;


public class fightFragment extends Fragment {
    public static Boolean isReturnFlag=true;
    private LinearLayout fighLayout;
    Timer timer = new Timer();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        System.out.println("===========================================onCreateView触发");
        return inflater.inflate(R.layout.fragment_fight, container, false);

    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isReturnFlag){
            Intent intent = new Intent(getActivity(), InterfaceActivity.class);
            startActivity(intent);
            timer.cancel();
            return;

        }
        startGoing();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("============================================onActivityCreated触发");
        startGoing();

    }
    private void startGoing(){
        isReturnFlag=true;
        fighLayout = (LinearLayout)getActivity().findViewById(R.id.fighLayout);
        fighLayout.setAlpha((float) 0.5);
        TimerTask task = new TimerTask(){
            public void run(){
                Intent intent = new Intent(getActivity(), battleActivity.class);
                startActivity(intent);
            }
        };


        timer.schedule(task,3000);

    }
}