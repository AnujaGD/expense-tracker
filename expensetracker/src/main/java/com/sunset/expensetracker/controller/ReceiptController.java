package com.sunset.expensetracker.controller;

import com.sunset.expensetracker.entity.Expense;
import com.sunset.expensetracker.entity.Receipt;
import com.sunset.expensetracker.service.FlaskService;
import com.sunset.expensetracker.service.ReceiptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private FlaskService flaskService;


    // Create or Update Receipt
    @PostMapping
    public ResponseEntity<Receipt> createOrUpdateReceipt(@RequestBody Receipt receipt) {
        Receipt createdReceipt = receiptService.createOrUpdateReceipt(receipt);

        return ResponseEntity.ok(createdReceipt);
    }

    // Get Receipt by ID
    @GetMapping("/{id}")
    public ResponseEntity<Receipt> getReceiptById(@PathVariable String id) {
        Optional<Receipt> receipt = receiptService.getReceiptById(id);
        return receipt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get All Receipts
    @GetMapping
    public ResponseEntity<List<Receipt>> getAllReceipts() {
        List<Receipt> receipts = receiptService.getAllReceipts();
        return ResponseEntity.ok(receipts);
    }

    // Delete Receipt by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceiptById(@PathVariable String id) {
        receiptService.deleteReceiptById(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping
    public ResponseEntity<Expense> deleteReceipt(@RequestBody Receipt receipt) {
        receiptService.deleteReceipt(receipt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/upload-image")
    public Receipt uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        Receipt receipt = flaskService.callFlaskEndpoint(file);
        receiptService.createOrUpdateReceipt(receipt);
        return receipt;
    }

}