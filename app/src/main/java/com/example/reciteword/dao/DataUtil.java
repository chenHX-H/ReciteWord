package com.example.reciteword.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.reciteword.Word;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.Charsets;

public class DataUtil {
    public static SQLiteDatabase db = null;
    public static Context context = null;
    public static int currentWordNum = 0;
    public static int currentOrder = 1;
    public static int currentOrder_Review = 1;
    public static Activity MainAcivity;

    public static void init(Context MainContext) {
        context = MainContext;
        createDB();
        //还需要判断是否是第一次启动
        if (isFirstRun()) {
//            loadWordBook("default_wordbook.json");
            UnZipAndLoadIntoSQLite("中考单词.zip");
            Toast.makeText(context, "第一次启动，开始加载单词书.", Toast.LENGTH_LONG).show();
        }
        currentWordNum = getWordCount();
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        fileOrDirectory.delete();
    }

    public static void UnZipAndLoadIntoSQLite(String zipName) {
        /***
         * 1.数据库版本号+1,使其重新创建。
         * 2.删除原来的数据文件  file/data/*
         * 3.解压zip文件到file/data/*
         * ***/
        WordDataOpenHelper.DATABASE_VERSION++;

        File dir = new File(context.getFilesDir(), "data");
        if (dir.exists()) {
            deleteRecursive(dir);

        }

        String suffix = ".zip";
        String bookName = zipName.replace(suffix, "");
        String zipPath = context.getFilesDir() + "/data/";
        try {
            UnZipAssetsFolder(zipName, zipPath);
            loadWordBook(bookName, zipPath + bookName + ".json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压assets目录下的zip到指定的路径
     *
     * @param zipFileString ZIP的名称，压缩包的名称：xxx.zip
     * @param outPathString 要解压缩路径
     * @throws Exception
     */
    private static void UnZipAssetsFolder(String zipFileString, String
            outPathString) throws Exception {
//        Charset UTF8 = Charset.forName("UTF-8");

        ZipInputStream inZip = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // WinZip对压缩文件名采取的是gbk格式，所以这里无论内容是什么格式，一律用gbk
            inZip = new ZipInputStream(context.getAssets().open(zipFileString), Charset.forName("GBK"));
        }
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
//                    file.mkdir();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }


    private static void loadWordBook(String wordBookFileName, String wordBookFilePath) {
        db.execSQL("DELETE FROM default_wordbook;");

        /** 该方法用于将单词书加载进数据库，适用于首次启动或加入新单词书时 **/
        // 1.从单词书中加载数据进内存
        Gson gson = new Gson();
        /**当从asset中读取时
         * //        String jsonString = getJsonFromAssets(wordBookFileName);
         * //        Word[] words = gson.fromJson(jsonString, Word[].class);
         * **/

        /*****尝试从文件中读取，失败
         *
         * //        Word[] words = null;
         * //        try {
         * //            words = gson.fromJson(new FileReader(wordBookFilePath), Word[].class);
         * //        } catch (FileNotFoundException e) {
         * //            e.printStackTrace();
         * //        }
         *
         *
         * *******/
        Word[] words = null;
        try {
            words = gson.fromJson(new FileReader(wordBookFilePath), Word[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Word> wordList = Arrays.asList(words);
        System.out.println("----------*******擦擦擦****" + wordList.get(1).getWord() + "********----------------");
        // 2.将数据加入SQLite数据库
//        String bookName = wordBookFileName.substring(0, wordBookFileName.lastIndexOf("."));
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            ContentValues values = new ContentValues();
//            values.put("id",i);
            values.put("word", word.getWord());
            values.put("pronounce", word.getPronounce());
            values.put("definition", word.getDefinition());
            values.put("sentence_en", word.getSentence_en());
            values.put("sentence_zh", word.getSentence_zh());
            db.insert(WordDataOpenHelper.TABLE_NAME, null, values);
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

    private static String getJsonFromFileDir(String jsonPath) throws IOException {
        String json = "";
        File file = new File(jsonPath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st = "";

        while ((st = br.readLine()) != null) {
            json += st;
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
