package com.sunset.expensetracker.repo;

import com.sunset.expensetracker.entity.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExpenseRepository extends MongoRepository<Expense, String> { }
