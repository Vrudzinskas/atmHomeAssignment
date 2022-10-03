package com.assignment.home.assignment.repository;

import com.assignment.home.assignment.entity.ATM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ATMRepository extends JpaRepository<ATM, String>
{
}
