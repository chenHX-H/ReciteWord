package com.example.reciteword.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reciteword.Data;
import com.example.reciteword.R;
import com.example.reciteword.Word;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ErrorAdapter extends ArrayAdapter<String> {
    public int resourceId;
    public static Set tempSet = new HashSet<String>();

    public ErrorAdapter(Context context, int textViewResourceId, List<String> data) {
        super(context, textViewResourceId, data);
        resourceId = textViewResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ;
        System.out.println("---------------------:::" + position);
        Word word = Data.getWordInstanceByName(getItem(position));
//        Word word = getItem(position);
        TextView error_word_TV = view.findViewById(R.id.error_word_TV);
        TextView error_word_mean_TV = view.findViewById(R.id.error_word_mean_TV);
        ToggleButton error_word_collect_IB = view.findViewById(R.id.error_word_collect_IB);

        error_word_TV.setText(word.getWord());
        error_word_mean_TV.setText(word.getDefinition());

        error_word_collect_IB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(getContext(), word.getWord().toString(), Toast.LENGTH_SHORT).show();
                    tempSet.add(word.getWord());
                } else {
                    Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();
                    tempSet.remove(word.getWord());
                }
            }
        });
        return view;
    }


}

