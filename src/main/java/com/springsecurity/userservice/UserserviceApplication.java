package com.springsecurity.userservice;

import com.springsecurity.userservice.busniess.UserService;
import com.springsecurity.userservice.entities.Role;
import com.springsecurity.userservice.entities.User;
import com.springsecurity.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@SpringBootApplication
@EnableSwagger2
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}
	@Bean
    CommandLineRunner run(UserService userService){
       return args -> {
          userService.saveRole("ROLE_USER");
		  userService.saveRole("ROLE_ADMIN");

          userService.saveUser(new User(null,"admin","admin","123456", new ArrayList<>()));

          userService.addRoleToUser("admin","ROLE_USER");
		  userService.addRoleToUser("admin","ROLE_ADMIN");
        };
    }
}
