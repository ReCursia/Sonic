package com.studentsteam.sonic.tools.database;

import com.badlogic.gdx.utils.Array;

import java.sql.*;

public class DatabaseSaves {
    public static synchronized Array<DataItem> getData() {
        Array<DataItem> items = new Array<DataItem>();
        try {
            Class.forName("org.sqlite.JDBC"); //Connect driver
            String url = "jdbc:sqlite:db.db"; //Path to our bd
            Connection con = DriverManager.getConnection(url); //Connecting to our db
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM saves");
                while (rs.next()) {
                    items.add(new DataItem(rs.getFloat(3),rs.getFloat(4),rs.getInt(2),rs.getInt(1)));
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
    public static synchronized void writeData(Array<DataItem> items) {
        try {
            Class.forName("org.sqlite.JDBC"); //Connect driver
            String url = "jdbc:sqlite:db.db"; //Path to our bd
            Connection con = DriverManager.getConnection(url); //Connecting to our db
            try {
                Statement stmt = con.createStatement();
                stmt.executeUpdate("DELETE FROM saves"); //Удаляем все записи
                for(DataItem item:items) {
                    PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO saves VALUES (?, ?, ?, ?)");
                    preparedStatement.setInt(1,item.index);
                    preparedStatement.setInt(2,item.rings);
                    preparedStatement.setFloat(3,item.x);
                    preparedStatement.setFloat(4,item.y);
                    preparedStatement.executeUpdate();
                }
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
