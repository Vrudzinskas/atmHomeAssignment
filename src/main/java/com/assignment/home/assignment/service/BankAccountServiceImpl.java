package com.assignment.home.assignment.service;

import com.assignment.home.assignment.entity.BankAccount;
import com.assignment.home.assignment.exceptions.NotFoundException;
import com.assignment.home.assignment.exceptions.PinAuthenticationException;
import com.assignment.home.assignment.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService
{
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void verifyBankAccount(String bankAccountNumber, String bankAccountPin)
    {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(bankAccountNumber);
        if (bankAccountOptional.isEmpty()) throw new NotFoundException(bankAccountNumber +" bank account not found.");
        if (!bankAccountOptional.get().getAccountPin().equals(bankAccountPin)) throw new PinAuthenticationException("Wrong pin");
    }

    public BigDecimal getAccountBalance(String bankAccountNumber, String bankAccountPin)
    {
        verifyBankAccount(bankAccountNumber, bankAccountPin);
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(bankAccountNumber);
        if (bankAccountOptional.isEmpty())
                throw new NotFoundException(bankAccountNumber + " bank account not found.");
        return bankAccountOptional.get().getAccountBalance();
    }

    public BigDecimal getAccountOverdraft(String bankAccountNumber, String bankAccountPin)
    {
        verifyBankAccount(bankAccountNumber, bankAccountPin);
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(bankAccountNumber);
        if (bankAccountOptional.isEmpty())
            throw new NotFoundException(bankAccountNumber + " bank account not found.");
        return bankAccountOptional.get().getAccountOverdraft();
    }

    public BigDecimal getBalanceAndOverdraft(String bankAccountNumber, String bankAccountPin)
    {
        verifyBankAccount(bankAccountNumber, bankAccountPin);
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(bankAccountNumber);
        if (bankAccountOptional.isEmpty())
            throw new NotFoundException(bankAccountNumber + " bank account not found.");
        BankAccount ba = bankAccountOptional.get();
        return ba.getAccountBalance().add(ba.getAccountOverdraft());
    }

    public boolean withdraw(String bankAccountNumber, String bankAccountPin, double amountToWithdraw)
    {
        verifyBankAccount(bankAccountNumber, bankAccountPin);

        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(bankAccountNumber);
        if (bankAccountOptional.isEmpty())
            throw new NotFoundException(bankAccountNumber + " bank account not found.");
        BankAccount ba = bankAccountOptional.get();

        if (BigDecimal.valueOf(amountToWithdraw).compareTo(ba.getAccountBalance().add(ba.getAccountOverdraft()))<=0 )
        {
            if (BigDecimal.valueOf(amountToWithdraw).compareTo(ba.getAccountBalance())<=0)
            {
                ba.setAccountBalance(ba.getAccountBalance().subtract(BigDecimal.valueOf(amountToWithdraw)));
                bankAccountRepository.save(ba);
                return true;
            }
            else
            {
                BigDecimal overdraftReduction = BigDecimal.valueOf(amountToWithdraw).subtract(ba.getAccountBalance());
                ba.setAccountBalance(BigDecimal.valueOf(0));
                ba.setAccountOverdraft(ba.getAccountOverdraft().subtract(overdraftReduction));
                bankAccountRepository.save(ba);
                return true;
            }
        }
        return false;
    }
}
