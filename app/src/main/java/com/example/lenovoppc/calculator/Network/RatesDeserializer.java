package com.example.lenovoppc.calculator.Network;

import com.example.lenovoppc.calculator.Model.Exchange;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Class is used to get the inner element of the json response without having to
 * create an entirely new POJO to hold these values.
 */
public class RatesDeserializer implements JsonDeserializer<Exchange> {
    @Override
    public Exchange deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Get the "rates" element from the parsed JSON
        JsonElement rates = json.getAsJsonObject().get("rates");

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(rates, Exchange.class);
    }
}
