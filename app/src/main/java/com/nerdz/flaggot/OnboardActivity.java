package com.nerdz.flaggot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OnboardActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        findViewById(R.id.buttonCards).setOnClickListener(this);
        findViewById(R.id.buttonQuiz).setOnClickListener(this);
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
                startActivity(quizIntent);
                break;
        }

    }
}
