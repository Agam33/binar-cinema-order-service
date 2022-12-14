package com.ra.order.service;

import com.ra.order.dto.model.FileDB;
import com.ra.order.util.Constants;
import com.ra.order.util.JasperUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final JasperUtil jasperUtil;

    public InvoiceServiceImpl(JasperUtil jasperUtil) {
        this.jasperUtil = jasperUtil;
    }

    @Override
    public FileDB generateInvoice(String filename) throws JRException {

        // data dummy
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("studioName", "A");
        parameters.put("cstName", "Sophie Amundsen");
        parameters.put("filmName", "Harry Potter Deathly Hallows (2007)");
        parameters.put("date", LocalDate.now().toString());
        parameters.put("time", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        parameters.put("row", "A");
        parameters.put("seatNo", "1");
        parameters.put("invoiceNo", "invoice-" + Constants.randomIdentifier("Sophie")[4]);

        JasperReport jspReport = jasperUtil.setJasperReport("/invoice/invoice.jrxml");
        JasperPrint jspPrint = jasperUtil.setJasperPrint(jspReport, parameters);

        FileDB fileDB = new FileDB();
        fileDB.setData(jasperUtil.toPdf(jspPrint));
        fileDB.setFileName(filename + "-invoice-" + Constants.randomIdentifier("Sophie")[4]);
        fileDB.setFileType("application/pdf");

        return fileDB;
    }
}
