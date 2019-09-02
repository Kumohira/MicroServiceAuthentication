package org.sid;

import org.sid.Entities.AppRole;
import org.sid.Entities.ModuleFeature;
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
            accountService.saveModuleFeature(new ModuleFeature(null, "DECES", "CREATE_DOSSIER_DECES", null));
            accountService.saveModuleFeature(new ModuleFeature(null, "DECES", "UPDATE_DOSSIER_DECES", null));
            accountService.saveModuleFeature(new ModuleFeature(null, "DECES", "DELETE_DOSSIER_DECES", null));
            accountService.saveModuleFeature(new ModuleFeature(null, "INVALIDITE", "CREATE_DOSSIER_INVALIDITE", null));
            accountService.saveModuleFeature(new ModuleFeature(null, "INVALIDITE", "UPDATE_DOSSIER_INVALIDITE", null));
            accountService.saveModuleFeature(new ModuleFeature(null, "INVALIDITE", "DELETE_DOSSIER_INVALIDITE", null));

            accountService.saveRole(new AppRole(null, "USER", null));
            accountService.saveRole(new AppRole(null, "ADMIN", null));

            Stream.of("user", "admin").forEach(username -> accountService.saveUser(username, "123", "123"));
            accountService.addRoleToUser("admin", "ADMIN");

            accountService.addModuleFeatureToRole("DECES", "CREATE_DOSSIER_DECES", "USER");
            accountService.addModuleFeatureToRole("DECES", "DELETE_DOSSIER_DECES", "USER");
            accountService.addModuleFeatureToRole("INVALIDITE", "UPDATE_DOSSIER_INVALIDITE", "USER");

            accountService.addModuleFeatureToRole("INVALIDITE", "CREATE_DOSSIER_INVALIDITE", "ADMIN");

            /*accountService.getAllModuleByRole("USER").forEach(module -> {
                accountService.getAllFeatureByModuleAndRole(module, "USER").forEach(feature -> {
                    System.out.println(feature);
                });
            });*/

            /*accountService.getModuleFeatureByRole("USER").forEach(moduleFeature -> {
                System.out.println(moduleFeature.getModule());
                System.out.println(moduleFeature.getFeature());
            });*/
        };
    }

    @Bean
    BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

