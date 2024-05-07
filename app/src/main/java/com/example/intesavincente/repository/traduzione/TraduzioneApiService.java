package com.example.intesavincente.repository.traduzione;

import com.example.intesavincente.utils.Constants;

import java.util.HashMap;
import java.util.List;
import com.example.intesavincente.model.TraduzioneBody;
import com.google.gson.JsonObject;


import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Query;

public interface TraduzioneApiService {

    //@Headers({ "Content-Type: application/json"})
    @POST(Constants.END_POINT)
    Call<LingvanexTranslateResponse> putDizionario(
            @Body TraduzioneBody  t,
            //@Header("x-rapidapi-host") String rapidHost,

            @Header("Content-Type:") String content,

            @Header("x-rapidapi-key") String apiKey);
}
