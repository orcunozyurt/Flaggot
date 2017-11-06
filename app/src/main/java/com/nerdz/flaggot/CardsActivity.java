package com.nerdz.flaggot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nerdz.flaggot.models.Country;
import com.nerdz.flaggot.services.RESTCountriesService;
import com.nerdz.flaggot.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardsActivity extends AppCompatActivity {

    private RESTCountriesService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        mService = ApiUtils.getSOService();
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
                    //mAdapter.updateAnswers(response.body().getItems());
                    Log.d(getLocalClassName(), "posts loaded from API");
                    List<Country> rs = response.body();
                    Log.d(getLocalClassName(), "onResponse: "+ response.body());
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
