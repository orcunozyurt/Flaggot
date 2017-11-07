package com.nerdz.flaggot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nerdz.flaggot.adapters.CardsRecyclerAdapter;
import com.nerdz.flaggot.models.Country;
import com.nerdz.flaggot.services.RESTCountriesService;
import com.nerdz.flaggot.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardsActivity extends AppCompatActivity {

    private RESTCountriesService mService;
    private RecyclerView cardsRecyclerView;
    private CardsRecyclerAdapter mAdapter;
    private List<Country> countryList  = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        mService = ApiUtils.getSOService();

        cardsRecyclerView = (RecyclerView) findViewById(R.id.cards_recycler_view);
        mAdapter = new CardsRecyclerAdapter(countryList,this);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        cardsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        cardsRecyclerView.setLayoutManager(mLayoutManager);
        cardsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cardsRecyclerView.setAdapter(mAdapter);
        loadCountries();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void loadCountries() {
        mService.getAllCountries().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {

                if(response.isSuccessful()) {
                    //Log.d(getLocalClassName(), "onResponse: "+ response.body());
                    countryList = response.body();
                    Log.d(getLocalClassName(), "onResponse: Length:"+countryList.size() + " first:"+countryList.get(0).getName());
                    mAdapter.updateCountries(countryList);
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                //showErrorMessage();
                Log.d("MainActivity", "error loading from API");

            }
        });
    }
}
