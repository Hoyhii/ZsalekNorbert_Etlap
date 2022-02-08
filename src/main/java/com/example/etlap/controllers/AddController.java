package com.example.etlap.controllers;

import com.example.etlap.EtlapDb;
import com.example.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddController extends Controller {
    @FXML
    private TextField inputNev;
    @FXML
    private TextArea inputLeiras;
    @FXML
    private ComboBox inputKategoria;
    @FXML
    private Spinner inputAr;
    private List<Kategoria> kategoriaList;
    private EtlapDb db;

    public void initialize(){
        kategoriaList = new ArrayList<>();
        try {
            db = new EtlapDb();
        } catch (SQLException e) {
            hibaKiir(e);
        }
        try {
            kategoriaList = db.getKategoria();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Kategoria kategoria : kategoriaList){
            inputKategoria.getItems().add(kategoria.getNev());
        }
        inputKategoria.getSelectionModel().selectFirst();
    }
    @FXML
    public void onAdd(ActionEvent actionEvent) {
        String nev = inputNev.getText().trim();
        String leiras = inputLeiras.getText().trim();
        int ar = 0;
        int kategoriaIndex = inputKategoria.getSelectionModel().getSelectedIndex();
        if (nev.isEmpty()){
            alert("A név kötelező");
            return;
        }
        if (leiras.isEmpty()){
            alert("A leírás kötelező");
            return;
        }
        try {
            ar = (int) inputAr.getValue();
        } catch (NullPointerException ex){
            alert("Az ár kötelező");
            return;
        }
        if (kategoriaIndex == -1){
            alert("Kategória kiválasztása köztelező");
            return;
        }

        String kategoriaString = (String) inputKategoria.getSelectionModel().getSelectedItem();
        int kategoriaInt = -1;
        try {
            EtlapDb db = new EtlapDb();
            for (Kategoria kategoria : kategoriaList){
                if(kategoria.getNev().equals(kategoriaString)){
                    kategoriaInt = kategoria.getId();
                    break;
                }
            }
            int siker = db.etlapHozzaadas(nev,leiras, kategoriaInt,ar);
            if (siker == 1){
                alert("Hozzáadás sikeres");
            } else {
                alert("Hozzáadás sikertelen");
            }
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
}
