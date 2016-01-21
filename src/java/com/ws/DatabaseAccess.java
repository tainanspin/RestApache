/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author G713
 */
public class DatabaseAccess
{
    Connection connection = null;

    public DatabaseAccess(String url, String driver, String user_name, String password)
    {
        /*String url = "jdbc:mysql://192.168.11.31:3306/";
        String driver = "com.mysql.jdbc.Driver";
        String database = "VIELIB";
        String user_name = "vuser";
    	String password = "v123";*/
        try
        {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, user_name, password);
        }
        catch (ClassNotFoundException e) 
        {
            System.out.println("錯誤發生在沒有驅動");
            e.printStackTrace();
	}
        catch (SQLException e) 
        {
            e.printStackTrace();
	}
    }
    
    public void disconnect() throws SQLException
    {
        connection.close();
    }
    
    public Connection getConnection() 
    {
	return this.connection;
    }
}
