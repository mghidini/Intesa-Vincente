package com.example.intesavincente.model;

import java.util.ArrayList;

public class Gruppo {
    private String ID;
    private String nome;
    private ArrayList<Utente> componenti = new ArrayList<>();

    public Gruppo(){

    }

    public Gruppo(String nome) {
        this.nome = nome;
    }

    public Gruppo(String ID, String  nome, Utente utente) {
        this.ID=ID;
        this.nome=nome;
        componenti.add(utente);
    }

    public Gruppo(String ID, String  nome, ArrayList<Utente> componenti) {
        this.ID=ID;
        this.nome=nome;
        this.componenti = componenti;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Utente> getComponenti() {
        return componenti;
    }

    public void setComponenti(Utente utente) {

    }

    @Override
    public String toString() {
        return "Gruppo{" +
                "nome='" + nome + '\'' +
                ", componenti=" + stampaLista()+
                '}';
    }
        public String stampaLista(){
        String stampa = "";
        for(int i=0;i<componenti.size();i++){
            if(componenti.get(i) != null){
                if(i == 0)
                    stampa = componenti.get(i).toString1();
                else
                    stampa = stampa + ", " + componenti.get(i).toString1();
            }
            else
                return stampa;
        }
        return stampa;
    }


    public String stampaNomeComponenti(){
        String stampa = "";
        System.out.println("Tipo: " + componenti.getClass());
        System.out.println("Size: " + componenti.size());
        for(int i=0;i<componenti.size();i++){
            Utente utente = (Utente) componenti.get(i);
            System.out.println("Utente: " + componenti.get(i));
            System.out.println("Tipo: " + utente.getClass());
            System.out.println("Condizione: " + (componenti.get(i) != null));
            if(componenti.get(i) != null){
                if(i == 0) {
                    System.out.println("Nickname utente if: ");
                    stampa = componenti.get(i).getNickname();

                }
                else {
                    System.out.println("Nickname utente else: ");
                    stampa = stampa + ", " + componenti.get(i).getNickname();

                }
            }
            else {
                System.out.println("Else del primo If");
                return stampa;
            }
        }
        return stampa;
    }
}

