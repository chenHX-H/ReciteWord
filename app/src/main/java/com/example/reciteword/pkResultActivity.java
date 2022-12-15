package com.example.reciteword;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.reciteword.adapter.BookAdapter;
import com.example.reciteword.adapter.ErrorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class pkResultActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk_result);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        listView = findViewById(R.id.listview);
        TextView result_TV = findViewById(R.id.result_TV);

        boolean isWin = getIntent().getBooleanExtra("isWin", true);
        if (isWin) {
            result_TV.setText("Succeed!");
        } else {
            result_TV.setText("Fail! ");
        }
        //intent传递来的数据
        List<String> errorsList = getIntent().getStringArrayListExtra("errorsList");
        System.out.println("*********************" + errorsList);
        //创建适配器，绑定数据源和控件UI布局项

        BookAdapter adapter = new BookAdapter(this, R.layout.error_item, errorsList);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        Button return_home_btn = findViewById(R.id.return_home);
        Button restart_pk_btn = findViewById(R.id.restart_pk);

        restart_pk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync_collect_data();
                finish();
                fightFragment.isReturnFlag=true;
            }
        });
        return_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync_collect_data();
                Intent intent = new Intent(getApplicationContext(), InterfaceActivity.class);
                startActivity(intent);
            }
        });


        System.out.println("-----------log:------------");
        System.out.println("isWin = " + isWin);
        System.out.println("errorsList = " + errorsList);
    }

    private void sync_collect_data() {
        SharedPreferences collectBookPre =
                getSharedPreferences("collectBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = collectBookPre.edit();
        //取出原数据，并浅拷贝
        Set<String> collects = collectBookPre.getStringSet("collects", null);
        if (collects == null) {
            collects = new HashSet<String>();
        }
        Set<String> newCollects = new HashSet<String>(collects);
        //将新数据加入(如果有)
        HashSet<String> set = (HashSet<String>) ErrorAdapter.tempSet;
        for (String s : set) {
            newCollects.add(s);
        }
        //清空临时变量区
        set.clear();
        editor.putStringSet("collects", newCollects);
        editor.apply();
    }
}

