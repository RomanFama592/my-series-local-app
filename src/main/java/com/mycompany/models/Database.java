package com.mycompany.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private String _url_db = null;
    private Connection conn = null;
    private PreparedStatement pstmt = null;

    public Database(String url_db) throws SQLException {
        _url_db = "jdbc:sqlite:" + url_db;
    }

    public class Query implements AutoCloseable {
        private String _query = null;
        private Object[] _parameters = null;

        public Query(String query) throws SQLException {
            _query = query;

            conn = connect();
            pstmt = conn.prepareStatement(_query);
        }

        /*
         * parameters: Only intergers & strings
         */
        public Query(String query, Object... parameters) throws SQLException, IllegalArgumentException {
            _query = query;
            _parameters = parameters;

            conn = connect();
            pstmt = conn.prepareStatement(_query);

            for (int index = 0; index < _parameters.length; index++) {
                if (_parameters[index] instanceof String) {
                    pstmt.setString(index + 1, (String) _parameters[index]);
                } else if (_parameters[index] instanceof Integer) {
                    pstmt.setInt(index + 1, (Integer) _parameters[index]);
                } else {
                    throw new IllegalArgumentException(
                            "unsupported data type: " + _parameters[index].getClass().getName());
                }
            }
        }

        public ResultSet executeQuery() throws SQLException {
            return pstmt.executeQuery();
        }

        public int executeUpdate() throws SQLException {
            return pstmt.executeUpdate();
        }

        @Override
        public void close() throws SQLException {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public PreparedStatement getPstmt() {
        return pstmt;
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException err) {
            System.err.println(err);
            err.printStackTrace();
            System.exit(1);
        }

        return DriverManager.getConnection(_url_db);
    }
}
