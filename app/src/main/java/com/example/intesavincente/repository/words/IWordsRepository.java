package com.example.intesavincente.repository.words;

public interface IWordsRepository {

    enum JsonParser {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };
    //metodo che permetta di ottenre info che mi servono (in questo caso info per ogni paese)
    void fetchWords();

}
