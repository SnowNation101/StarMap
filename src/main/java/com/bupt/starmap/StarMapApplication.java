package com.bupt.starmap;

import com.bupt.starmap.domain.Role;
import com.bupt.starmap.domain.Stargazer;
import com.bupt.starmap.service.StargazerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class StarMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarMapApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(StargazerService stargazerService) {
		return args -> {
			stargazerService.saveRole(new Role(null, "ROLE_USER"));
			stargazerService.saveRole(new Role(null, "ROLE_MANAGER"));
			stargazerService.saveRole(new Role(null, "ROLE_ADMIN"));
			stargazerService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			stargazerService.saveStargazer(new Stargazer(null, "zch", "2020211696",
					"123456", new ArrayList<>()));
			stargazerService.saveStargazer(new Stargazer(null, "hyt", "2020211700",
					"123456", new ArrayList<>()));
			stargazerService.saveStargazer(new Stargazer(null, "xph", "2020211694",
					"123456", new ArrayList<>()));
			stargazerService.saveStargazer(new Stargazer(null, "xbx", "2020211695",
					"123456", new ArrayList<>()));

			stargazerService.addRoleToStargazer("2020211696", "ROLE_USER");
			stargazerService.addRoleToStargazer("2020211696", "ROLE_ADMIN");
			stargazerService.addRoleToStargazer("2020211700", "ROLE_ADMIN");
			stargazerService.addRoleToStargazer("2020211694", "ROLE_MANAGER");
			stargazerService.addRoleToStargazer("2020211695", "ROLE_SUPER_ADMIN");


		};
	}

}
