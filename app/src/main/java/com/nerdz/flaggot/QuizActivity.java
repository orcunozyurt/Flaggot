package com.nerdz.flaggot;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.nerdz.flaggot.models.Country;
import com.nerdz.flaggot.models.Question;
import com.nerdz.flaggot.services.RESTCountriesService;
import com.nerdz.flaggot.utils.ApiUtils;
import com.nerdz.flaggot.utils.MyCustomProgressDialog;

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
    private ViewPager mViewPager;
    private List<Country> countryList  = new ArrayList<>();
    private ProgressDialog pDialog;
    private RESTCountriesService mService;
    private List<Question> mQuestionsList = new ArrayList<>();
    private List<String> mChoicesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mService = ApiUtils.getSOService();


        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), countryList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        pDialog = MyCustomProgressDialog.ctor(this,R.style.ProgressDialog);
        pDialog.show();
        loadCountries();


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
                    prepareQuestions(countryList);
                    mSectionsPagerAdapter.updateAdapter(countryList);
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

        private static final String ARG_SECTION_NUMBER = "section_number";
        private CardView firstChildCardView;
        private ImageView firstChildImageView;
        private FButton choiceOneButton;
        private FButton choiceTwoButton;
        private FButton choiceThreeButton;
        private FButton choiceFourButton;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Country> mData;

        public SectionsPagerAdapter(FragmentManager fm, List<Country> data) {
            super(fm);
            this.mData = data;
        }
        public void updateAdapter(List<Country> items) {
            mData = items;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
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

    }
    private List<String> preparePossibleChoices (List<Country> mCountries){
        List<String> initialQuestions = prepareInitialQuestions(mCountries);

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

            Question question = new Question(country.getFlag(),country.getName());
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
