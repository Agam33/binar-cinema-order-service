package com.ra.order.controller;

import com.ra.order.dto.model.FileDB;
import com.ra.order.dto.response.ResponseError;
import com.ra.order.service.InvoiceService;
import com.ra.order.util.Constants;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(Constants.INVOICE_ENDPOINT)
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping(value = "/download")
    public ResponseEntity<?> downloadInvoiceFile(@RequestParam("fileName") String fileName) {
        try {
            FileDB fileDB = invoiceService.generateInvoice(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileDB.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileDB.getFileName() + "\"")
                    .body(new ByteArrayResource(fileDB.getData()));
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseError(HttpStatus.NO_CONTENT.value(), new Date(), Constants.ERROR_MSG));
        }
    }
}
