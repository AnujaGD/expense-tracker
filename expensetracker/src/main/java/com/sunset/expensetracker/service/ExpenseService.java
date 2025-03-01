package com.sunset.expensetracker.service;

import com.sunset.expensetracker.entity.Expense;
import com.sunset.expensetracker.repo.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }
    public Expense updateExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);
    }
    public Expense getExpense(String id) {
        return expenseRepository.findById(id).get();
    }

}
