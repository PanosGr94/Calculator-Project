package com.example.lenovoppc.calculator.Online;

import com.example.lenovoppc.calculator.Model.Exchange;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface JSONPlaceHolderApi {
    /*@GET("/posts/{id}")
    public Call<Exchange> getPostWithID(@Path("id") int id);*/

    @GET("latest")
    public Call<Exchange> getExchanges(@Query("access_key") String access_key, @Query("symbols") String symbols);
}