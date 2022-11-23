package com.example.reciteword;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class battleActivity extends AppCompatActivity {
    private Button choose1Button, choose2Button, choose3Button; // 3个单词按钮
    private TextView user1Score, user2Score, wordFightText;   // 左右得分Text，单词显示Text
    private int right_Opinion_Order = 0, myScore = 0, youScore = 0, fullScore = 100, thisWordNum;
    private final int AI_Score_Rate_InMyWin = 7, AI_Score_Rate_InMyFail = 6;
    private String word, definition1, definition2, definition3;
    private List<String> errorsWordList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        fightFragment.isReturnFlag=false;


        /***控件绑定***/
        choose1Button = (Button) findViewById(R.id.choose1Button);
        choose2Button = (Button) findViewById(R.id.choose2Button);
        choose3Button = (Button) findViewById(R.id.choose3Button);
        user1Score = (TextView) findViewById(R.id.user1Score);
        user2Score = (TextView) findViewById(R.id.user2Score);
        wordFightText = (TextView) findViewById(R.id.wordFightText);
        TextView required_score_TV=findViewById(R.id.required_score);
        required_score_TV.setText(String.valueOf(fullScore));

        /***  ***/

        initWord();
        /***事件绑定***/
        choose1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (right_Opinion_Order == 0) {
                    calc_score(true);
                } else {
                    calc_score(false);
                }
                checkGameEnd();
                initWord();
            }
        });
        choose2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (right_Opinion_Order == 1) {  //按对了
                    calc_score(true);
                } else {
                    calc_score(false);
                }
                checkGameEnd();
                initWord();
            }
        });
        choose3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (right_Opinion_Order == 2) {  //按对了
                    calc_score(true);
                } else {
                    calc_score(false);

                }
                checkGameEnd();
                initWord();
            }
        });
    }

    private void calc_score(Boolean isWin) {
        if (isWin) {
            myScore += 10;
            //AI 加分；70%
            if (getNum(10) <= AI_Score_Rate_InMyWin)
                youScore += 10;
            return;
        }
        if (getNum(10) <= AI_Score_Rate_InMyFail)
            youScore += 10;

        errorsWordList.add(word);

    }

    private void checkGameEnd() {
        Boolean isWin = true;
        // 传递错误列表数据
        if (myScore >= fullScore && youScore <= fullScore) {
            isWin = true;
        } else if (myScore <= fullScore && youScore >= fullScore) {
            isWin = false;
        }else{
            return;
        }
        Intent intent = new Intent(this, pkResultActivity.class);
        intent.putExtra("isWin",isWin);
        intent.putStringArrayListExtra("errorsList",(ArrayList<String>) errorsWordList);
        startActivity(intent);
        finish();
    }



    private void initWord() {
        int rNum = getNum(90); //随机取一个单词作为正确答案
        thisWordNum = rNum;
        Boolean is=Data.getAll().isEmpty();
        System.out.println("----------------test==="+String.valueOf(rNum)+"----"+Data.getAll().size()+String.valueOf(is)+"-----------------");


        word = Data.getWord(rNum);
        right_Opinion_Order = getNum(3); //随机出正确答案的选项位置
        //将2个错误答案和1个正确答案，打乱顺序混淆填入。
        switch (right_Opinion_Order) {
            case 0:
                definition1 = Data.getwordDefine(rNum);
                definition2 = Data.getwordDefine(getNum(90));
                definition3 = Data.getwordDefine(getNum(90));
                break;
            case 1:
                definition1 = Data.getwordDefine(getNum(90));
                definition2 = Data.getwordDefine(rNum);
                definition3 = Data.getwordDefine(getNum(90));
                break;
            default:
                definition1 = Data.getwordDefine(getNum(90));
                definition2 = Data.getwordDefine(getNum(90));
                definition3 = Data.getwordDefine(rNum);
                break;
        }
        choose1Button.setText(definition1);
        choose2Button.setText(definition2);
        choose3Button.setText(definition3);
        wordFightText.setText(word);
        user1Score.setText("得分:" + myScore);
        user2Score.setText("得分:" + youScore);
    }

    private static int getNum(int endNum) {
        if (endNum > 0) {
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }
}