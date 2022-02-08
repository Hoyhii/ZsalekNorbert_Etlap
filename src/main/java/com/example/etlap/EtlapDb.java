package com.example.etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtlapDb {
    Connection conn;

    public EtlapDb() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/etlapdb","root", "");
    }
    public List<Etlap> getEtlap() throws SQLException {
        List<Etlap> etlapLista = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM etlap INNER JOIN kategoria ON kategoria.id = etlap.kategoria_id;";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()){
            int id = result.getInt("id");
            String nev = result.getString("etlap.nev");
            String leiras = result.getString("leiras");
            String kategoria = result.getString("kategoria.nev");
            int ar = result.getInt("ar");
            Etlap etel = new Etlap(id, nev,leiras, kategoria, ar);
            etlapLista.add(etel);
        }
        return etlapLista;
    }
    public List<Etlap> getSzur(String szures) throws SQLException {
        List<Etlap> etlapList = new ArrayList<>();
        String sql = "SELECT * FROM etlap JOIN kategoria ON kategoria.id = etlap.kategoria_id WHERE kategoria.nev = ?;";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, szures);
        ResultSet RS = stmt.executeQuery();
        while (RS.next()){
            int id = RS.getInt("id");
            String nev = RS.getString("etlap.nev");
            String leiras = RS.getString("leiras");
            String kategoria = RS.getString("kategoria.nev");
            int ar = RS.getInt("ar");
            Etlap etel = new Etlap(id, nev,leiras, kategoria, ar);
            etlapList.add(etel);
        }
        return etlapList;
    }
    public List<Kategoria> getKategoria() throws SQLException {
        List<Kategoria> kategoriaLista = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM kategoria;";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()){
            int id = result.getInt("id");
            String nev = result.getString("nev");
            Kategoria kategoria = new Kategoria(id, nev);
            kategoriaLista.add(kategoria);
        }
        return kategoriaLista;
    }
    public int etlapHozzaadas(String nev, String leiras, int kategoria, int ar) throws SQLException {
        String sql = "INSERT INTO etlap(nev, leiras, ar, kategoria_id) VALUES (?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,nev);
        stmt.setString(2,leiras);
        stmt.setInt(3,ar);
        stmt.setInt(4,kategoria);
        return stmt.executeUpdate();
    }
    public int kategoriaHozzaAdas(String nev) throws SQLException {
        String sql = "INSERT INTO kategoria(nev) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,nev);
        return stmt.executeUpdate();
    }
    public boolean etelTorles(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }
    public boolean kategoriaTorles(int id) throws SQLException {
        String sql = "DELETE FROM kategoria WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }
    public int fixToAllEmeles(int szazalek) throws SQLException {
        String sql = "UPDATE etlap SET etlap.ar = etlap.ar + ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, szazalek);
        return stmt.executeUpdate();
    }
    public int fixToOneEmeles(int szazalek, int id) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar + ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, szazalek);
        stmt.setInt(2, id);
        return stmt.executeUpdate();
    }
    public int percentageToOneEmeles(int szazalek, int id) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar * (? / 100 + 1) WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, szazalek);
        stmt.setInt(2, id);
        return stmt.executeUpdate();
    }
    public int percentageToAllEmeles(int szazalek) throws SQLException {
        String sql = "UPDATE etlap SET etlap.ar = etlap.ar * (? / 100 + 1) ";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, szazalek);
        return stmt.executeUpdate();
    }
}
