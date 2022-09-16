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

import java.util.List;

/**
 * The user service interface
 * @author David Zhang
 * @version 1.0
 */
public interface UserService {
  User saveUser(User user);

  User upadateUser(UserUpdateRequest request, String username);

  Role saveRole(Role role);

  User getUser(String username);

  void addRoleToUser(String username, String roleName);

  List<User> getUsers();

  String changePassword(String password, String username);
}
