package com.example.intesavincente.repository.traduzione;

import android.app.Application;

import com.example.intesavincente.model.TraduzioneBody;
import com.example.intesavincente.repository.words.IWordsRepository;
import com.example.intesavincente.service.WordsApiService;
import com.example.intesavincente.utils.Constants;
import com.example.intesavincente.repository.traduzione.TraduzioneResponse;
import com.example.intesavincente.utils.ServiceLocator;

import com.example.intesavincente.model.WordsResponse;


import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

import java.nio.file.attribute.AclEntry;
import java.util.HashMap;
import java.util.List;


public class TraduzioneRepository implements ITraduzioneRepository {

    private final Application mApplication;
    private final TraduzioneApiService mTraduzioneApiService;
    private final TraduzioneResponse mTraduzioneResponse;

    public TraduzioneRepository(Application application, TraduzioneResponse traduzioneResponse) {
        this.mApplication = application;
        this.mTraduzioneApiService = ServiceLocator.getInstance().getTraduzioneApiService(); //restituisce cleint retrofit
        this.mTraduzioneResponse = traduzioneResponse;
    }

    @Override
    public void fetchTraduzione(String parola) {
        System.out.println("parola222 " + parola);
        TraduzioneBody t=new TraduzioneBody("en_GB", "it_IT", parola, "api");
        HashMap<String,String> hashMap=new HashMap<>();
        Call<LingvanexTranslateResponse> mTraduzioneResponse1 = mTraduzioneApiService.putDizionario(t, "application/json",Constants.API_KEY);
        System.out.println("parolafuori2"+mTraduzioneResponse1.toString());
        mTraduzioneResponse1.enqueue(new Callback<LingvanexTranslateResponse>(){
            @Override
            public void onResponse(Call<LingvanexTranslateResponse> call, Response<LingvanexTranslateResponse> response) {
                System.out.println("parola333" + response.message() + " e "+ response.errorBody()+" e ");
                System.out.println("parola333" );
                if (response.body() != null && response.isSuccessful()){
                    System.out.println("parola333" + response.body().getResult());
                    String parolaGiusta = response.body().getResult();
                    mTraduzioneResponse.onTradotto(parolaGiusta);
                }
            }
            @Override
            public void onFailure(Call<LingvanexTranslateResponse> call, Throwable t) {
                System.out.println("errore");
                System.out.println("errore"+ t.getMessage());
            }
        });
    }
}
