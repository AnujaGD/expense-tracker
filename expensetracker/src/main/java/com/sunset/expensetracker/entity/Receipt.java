package com.sunset.expensetracker.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "receipts")
public class Receipt {

    @Id
    private String id;
    private String receiptNumber;  // Unique receipt identifier
    private LocalDate date;        // Date of the receipt
    private String vendor;         // Vendor name
    private List<Expense> expenses; // List of expenses in the receipt
    private double totalAmount;    // Total amount of all expenses in the receipt

    // Default constructor
    public Receipt() {}

    // Constructor with all fields
    public Receipt(String receiptNumber, LocalDate date, String vendor, List<Expense> expenses) {
        this.receiptNumber = receiptNumber;
        this.date = date;
        this.vendor = vendor;
        this.expenses = expenses;
        this.totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum(); // Calculate total amount
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        this.totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum(); // Recalculate total when expenses change
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}