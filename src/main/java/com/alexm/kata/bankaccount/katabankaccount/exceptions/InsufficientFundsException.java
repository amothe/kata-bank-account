package com.alexm.kata.bankaccount.katabankaccount.exceptions;

public class InsufficientFundsException extends AccountOperationException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
