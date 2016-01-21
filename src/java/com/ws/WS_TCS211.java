/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import com.ws.DatabaseAccess;
import java.awt.Container;
import java.io.IOException;
import java.sql.Connection;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.view.JRViewer;

/**
 * REST Web Service
 *
 * @author PUSER
 */
@Path("WS_TCS211")
public class WS_TCS211 
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
    
    public WS_TCS211(@Context ServletContext sc) 
    {
        db = (DatabaseAccess) sc.getAttribute("db");
        this.connection = db.getConnection();
    }
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String ShowMessage()
    {
        String output = " ";
        return output;
    }

    @Path("/pdf")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void tcs211_pdf(
                    @FormParam("account") String tchemp,
                    @FormParam("yearselect") String tchyer,
                    @Context HttpServletRequest request,
                    @Context HttpServletResponse response
    )
    throws IOException 
    {
        if ((tchemp != null) && (tchyer != null) &&
            (!"".equals(tchemp)) && (!"".equals(tchyer))) {
            runPDF(tchemp, tchyer, request, response);
        } 
        else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            StringBuffer parString = new StringBuffer();
            parString.append("tchemp='" + tchemp + "',\n");
            parString.append("tchyer='" + tchyer + "'");
            out.println(parString.toString());
            out.flush();
        }
    }
    
    public void runPDF (String tchemp,
                        String tchyer,
                        @Context HttpServletRequest servletRequest,
                        @Context HttpServletResponse servletResponse)
    throws IOException
    {
        try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("EMP", tchemp);
	    parameters.put("Year", tchyer);
	    parameters.put("User", "Wez");
            
            String jasperPath = servletRequest.getServletContext().getRealPath("/");
            //jasperPath += "PrintFile/TCS211.jasper";
            jasperPath += "PrintFile\\TCS211.jasper";
            JasperReport theJASPER = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);
	    JasperPrint theReport = JasperFillManager.fillReport(theJASPER, parameters, connection);

	    JasperExportManager.exportReportToPdfStream(theReport, servletResponse.getOutputStream());

	    servletResponse.getOutputStream().flush();
	    servletResponse.getOutputStream().close();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {

	}
    }
    
    @Path("/excel")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void tcs211_excel(
                    @FormParam("account") String tchemp,
                    @FormParam("yearselect") String tchyer,
                    @Context HttpServletRequest servletRequest,
                    @Context HttpServletResponse servletResponse
    )
    throws JRException, IOException 
    {
	try {
	    Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("EMP", tchemp);
	    parameters.put("Year", tchyer);
	    parameters.put("User", "Wez");
            
            String jasperPath = servletRequest.getServletContext().getRealPath("/");
            jasperPath += "\\PrintFile\\TCS211.jasper";
            JasperReport theJASPER = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);
	    JasperPrint theReport = JasperFillManager.fillReport(theJASPER, parameters, connection);
            
            
            JRXlsExporter xlsExporter = new JRXlsExporter();

            xlsExporter.setExporterInput(new SimpleExporterInput(theReport));
            xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput("TCS211.xls"));
            SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
            xlsReportConfiguration.setOnePagePerSheet(false);
            xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
            xlsReportConfiguration.setDetectCellType(false);
            xlsReportConfiguration.setWhitePageBackground(false);
            xlsExporter.setConfiguration(xlsReportConfiguration);

            xlsExporter.exportReport();

            
            
            /*
            servletResponse.setContentType("application/vnd.ms-excel");
            String fileName = "TCS211.xls";
            servletResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);

            ServletOutputStream ouputStream = servletResponse.getOutputStream();
            JRXlsExporter exporter = new JRXlsExporter();
            
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, theReport);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

            exporter.exportReport();
            ouputStream.flush();
            ouputStream.close();    */
            /*
            JRViewer viewer = new JRViewer(theReport);
            Container reportContainer = getContentPane();
            reportContainer.removeAll();
            reportContainer.add(viewer);
            reportContainer.revalidate();*/
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {

	}
    }
    
}
