package com.alexm.kata.bankaccount.katabankaccount.exceptions;

public class AccountAlreadyExistsException extends AccountOperationException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
