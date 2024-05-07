package com.example.intesavincente.repository.traduzione;

import com.example.intesavincente.model.WordsResponse;

import retrofit2.Call;
import retrofit2.Response;
public interface TraduzioneResponse {
    void onTradotto(String parola);
    void onErrore(String err);
}
