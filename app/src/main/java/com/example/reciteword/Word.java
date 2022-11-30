package com.example.reciteword;

import android.app.Person;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Word {
    private String word;    //单词
    private String pronounce;    //音标
    private String definition;  //翻译
    private String sentence;
    private String sentence_zh;
    private String sentence_en;


    public Word(String word, String pron, String definition, int showNum, int flag) {
        this.word = word;
        this.pronounce = pron;
        this.definition = definition;

    }

    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronounce() {
        return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence_zh() {
        return sentence_zh;
    }

    public void setSentence_zh(String sentence_zh) {
        this.sentence_zh = sentence_zh;
    }

    public String getSentence_en() {
        return sentence_en;
    }

    public void setSentence_en(String sentence_en) {
        this.sentence_en = sentence_en;
    }


    public String getPron() {
        return pronounce;
    }


    public String getSentence() {
        return sentence;
    }
}

