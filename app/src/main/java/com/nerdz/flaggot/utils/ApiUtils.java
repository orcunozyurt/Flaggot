package com.nerdz.flaggot.utils;

import com.nerdz.flaggot.services.RESTCountriesService;
import com.nerdz.flaggot.services.RetrofitClient;

/**
 * Created by orcunozyurt on 11/6/17.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://restcountries.eu/";

    public static RESTCountriesService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(RESTCountriesService.class);
    }
}