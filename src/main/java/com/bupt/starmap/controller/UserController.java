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

package com.bupt.starmap.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bupt.starmap.controller.utils.RoleToUserForm;
import com.bupt.starmap.controller.utils.UserRegisterRequest;
import com.bupt.starmap.controller.utils.UserUpdateRequest;
import com.bupt.starmap.domain.Node;
import com.bupt.starmap.domain.Role;
import com.bupt.starmap.domain.User;
import com.bupt.starmap.repo.NodeRepo;
import com.bupt.starmap.repo.RoleRepo;
import com.bupt.starmap.repo.UserRepo;
import com.bupt.starmap.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The User Controller, including apis for adding roles to users
 * @author David Zhang
 * @version 1.0
 */
@RolesAllowed("ROLE_USER")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserRepo userRepo;
  private final RoleRepo roleRepo;
  private final NodeRepo nodeRepo;

  /**
   * Get all users from database, now used for test
   * Later can be accessed by admin user
   * @return List of all users
   */
  @GetMapping("/user/get/all")
  public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok().body(userService.getUsers());
  }

  /**
   * Register a new user(save a new user to the database)
   * @param userRegisterRequest
   * @return Boolean, true for success register
   */
  @PostMapping("/user/register")
  public Boolean saveUser(@RequestBody UserRegisterRequest userRegisterRequest) {
    if (userRepo.existsUserByUsername(userRegisterRequest.getUsername())) {
      return false;
    }
    User user = new User(
        userRegisterRequest.getUsername(),
        userRegisterRequest.getNickname(),
        userRegisterRequest.getPassword(),
        LocalDate.now(),
        "",
        "",
        new ArrayList<>()
    );
    Role role = roleRepo.findByName("ROLE_USER");
    user.getRoles().add(role);
    userService.saveUser(user);
    Node node = new Node();
    node.setNodeId(0L);
    node.setUsername(userRegisterRequest.getUsername());
    node.setParentId(114514L);
    node.setValue("根节点");
    nodeRepo.save(node);
    return true;
  }


  /**
   * Get user info of current login user
   * @return current login user
   */
  @GetMapping("/user/get")
  public ResponseEntity<User> getUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok().body(userService.getUser(username));
  }

  /**
   * Update user info of current login user
   * @param request
   * @return Current user revised info
   */
  @PostMapping("/user/update")
  public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/user/update").toUriString());
    return ResponseEntity.created(uri).body(userService.upadateUser(request, username));
  }

  /**
   * Change password, not used for now
   * @param password
   * @return new password
   */
  @PostMapping("/user/change/password")
  public ResponseEntity<String> changePassword(@RequestParam String password) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/user/change/password").toUriString());
    return ResponseEntity.created(uri).body(userService.changePassword(password, username));
  }

  /**
   * Save a role to the role table
   * @param role
   * @return
   */
  @PostMapping("/role/save")
  public ResponseEntity<Role> saveRole(@RequestBody Role role) {
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/role/save").toUriString());
    return ResponseEntity.created(uri).body(userService.saveRole(role));
  }

  /**
   * Add role to current user, the role must from the role table
   * @param form
   * @return
   */
  @PostMapping("/role/to/user")
  public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
    userService.addRoleToUser(form.getUsername(), form.getRoleName());
    return ResponseEntity.ok().build();
  }

  /**
   * Refresh token
   * @param request
   * @param response
   * @throws IOException
   */
  @GetMapping("/token/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String authorizationHeader = request.getHeader(AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        String refresh_token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        String username = decodedJWT.getSubject();
        User user = userService.getUser(username);

        String access_token = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
            .withIssuer(request.getRequestURL().toString())
            .withClaim("roles", user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toList()))
            .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
      } catch (Exception exception) {
        response.setHeader("error", exception.getMessage());
        response.setStatus(UNAUTHORIZED.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }
    } else {
      throw new RuntimeException("Refresh token is missing");
    }
  }
}