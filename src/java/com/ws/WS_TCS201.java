/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.*;
import org.glassfish.jersey.server.JSONP;




/**
 * REST Web Service
 *
 * @author G718
 */
@Path("WS_TCS201")
public class WS_TCS201 {
    
    DatabaseAccess data;
    Connection connection;
    private DatabaseAccess db;
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of WS_TCS201
     */
    public WS_TCS201(@Context ServletContext sc) {
        db = (DatabaseAccess) sc.getAttribute("db");
        this.connection = db.getConnection();
    }

    /**
     * Retrieves representation of an instance of com.ws.WS_TCS201
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of WS_TCS201
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    @Path("/GetID/{com}")
    @JSONP(queryParam="callback")
    @GET
    @Produces({"application/x-javascript"})
    public String GetID(@QueryParam("callback") String callback,@PathParam("com") String com)
    {
        //JOptionPane.showMessageDialog(null, "向右走", "Which way?", JOptionPane.INFORMATION_MESSAGE );
        JSONObject obj1 = new JSONObject();
        LinkedList l1 = new LinkedList();
        //JSONArray l1 = new JSONArray();
        
        PreparedStatement prepStmt = null;
        
        DateFormat day = new SimpleDateFormat("yyyyMMdd");
        String tmpday=day.format(new java.util.Date());
        
        try 
        {
            String cSQL= "SELECT tceemp,tcenam FROM TCSTCE " +
                         "WHERE tcecom = ? AND ( tceljd=0 OR tceljd + 100 > \"" + tmpday + "\" ) " +
                         "ORDER BY tceemp,tcecom ";
            prepStmt = connection.prepareStatement(cSQL);
            prepStmt.setString(1, com);
            ResultSet result = prepStmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            while (result.next())
            {
                LinkedHashMap m1 = new LinkedHashMap();
                for(int j = 1; j <= numcols; j++)
                {
                    Object obj = result.getObject(j);
                    m1.put(rsmd.getColumnName(j).toString(),obj.toString());
                }
                l1.add(m1);
            }
            obj1.put("record", l1);
        } 
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            prepStmt = null;
            e.printStackTrace();
        }
        return obj1.toString();
    }
    
    @Path("/GetTCD")
    @JSONP(queryParam="callback")
    @GET
    @Produces({"application/x-javascript"})
    public String GetTCD(@QueryParam("callback") String callback)
    {
        
        JSONObject obj1 = new JSONObject();
        LinkedList l1 = new LinkedList();
        //JSONArray l1 = new JSONArray();
        
        PreparedStatement prepStmt = null;
        
        try 
        {
            String cSQL= "SELECT tcctcd,CONCAT(tcctcd,\" - \",trim(tcctxt)) AS name FROM TCSTCC " +
                         "WHERE tcctcd NOT IN (\"A\",\"L\",\"N\",\"J\",\"R\",\"E\") " +
                         "ORDER BY tcctcd ";
            prepStmt = connection.prepareStatement(cSQL);
            ResultSet result = prepStmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            while (result.next())
            {
                LinkedHashMap m1 = new LinkedHashMap();
                for(int j = 1; j <= numcols; j++)
                {
                    Object obj = result.getObject(j);
                    m1.put(rsmd.getColumnName(j).toString(),obj.toString());
                }
                l1.add(m1);
            }
            obj1.put("record",l1);
        } 
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            prepStmt = null;
            e.printStackTrace();
        }
        return obj1.toString();
    }
    
    @Path("/GetDETAIL/{com}/{account}")
    @JSONP(queryParam="callback")
    @GET
    @Produces({"application/x-javascript"})
    public String GetDETAIL(@QueryParam("callback") String callback,@PathParam("com") String com,@PathParam("account") String account)
    {
        
        JSONObject obj1 = new JSONObject();
        LinkedList l1 = new LinkedList();
        
        PreparedStatement prepStmt = null;
        
        try 
        {
            //基本資料
            
            String cSQL = " SELECT tceemp, tceapd, tceall, tcetkb, tcetkt FROM TCSTCE " +
                          " WHERE tcecom= ? AND tceemp= ? " +
                          " ORDER BY tceapd DESC";
            prepStmt = connection.prepareStatement(cSQL);
            prepStmt.setString(1, com);
            prepStmt.setString(2, account);
            ResultSet result = prepStmt.executeQuery();
            
            if (result.next())
            {
                LinkedHashMap m1 = new LinkedHashMap();
                
                Object obj = result.getObject(2);
                
                //到職日
                m1.put("arrive",obj.toString().substring(0,4) + "/" + obj.toString().substring(4,6) + "/" + obj.toString().substring(6,8) );
                
                //起算日
                if (Integer.parseInt(obj.toString()) < 20100913)
                {
                    m1.put("start","01/01");
                }
                else
                {
                    m1.put("start",obj.toString().substring(4,6) + "/" + obj.toString().substring(6,8) );
                }
                
                //核幾天數
                obj = result.getObject(3);
                m1.put("allday",obj.toString());
                        
                l1.add(m1);
            }
            obj1.put("base", l1);

            //明細資料
            result.close();
            l1.clear();
            
            cSQL =  " SELECT tch.tchyer,CONCAT(tch.tchtcd,\" - \",tcc.tcctxt) AS tcdnam,tch.tchdst,tch.tchded,tch.tchday,tch.tchlst,tch.tchtxt,tch.tchtcd,tch.tchtck, " +
                    "        IFNULL(tchgrp.maxtck,\"\") AS maxtck, IFNULL(tchgrp.maxdst,0) AS maxdst " +
                    " FROM TCSTCH AS tch " +
                    " LEFT JOIN (SELECT DISTINCT tcecom,tceemp,tcenam FROM TCSTCE) AS tce " +
                    "        ON tcecom=tchcom AND tce.tceemp=tch.tchemp " +
                    " LEFT JOIN (SELECT tcctcd, tcctxt FROM TCSTCC ) AS tcc " +
                    "        ON tcc.tcctcd=tch.tchtcd " +
                    " LEFT JOIN ( SELECT tchcom,tchemp,tchyer,max(tchtck) AS maxtck,max(tchdst) AS maxdst FROM TCSTCH " +
                    "             WHERE tchtcd not in (\"B\",\"T\",\"M\",\"F\",\"W\") " +
                    "             GROUP BY tchcom,tchemp,tchyer ) AS tchgrp " +
                    "        ON tch.tchcom = tchgrp.tchcom AND tch.tchemp = tchgrp.tchemp " +
                    "       AND tch.tchyer = tchgrp.tchyer " +
                    " WHERE tch.tchcom= ? AND tch.tchemp= ? " +
                    "   AND tcc.tcctcd NOT IN (\"A\",\"L\",\"R\",\"J\",\"N\") " +
                    "   AND tch.tchmrk=\" \" AND tch.tchyer >= 2014 " +
                    " ORDER BY tch.tchemp,tch.tchdst DESC ";
            //"       tchmrk=\" \" AND tchyer >= CONV( SUBSTR(NOW( ),1,4),10,10) -1 " +
            prepStmt = connection.prepareStatement(cSQL);
            prepStmt.setString(1, com);
            prepStmt.setString(2, account);
            result = prepStmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            int numcols = rsmd.getColumnCount();
            
            while (result.next())
            {
                LinkedHashMap m1 = new LinkedHashMap();
                for(int j = 1; j <= numcols; j++)
                {
                    Object obj = result.getObject(j);
                    m1.put(rsmd.getColumnName(j).toString(),obj.toString());
                }
                Object obj = result.getObject("tchtcd");
                String chk1=obj.toString();
                obj = result.getObject("tchtck"); 
                String chk2=obj.toString();
                obj = result.getObject("tchdst"); 
                String chk3=obj.toString();
                obj = result.getObject("maxdst"); 
                String chk4=obj.toString();
                if ( (( chk1.equals("M") || chk1.equals("F") || chk1.equals("W") || chk1.equals("B") || chk1.equals("T") ) && chk2.equals("-")) 
                  || ( !chk1.equals("M") && !chk1.equals("F") && !chk1.equals("W") && !chk1.equals("B") && !chk1.equals("T") && chk3.equals(chk4) ) )
                {
                    m1.put("edit","Y");
                }
                else
                {
                    m1.put("edit","N");
                }
                l1.add(m1);
            }
            obj1.put("detail", l1);
        } 
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            prepStmt = null;
            e.printStackTrace();
        }
        return obj1.toString();
    }
    
    @Path("/Act")
    @JSONP(queryParam="callback")
    @POST
    @Produces({"application/x-javascript"})
    public String Act(@QueryParam("callback") String callback,
                      @FormParam("state") String state,
                      @FormParam("com") String com,
                      @FormParam("emp") String emp,
                      @FormParam("tcd") String tcd,
                      @FormParam("year") String year,
                      @FormParam("strdate") String strdate,
                      @FormParam("enddate") String enddate,
                      @FormParam("days") String days,
                      @FormParam("comment") String comment,
                      @FormParam("oldstrdate") String oldstrdate,
                      @Context HttpServletResponse servletResponse)
    {
        
        JSONObject obj1 = new JSONObject();
        LinkedList l1 = new LinkedList();
        String cSQL="";
        int yeartkb=0, yeartkt=0, cnttkb=0, cnttkt=0 ;
        float cntdays=0, allowdays=0, tmplst=0;
        String tmptck="-";
        String msg = "";
        String yearStartDate = "";
        
        PreparedStatement prepStmt = null;
              
        try 
        {
            if ( !state.equals("D") && (msg.equals("")) )
            { 
                //資料日期判斷基準
                if ( state.equals("I") )
                {
                    oldstrdate="0";
                }
                
                //基本資料抓取
                cSQL = " SELECT tceemp, tceapd, tceall, tcetkb, tcetkt FROM TCSTCE " +
                       " WHERE tcecom= ? AND tceemp= ? " +
                       " ORDER BY tceapd DESC";
                prepStmt = connection.prepareStatement(cSQL);
                prepStmt.setString(1, com);
                prepStmt.setString(2, emp);
                ResultSet result = prepStmt.executeQuery();

                if (result.next())
                {
                    //核幾天數
                    Object obj = result.getObject(3);
                    allowdays = Integer.parseInt(obj.toString());
                    obj = result.getObject(4);
                    yeartkb = Integer.parseInt(obj.toString());
                    obj = result.getObject(5);
                    yeartkt = Integer.parseInt(obj.toString());

                    //跨年度檢核***************************************************************
                    if (msg.equals("")) {
                        obj = result.getObject(2);
                        yearStartDate = obj.toString();
                        if (Integer.parseInt(yearStartDate) <= 20100913) {
                            //2010.9.13以前到職 是以01/01起算
                            if ((Integer.parseInt(strdate.substring(4))<Integer.parseInt("0101")) && 
                                (Integer.parseInt(enddate.substring(4))>=Integer.parseInt("0101")))
                            {
                                msg = "C";
                            } 
                        } else {
                            //2010.9.13以後到職 要以到職日起算
                            yearStartDate = year + yearStartDate.substring(4);  
                            if ((Integer.parseInt(strdate)<Integer.parseInt(yearStartDate)) && (Integer.parseInt(enddate)>=Integer.parseInt(yearStartDate)))
                            {
                                msg = "C";
                            }    
                        }
                    }
                }
                else 
                {
                    msg = "H";
                }
                
                //日期檢核
                if  ((Integer.parseInt(strdate) > Integer.parseInt(enddate)) && (msg.equals("")))
                {
                    msg = "A";
                    //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorA.html");
                    //return "N";
                }
            
                //一個日期僅可做一個考勤代碼*****************************************************
                if ((state.equals("I") || ( state.equals("U") && !strdate.equals(oldstrdate) ))  && (msg.equals("")))
                {
                    cSQL =  "SELECT * FROM TCSTCH " + 
                            "WHERE TCHCOM=? AND TCHEMP=? AND " + 
                            "      ?<=TCHDED AND ?>=TCHDST ";
                    prepStmt = connection.prepareStatement(cSQL);
                    prepStmt.setString(1, com);
                    prepStmt.setString(2, emp);
                    //prepStmt.setString(3, tcd);
                    prepStmt.setString(3, strdate);
                    prepStmt.setString(4, enddate);
                    result = prepStmt.executeQuery();
                    if ( result.next() )
                    {
                        msg = "B";
                        //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorB.html");
                        //return "N";
                    }
                }
            
                //跨年度檢核************************************************************* 
                /*
                if ((Integer.parseInt(strdate.substring(0,4)) != Integer.parseInt(enddate.substring(0,4))) && (msg.equals("")))
                {
                    msg = "C:日期跨度不允許跨年度";
                    //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorC.html");
                    //return "N";
                }*/
                            
                //實際天數<=迄日-起日
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
            
                c1.setTime(sdf.parse(strdate));
                c2.setTime(sdf.parse(enddate));
                cntdays = c2.get(Calendar.DATE) - c1.get(Calendar.DATE) + 1;
                if ((Integer.parseInt(days) > cntdays) && (msg.equals("")))
                {
                    msg = "E";
                    //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorE.html");
                    //return "N";
                }
            
                //機票補助須確認可用次數
                if ( tcd.equals("B") || tcd.equals("T") )
                {
                    cSQL =  " SELECT SUM(CASE WHEN tchtcd = \"B\" THEN 1 ELSE 0 END ) AS sumtkb," +
                            "        SUM(CASE WHEN tchtcd = \"T\" AND tchtck = \"A\" THEN 1 WHEN tchtcd = \"T\" AND tchtck = \"B\" THEN 2 ELSE 0 END ) AS sumtkt " +
                            " FROM TCSTCH " +
                            " WHERE tchcom = ? AND tchemp = ? AND tchyer = ? AND tchdst <> ? AND tchtcd IN (\"B\",\"T\") AND tchtck NOT IN (\"-\",\"0\") ";
                    prepStmt = connection.prepareStatement(cSQL);
                    prepStmt.setString(1, com);
                    prepStmt.setString(2, emp);
                    prepStmt.setString(3, year);
                    prepStmt.setString(4, strdate);
                    result = prepStmt.executeQuery();
                    if ( result.next())
                    {
                        Object obj = result.getObject(1);
                        if ( obj != null )
                        {
                            cnttkb = Integer.parseInt(obj.toString());
                        }
                        obj = result.getObject(2);
                        if ( obj != null )
                        {
                            cnttkt = Integer.parseInt(obj.toString());
                        }
                    }
                    
                    if ( cnttkb > yeartkb || cnttkt > yeartkt )
                    {
                        tmptck = "0";
                    }
                }
                else if ( tcd.equals("M") && (msg.equals("")))
                {
                    cSQL =  " SELECT * FROM TCSTCH " +
                            " WHERE tchcom = ? AND tchemp= ? AND tchtcd = ? AND tchtck NOT IN (\"-\",\"X\") ";
                    prepStmt = connection.prepareStatement(cSQL);
                    prepStmt.setString(1, com);
                    prepStmt.setString(2, emp);
                    prepStmt.setString(3, tcd);
                    result = prepStmt.executeQuery();
                    if ( result.next() )
                    {
                        msg = "F";
                        //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorF.html");
                        //return "N";
                    }
                }
                else if ( tcd.equals("F") )
                {
                    cSQL =  " SELECT COUNT(*) AS times FROM TCSTCH " +
                            " WHERE tchcom = ? AND tchemp= ? AND tchyer = ? AND tchtcd = ? AND tchtck NOT IN (\"-\",\"X\") ";
                    prepStmt = connection.prepareStatement(cSQL);
                    prepStmt.setString(1, com);
                    prepStmt.setString(2, emp);
                    prepStmt.setString(3, year);
                    prepStmt.setString(4, tcd);
                    result = prepStmt.executeQuery();
                    if ( result.next() )
                    {
                        Object obj = result.getObject(1);
                        if ( obj != null && Integer.parseInt(obj.toString()) == 1 )
                        {
                            tmptck = "X";
                        } 
                    }
                }
            
                //陪產假最多五天********************************************************
                if ( tcd.equals("C") && (msg.equals("")))
                {                
                    cSQL =  " SELECT tchtcd, sum(tchday) AS tchday FROM TCSTCH " +
                            " WHERE tchcom = ? AND tchemp = ? " +
                            "   AND tchyer = ? AND tchdst <> ? " +
                            "   AND tchtcd = \"C\" " +
                            " GROUP BY tchemp ";
                    prepStmt = connection.prepareStatement(cSQL);
                    prepStmt.setString(1, com);
                    prepStmt.setString(2, emp);
                    prepStmt.setString(3, year);
                    prepStmt.setString(4, oldstrdate);
                    result = prepStmt.executeQuery();
                    if ( !result.wasNull() )
                    {
                        Object obj = result.getObject(2);
                        cntdays=Integer.parseInt(obj.toString())+Integer.parseInt(days);
                        if (cntdays > 5)
                        {
                            msg = "G";
                            //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorG.html");
                            //return "N";
                        }
                    }
                }
            
                if (msg.equals("")) {
                    //實際天數<=本年度可用天數
                    cSQL =  " SELECT * FROM TCSTCH " +
                            " WHERE tchcom = ? AND tchemp = ? " +
                            "   AND tchyer = ? AND tchdst <> ? "+  
                            " ORDER BY tchdst DESC ";
                    prepStmt = connection.prepareStatement(cSQL);
                    prepStmt.setString(1, com);
                    prepStmt.setString(2, emp);
                    prepStmt.setString(3, year);
                    prepStmt.setString(4, oldstrdate);
                    result = prepStmt.executeQuery();
                    if ( result.next() )
                    {
                        Object obj = result.getObject(9);
                        tmplst=Float.parseFloat(obj.toString());
                        if ( state.equals("U") )
                        {
                            obj = result.getObject(8);
                            tmplst=tmplst + Integer.parseInt(obj.toString());
                        }
                        tmplst=tmplst - Integer.parseInt(days);
                    }
                    else
                    {
                        tmplst=allowdays - Integer.parseInt(days);
                    }

                    if ( tmplst < 0 )
                    {
                        msg = "D";
                        //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorD.html");
                        //return "N";
                    }
                }
            }
            
            if (!msg.equals("")){
                obj1.put("Msg", msg);
                return obj1.toString();
            }
            
            //資料儲存
            DateFormat day = new SimpleDateFormat("yyyyMMdd");
            String upddate=day.format(new java.util.Date());
            DateFormat time = new SimpleDateFormat("HHmmss");
            String updtime=time.format(new java.util.Date());
            
            if ( state.equals("I") )
            {
                cSQL = " INSERT INTO TCSTCH VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                prepStmt = connection.prepareStatement(cSQL);
                prepStmt.setString(1, com);
                prepStmt.setInt(2, Integer.parseInt(year));
                prepStmt.setString(3, emp);
                prepStmt.setString(4, tcd);
                prepStmt.setFloat(5, Float.parseFloat(String.valueOf(allowdays)));
                prepStmt.setInt(6, Integer.parseInt(strdate));
                prepStmt.setInt(7, Integer.parseInt(enddate));
                prepStmt.setFloat(8, Float.parseFloat(days));
                prepStmt.setFloat(9, tmplst);
                prepStmt.setString(10,tmptck);
                prepStmt.setString(11,"");
                prepStmt.setString(12,comment);
                prepStmt.setString(13,"WS_TCS201");
                prepStmt.setString(14,"TEST");
                prepStmt.setInt(15,Integer.parseInt(upddate));
                prepStmt.setInt(16,Integer.parseInt(updtime));
                
                if ( prepStmt.executeUpdate() == 0 )
                {
                    obj1.put("Msg","SI");
                    return obj1.toString();
                    //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorI.html");
                    //return "N";
                }
            }
            else if ( state.equals("U") )
            {
                cSQL =  " UPDATE TCSTCH " +
                        " SET tchdst = ?, " +
                        " tchded = ?, " +
                        " tchday = ?, " +
                        " tchlst = ?, " +
                        " tchtxt = ?, " +
                        " tchpg = ?, " +
                        " tchus = ?, " +
                        " tchdt = ?, " +
                        " tchtm = ? " +
                        " WHERE tchcom = ? AND tchemp = ? AND tchtcd = ? AND tchdst = ? " ;
                prepStmt = connection.prepareStatement(cSQL);
                prepStmt.setInt(1, Integer.parseInt(strdate));
                prepStmt.setInt(2, Integer.parseInt(enddate));
                prepStmt.setFloat(3, Float.parseFloat(days));
                prepStmt.setFloat(4, tmplst);
                prepStmt.setString(5,comment);
                prepStmt.setString(6,"WS_TCS201");
                prepStmt.setString(7,"TEST");
                prepStmt.setInt(8,Integer.parseInt(upddate));
                prepStmt.setInt(9,Integer.parseInt(updtime));
                prepStmt.setString(10, com);
                prepStmt.setString(11, emp);
                prepStmt.setString(12, tcd);
                prepStmt.setString(13, oldstrdate);
                String tmptest=prepStmt.toString();
                
                if ( prepStmt.executeUpdate() == 0 )
                {
                    obj1.put("Msg","SU");
                    return obj1.toString();
                    //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorU.html");
                    //return "N";
                }
            }
            else if ( state.equals("D") )
            {
                cSQL = " DELETE FROM TCSTCH " +
                       " WHERE tchcom = ? AND tchemp = ? AND tchtcd = ? AND tchdst = ? " ;
                prepStmt = connection.prepareStatement(cSQL);
                prepStmt.setString(1, com);
                prepStmt.setString(2, emp);
                prepStmt.setString(3, tcd);
                prepStmt.setString(4, oldstrdate);
                if ( prepStmt.executeUpdate() == 0 )
                {
                    obj1.put("Msg","SD");
                    return obj1.toString();
                    //servletResponse.sendRedirect("/RestApache/Pages/TCS211_ErrorD.html");
                    //return "N";
                }
            }
            
            obj1.put("Msg","SY");
            //servletResponse.sendRedirect("/RestApache/Pages/TCS211_Success.html");
        }
        catch (SQLException e) 
        {
            prepStmt = null;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            prepStmt = null;
            e.printStackTrace();
        }
        
        return obj1.toString();
        //return "Y";
        
    }
}
