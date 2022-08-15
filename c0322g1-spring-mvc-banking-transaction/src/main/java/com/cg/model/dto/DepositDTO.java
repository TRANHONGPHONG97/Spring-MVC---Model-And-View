package com.cg.model.dto;

import java.math.BigDecimal;

public class DepositDTO {
    private Long id;
    private Long customerId;
    private String fullName;
    private BigDecimal balance;
    private BigDecimal transactionAmount;

    public DepositDTO() {
    }

    public DepositDTO(Long id, Long customerId, String fullName, BigDecimal balance, BigDecimal transactionAmount) {
        this.id = id;
        this.customerId = customerId;
        this.fullName = fullName;
        this.balance = balance;
        this.transactionAmount = transactionAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}