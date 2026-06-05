package com.gaonkar.spendwise.service;

import com.gaonkar.spendwise.dto.ExpenseRequest;
import com.gaonkar.spendwise.dto.ExpenseSummaryResponse;
import com.gaonkar.spendwise.exception.ResourceNotFoundException;
import com.gaonkar.spendwise.model.Category;
import com.gaonkar.spendwise.model.Expense;
import com.gaonkar.spendwise.model.User;
import com.gaonkar.spendwise.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public ExpenseService(ExpenseRepository expenseRepository, UserService userService, CategoryService categoryService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Transactional
    public Expense createExpense(ExpenseRequest request) {
        User user = userService.getUserById(request.getUserId());
        Category category = categoryService.getCategoryById(request.getCategoryId());

        Expense expense = new Expense(
                request.getAmount(),
                request.getDescription(),
                request.getExpenseDate(),
                category,
                user
        );
        return expenseRepository.save(expense);
    }

    @Transactional
    public Expense updateExpense(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        // Re-evaluate Category if changed
        if (!expense.getCategory().getId().equals(request.getCategoryId())) {
            Category category = categoryService.getCategoryById(request.getCategoryId());
            expense.setCategory(category);
        }

        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());

        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    public List<Expense> getExpensesByUser(Long userId) {
        userService.getUserById(userId); // Validate user
        return expenseRepository.findByUserId(userId);
    }

    public List<ExpenseSummaryResponse> getMonthlySummary(Long userId, int year, int month) {
        userService.getUserById(userId); // Validate user

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);

        // Group expenses by category name and sum amounts using Java 8 Streams
        Map<String, Double> summaryMap = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        // Convert Map to list of Response objects
        return summaryMap.entrySet().stream()
                .map(entry -> new ExpenseSummaryResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
