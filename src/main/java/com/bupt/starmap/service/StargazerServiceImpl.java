package com.bupt.starmap.service;

import com.bupt.starmap.domain.Role;
import com.bupt.starmap.domain.Stargazer;
import com.bupt.starmap.repo.RoleRepo;
import com.bupt.starmap.repo.StargazerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StargazerServiceImpl implements StargazerService{

    private final StargazerRepo stargazerRepo;
    private final RoleRepo roleRepo;

    @Override
    public Stargazer saveStargazer(Stargazer stargazer) {
        log.info("Save new stargazer {} to the database", stargazer.getName());
        return stargazerRepo.save(stargazer);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Save new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToStargazer(String username, String roleName) {
        log.info("Adding role {} to stargazer {}", roleName, username);
        Stargazer stargazer = stargazerRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        stargazer.getRoles().add(role);
    }

    @Override
    public Stargazer getStargazer(String username) {
        log.info("Fetching stargazer {}", username);
        return stargazerRepo.findByUsername(username);
    }

    @Override
    public List<Stargazer> getStargazers() {
        log.info("Fetching all stargazers");
        return stargazerRepo.findAll();
    }
}
