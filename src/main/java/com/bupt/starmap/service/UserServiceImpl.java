/*
 * Copyright (C) 2022 David "SnowNation" Zhang
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License
 *  is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing permissions and limitations under
 *  the License.
 */

package com.bupt.starmap.service;

import com.bupt.starmap.controller.utils.UserUpdateRequest;
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

/**
 * The user service implementation
 * @author David Zhang
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepo userRepo;
  private final RoleRepo roleRepo;
  private final PasswordEncoder passwordEncoder;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
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
    return userRepo.save(user);
  }

  @Override
  public Role saveRole(Role role) {
    log.info("Save new role {} to the database", role.getName());
    return roleRepo.save(role);
  }

  @Override
  public void addRoleToUser(String username, String roleName) {
    log.info("Adding role {} to user {}", roleName, username);
    User user = userRepo.findByUsername(username);
    Role role = roleRepo.findByName(roleName);
    user.getRoles().add(role);
  }

  @Override
  public User getUser(String username) {
    log.info("Fetching user {}", username);
    return userRepo.findByUsername(username);
  }

  @Override
  public List<User> getUsers() {
    log.info("Fetching all users");
    return userRepo.findAll();
  }

  @Override
  public User upadateUser(UserUpdateRequest request, String username) {
    userRepo.findByUsername(username).setNickname(request.getNickname());
    userRepo.findByUsername(username).setDob(request.getDob());
    userRepo.findByUsername(username).setPhoneNo(request.getPhoneNo());
    userRepo.findByUsername(username).setCurrent(request.getCurrent());
    return userRepo.findByUsername(username);
  }

  @Override
  public String changePassword(String password, String username) {
    userRepo.findByUsername(username).setPassword(password);
    return password;
  }

}
