/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

import java.sql.SQLException;
import java.util.*;

import com.mycompany.models.SerieElement.SerieNotExists;

/**
 *
 * @author famar
 */
public class SerieManager {
    private String urlDb = null;
    private Database database = null;
    private ArrayList<SerieElement> serieList = new ArrayList<SerieElement>();

    final public String tableName = "series";

    private final String SerieTable = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
            + "id INTEGER PRIMARY KEY NOT NULL, \n"
            + "title TEXT, \n"
            + "link TEXT, \n"
            + "chapter INTEGER DEFAULT 1, \n"
            + "season INTEGER DEFAULT 1, \n"
            + "state INTEGER DEFAULT " + SerieStates.pending + ")";

    public SerieManager() {
        this.urlDb = "jdbc:sqlite::memory:";

        try {
            database = new Database(this.urlDb);

            try (var query = database.new Query(SerieTable)) {
                query.executeUpdate();
            }

            loadSeries();

        } catch (SQLException err) {
            System.err.println(err);
            err.printStackTrace();
        }
    }

    public SerieManager(String urlDb) {
        this.urlDb = urlDb;
        try {
            database = new Database(this.urlDb);

            try (var query = database.new Query(SerieTable)) {
                query.executeUpdate();
            }

            loadSeries();

        } catch (SQLException err) {
            System.err.println(err);
            err.printStackTrace();
        }
    }

    public ArrayList<SerieElement> getSeries() {
        return serieList;
    }

    public SerieElement addSerie() {
        try {
            var serie = new SerieElement(tableName, database);
            serieList.add(serie);
            return serie;
        } catch (SQLException err) {
            System.err.println(err);
            err.printStackTrace();
            return null;
        }
    }

    private SerieElement getSerie(int id) {
        try {
            var serie = new SerieElement(tableName, database, id);
            serieList.add(serie);
            return serie;
        } catch (SQLException err) {
            System.err.println(err);
            err.printStackTrace();
            return null;
        } catch (SerieNotExists err) {
            System.err.println(err);
            err.printStackTrace();
            return null;
        }
    }

    private void loadSeries() {
        try (var query = database.new Query("SELECT * FROM " + tableName)) {
            var rs = query.executeQuery();

            while (rs.next()) {
                var id = rs.getInt("id");
                getSerie(id);
            }

        } catch (SQLException err) {
            System.err.println(err);
            err.printStackTrace();
        }
    }

    public static void main(String args[]) {
        SerieManager seriedb = new SerieManager("series.db");
        seriedb.addSerie();
        seriedb.addSerie();
        seriedb.addSerie();
        seriedb.addSerie();
        seriedb.addSerie();
        seriedb.addSerie();
        seriedb.addSerie();
        for (var serie : seriedb.getSeries()) {
            if (seriedb.getSeries().indexOf(serie) % 2 == 0) {
                serie.title = "pepe";
                serie.save();
                System.out.println(serie.toString());
            } else {
                serie.delete();
            }
        }
    }
}
