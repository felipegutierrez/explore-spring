package com.github.felipegutierrez.explore.spring.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
public class InvoiceGenerator {
    private final Random invoiceIndex;
    private final Random invoiceNumber;
    private final Random numberOfItems;
    private final PosInvoice[] invoices;
    private final PosInvoiceAvro[] invoicesAvro;

    @Autowired
    AddressGenerator addressGenerator;
    @Autowired
    ProductGenerator productGenerator;

    public InvoiceGenerator() {
        URL resource = getClass().getClassLoader().getResource("data/invoice.json");
        invoiceIndex = new Random();
        invoiceNumber = new Random();
        numberOfItems = new Random();
        ObjectMapper mapper;
        mapper = new ObjectMapper();
        try {
            invoices = mapper.readValue(new File(resource.toURI()), PosInvoice[].class);
            invoicesAvro = mapper.readValue(new File(resource.toURI()), PosInvoiceAvro[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getIndex() {
        return invoiceIndex.nextInt(100);
    }

    private int getNewInvoiceNumber() {
        return invoiceNumber.nextInt(99999999) + 99999;
    }

    private int getNoOfItems() {
        return numberOfItems.nextInt(4) + 1;
    }

    public PosInvoice getNextInvoiceJson() {
        PosInvoice invoice = invoices[getIndex()];
        invoice.setInvoiceNumber(Integer.toString(getNewInvoiceNumber()));
        invoice.setCreatedTime(System.currentTimeMillis());
        if ("HOME-DELIVERY".equalsIgnoreCase(invoice.getDeliveryType())) {
            DeliveryAddress deliveryAddress = addressGenerator.getNextAddress();
            invoice.setDeliveryAddress(deliveryAddress);
        }
        int itemCount = getNoOfItems();
        double totalAmount = 0.0;
        List<LineItem> items = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            LineItem item = productGenerator.getNextProduct();
            totalAmount = totalAmount + item.getTotalValue();
            items.add(item);
        }
        invoice.setNumberOfItems(itemCount);
        invoice.setInvoiceLineItems(items);
        invoice.setTotalAmount(totalAmount);
        invoice.setTaxableAmount(totalAmount);
        invoice.setCGST(totalAmount * 0.025);
        invoice.setSGST(totalAmount * 0.025);
        invoice.setCESS(totalAmount * 0.00125);
        log.debug(invoice);
        return invoice;
    }

    public PosInvoiceAvro getNextInvoiceAvro() {
        PosInvoiceAvro invoice = invoicesAvro[getIndex()];
        invoice.setInvoiceNumber(Integer.toString(getNewInvoiceNumber()));
        invoice.setCreatedTime(System.currentTimeMillis());
        if ("HOME-DELIVERY".equalsIgnoreCase(invoice.getDeliveryType())) {
            DeliveryAddressAvro deliveryAddress = addressGenerator.getNextAddressAvro();
            invoice.setDeliveryAddress(deliveryAddress);
        }
        int itemCount = getNoOfItems();
        double totalAmount = 0.0;
        List<LineItemAvro> items = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            LineItemAvro item = productGenerator.getNextProductAvro();
            totalAmount = totalAmount + item.getTotalValue();
            items.add(item);
        }
        invoice.setNumberOfItems(itemCount);
        invoice.setInvoiceLineItems(items);
        invoice.setTotalAmount(totalAmount);
        invoice.setTaxableAmount(totalAmount);
        invoice.setCGST(totalAmount * 0.025);
        invoice.setSGST(totalAmount * 0.025);
        invoice.setCESS(totalAmount * 0.00125);
        log.debug(invoice);
        return invoice;
    }
}
