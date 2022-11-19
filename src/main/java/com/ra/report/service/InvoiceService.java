package com.ra.report.service;

import com.ra.report.dto.model.FileDB;
import net.sf.jasperreports.engine.JRException;

public interface InvoiceService {

    FileDB generateInvoice(String fileName) throws JRException;
}
