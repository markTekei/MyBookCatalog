package com.example.mybookcatalog;

import java.io.Serializable;

public class Customer implements Serializable {
    private String name;
    private String type; // School, Bookstore, Library, etc.
    private String contactInfo;
    private double creditLimit;
    private double outstandingBalance;

    public Customer(String name, String type, String contactInfo, double creditLimit, double outstandingBalance) {
        this.name = name;
        this.type = type;
        this.contactInfo = contactInfo;
        this.creditLimit = creditLimit;
        this.outstandingBalance = outstandingBalance;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getContactInfo() { return contactInfo; }
    public double getCreditLimit() { return creditLimit; }
    public double getOutstandingBalance() { return outstandingBalance; }
}
