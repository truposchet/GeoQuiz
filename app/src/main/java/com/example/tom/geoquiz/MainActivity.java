package com.example.tom.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private Button mTrueButton;
    SparseBooleanArray blockedButt = new SparseBooleanArray();
    private Button mFalseButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private int answeredQuestions = 0;
    private int trueAnswers=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        for(int i = 0; i<=mQuestionBank.length; i++){
            blockedButt.put(i, false);
        }
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex+1)%mQuestionBank.length;
                checkButt(mCurrentIndex);
                updateQuestion();
            }
        });
        mPrevButton =  findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex-1)%mQuestionBank.length;
                checkButt(mCurrentIndex);
                updateQuestion();
            }
        });
        mTrueButton =  findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockButt(mCurrentIndex, true);
                checkButt(mCurrentIndex);
                answeredQuestions++;
                checkAnswer(true);
            }
        });
        mFalseButton =  findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockButt(mCurrentIndex, true);
                checkButt(mCurrentIndex);
                answeredQuestions++;
                checkAnswer(false);
            }
        });
        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this,
                        answerIsTrue);
                startActivity(intent);
            }
        });
    }


    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;
        if(userPressedTrue == answerIsTrue){
            trueAnswers++;
            messageResId = R.string.correct_toast;
        }
        else{
            messageResId=R.string.incorrect_toast;
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
        if (answeredQuestions == mQuestionBank.length){
            Toast.makeText(this,trueAnswers+"/"+mQuestionBank.length,Toast.LENGTH_SHORT).show();
        }
    }

    public void blockButt(int currentIndex, boolean block){
        blockedButt.put(currentIndex, block);
    }

    public void checkButt(int currentIndex){
        if(blockedButt.get(currentIndex)){
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
        if(!blockedButt.get(currentIndex)){
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() calleds");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private int mCurrentIndex = 0;
}
