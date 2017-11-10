package com.nerdz.flaggot;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class OnboardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboard);

        findViewById(R.id.buttonCards).setOnClickListener(this);
        findViewById(R.id.buttonQuiz).setOnClickListener(this);
        scoreTextView = (TextView) findViewById(R.id.score_text_view);
        //scoreTextView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.buttonCards:
                Intent cardsIntent = new Intent(this, CardsActivity.class);
                startActivity(cardsIntent);
                break;
            case R.id.buttonQuiz:

                Intent quizIntent = new Intent(this, QuizActivity.class);
                startActivityForResult(quizIntent, 1);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int result=data.getIntExtra("result",0);
                scoreTextView.setText("SCORE : "+ result);
                scoreTextView.setVisibility(View.VISIBLE);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
