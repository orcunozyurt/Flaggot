package com.nerdz.flaggot.models;

/**
 * Created by orcunozyurt on 11/8/17.
 */

public class Question {
    private String flagURL;
    private String choiceOne;
    private String choiceTwo;
    private String choiceThree;
    private String answer;

    public Question(String flagURL, String choiceOne, String choiceTwo, String choiceThree, String answer) {
        this.flagURL = flagURL;
        this.choiceOne = choiceOne;
        this.choiceTwo = choiceTwo;
        this.choiceThree = choiceThree;
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
}
