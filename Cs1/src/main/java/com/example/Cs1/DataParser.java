package com.example.Cs1;

import com.google.gson.Gson;

public class DataParser {

    private Gson gson;

    public DataParser() {
        this.gson = new Gson();
    }

    public PersonSimpleData parsePersonSimpleData(String json) {
        return gson.fromJson(json, PersonSimpleData.class);
    }

    public AddressData parseAddressData(String json) {
        return gson.fromJson(json, AddressData.class);
    }

    public MovementsData parseMovementsData(String json) {
        return gson.fromJson(json, MovementsData.class);
    }

    public StayData parseStayData(String json) {
        return gson.fromJson(json, StayData.class);
    }
}
