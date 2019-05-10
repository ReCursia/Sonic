package com.studentsteam.sonic.tools.database;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

public class DatabaseReport {

    public synchronized static void printReport(String template, String resultpath) {
        try {
            Class.forName("org.sqlite.JDBC"); //Connect driver
            String url = "jdbc:sqlite:db.db"; //Path to our bd
            Connection con = DriverManager.getConnection(url); //Connecting to our db
            //Creating report
            JasperReport jasperReport = JasperCompileManager.compileReport(template);
            //Fill with data
            JasperPrint print = JasperFillManager.fillReport(jasperReport,new HashMap(),con);
            if(resultpath.endsWith(".pdf")){
                JRPdfExporter exporter = new JRPdfExporter();
                //Input and output
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(resultpath));
                exporter.exportReport();
            }else {
                HtmlExporter exporter = new HtmlExporter();
                //Input and output
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(resultpath));
                exporter.exportReport();
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
