/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author G713
 */
public class ServiceListener implements ServletContextListener, HttpSessionListener
{
    DatabaseAccess db;
    String url, driver, database, user_name, password;
    ServletContext sc;
    
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        sc = sce.getServletContext();
        url = sc.getInitParameter("db_url");
        driver = sc.getInitParameter("db_driver");
        database = sc.getInitParameter("db_database");
        user_name = sc.getInitParameter("db_username");
    	password = sc.getInitParameter("db_password");
        db = new DatabaseAccess(url + database, driver, user_name, password);
        sc.setAttribute("db", db);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent hse)
    {
        //db = new DatabaseAccess(url + database, driver, user_name, password);
        //sc.setAttribute("db", db);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse)
    {
        /*try 
        {
            db.disconnect();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ServiceListener.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
