package com.bupt.starmap.repo;

import com.bupt.starmap.domain.Stargazer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StargazerRepo
        extends JpaRepository<Stargazer, Long> {
    Stargazer findByUsername(String username);
}