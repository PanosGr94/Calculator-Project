package com.example.lenovoppc.calculator.Network;

import com.example.lenovoppc.calculator.Model.Exchange;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {

    //This is where the url is built. I pass in the query values from NetworkStatusReceiver when
    //starting the call for response. Retrofit returns Call<E>
    @GET("latest")
    public Call<Exchange> getExchanges(@Query("access_key") String access_key,
                                       @Query("symbols") String symbols);
}