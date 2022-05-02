/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author DSetiawan
 */
public class MySQLConnection {
    public Connection connection;
    public Statement stmt;
    public Connection connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to Connect. " + e);
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperativa", "dicky","dickys08");
            stmt = connection.createStatement();
            System.out.println("Successfully Connected to Database");
        } catch (SQLException e){
            System.out.println("Database Not Found. " + e);
        }
        
        return connection;
    }
    
    public static void main(String[] args) {
        MySQLConnection connection = new MySQLConnection();
        connection.connect();
    }
}
