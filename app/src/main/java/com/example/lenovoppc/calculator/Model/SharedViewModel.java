package com.example.lenovoppc.calculator.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> valueToExchange = new MutableLiveData<String>();
    private final MutableLiveData<Exchange> exchangeRates = new MutableLiveData<Exchange>();

    public void updateValue(String newValue) {
        valueToExchange.setValue(newValue);
    }

    public LiveData<String> getValueToExchange() {
        return valueToExchange;
    }

    public void updateRates(Exchange newExchangeRates){
        exchangeRates.setValue(newExchangeRates);
    }

    public LiveData<Exchange> getNewExchangeRates() {
        return exchangeRates;
    }
}