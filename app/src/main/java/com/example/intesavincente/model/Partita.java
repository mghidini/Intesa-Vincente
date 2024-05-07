package com.example.intesavincente.model;

import java.util.Objects;

public class Partita{
    private String gruppoID;
    private int passo;
    private int parole_indovinate;
    private Boolean isAttiva;
    private String idPartita;
    private String parola;

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public Partita(String gruppoID, int passo, int parole_indovinate, String idPartita) {
        this.gruppoID = gruppoID;
        this.passo = passo;
        this.parole_indovinate = parole_indovinate;
        this.idPartita = idPartita;
        this.isAttiva = true;
    }


    public Partita(){
        this.passo = 3;
        this.parole_indovinate = 0;
        this.isAttiva = false;
        this.parola="prova";
    }

    public Partita(String gruppo,String idPartita) {
        this.gruppoID = gruppo;
        this.idPartita=idPartita;
        this.passo = 3;
        this.parole_indovinate = 0;
        this.isAttiva = true;
        this.parola="prova";
    }
    public Partita(String idPartita) {
        this.idPartita=idPartita;
        this.passo = 3;
        this.parole_indovinate = 0;
        this.isAttiva = true;
        this.parola="prova";
    }
    public String getIdPartita() {
        return idPartita;
    }

    public void setIdPartita(String idPartita) {
        this.idPartita = idPartita;
    }

    public String getGruppoID() {
        return gruppoID;
    }

    public void setGruppoID(String gruppoID) {
        this.gruppoID = gruppoID;
    }

    public int getPasso() {
        return passo;
    }

    public void setPasso(int passo) {
        this.passo = passo;
    }

    public int getParole_indovinate() {
        return parole_indovinate;
    }

    public void setParole_indovinate(int parole_indovinate) {
        this.parole_indovinate = parole_indovinate;
    }

    public Boolean getAttiva() {
        return isAttiva;
    }

    public void setAttiva(Boolean attiva) {
        isAttiva = attiva;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partita turno = (Partita) o;
        return passo == turno.passo && parole_indovinate == turno.parole_indovinate && gruppoID.equals(turno.gruppoID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gruppoID, passo, parole_indovinate);
    }

    @Override
    public String toString() {
        return "Turno{" +
                "gruppo='" + gruppoID + '\'' +
                ", passo=" + passo + '\'' +
                ", parole indovinate=" + parole_indovinate +
                '}';
    }
}
