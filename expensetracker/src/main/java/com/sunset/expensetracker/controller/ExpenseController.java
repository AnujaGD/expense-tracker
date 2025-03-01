package com.sunset.expensetracker.controller;

import com.sunset.expensetracker.entity.Expense;
import com.sunset.expensetracker.entity.Receipt;
import com.sunset.expensetracker.repo.ExpenseRepository;
import com.sunset.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // Allow all origins (for development)

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.createExpense(expense);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Expense> deleteExpense(@RequestBody Expense expense) {
        expenseService.deleteExpense(expense);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@RequestBody String id) {
        Expense savedExpense = expenseService.getExpense(id);
        return new ResponseEntity<>(savedExpense,HttpStatus.OK);
    }

    // Get All Receipts
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }
    @PostMapping("/update")
    public ResponseEntity<Expense> updateExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.updateExpense(expense);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }


}