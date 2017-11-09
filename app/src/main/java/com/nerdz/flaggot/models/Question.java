package com.nerdz.flaggot.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by orcunozyurt on 11/8/17.
 */

public class Question implements Parcelable {
    private String flagURL;
    private String choiceOne;
    private String choiceTwo;
    private String choiceThree;
    private String answer;

    public Question(String flagURL, String answer, String choiceOne, String choiceTwo, String choiceThree) {
        this.flagURL = flagURL;
        this.choiceOne = choiceOne;
        this.choiceTwo = choiceTwo;
        this.choiceThree = choiceThree;
        this.answer = answer;
    }

    public Question(String flagURL, String answer) {
        this.flagURL = flagURL;
        this.answer = answer;
    }

    public String getFlagURL() {
        return flagURL;
    }

    public void setFlagURL(String flagURL) {
        this.flagURL = flagURL;
    }

    public String getChoiceOne() {
        return choiceOne;
    }

    public void setChoiceOne(String choiceOne) {
        this.choiceOne = choiceOne;
    }

    public String getChoiceTwo() {
        return choiceTwo;
    }

    public void setChoiceTwo(String choiceTwo) {
        this.choiceTwo = choiceTwo;
    }

    public String getChoiceThree() {
        return choiceThree;
    }

    public void setChoiceThree(String choiceThree) {
        this.choiceThree = choiceThree;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean isComplete(){
        return (flagURL != null && answer != null && choiceOne != null && choiceTwo != null && choiceThree != null)? true : false;
    }

    public void addChoice(String choice){
        if(choiceOne == null){
            choiceOne = choice;
        }else if(choiceTwo == null){
            choiceTwo = choice;
        }else if (choiceThree == null){
            choiceThree = choice;
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "flagURL='" + flagURL + '\'' +
                ", choiceOne='" + choiceOne + '\'' +
                ", choiceTwo='" + choiceTwo + '\'' +
                ", choiceThree='" + choiceThree + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.flagURL);
        dest.writeString(this.choiceOne);
        dest.writeString(this.choiceTwo);
        dest.writeString(this.choiceThree);
        dest.writeString(this.answer);
    }

    protected Question(Parcel in) {
        this.flagURL = in.readString();
        this.choiceOne = in.readString();
        this.choiceTwo = in.readString();
        this.choiceThree = in.readString();
        this.answer = in.readString();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
