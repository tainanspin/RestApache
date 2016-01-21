/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import com.ds.DS_PSMADP;
import com.ds.DS_TCSTCC;
import com.ds.DS_TCSTCE;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
//import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author G713
 */
@Path("WS_TCS")
public class WS_TCS
{
    DatabaseAccess data;
    Connection connection;
    private DatabaseAccess db;

    @Context
    private UriInfo context;
    
    /*public void setDB(@Context ServletContext sc, @Context HttpServletRequest req)
    {
        db = (DatabaseAccess) sc.getAttribute("db");
        this.db = db;
        this.connection = db.getConnection();
    }*/

    /**
     * Creates a new instance of WS_TCSResource
     */
    public WS_TCS(@Context ServletContext sc)
    {
        db = (DatabaseAccess) sc.getAttribute("db");
        this.connection = db.getConnection();
    }
    
    //@Path("{note}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DS_PSMADP> GetDepartment() 
    throws IOException 
    {
        List<DS_PSMADP> output = new ArrayList<DS_PSMADP>();
        PreparedStatement prepStmt = null;
        try 
        {
            String cSQL = "SELECT ADPDPT, ADPNM2 FROM PSMADP WHERE ADPEDT = ? AND ADPUSE <> ? ORDER BY ADPDPT";
            prepStmt = connection.prepareStatement(cSQL);
            prepStmt.setString(1, "99999999");
            prepStmt.setString(2, "X");
            ResultSet result = prepStmt.executeQuery();
            if (result.next())
            {
                while (result.next())
                {
                    DS_PSMADP dep = new DS_PSMADP();
                    dep.setDepid(result.getString(1));
                    dep.setDepname(result.getString(2));
                    output.add(dep);
                }
            }
        } 
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
        return output;
    }
    
    @Path("/GetLeave")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DS_TCSTCC> GetLeave()
    {
        List<DS_TCSTCC> output = new ArrayList<DS_TCSTCC>();
        PreparedStatement prepStmt = null;
        try 
        {
            String cSQL = "SELECT TCCTCD, TCCTXT FROM TCSTCC ORDER BY TCCTCD";
            prepStmt = connection.prepareStatement(cSQL);
            ResultSet result = prepStmt.executeQuery();
            if (result.next())
            {
                while (result.next())
                {
                    DS_TCSTCC lea = new DS_TCSTCC();
                    lea.setTccid(result.getString(1));
                    lea.setTccname(result.getString(2));
                    output.add(lea);
                }
            }
        } 
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
        return output;
    }
    
    @Path("/GetAccount")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DS_TCSTCE> GetAccount()
    {
        List<DS_TCSTCE> output = new ArrayList<DS_TCSTCE>();
        PreparedStatement prepStmt = null;
        try 
        {
            String cSQL = "SELECT TCEEMP FROM TCSTCE WHERE TCELJD = 0 ORDER BY TCEEMP";
            prepStmt = connection.prepareStatement(cSQL);
            ResultSet result = prepStmt.executeQuery();
            if (result.next())
            {
                while (result.next())
                {
                    DS_TCSTCE acc = new DS_TCSTCE();
                    acc.setEmp(result.getString(1));
                    output.add(acc);
                }
            }
        } 
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
        return output;
    }
    
    @Path("/GetPerLel/{account}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DS_TCSTCE> GetPerLel(@PathParam("account") String account)
    {
        List<DS_TCSTCE> output = new ArrayList<DS_TCSTCE>();
        PreparedStatement prepStmt = null;
        try 
        {
            String cSQL = "SELECT TCENAM, TCEBTD, TCEALL, TCETKB, TCETKT FROM TCSTCE WHERE TCELJD = 0 AND TCEEMP = ?";
            prepStmt = connection.prepareStatement(cSQL);
            prepStmt.setString(1, account);
            ResultSet result = prepStmt.executeQuery();
            while (result.next())
            {
                DS_TCSTCE acc = new DS_TCSTCE();
                acc.setNam(result.getString(1));
                acc.setBtd(result.getString(2));
                acc.setAll(result.getString(3));
                acc.setTkb(result.getString(4));
                acc.setTkt(result.getString(5));
                output.add(acc);
            }
        } 
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
        return output;
    }
}
