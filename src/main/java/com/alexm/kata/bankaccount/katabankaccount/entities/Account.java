package com.alexm.kata.bankaccount.katabankaccount.entities;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entity representing a Client Account
 */
public class Account {
    /** Account ID, Should be used for hash/id purposes */
    private String id;
    /** This account owner */
    private Client owner;
    /** Current account balance */
    private BigDecimal balance;

    public Account(Client owner) {
        this.id = UUID.randomUUID().toString(); // Collision risk, i know
        this.owner = owner;
        this.balance = BigDecimal.ZERO;
    }

    /** Add an amount to this account balance */
    public BigDecimal deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        return this.balance;
    }

    /** Subtract an amount from this account balance */
    public BigDecimal withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        return this.balance;
    }

    public String getId() {
        return id;
    }

    public Client getOwner() {
        return this.owner;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }
}
