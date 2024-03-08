package com.mycompany.models;

import java.sql.SQLException;
import java.text.MessageFormat;

public class SerieElement {
    private final int id;
    private Database database = null;
    private String tableName = null;
    public String title = "";
    public String link = "";
    public int chapter = 1;
    public int season = 1;
    public SerieStates state = SerieStates.pending;

    class SerieNotExists extends Exception {
    }

    public SerieElement(String tableName, Database database, int id) throws SQLException, SerieNotExists {
        this.id = id;
        this.database = database;
        this.tableName = tableName;

        try (var query = this.database.new Query("SELECT * FROM " + tableName + " WHERE id = ?", id)) {
            var rs = query.executeQuery();
            this.title = rs.getString("title");
            this.link = rs.getString("link");
            this.chapter = rs.getInt("chapter");
            this.season = rs.getInt("season");
            this.state = SerieStates.values()[rs.getInt("state")];
        }
    }

    public SerieElement(String tableName, Database database) throws SQLException {
        this.database = database;
        this.tableName = tableName;

        try (var query = this.database.new Query(
                "INSERT INTO " + tableName + " (title, link, chapter, season, state) VALUES (?,?,?,?,?)", title, link,
                chapter,
                season, state.value)) {
            query.executeUpdate();
            this.id = database.getPstmt().getGeneratedKeys().getInt(1);
        }
    }

    public int getId() {
        return id;
    }

    public boolean delete() {
        try (var query = database.new Query(
                "DELETE FROM " + tableName + " WHERE id = ?;",
                this.id)) {
            query.executeUpdate();
            return true;
        } catch (SQLException err) {
            System.err.println(err);
            err.printStackTrace();
            return false;
        }
    }

    public boolean save() {
        try (var query = database.new Query(
                "UPDATE " + tableName + " SET title = ?, link = ?, season = ?, chapter = ? WHERE id = ?", title, link,
                season,
                chapter, id)) {
            query.executeUpdate();
            return true;
        } catch (SQLException err) {
            System.err.println(err);
            err.printStackTrace();
            return false;
        } catch (IllegalArgumentException err) {
            System.err.println("Arguments in sql query are invalid");
            err.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return MessageFormat.format("id: {0}, title: {1}, link: {2}, chapter: {3}, season: {4}, state: {5}",
                this.id,
                this.title,
                this.link,
                this.chapter,
                this.season,
                this.state);
    }

    public Object toArray() {
        String[] arrayValues = { String.valueOf(this.id), this.title, this.link, String.valueOf(this.chapter),
                String.valueOf(this.season), this.state.name() };
        return arrayValues;
    }

}
