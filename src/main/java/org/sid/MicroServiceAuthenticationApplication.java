package org.sid;

import org.sid.Entities.AppRole;
import org.sid.Service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class MicroServiceAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceAuthenticationApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService) {
        return args -> {
            accountService.saveRole(new AppRole(null, "USER"));
            accountService.saveRole(new AppRole(null, "ADMIN"));
            Stream.of("user", "admin").forEach(username -> accountService.saveUser(username, "123", "123"));
            accountService.addRoleToUser("admin", "ADMIN");
        };
    }

    @Bean
    BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

