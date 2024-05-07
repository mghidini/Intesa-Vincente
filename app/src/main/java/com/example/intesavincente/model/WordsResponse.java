package com.example.intesavincente.model;

import java.util.List;

public class WordsResponse {

    private String parola;

    public WordsResponse(String parola) {
        this.parola = parola;
    }

    public WordsResponse() {
    }

    public String getParola() {
        return parola;
    }

    public void setStatus(String status) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "NewsResponse{" +
                "parola='" + parola +
                '}';
    }
}
