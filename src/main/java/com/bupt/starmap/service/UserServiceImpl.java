package com.bupt.starmap.service;

import com.bupt.starmap.domain.Role;
import com.bupt.starmap.domain.User;
import com.bupt.starmap.repo.RoleRepo;
import com.bupt.starmap.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepo stargazerRepo;
  private final RoleRepo roleRepo;
  private final PasswordEncoder passwordEncoder;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = stargazerRepo.findByUsername(username);
    if (user == null) {
      log.error("User not found in the database");
      throw new UsernameNotFoundException("User not found in the database");
    } else {
      log.info("User found in the database: {}", username);
    }

    Collection<SimpleGrantedAuthority> authorites = new ArrayList<>();
    user.getRoles().forEach(role -> {
      authorites.add(new SimpleGrantedAuthority(role.getName()));
    });
    return new org.springframework.security.core.userdetails
        .User(user.getUsername(), user.getPassword(), authorites);
  }

  @Override
  public User saveUser(User user) {
    log.info("Save new user {} to the database", user.getUsername());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return stargazerRepo.save(user);
  }

  @Override
  public Role saveRole(Role role) {
    log.info("Save new role {} to the database", role.getName());
    return roleRepo.save(role);
  }

  @Override
  public void addRoleToUser(String username, String roleName) {
    log.info("Adding role {} to stargazer {}", roleName, username);
    User user = stargazerRepo.findByUsername(username);
    Role role = roleRepo.findByName(roleName);
    user.getRoles().add(role);
  }

  @Override
  public User getUser(String username) {
    log.info("Fetching stargazer {}", username);
    return stargazerRepo.findByUsername(username);
  }

  @Override
  public List<User> getUsers() {
    log.info("Fetching all stargazers");
    return stargazerRepo.findAll();
  }

}
