package com.bupt.starmap.repository;

import com.bupt.starmap.model.Stargazer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StargazerRepository
        extends JpaRepository<Stargazer, String> {
    Stargazer findByUsername(String username);
}