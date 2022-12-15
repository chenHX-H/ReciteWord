package com.example.reciteword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.reciteword.dao.DataUtil;
import com.example.reciteword.dao.WordDataOpenHelper;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*****开始选择单词书，并解压数据*****/
        //将解压好的单词数据，存储进sqlite
        DataUtil.init(getApplicationContext());
        DataUtil.MainAcivity=this;
        Intent intent = new Intent(MainActivity.this, InterfaceActivity.class);
        startActivity(intent);

    }

}