package com.example.etlap.controllers;

import com.example.etlap.EtlapDb;
import com.example.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class KategoriaController extends Controller {
    @FXML
    private TextField inputKategoriaNev;
    private List<Kategoria> kategoriaList;
    private EtlapDb db;

    public void onAddKategoria(ActionEvent actionEvent) {
        String nev = inputKategoriaNev.getText();
        boolean bennevan = false;

        try {
            db = new EtlapDb();
        } catch (SQLException e) {
            hibaKiir(e);
            return;
        }

        try {
            kategoriaList = db.getKategoria();
        } catch (SQLException e) {
            hibaKiir(e);
        }

        for (Kategoria kategoria : kategoriaList)
        {
            if(kategoria.getNev().equalsIgnoreCase(nev)){
                bennevan = true;
                break;
            }
        }

        if(bennevan){
            alert("Már benne van!");
            return;
        }
        try {
            int siker = db.kategoriaHozzaAdas(nev);
            if (siker == 1){
                alert("Sikeres!");
            } else {
                alert("Sikertelen!");
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }

    }
}
