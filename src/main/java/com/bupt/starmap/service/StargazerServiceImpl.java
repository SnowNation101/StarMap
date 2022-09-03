package com.bupt.starmap.service;

import com.bupt.starmap.domain.Role;
import com.bupt.starmap.domain.Stargazer;
import com.bupt.starmap.repo.RoleRepo;
import com.bupt.starmap.repo.StargazerRepo;
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
import java.util.SimpleTimeZone;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StargazerServiceImpl implements StargazerService, UserDetailsService {

    private final StargazerRepo stargazerRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Stargazer stargazer = stargazerRepo.findByUsername(username);
        if (stargazer == null) {
            log.error("Stargazer not found in the database");
            throw new UsernameNotFoundException("Stargazer not found in the database");
        } else {
            log.info("Stargazer found in the database: {}", username);
        }

        Collection<SimpleGrantedAuthority> authorites = new ArrayList<>();
        stargazer.getRoles().forEach(role -> {
            authorites.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails
                .User(stargazer.getUsername(), stargazer.getPassword(), authorites);
    }

    @Override
    public Stargazer saveStargazer(Stargazer stargazer) {
        log.info("Save new stargazer {} to the database", stargazer.getName());
        stargazer.setPassword(passwordEncoder.encode(stargazer.getPassword()));
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
