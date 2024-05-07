package com.example.intesavincente.utils;

import com.example.intesavincente.model.WordsResponse;

import retrofit2.Call;
import retrofit2.Response;

public interface ResponseCallback {

    void onResponse(String parola);
    void onFailure(String errorMessage);

}
