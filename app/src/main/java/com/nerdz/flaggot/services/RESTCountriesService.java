package com.nerdz.flaggot.services;

import com.nerdz.flaggot.models.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by orcunozyurt on 11/6/17.
 */

public interface RESTCountriesService {

    @GET("/rest/v2/all")
    Call<List<Country>> getAllCountries();


}
