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

import com.example.reciteword.dao.DataUtil;

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
//        tipsButton = (ImageButton) getActivity().findViewById(R.id.tipsButton);
        wordText = (TextView) getActivity().findViewById(R.id.wordText);
        definitionText = (TextView) getActivity().findViewById(R.id.definitionText);

        wordText.setText(DataUtil.getWordInstanceById(DataUtil.currentOrder).getWord());
        System.out.println("测试测试:"+wordText.getText().toString());
        definitionText.setText("");

        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                knowButton.setText("认识");
                unknowButton.setText("不认识");
                NoKnowWord = false;
//                Data.setRandNum();
                DataUtil.currentOrder++;
                definitionText.setText("");
                if (DataUtil.currentOrder >= DataUtil.currentWordNum) {
                    DataUtil.currentOrder = 0;
                }
                wordText.setText(DataUtil.getWordInstanceById(DataUtil.currentOrder).getWord());

            }
        });
        unknowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                definitionText.setText(Data.getPron(Data.getRandNum()) + "\n" + Data.getwordDefine(Data.getRandNum()));
                Word word = DataUtil.getWordInstanceById(DataUtil.currentOrder);
                String str=word.getPronounce()+"\n"+word.getDefinition();
                definitionText.setText(str);
                if (!NoKnowWord) {
                    unknowButton.setText("收藏单词");
                    knowButton.setText("下一个");
                    NoKnowWord = true;
                    return;
                }

                knowButton.setText("认识");
                unknowButton.setText("不认识");
                NoKnowWord = false;

                //加入收藏本
                addWordIntoCollects(wordText.getText().toString());
//                Data.setRandNum();
                definitionText.setText("");
                DataUtil.currentOrder++;
                if(DataUtil.currentOrder>=DataUtil.currentWordNum){
                    DataUtil.currentOrder=0;
                }
                wordText.setText(DataUtil.getWordInstanceById(DataUtil.currentOrder).getWord());
//                wordText.setText(Data.getWord(Data.getRandNum()));

            }
        });
//        tipsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                definitionText.setText(Data.getPron(Data.getRandNum()) + "\n" + Data.getwordDefine(Data.getRandNum()));
//                definitionText.setText(DataUtil.getPronounce(DataUtil.cu));
//            }
//        });
    }

    private void addWordIntoCollects(String word) {
        SharedPreferences collectBookPre =
                getActivity().getSharedPreferences("collectBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = collectBookPre.edit();
        Set<String> collects = collectBookPre.getStringSet("collects", null);
        if (collects == null) {
            collects = new HashSet<String>();
        }
        Set<String> newCollects = new HashSet<String>(collects);
        newCollects.add(word);

        editor.putStringSet("collects", newCollects);
        editor.apply();

    }
}