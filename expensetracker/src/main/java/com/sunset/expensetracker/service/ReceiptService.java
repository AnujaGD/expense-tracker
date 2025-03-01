package com.sunset.expensetracker.service;

import com.sunset.expensetracker.entity.Receipt;
import com.sunset.expensetracker.repo.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ExpenseService expenseService;
    // Create or Update Receipt
    public Receipt createOrUpdateReceipt(Receipt receipt) {
        receipt.getExpenses().forEach(expense -> expenseService.createExpense(expense));
        return receiptRepository.save(receipt);
    }

    // Get Receipt by ID
    public Optional<Receipt> getReceiptById(String id) {
        return receiptRepository.findById(id);
    }

    // Get All Receipts
    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }

    // Delete Receipt by ID
    public void deleteReceiptById(String id) {
        receiptRepository.deleteById(id);
    }

    public void uploadReceipt(MultipartFile image) throws IOException {
        // Save the image in MongoDB using GridFS
        gridFsTemplate.store(image.getInputStream(), image.getOriginalFilename(), image.getContentType());

    }

    public void deleteReceipt(Receipt receipt) {
        receiptRepository.delete(receipt);
    }
}