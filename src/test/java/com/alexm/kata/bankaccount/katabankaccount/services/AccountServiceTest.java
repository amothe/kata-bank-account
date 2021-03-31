package com.alexm.kata.bankaccount.katabankaccount.services;

import com.alexm.kata.bankaccount.katabankaccount.entities.Client;
import com.alexm.kata.bankaccount.katabankaccount.entities.Operation;
import com.alexm.kata.bankaccount.katabankaccount.exceptions.AccountOperationException;
import com.alexm.kata.bankaccount.katabankaccount.exceptions.NegativeAmountException;
import com.alexm.kata.bankaccount.katabankaccount.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    AccountService accountService;
    Client client;

    @Test
    void test_deposit() {
        assertDoesNotThrow(() -> accountService.deposit(client, BigDecimal.valueOf(50.45)));
        assertEquals(BigDecimal.valueOf(150.45), accountService.accountBalance(client));
    }

    @Test
    void test_negative_deposit() {
        assertThrows(NegativeAmountException.class, () -> accountService.deposit(client, BigDecimal.valueOf(-50)));
    }

    @Test
    void test_withdraw() {
        assertDoesNotThrow(() -> accountService.withdraw(client, BigDecimal.valueOf(50.45)));
        assertEquals(BigDecimal.valueOf(49.55), accountService.accountBalance(client));
    }

    @Test
    void test_negative_withdraw() {
        assertThrows(NegativeAmountException.class, () -> accountService.withdraw(client, BigDecimal.valueOf(-50)));
    }

    @Test
    void test_withdraw_too_much() {
        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(client, BigDecimal.valueOf(500)));
    }

    @Test
    void test_withdrawAll() {
        assertDoesNotThrow(() -> accountService.withdrawAll(client));
        assertEquals(BigDecimal.valueOf(0), accountService.accountBalance(client));
    }

    @Test
    void test_history() {
        assertDoesNotThrow(() -> accountService.withdraw(client, BigDecimal.valueOf(45)));
        assertDoesNotThrow(() -> accountService.deposit(client, BigDecimal.valueOf(100)));
        List<Operation> history = assertDoesNotThrow(() -> accountService.history(client));

        // There already is a deposit in the setup so we should have an history with 3 operations
        assertEquals(3, history.size());
    }

    @Test
    void test_empty_history() {
        Client client = new Client();
        assertDoesNotThrow(() -> accountService.createAccount(client));
        List<Operation> history = assertDoesNotThrow(() -> accountService.history(client));
        // A new account history should be empty
        assertEquals(0, history.size());
    }

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
        client = new Client();

        // Setup an account for the client with some money
        try {
            accountService.createAccount(client);
            accountService.deposit(client, BigDecimal.valueOf(100));
        } catch (AccountOperationException e) {
            e.printStackTrace();
        }
    }
}