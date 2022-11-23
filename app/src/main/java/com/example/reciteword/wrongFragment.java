package com.example.reciteword;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.reciteword.adapter.WordAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class wrongFragment extends Fragment {

    private List<Word> wordList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrong, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SharedPreferences sharedPre = getActivity().getSharedPreferences("collectBook", Context.MODE_PRIVATE);
        Set<String> collects = sharedPre.getStringSet("collects", null);
        if (collects==null){
            collects=new HashSet<String>();
        }
        for (String collect : collects) {
            wordList.add(Data.getWordInstanceByName(collect));
        }

        WordAdapter adapter = new WordAdapter(getActivity(), R.layout.word_item, wordList);
        ListView listView = (ListView) getActivity().findViewById(R.id.wrong_list_view);
        listView.setAdapter(adapter);
    }

}