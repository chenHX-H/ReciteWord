package com.example.reciteword.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.reciteword.Word;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataUtil {
    public static SQLiteDatabase db = null;
    public static Context context = null;
    public static int currentWordNum = 0;
    public static int currentOrder = 1;
    public static int currentOrder_Review = 1;

    public static void init(Context MainContext) {
        context = MainContext;
        createDB();
        //还需要判断是否是第一次启动


        if (isFirstRun()) {
            loadWordBook("default_wordbook.json");
            Toast.makeText(context,"第一次启动，开始加载单词书.",Toast.LENGTH_LONG).show();
        }
        currentWordNum = getWordCount();
    }

    private static void loadWordBook(String wordBookFileName) {
        /** 该方法用于将单词书加载进数据库，适用于首次启动或加入新单词书时 **/
        // 1.从单词书中加载数据进内存
        Gson gson = new Gson();
        String jsonString = getJsonFromAssets(wordBookFileName);
        Word[] words = gson.fromJson(jsonString, Word[].class);
        List<Word> wordList = Arrays.asList(words);
        // 2.将数据加入SQLite数据库
        String bookName = wordBookFileName.substring(0, wordBookFileName.lastIndexOf("."));
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            ContentValues values = new ContentValues();
//            values.put("id",i);
            values.put("word", word.getWord());
            values.put("pronounce", word.getPronounce());
            values.put("definition", word.getDefinition());
            values.put("sentence_en", word.getSentence_en());
            values.put("sentence_zh", word.getSentence_zh());
            db.insert(bookName, null, values);
        }
    }

    private static void createDB() {
        WordDataOpenHelper wordDataOpenHelper = new WordDataOpenHelper(context, "Words.db");
        SQLiteDatabase writableDatabase = wordDataOpenHelper.getWritableDatabase();
        db = writableDatabase;
        System.out.println("数据库里面的版本号:" + db.getVersion());
    }

    private static String getJsonFromAssets(String bookNameJson) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(bookNameJson);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Word getWordInstance(String wordName) {
        Cursor cursor = db.rawQuery("select * from default_wordbook where word=?", new String[]{wordName});
        Word word = new Word(wordName);
        while (cursor.moveToNext()) {

            String pronounce = cursor.getString(2);
            String definition = cursor.getString(3);
            String sentence_en = cursor.getString(4);
            String sentence_zh = cursor.getString(5);
            word.setDefinition(definition);
            word.setPronounce(pronounce);
            word.setSentence_en(sentence_en);
            word.setSentence_zh(sentence_zh);
        }
        return word;
    }

    public static Word getWordInstanceById(int id) {
        Cursor cursor = db.rawQuery("select * from default_wordbook where id=?", new String[]{String.valueOf(id + 1)});
        Word word = new Word("");
        while (cursor.moveToNext()) {
            String wordName = cursor.getString(1);
            String pronounce = cursor.getString(2);
            String definition = cursor.getString(3);
            String sentence_en = cursor.getString(4);
            String sentence_zh = cursor.getString(5);

            word.setWord(wordName);
            word.setDefinition(definition);
            word.setPronounce(pronounce);
            word.setSentence_en(sentence_en);
            word.setSentence_zh(sentence_zh);
        }
        return word;
    }

    public static String getDefinition(String wordName) {
        Cursor cursor = db.rawQuery("select definition from default_wordbook where word=?", new String[]{wordName});
        //cursor是默认从-1的，如果不适用 cursor.moveToNext()循环读取，需要+1
        cursor.moveToNext();
        String definition = cursor.getString(0);
        return definition;
    }

    public static String getPronounce(String wordName) {
        Cursor cursor = db.rawQuery("select pronounce from default_wordbook where word=?", new String[]{wordName});
        //cursor是默认从-1的，如果不适用 cursor.moveToNext()循环读取，需要+1
        cursor.moveToNext();
        String pronounce = cursor.getString(0);
        return pronounce;
    }

    public static String getSentence_ZH(String wordName) {
        Cursor cursor = db.rawQuery("select sentence_zh from default_wordbook where word=?", new String[]{wordName});
        //cursor是默认从-1的，如果不适用 cursor.moveToNext()循环读取，需要+1
        cursor.moveToNext();
        String sentence_zh = cursor.getString(0);
        return sentence_zh;
    }

    public static String getSentence_EN(String wordName) {
        Cursor cursor = db.rawQuery("select sentence_en from default_wordbook where word=?", new String[]{wordName});
        //cursor是默认从-1的，如果不适用 cursor.moveToNext()循环读取，需要+1
        cursor.moveToNext();
        String sentence_en = cursor.getString(0);
        return sentence_en;
    }

    public static int getWordCount() {

        String sql = "select count(*) from default_wordbook";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        return count;
    }

    public static int getRandNum(int endNum) {
        if (endNum > 0) {
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }

    private static Boolean isFirstRun() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FirstRun", 0);
        Boolean first_run = sharedPreferences.getBoolean("First", true);
        if (first_run) {
            sharedPreferences.edit().putBoolean("First", false).commit();
            return true;
        }
        return false;

    }

}
