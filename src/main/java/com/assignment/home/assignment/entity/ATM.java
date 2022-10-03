package com.assignment.home.assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="atm")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ATM
{
    @Id
    private String atmId;
    private int fiftyEuroNotes;
    private int twentyEuroNotes;
    private int tenEuroNotes;
    private int fiveEuroNotes;
}
