package com.bupt.starmap.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bupt.starmap.controller.utils.RoleToUserForm;
import com.bupt.starmap.controller.utils.UserRegisterRequest;
import com.bupt.starmap.controller.utils.UserUpdateRequest;
import com.bupt.starmap.domain.Role;
import com.bupt.starmap.domain.User;
import com.bupt.starmap.repo.RoleRepo;
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
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RolesAllowed("ROLE_USER")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final RoleRepo roleRepo;

  // Get all user information
  @GetMapping("/user/get/all")
  public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok().body(userService.getUsers());
  }

  // Register (save new user)
  @PostMapping("/user/register")
  public ResponseEntity<User> saveUser(@RequestBody UserRegisterRequest userRegisterRequest) {
    User user = new User(
        userRegisterRequest.getUsername(),
        userRegisterRequest.getNickname(),
        userRegisterRequest.getPassword(),
        LocalDate.now(),
        "",
        new ArrayList<>()
    );
    Role role = roleRepo.findByName("ROLE_USER");
    user.getRoles().add(role);
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/user/register").toUriString());
    return ResponseEntity.created(uri).body(userService.saveUser(user));
  }


  // Get current user info
  @GetMapping("/user/get")
  public ResponseEntity<User> getUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok().body(userService.getUser(username));
  }

  // Update user profile
  @PostMapping("/user/update")
  public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userService.getUser(username);
    user.setNickname(request.getNickname());
    user.setPassword(request.getPassword());
    user.setDob(request.getDob());
    user.setPhoneNo(request.getPhoneNo());
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/user/update").toUriString());
    return ResponseEntity.created(uri).body(userService.saveUser(user));
  }

  // Save new role
  @PostMapping("/role/save")
  public ResponseEntity<Role> saveRole(@RequestBody Role role) {
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/role/save").toUriString());
    return ResponseEntity.created(uri).body(userService.saveRole(role));
  }

  // Add role to user
  @PostMapping("/role/to/user")
  public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
    userService.addRoleToUser(form.getUsername(), form.getRoleName());
    return ResponseEntity.ok().build();
  }

  // Refresh token
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
        response.setStatus(FORBIDDEN.value());
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