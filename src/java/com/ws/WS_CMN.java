/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author G713
 */
@Path("CMNservice")
public class WS_CMN
{
    DatabaseAccess data;
    Connection connection;
    private DatabaseAccess db;

    @Context
    private UriInfo context;

    public void setDB(@Context ServletContext sc, @Context HttpServletRequest req)
    {
        db = (DatabaseAccess) sc.getAttribute("db");
        this.db = db;
        this.connection = db.getConnection();
    }
    
    public WS_CMN(@Context ServletContext sc)
    {
        db = (DatabaseAccess) sc.getAttribute("db");
        this.connection = db.getConnection();
    }
    
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void LoginCheck(
                    @FormParam("account") String id,
                    @FormParam("password") String pwd,
                    @Context HttpServletResponse servletResponse
    ) 
    throws IOException 
    {
        PreparedStatement prepStmt = null;
        try 
        {
            String cSQL = "SELECT * FROM SYM3 WHERE SYM3S1 = ? AND SYM302 = ?";
            prepStmt = connection.prepareStatement(cSQL);
            prepStmt.setString(1, id);
            prepStmt.setString(2, pwd);
            ResultSet result = prepStmt.executeQuery();
            if (!result.next())
            {
                servletResponse.sendRedirect("/RestApache/Pages/sign-in-error.html");
            }
            else
            {
                servletResponse.sendRedirect("/RestApache/Pages/index.html");
            }
        } 
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
    }
}
