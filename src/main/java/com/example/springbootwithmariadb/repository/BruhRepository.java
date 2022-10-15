package com.example.springbootwithmariadb.repository;

import com.example.springbootwithmariadb.model.Bruh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BruhRepository extends JpaRepository<Bruh, Long> {
}
