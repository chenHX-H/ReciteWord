package com.example.reciteword;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ReviewFragment extends Fragment {

//    private ReviewViewModel mViewModel;
    private Map sentenceData = new HashMap();
    private Boolean isShowMeaning = true;
    private Word currentWord = null;
    MediaPlayer m1=null,m2=null;



    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        System.out.println("-----------onCreateView");

        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        // TODO: Use the ViewModel
        System.out.println("-----------onActivityCreated");
        initEvent();
        Data.currentOrder--;
        if(Data.currentOrder<0){
            Data.currentOrder=0;
        }
        getSentence_meaning();
        play_loop();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void initEvent() {
        ImageButton isShowMeaning_IB = getActivity().findViewById(R.id.sentence_meaning_show);
        isShowMeaning_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowMeaning = !isShowMeaning;
                showInfoWithWord(currentWord);
                System.out.println("ImageButton-Hide = " + isShowMeaning);
            }
        });
        Switch sb = getActivity().findViewById(R.id.soundsSwitch);
        sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (m1 !=null){
                        m1.setVolume(1,1);
                    }
                    if (m1!=null){
                        m2.setVolume(1,1);

                    }
                } else {
                    if (m1!=null){
                        m1.setVolume(0,0);
                    }
                    if (m2!=null){
                        m2.setVolume(0,0);
                    }
                }
            }
        });

    }
    public void checkVolum(){
        Switch sb = getActivity().findViewById(R.id.soundsSwitch);
        if (sb.isChecked()) {
            if (m1 !=null){
                m1.setVolume(1,1);
            }
            if (m2!=null){
                m2.setVolume(1,1);

            }
        } else {
            if (m1!=null){
                m1.setVolume(0,0);
            }
            if (m2!=null){
                m2.setVolume(0,0);
            }
        }
    }

    public void play_loop() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (Data.currentOrder <= 90) {
                    if (Data.currentOrder == 90) {
                        Data.currentOrder = 0;
                    }
                    if (!isAdded()) {
                        continue;
                    }
                    Word wordInstance = Data.getWordInstance(Data.currentOrder);
                    currentWord = wordInstance;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showInfoWithWord(wordInstance);
                        }
                    });

                    playAudio(wordInstance.getWord());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    playAudio_sentence(wordInstance.getWord());


                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Data.currentOrder++;
                    if(Data.currentOrder>=90){
                        Data.currentOrder=0;
                    }
                }

            }
        }).start();


    }


    public void showInfoWithWord(Word word) {


        TextView tv_word = getActivity().findViewById(R.id.word_show_TV);
        TextView tv_mean = getActivity().findViewById(R.id.meaning);
        TextView tv_soundmark = getActivity().findViewById(R.id.soundmark);
        TextView tv_sentence = getActivity().findViewById(R.id.sentence);
        TextView tv_sentence_mean = getActivity().findViewById(R.id.sentence_meaning);


        String d = (String) sentenceData.get(word.getWord());
        String sentence = d.split("=")[0];
        String sentence_meaning = d.split("=")[1];

        tv_word.setText(word.getWord());
        tv_soundmark.setText(word.getPron());
        tv_mean.setText(word.getDefinition());
        tv_sentence.setText(sentence);
        if (!isShowMeaning) {
            tv_sentence_mean.setText("*************");
        } else {
            tv_sentence_mean.setText(sentence_meaning);
        }


    }

    private void getSentence_meaning() {
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open("word_sentence.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (reader.ready()) {
                String s = reader.readLine();
                String[] list = s.split("=");
                sentenceData.put(list[0], list[1] + '=' + list[2]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(sentenceData.get("terror"));

    }

    public void playAudio(String word) {
        MediaPlayer mediaPlayer_audio =m1= new MediaPlayer();
        checkVolum();
        AssetFileDescriptor fd = null;


        try {
            fd = getResources().getAssets().openFd("audio/" + word + ".mp3");
            mediaPlayer_audio.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer_audio.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer_audio.start();

    }

    public void playAudio_sentence(String word) {
        MediaPlayer mediaPlayer_sentence =m2= new MediaPlayer();
        checkVolum();

        AssetFileDescriptor fd = null;

        try {
            fd = getResources().getAssets().openFd("sentence_audio/" + word + ".mp3");
            mediaPlayer_sentence.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer_sentence.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer_sentence.start();
    }

}