package com.alexm.kata.bankaccount.katabankaccount.services;

import com.alexm.kata.bankaccount.katabankaccount.entities.Account;
import com.alexm.kata.bankaccount.katabankaccount.entities.Client;
import com.alexm.kata.bankaccount.katabankaccount.entities.Operation;
import com.alexm.kata.bankaccount.katabankaccount.exceptions.AccountAlreadyExistsException;
import com.alexm.kata.bankaccount.katabankaccount.exceptions.NegativeAmountException;
import com.alexm.kata.bankaccount.katabankaccount.exceptions.InsufficientFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountService {

    private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

    // This will replace a Database account table
    private final Map<Client, Account> accounts;
    // This will replace a Database operations table
    private final List<Operation> operations;

    public AccountService() {
        accounts = new HashMap<>();
        operations = new ArrayList<>();
    }

    /**
     * Deposit an amount on a Client Account
     * @param client Client depositing money on his account
     * @param amount The amount to deposit, must be positive
     * @return the deposit resulting operation
     * @throws NegativeAmountException if the given amount is negative
     */
    public Operation deposit(Client client, BigDecimal amount) throws NegativeAmountException {

        // For obvious reasons we should not allow a negative amount here
        checkAmountIsPositive(amount);

        // Get the client account
        Account account = accounts.get(client);

        // Synchronized on the account client field
        synchronized (account.getOwner()) {

            // Compute the new balance
            BigDecimal balance = account.deposit(amount);
            // Create a financial operation
            Operation operation = new Operation("deposit", account, LocalDateTime.now(), amount, balance);
            // Register the operation
            registerOperation(operation);

            logger.info("Deposit Operation : {}", operation.toString());

            return operation;
        }
    }

    /**
     * Withdraw an amount from a Client Account
     * @param client Client withdrawing money on his account
     * @param amount The amount to withdraw, must be positive
     * @return the withdraw resulting operation
     * @throws NegativeAmountException if the given amount is negative
     * @throws InsufficientFundsException if the given amount to withdraw is greater than the current balance
     */
    public Operation withdraw(Client client, BigDecimal amount) throws NegativeAmountException, InsufficientFundsException {

        // For obvious reasons we should not allow a negative amount here
        checkAmountIsPositive(amount);

        // Get the client account
        Account account = accounts.get(client);

        // Synchronized on the account client field
        synchronized (account.getOwner()) {

            // Compute the new balance
            BigDecimal balance = account.withdraw(amount);
            // As allowing debt is not specified we should check the balance never get negative
            checkBalanceIsPositive(balance);
            // Create a financial operation
            Operation operation = new Operation("withdraw", account, LocalDateTime.now(), amount, balance);
            // Register the operation
            registerOperation(operation);

            logger.info("Withdraw Operation : {}", operation.toString());

            return operation;
        }
    }

    /**
     * Withdraw the current balance from a Client Account
     * @param client Client withdrawing money on his account
     * @return the withdraw resulting operation
     */
    public Operation withdrawAll(Client client) {
        // Get the client account
        Account account = accounts.get(client);
        try {
            return withdraw(client, account.getBalance());
        } catch (InsufficientFundsException | NegativeAmountException e) {
            // If this ever happen it will probably be caused by a concurrent withdraw issue
            logger.error("Error trying to withdraw all the client savings.",e);
        }
        return null;
    }

    /**
     * Client account history
     * @return the List of operations registered for the given client account
     */
    public List<Operation> history(Client client) {
        // Get the client account
        Account account = accounts.get(client);

        // Get each operation filtering on the account then sort before returning as a list
        List<Operation> history = operations.stream()
                .filter(op -> op.getAccount().equals(account))
                .sorted()
                .collect(Collectors.toList());

        // Logging the history for the kata
        logger.info("Account {} history :", account.getId());
        history.forEach(e -> logger.info(e.toString()));

        return history;
    }

    /**
     * Create a new account for a client. Client should not already have an account.
     */
    public void createAccount(Client client) throws AccountAlreadyExistsException {
        if (accounts.containsKey(client))
            throw new AccountAlreadyExistsException("This client already has an account");
        accounts.put(client, new Account(client));
    }

    /**
     * Return a client account current balance
     */
    public BigDecimal accountBalance(Client client) {
        return accounts.get(client).getBalance();
    }

    private void registerOperation(Operation operation) {
        // Here we could run any operation or account check before saving it

        // Then we would save the operation.getAccount() new state in database

        // Finally, we save the operation for historisation in database
        operations.add(operation);
    }

    private void checkAmountIsPositive( BigDecimal amount ) throws NegativeAmountException {
        // Amount must be positive
        if (amount.signum() < 0)
            throw new NegativeAmountException("Cannot withdraw a negative amount.");
    }

    private void checkBalanceIsPositive(BigDecimal balance) throws InsufficientFundsException {
        // Amount must be positive
        if (balance.signum() < 0)
            throw new InsufficientFundsException("Unsufficient funds for this operation");
    }
}
