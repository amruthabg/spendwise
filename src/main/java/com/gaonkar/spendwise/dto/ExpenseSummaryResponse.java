package com.gaonkar.spendwise.dto;

public class ExpenseSummaryResponse {

    private String categoryName;
    private Double totalAmount;

    public ExpenseSummaryResponse() {}

    public ExpenseSummaryResponse(String categoryName, Double totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
