package com.assignment.home.assignment.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class WithdrawRequest
{
    @NotBlank(message = "Bank account cannot be blank")
    String bankAccountNumber;
    @NotBlank(message = "Account pin cannot be blank")
    String bankAccountPin;
    @NotNull(message = "Amount to withdraw cannot be blank")
    @Min(5)
    BigDecimal withdrawAmount;
}
