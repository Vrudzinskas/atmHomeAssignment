package com.assignment.home.assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Currency;

@Entity(name="bank_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount
{
    @Id
    private String accountNumber;
    private String accountPin;
    private BigDecimal accountBalance;
    private BigDecimal accountOverdraft;
}
