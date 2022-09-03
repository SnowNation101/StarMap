package com.bupt.starmap.service;

import com.bupt.starmap.domain.Role;
import com.bupt.starmap.domain.Stargazer;

import java.util.List;

public interface StargazerService {
    Stargazer saveStargazer(Stargazer stargazer);
    Role saveRole(Role role);
    void addRoleToStargazer(String username, String roleName);
    Stargazer getStargazer(String username);
    List<Stargazer> getStargazers();

}
