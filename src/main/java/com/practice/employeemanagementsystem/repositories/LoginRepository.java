package com.practice.employeemanagementsystem.repositories;

import com.practice.employeemanagementsystem.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
}
