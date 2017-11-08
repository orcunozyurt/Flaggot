package com.nerdz.flaggot;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nerdz.flaggot.adapters.CardsRecyclerAdapter;
import com.nerdz.flaggot.models.Country;
import com.nerdz.flaggot.services.RESTCountriesService;
import com.nerdz.flaggot.utils.ApiUtils;
import com.nerdz.flaggot.utils.MyCustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RESTCountriesService mService;
    private RecyclerView cardsRecyclerView;
    private CardsRecyclerAdapter mAdapter;
    private ProgressDialog pDialog;
    private static List<Country> countryList  = new ArrayList<>();


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
        pDialog = MyCustomProgressDialog.ctor(this,R.style.ProgressDialog);
        pDialog.show();
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

                pDialog.hide();
                pDialog.dismiss();

                if(response.isSuccessful()) {
                    //Log.d(getLocalClassName(), "onResponse: "+ response.body());
                    countryList = response.body();
                    mAdapter.updateCountries(countryList);
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
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mAdapter.updateCountries(countryList);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        mAdapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.filter(query);
        return true;
    }

    private void showErrorDialog(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(CardsActivity.this, R.style.AppCompatAlertDialogStyle);
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
