package com.example.reciteword;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class reciteFragment extends Fragment {


    private Button knowButton, unknowButton;
    private ImageButton tipsButton;
    private TextView wordText, definitionText;
    private boolean NoKnowWord = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recite, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        knowButton = (Button) getActivity().findViewById(R.id.knowButton);
        unknowButton = (Button) getActivity().findViewById(R.id.unknowButton);
        tipsButton = (ImageButton) getActivity().findViewById(R.id.tipsButton);
        wordText = (TextView) getActivity().findViewById(R.id.wordText);
        definitionText = (TextView) getActivity().findViewById(R.id.definitionText);

        wordText.setText(Data.getWord(Data.getRandNum()));
        definitionText.setText("");

        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                knowButton.setText("认识");
                unknowButton.setText("不认识");
                NoKnowWord=false;
                Data.setRandNum();
                definitionText.setText("");

                wordText.setText(Data.getWord(Data.getRandNum()));

            }
        });
        unknowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                definitionText.setText(Data.getPron(Data.getRandNum()) + "\n" + Data.getwordDefine(Data.getRandNum()));
                if (!NoKnowWord) {
                    unknowButton.setText("收藏单词");
                    knowButton.setText("下一个");
                    NoKnowWord = true;
                    return;
                }

                knowButton.setText("认识");
                unknowButton.setText("不认识");
                NoKnowWord=false;

                //加入收藏本
                addWordIntoCollects(wordText.getText().toString());
                Data.setRandNum();
                definitionText.setText("");
                wordText.setText(Data.getWord(Data.getRandNum()));

            }
        });
        tipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                definitionText.setText(Data.getPron(Data.getRandNum()) + "\n" + Data.getwordDefine(Data.getRandNum()));
            }
        });
    }

    private void addWordIntoCollects(String word){
        SharedPreferences collectBookPre = getActivity().getSharedPreferences("collectBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = collectBookPre.edit();
        Set<String> collects = collectBookPre.getStringSet("collects", null);
        if (collects==null){
            collects=new HashSet<String>();
        }
        Set<String> newCollects = new HashSet<String>(collects);
        newCollects.add(word);

        editor.putStringSet("collects",newCollects);
        editor.apply();

    }
}