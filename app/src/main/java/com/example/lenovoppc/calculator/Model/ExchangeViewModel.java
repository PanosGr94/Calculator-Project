package com.example.lenovoppc.calculator.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ExchangeViewModel extends ViewModel {
    private final MutableLiveData<Exchange> exchangeRates = new MutableLiveData<Exchange>();

    public void updateRates(Exchange newExchangeRates){
        exchangeRates.setValue(newExchangeRates);
    }

    public LiveData<Exchange> getNewExchangeRates() {
        return exchangeRates;
    }
}