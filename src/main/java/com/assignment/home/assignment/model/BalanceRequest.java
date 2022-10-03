package com.assignment.home.assignment.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class BalanceRequest
{
    @NotBlank(message = "Bank account cannot be blank")
    String bankAccountNumber;

    @NotBlank(message = "Account pin cannot be blank")
    String bankAccountPin;
}
