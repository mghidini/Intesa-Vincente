package com.example.intesavincente.repository.traduzione;

import org.json.JSONException;

public interface ITraduzioneRepository {
    enum JsonParser {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };
    //metodo che permetta di ottenre info che mi servono (in questo caso info per ogni paese)
    void fetchTraduzione(String parola) throws JSONException;
}
