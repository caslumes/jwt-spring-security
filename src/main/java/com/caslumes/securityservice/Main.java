package com.caslumes.securityservice;

import com.caslumes.securityservice.model.role.Role;
import com.caslumes.securityservice.model.user.User;
import com.caslumes.securityservice.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

import static com.caslumes.securityservice.model.role.RoleName.*;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null, ROLE_USER.getRoleName()));
			userService.saveRole(new Role(null, ROLE_MANAGER.getRoleName()));
			userService.saveRole(new Role(null, ROLE_ADMIN.getRoleName()));
			userService.saveRole(new Role(null, ROLE_SUPER_ADMIN.getRoleName()));

			userService.saveUser(new User(null,
					"Lucas Marques",
					"caslumes",
					"123",
					new ArrayList<>()));

			userService.saveUser(new User(null,
					"Maria Silva",
					"silvineia",
					"123",
					new ArrayList<>()));

			userService.saveUser(new User(null,
					"Thierry Albano",
					"th_albano",
					"123",
					new ArrayList<>()));

			userService.saveUser(new User(null,
					"Adriel Duarte",
					"adriel_duart3",
					"123",
					new ArrayList<>()));

			userService.addRoleToUser("caslumes", ROLE_SUPER_ADMIN.getRoleName());
			userService.addRoleToUser("silvineia", ROLE_ADMIN.getRoleName());
			userService.addRoleToUser("th_albano", ROLE_USER.getRoleName());
			userService.addRoleToUser("adriel_duart3", ROLE_MANAGER.getRoleName());
		};
	}
}
