package com.sunset.expensetracker;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.List;

public class ReceiptResponse {

    @JsonProperty("items")
    private Map<String, String> items;

    @JsonProperty("receipt_text")
    private List<String> receiptText;

    // Getters and Setters
    public Map<String, String> getItems() {
        return items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }

    public List<String> getReceiptText() {
        return receiptText;
    }

    public void setReceiptText(List<String> receiptText) {
        this.receiptText = receiptText;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "items=" + items +
                ", receiptText=" + receiptText +
                '}';
    }
}