package com.oceanstyxx.pubdriver.model;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by amit on 1/5/16.
 */
public class Invoices {

    /*public InvoiceData[] getInvoices() {
        InvoiceData[] data = new InvoiceData[20];

        for(int i = 0; i < 20; i ++) {
            InvoiceData row = new InvoiceData();
            row.id = (i+1);
            row.invoiceNumber = row.id;
            row.amountDue = BigDecimal.valueOf(20.00 * i);
            row.invoiceAmount = BigDecimal.valueOf(120.00 * (i+1));
            row.invoiceDate = new Date();
            row.customerName =  "Thomas John Beckett";
            row.customerAddress = "1112, Hash Avenue, NYC";

            data[i] = row;
        }
        return data;

    }*/

    public InvoiceData[] getInvoices(ArrayList<Billing> billing) {

        InvoiceData[] data = new InvoiceData[billing.size()];

        int count = 0;
        for(Billing bill:billing){
            InvoiceData row = new InvoiceData();
            row.chargeDetails = bill.getPrice_breakup();
            row.qty = bill.getQuantity();
            row.unitRate = bill.getUnit_price();
            row.total = bill.getTotal_price();
            data[count] = row;
            count++;
        }

        return data;

    }
}