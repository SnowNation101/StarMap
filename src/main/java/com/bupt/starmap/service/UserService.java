package com.bupt.starmap.service;

import com.bupt.starmap.domain.Role;
import com.bupt.starmap.domain.User;

import java.util.List;

public interface UserService {
  User saveUser(User user);
  Role saveRole(Role role);
  User getUser(String username);
  void addRoleToUser(String username, String roleName);
  List<User> getUsers();
}
