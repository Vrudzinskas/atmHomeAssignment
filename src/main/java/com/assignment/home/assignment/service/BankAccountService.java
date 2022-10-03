package com.assignment.home.assignment.service;

import java.math.BigDecimal;

public interface BankAccountService
{
    void verifyBankAccount(String bankAccountNumber, String bankAccountPin);
    BigDecimal getAccountBalance(String bankAccountNumber, String bankAccountPin);
    BigDecimal getAccountOverdraft(String bankAccountNumber, String bankAccountPin);
    BigDecimal getBalanceAndOverdraft(String bankAccountNumber, String bankAccountPin);
    boolean withdraw(String bankAccountNumber, String bankAccountPin, double amountToWithdraw);
}
