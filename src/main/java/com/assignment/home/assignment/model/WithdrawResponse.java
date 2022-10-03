package com.assignment.home.assignment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawResponse
{
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int fiftyEuroNotes;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int twentyEuroNotes;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int tenEuroNotes;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int fiveEuroNotes;
    BigDecimal balance;
    BigDecimal overdraft;
}
