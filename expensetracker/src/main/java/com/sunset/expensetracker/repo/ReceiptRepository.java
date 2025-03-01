package com.sunset.expensetracker.repo;

import com.sunset.expensetracker.entity.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends MongoRepository<Receipt, String> {
    // You can add custom queries here, e.g., find by vendor or receipt number
}