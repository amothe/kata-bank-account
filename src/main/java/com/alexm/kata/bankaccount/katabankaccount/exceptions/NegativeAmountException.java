package com.alexm.kata.bankaccount.katabankaccount.exceptions;

public class NegativeAmountException extends AccountOperationException {
    public NegativeAmountException(String message) {
        super(message);
    }
}
