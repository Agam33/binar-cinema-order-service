package com.ra.order.service;

import com.ra.order.dto.model.FileDB;
import net.sf.jasperreports.engine.JRException;

public interface InvoiceService {

    FileDB generateInvoice(String fileName) throws JRException;
}
