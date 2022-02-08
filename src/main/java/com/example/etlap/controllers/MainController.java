package com.example.etlap.controllers;

import com.example.etlap.Etlap;
import com.example.etlap.EtlapDb;
import com.example.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class MainController extends Controller {

    @FXML
    private Spinner inputSzazalek;
    @FXML
    private Spinner inputFt;
    @FXML
    private ChoiceBox<String> etlapSzur;
    @FXML
    private TableView<Etlap> etlapTable;
    @FXML
    private TableColumn colNev;
    @FXML
    private TableColumn colAr;
    @FXML
    private TableColumn colKategoria;
    @FXML
    private TableColumn colKategoriaNev;
    @FXML
    private TableView<Kategoria> kategoriaTable;
    @FXML
    private TextField leirasTXT;


    private EtlapDb db;

    public void initialize(){
        colNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        colKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        colAr.setCellValueFactory(new PropertyValueFactory<>("ar"));
        colKategoriaNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        try {
            db = new EtlapDb();
            etlapFeltoltes();
            kategoriaListaFeltolt();
        } catch (SQLException e) {
            hibaKiir(e);
        }
        etlapSzur.getSelectionModel().selectFirst();
    }

    @FXML
    public void etelHozzadas(ActionEvent actionEvent) {
        try {
            Controller hozzaadas = ujablak("add-view.fxml", "Étel hozzáadása", 400, 500);
            hozzaadas.getStage().setOnCloseRequest(event -> etlapFeltoltes());
            hozzaadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    public void kategoriaAdd(ActionEvent actionEvent) {
        try {
            Controller hozzaadas = ujablak("kategoria-view.fxml", "Kategória hozzáadása", 250, 150);
            hozzaadas.getStage().setOnCloseRequest(event -> kategoriaListaFeltolt());
            hozzaadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void etelTorles(ActionEvent actionEvent) {
        int jeloltEtel = etlapTable.getSelectionModel().getSelectedIndex();
        if (jeloltEtel == -1){
            alert("Elöbb válassz valamit!");
            return;
        }
        Etlap torlendoEtel = (Etlap) etlapTable.getSelectionModel().getSelectedItem();
        if (!confirm("Biztos törölni szeretnéd: "+torlendoEtel.getNev()+"?")){
            return;
        }
        try {
            db.etelTorles(torlendoEtel.getId());
            alert("Sikeres");
            etlapFeltoltes();
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }
    public void kategoriaTorles(ActionEvent actionEvent) {
        int selectedIndex = kategoriaTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("Elöbb válassz valamit!");
            return;
        }
        Kategoria jeloltKategoria = (Kategoria) kategoriaTable.getSelectionModel().getSelectedItem();
        if (!confirm("Biztos törölni szeretnéd: "+jeloltKategoria.getNev()+"?")){
            return;
        }
        try {
            db.kategoriaTorles(jeloltKategoria.getId());
            alert("Sikeres törlés");
            kategoriaListaFeltolt();
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    public void forintEmeles(ActionEvent actionEvent) {
        int jelolt = etlapTable.getSelectionModel().getSelectedIndex();
        int ft = 0;
        try {
            ft = (int) inputFt.getValue();
        } catch (Exception e){
            alert("A mező kitöltése kötelező számmal");
            return;
        }
        if(ft < 50 || ft > 3000){
            alert("Csak 50 és 3000 közti értéket adhatsz meg");
            return;
        }
        if (!confirm("Biztos emeled az árat?")){
            return;
        }
        if(jelolt == -1){
            try {
                db.fixToAllEmeles(ft);
                alert("Sikeres emelés");
                etlapFeltoltes();
            } catch (SQLException e) {
                hibaKiir(e);
            }
        } else {
            Etlap emelendoEtel = (Etlap) etlapTable.getSelectionModel().getSelectedItem();
            try {
                db.fixToOneEmeles(ft, emelendoEtel.getId());
                alert("Sikeres Emelés");
                etlapFeltoltes();
            } catch (SQLException e) {
                hibaKiir(e);
            }
        }
    }

    public void szazalekosEmeles(ActionEvent actionEvent) {
        int jelolt = etlapTable.getSelectionModel().getSelectedIndex();
        int szazalek = 0;
        try {
            szazalek = (int) inputSzazalek.getValue();
        } catch (Exception e){
            alert("A mező kitöltése kötelező és csak számot fogad el");
            return;
        }
        if(szazalek < 5 || szazalek > 50){
            alert("5 és 50 között lehet az értéke");
            return;
        }
        if (!confirm("Biztos emeled az árat?")){
            return;
        }
        if(jelolt == -1){
            try {
                db.percentageToAllEmeles(szazalek);
                alert("Sikeres");
                etlapFeltoltes();
            } catch (SQLException e) {
                hibaKiir(e);
            }
        } else {
            Etlap etel = (Etlap) etlapTable.getSelectionModel().getSelectedItem();
            try {
                db.percentageToOneEmeles(szazalek, etel.getId());
                alert("Sikeres");
                etlapFeltoltes();
            } catch (SQLException e) {
                hibaKiir(e);
            }
        }
    }

    private void etlapFeltoltes(){
        try {
            List<Etlap> etlapList = db.getEtlap();
            etlapTable.getItems().clear();
            for(Etlap etlap: etlapList){
                etlapTable.getItems().add(etlap);
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    private void kategoriaListaFeltolt(){
        try {
            List<Kategoria> kategoriaList = db.getKategoria();
            kategoriaTable.getItems().clear();
            etlapSzur.getItems().clear();
            etlapSzur.getItems().add("összes");
            for(Kategoria kategoria: kategoriaList){
                kategoriaTable.getItems().add(kategoria);
                etlapSzur.getItems().add(kategoria.getNev());
            }
            etlapSzur.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    private void szurtListaFeltoltes(String szures){
        try {
            List<Etlap> etlapList = db.getSzur(szures);
            etlapTable.getItems().clear();
            for(Etlap etlap: etlapList){
                etlapTable.getItems().add(etlap);
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    public void item(MouseEvent mouseEvent) {
        Etlap jeloltEtel = etlapTable.getSelectionModel().getSelectedItem();
        leirasTXT.setText(jeloltEtel.getLeiras());
    }

    public void szures(ActionEvent actionEvent) {
        String szur = etlapSzur.getSelectionModel().getSelectedItem();
        if (szur == "összes"){
            etlapFeltoltes();
            return;
        } else {
            szurtListaFeltoltes(szur);
        }
    }
}