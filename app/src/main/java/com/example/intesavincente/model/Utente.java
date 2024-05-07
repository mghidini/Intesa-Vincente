package com.example.intesavincente.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;

public class Utente {
    public String nickname;
    public String idUtente;
    private String mail;
    private ArrayList<String> partite =new ArrayList<>();

    public Utente(){

    }
    public Utente(String idUtente,String nickname, String mail, ArrayList<String> partite) {
        this.idUtente = idUtente;
        this.nickname = nickname;
        this.mail = mail;
        this.partite = partite;

    }
    public Utente(String idUtente,String nickname, String mail) {
        this.idUtente = idUtente;
        this.nickname = nickname;
        this.mail = mail;
    }
    public void setId(String idUtente){
        FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getIdUtente(){
        return idUtente;
    }
    public Utente(String idUtente,String nickname, String mail, String idPartita) {
        this.idUtente = idUtente;
        this.nickname = nickname;
        this.mail = mail;
        partite.add(idPartita);
    }
    public String getMail() {
        return mail;
    }

    public String getNickname() {
        return nickname;
    }



    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ArrayList<String> getPartite() {
        return partite;
    }
    public void setPartite(ArrayList<String> partite){
        this.partite=partite;
    }
    public void aggiungiPartita(String partitaID) {
        partite.add(partitaID);
    }

    //@Override
    public String toString1() {
        return "Utente{" +
                "nickname='" + nickname + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }

}
