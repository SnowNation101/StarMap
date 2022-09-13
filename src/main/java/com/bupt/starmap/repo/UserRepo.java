package com.bupt.starmap.repo;

import com.bupt.starmap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
  User findByUsername(String username);
}