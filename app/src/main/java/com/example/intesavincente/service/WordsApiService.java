package com.example.intesavincente.service;

import com.example.intesavincente.model.WordsResponse;
import com.example.intesavincente.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface WordsApiService {

    @GET(Constants.RANDOM_WORD_ENDPOINT)
    Call<List<String>> getWords();

}
