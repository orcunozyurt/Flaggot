package com.nerdz.flaggot;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.nerdz.flaggot.models.Country;
import com.nerdz.flaggot.models.Question;
import com.nerdz.flaggot.services.RESTCountriesService;
import com.nerdz.flaggot.utils.ApiUtils;
import com.nerdz.flaggot.utils.MyCustomProgressDialog;
import com.nerdz.flaggot.utils.NonSwipeableViewPager;
import com.nerdz.flaggot.utils.SvgDecoder;
import com.nerdz.flaggot.utils.SvgDrawableTranscoder;
import com.nerdz.flaggot.utils.SvgSoftwareLayerSetter;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private NonSwipeableViewPager mViewPager;
    private List<Country> countryList  = new ArrayList<>();
    private ProgressDialog pDialog;
    private RESTCountriesService mService;
    private List<Question> mQuestionsList = new ArrayList<>();
    private int mScore = 0;
    private int mFalseCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mService = ApiUtils.getSOService();


        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), mQuestionsList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        pDialog = MyCustomProgressDialog.ctor(this,R.style.ProgressDialog);
        pDialog.show();
        loadCountries();


    }

    public void nextQuestion(Boolean isCorrectAnswer){
        if(isCorrectAnswer){
            mScore +=1;
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }else {
            mFalseCount +=1;
            if( mFalseCount >= 3){

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",mScore);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }else{
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        }

    }



    public void loadCountries() {
        mService.getAllCountries().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {

                pDialog.hide();
                pDialog.dismiss();

                if(response.isSuccessful()) {
                    //Log.d(getLocalClassName(), "onResponse: "+ response.body());
                    countryList = response.body();
                    List<Question> completeQuestionsList = prepareQuestions(countryList);
                    Log.d(getLocalClassName(), "onResponse: " + completeQuestionsList);
                    mSectionsPagerAdapter.updateAdapter(completeQuestionsList);
                }else {
                    int statusCode  = response.code();
                    if (statusCode != 200){
                        showErrorDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                pDialog.hide();
                pDialog.dismiss();
                showErrorDialog();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_QUESTION = "question";
        private CardView firstChildCardView;
        private ImageView firstChildImageView;
        private FButton choiceOneButton;
        private FButton choiceTwoButton;
        private FButton choiceThreeButton;
        private FButton choiceFourButton;
        private LottieAnimationView animationView;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(Question q) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putParcelable(ARG_QUESTION, q);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
            firstChildCardView = (CardView) rootView.findViewById(R.id.first_child_card_view);
            firstChildImageView = (ImageView) rootView.findViewById(R.id.first_child_image_view);
            choiceOneButton = (FButton) rootView.findViewById(R.id.choice_one_button);
            choiceTwoButton = (FButton) rootView.findViewById(R.id.choice_two_button);
            choiceThreeButton = (FButton) rootView.findViewById(R.id.choice_three_button);
            choiceFourButton = (FButton) rootView.findViewById(R.id.choice_four_button);

            animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);

            final Question mQuestion = getArguments().getParcelable(ARG_QUESTION);

            List<String> optionsList = new ArrayList<>();
            optionsList.add(mQuestion.getAnswer());
            optionsList.add(mQuestion.getChoiceOne());
            optionsList.add(mQuestion.getChoiceTwo());
            optionsList.add(mQuestion.getChoiceThree());
            Collections.shuffle(optionsList);

            choiceOneButton.setText(optionsList.get(0));
            choiceTwoButton.setText(optionsList.get(1));
            choiceThreeButton.setText(optionsList.get(2));
            choiceFourButton.setText(optionsList.get(3));

            choiceOneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(choiceOneButton.getText().toString().equalsIgnoreCase(mQuestion.getAnswer())){
                        correctAnswer();
                    }
                    else{
                        wrongAnswer();
                    }

                }
            });
            choiceTwoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(choiceTwoButton.getText().toString().equalsIgnoreCase(mQuestion.getAnswer())){
                        correctAnswer();
                    }
                    else{
                        wrongAnswer();
                    }

                }
            });
            choiceThreeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(choiceThreeButton.getText().toString().equalsIgnoreCase(mQuestion.getAnswer())){
                        correctAnswer();
                    }
                    else{
                        wrongAnswer();
                    }

                }
            });
            choiceFourButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(choiceFourButton.getText().toString().equalsIgnoreCase(mQuestion.getAnswer())){
                        correctAnswer();
                    }
                    else{
                        wrongAnswer();
                    }

                }
            });

            getFlagImage(firstChildImageView,mQuestion.getFlagURL());

            return rootView;
        }

        public void getFlagImage(ImageView flagImageView, String url) {
            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

            requestBuilder = Glide.with(this)
                    .using(Glide.buildStreamModelLoader(Uri.class,this.getActivity()), InputStream.class)
                    .from(Uri.class)
                    .as(SVG.class)
                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                    .sourceEncoder(new StreamEncoder())
                    .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                    .decoder(new SvgDecoder())
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error)
                    .animate(android.R.anim.fade_in)
                    .listener(new SvgSoftwareLayerSetter<Uri>());

            Uri uri = Uri.parse(url);
            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    // SVG cannot be serialized so it's not worth to cache it
                    .load(uri)
                    .into(flagImageView);

        }

        public void correctAnswer(){

            firstChildCardView.setVisibility(View.INVISIBLE);
            animationView.setAnimation("emoji_wink.json");
            animationView.loop(false);
            animationView.playAnimation();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((QuizActivity) getActivity()).nextQuestion(true);
                }
            }, 2000);






        }

        public void wrongAnswer(){

            firstChildCardView.setVisibility(View.INVISIBLE);
            animationView.setAnimation("emoji_shock.json");
            animationView.loop(false);
            animationView.playAnimation();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((QuizActivity) getActivity()).nextQuestion(false);
                }
            }, 2000);

        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Question> mData;

        public SectionsPagerAdapter(FragmentManager fm, List<Question> data) {
            super(fm);
            this.mData = data;
        }
        public void updateAdapter(List<Question> items) {
            mData = items;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(mData.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mData == null ? 0 : mData.size() ;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private List<Question> prepareQuestions (List<Country> mCountries){
        Collections.shuffle(mCountries);
        List<Question> initialQuestions = prepareInitialQuestions(mCountries);
        List<String> initialChoices = preparePossibleChoices(mCountries);

        Random rand = new Random();

        for (Question question : initialQuestions){

            while ( question.isComplete() == false){

                int randomindex = rand.nextInt(initialChoices.size());
                String candidateChoice = initialChoices.get(randomindex);
                if(isGoodCandidate(question, candidateChoice)){
                    question.addChoice(candidateChoice);
                }

            }
        }

        return initialQuestions;

    }
    private List<String> preparePossibleChoices (List<Country> mCountries){
        List<String> possibleChoices = new ArrayList<>();

        for (Country country : mCountries){
            possibleChoices.add(country.getName().toLowerCase());
        }

        return possibleChoices;
    }

    private Boolean isGoodCandidate(Question initialquestion, String choiceCandidate){

        if (initialquestion.getChoiceOne() != null && initialquestion.getChoiceTwo() != null &&
                initialquestion.getChoiceThree() != null){

            return false;
        }

        if(initialquestion.getAnswer().equalsIgnoreCase(choiceCandidate) ){
            return false;
        }

        if((initialquestion.getChoiceOne() !=null && initialquestion.getChoiceOne().equalsIgnoreCase(choiceCandidate)) ||
                (initialquestion.getChoiceTwo() !=null && initialquestion.getChoiceOne().equalsIgnoreCase(choiceCandidate)) ||
                (initialquestion.getChoiceThree() !=null && initialquestion.getChoiceOne().equalsIgnoreCase(choiceCandidate))){

            return false;
        }

        return true;


    }

    private List<Question> prepareInitialQuestions(List<Country> mCountries){

        Collections.shuffle(mCountries);

        for(Country country : mCountries){

            Question question = new Question(country.getFlag(),country.getName().toLowerCase());
            mQuestionsList.add(question);
        }

        return mQuestionsList;


    }

    private void showErrorDialog(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(QuizActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.ohsnap));
        builder.setMessage(getResources().getString(R.string.smthwentwrong));
        builder.setPositiveButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadCountries();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.show();
    }
}
