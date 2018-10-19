package com.example.lenovoppc.calculator.Network;

import com.example.lenovoppc.calculator.Model.Exchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "http://data.fixer.io/api/";
    private Retrofit mRetrofit;

    private NetworkService() {
        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(Exchange.class, new RatesDeserializer())
                        .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public JSONPlaceHolderApi getJSONApi() {
        return mRetrofit.create(JSONPlaceHolderApi.class);
    }
}