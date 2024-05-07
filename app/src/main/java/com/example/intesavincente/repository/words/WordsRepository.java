package com.example.intesavincente.repository.words;

import android.app.Application;
import android.os.Build;
import retrofit2.Call;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.intesavincente.R;
import com.example.intesavincente.model.WordsResponse;
import com.example.intesavincente.service.WordsApiService;
import com.example.intesavincente.utils.Constants;
import com.example.intesavincente.utils.ResponseCallback;
import com.example.intesavincente.utils.ServiceLocator;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class WordsRepository implements IWordsRepository {

    private static final String TAG = "WordsRepository";

    private final Application mApplication;
    private final WordsApiService mWordsApiService;
    private final ResponseCallback mResponseCallback;

    public WordsRepository(Application application, ResponseCallback responseCallback) {
        this.mApplication = application;
        this.mWordsApiService = ServiceLocator.getInstance().getWordsApiService(); //restituisce cleint retrofit
        this.mResponseCallback = responseCallback;
    }


    @Override
    public void fetchWords() {
        System.out.println("parolafuori");
        Call<List<String>> mWordsResponse = mWordsApiService.getWords();
        System.out.println("parolafuori1"+mWordsResponse.toString());
        mWordsResponse.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                System.out.println("funziona");
                if (response.body() != null && response.isSuccessful())
                    System.out.println("parola" + response.toString());

                System.out.println("parola1 " + response.body().toString());
                String parola1=response.body().toString().substring(1, response.body().toString().length()-1);
                System.out.println("parola2 " + response.body());
                System.out.println("parola123 " + parola1);
                mResponseCallback.onResponse(parola1);
            }


            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.out.println("non funziona" );
                mResponseCallback.onFailure(t.getMessage());
            }
        });
    }
}

