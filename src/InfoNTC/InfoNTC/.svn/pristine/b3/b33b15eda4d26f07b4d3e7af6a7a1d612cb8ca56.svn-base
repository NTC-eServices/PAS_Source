import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import lk.informatics.ntc.model.dto.QueueNumberDTO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class PrintToken {

    public static void main(String[] args){
         
        try {
            PrintToken pt = new PrintToken();
            pt.printToken("", "", "", "", "" , "");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrintToken.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printToken(String queueNo, String transType, String permitNo, String ownerName, String appNo, String vehicleNo) throws FileNotFoundException{
         try {
              queueNo="IN1002";
              transType="Vehicle Inspection"; 
              permitNo="PM ok ok"; 
              ownerName="Mr. Peter"; 
              appNo="AP00215";
              vehicleNo="REG-1235";
                     
                     
                String sourceFileName= "D:\\today\\InfoNTC\\src\\lk\\informatics\\ntc\\view\\reports\\temp_token.jrxml";

                java.util.Date date = new java.util.Date();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

                List<QueueNumberDTO> dtoList = new ArrayList<QueueNumberDTO>();
                QueueNumberDTO queueNoDTO = new QueueNumberDTO();
                queueNoDTO.setApplicationNo("Application No. : "+appNo);
                queueNoDTO.setVehicleNo("Vehicle No. : "+vehicleNo);
                dtoList.add(queueNoDTO);

                // Parameters for report
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("DATE", df.format(date));
                parameters.put("TOKEN_NO", queueNo);
                parameters.put("TRANS_TYPE", transType);
                parameters.put("PERMIT_NO", permitNo);
                parameters.put("OWNER_NAME", ownerName);

                JasperDesign jasperDesign;

                /*jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));*/
                
                String path= sourceFileName;
                InputStream input = new FileInputStream(new File(path));
                jasperDesign = JRXmlLoader.load(input);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                //JasperDesign jasperDesign = JRXmlLoader.load(in);
               // JasperReport report = JasperCompileManager.compileReport(jasperDesign);
                JRBeanCollectionDataSource beanCollectionDataSource=new JRBeanCollectionDataSource(dtoList, false);
                //JRDataSource datas = new JREmptyDataSource();

                JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);

                byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
                InputStream stream = new ByteArrayInputStream(pdfByteArray);
               // StreamedContent files = new DefaultStreamedContent(stream, "Application/pdf", pdfName+".pdf");
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            if (service != null) {
                String printServiceName = service.getName();
                System.out.println("Print Service Name = " + printServiceName);
            } else {
                System.out.println("No default print service found.");
            }
            
                //service.createPrintJob().print(doc, null);
                printerJob.setPrintService(service);
                boolean printSucceed = JasperPrintManager.printReport(jasperPrint, false);
                System.out.println(printSucceed);
               
                
                
                
       } catch (JRException ex) {
           ex.printStackTrace();
            Logger.getLogger(PrintToken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrinterException ex) {
            Logger.getLogger(PrintToken.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}