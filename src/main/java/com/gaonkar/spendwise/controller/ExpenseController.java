package com.gaonkar.spendwise.controller;

import com.gaonkar.spendwise.dto.ExpenseRequest;
import com.gaonkar.spendwise.dto.ExpenseSummaryResponse;
import com.gaonkar.spendwise.model.Expense;
import com.gaonkar.spendwise.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody ExpenseRequest request) {
        Expense expense = expenseService.createExpense(request);
        return new ResponseEntity<>(expense, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        Expense updatedExpense = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpensesByUser(@RequestParam Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUser(userId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ExpenseSummaryResponse>> getMonthlySummary(
            @RequestParam Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        List<ExpenseSummaryResponse> summary = expenseService.getMonthlySummary(userId, year, month);
        return ResponseEntity.ok(summary);
    }
}
