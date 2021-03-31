package com.alexm.kata.bankaccount.katabankaccount.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a Financial Operation, usually on a Client Account
 */
public class Operation implements Comparable<Operation> {
    /** Operation target Account */
    private Account account;
    /** Operation type { deposit, withdraw } */
    private String operation; // Note that this should be an enum/class
    /** Operation date & time */
    private LocalDateTime date;
    /** Operation amount */
    private BigDecimal amount; // A currency specifying class would be advised
    /** Predicted account balance after the operation */
    private BigDecimal balance;

    public Operation(String operation, Account account, LocalDateTime date, BigDecimal amount, BigDecimal balance) {
        this.operation = operation;
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.balance = balance;
    }

    public Account getAccount() {
        return account;
    }

    public String getOperation() {
        return operation;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public int compareTo(Operation o) {
        return this.date.compareTo(o.date);
    }

    @Override
    public String toString() {
        return "Operation{" +
                ", operation='" + operation + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", balance=" + balance +
                '}';
    }
}
