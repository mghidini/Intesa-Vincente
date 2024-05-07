package com.example.intesavincente.utils;

import com.example.intesavincente.repository.traduzione.TraduzioneApiService;
import com.example.intesavincente.service.WordsApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {

    private static ServiceLocator instance = null;

    private ServiceLocator() {}
    //non vado a creare + servicelocator, ma accedo in modo statico alla classe
    //sopravvive instance in vari punti della app
    //pattern singleton
    //servicelocator è una specie di registro
    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    /**
     * It creates an instance of NewsApiService using Retrofit.
     * @return an instance of NewsApiService.
     */
    public WordsApiService getWordsApiService() {
        //creato client di tipo retrofit, associo a interfaccia newsapiservice
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WORDS_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(WordsApiService.class);
    }

    public TraduzioneApiService getTraduzioneApiService() {
        //creato client di tipo retrofit, associo a interfaccia newsapiservice
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.TRADUCT_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(TraduzioneApiService.class);
    }

}
