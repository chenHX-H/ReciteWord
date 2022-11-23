package com.example.reciteword.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciteword.Data;
import com.example.reciteword.R;
import com.example.reciteword.Word;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordAdapter extends ArrayAdapter<Word> {
    public int resourceId;
    private List<Word> data;
    public WordAdapter(Context context, int textViewResourceId, List<Word> data){
        super(context,textViewResourceId,data);
        resourceId = textViewResourceId;
        this.data=data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Word word = getItem(position); //获取当前项的实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView wordContext = (TextView)view.findViewById(R.id.word_context);
        TextView wordDefinition = (TextView)view.findViewById(R.id.word_definition);
        TextView wordPron = (TextView)view.findViewById(R.id.word_pron);
        ImageButton collect_IB =(ImageButton) view.findViewById(R.id.collect_IB);

        collect_IB.setTag(4);
        collect_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),word.getWord().toString()+" 已取消收藏",Toast.LENGTH_LONG).show();
                onDeleteCollectWord(word.getWord());
            }
        });
        wordContext.setText(word.getWord());
        wordDefinition.setText(word.getDefinition());
        wordPron.setText(word.getPron()+"");
        return view;
    }
    private void onDeleteCollectWord(String word){
        SharedPreferences collectBookPre = getContext().getSharedPreferences("collectBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = collectBookPre.edit();

        Set<String> collects = collectBookPre.getStringSet("collects", null);
        if (collects==null){
            collects=new HashSet<String>();
        }
        Set<String> newCollects = new HashSet<String>(collects);
        newCollects.remove(word);

        editor.putStringSet("collects",newCollects);
        editor.apply();

        //更新数据源
        this.data.clear();
        for (String collect : newCollects) {
            this.data.add(Data.getWordInstanceByName(collect));
        }

        this.notifyDataSetChanged(); //调用adapter的方法刷新

    }
}
