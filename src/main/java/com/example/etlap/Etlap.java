package com.example.etlap;

public class Etlap {
    private int id;
    private String nev;
    private String leiras;
    private String kategoria;
    private int ar;

    public Etlap(int id, String nev, String leiras,String kategoria, int ar) {
        this.id = id;
        this.nev = nev;
        this.kategoria = kategoria;
        this.leiras = leiras;
        this.ar = ar;
    }

    public int getId() {
        return id;
    }

    public String getNev() {
        return nev;
    }

    public String getLeiras() {
        return leiras;
    }

    public String getKategoria() {
        return kategoria;
    }

    public int getAr() {
        return ar;
    }
}
