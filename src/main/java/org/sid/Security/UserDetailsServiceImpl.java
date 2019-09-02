package org.sid.Security;

import org.sid.Entities.AppUser;
import org.sid.Entities.CustomUserDetails;
import org.sid.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    // Spring Security Filter Chain, execute cette '2 eme' methode pour avoir un object user/roles
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // si l'user n'existe pas == generer exception
        // si non == return un object de type User
        AppUser appUser = accountService.loadUserByUsername(username);
        if (appUser == null) throw new UsernameNotFoundException("Invalide User");
        List<GrantedAuthority> authorities = new ArrayList<>();

        Set<GrantedAuthority> roles = new HashSet<>();
        Set<GrantedAuthority> modules = new HashSet<>();
        Set<GrantedAuthority> features = new HashSet<>();

        appUser.getRoles().forEach(appRole -> {
            roles.add(new SimpleGrantedAuthority(appRole.getRoleName()));
            accountService.getAllModuleByRole(appRole.getRoleName()).forEach(module -> {
                modules.add(new SimpleGrantedAuthority(module));
                accountService.getAllFeatureByModuleAndRole(module, appRole.getRoleName()).forEach(feature -> {
                    features.add(new SimpleGrantedAuthority(feature));
                });
            });
        });

        /*appUser.getRoles().forEach(appRole -> {
            authorities.add(new SimpleGrantedAuthority(appRole.getRoleName()));
        });*/

        /*accountService.getAllModuleByRole("USER").forEach(module -> {
            authorities.add(new SimpleGrantedAuthority("/"));
            authorities.add(new SimpleGrantedAuthority(module));
            accountService.getAllFeatureByModuleAndRole(module, "USER").forEach(feature -> {
                authorities.add(new SimpleGrantedAuthority(feature));
            });
        });*/

        return new CustomUserDetails(appUser.getUsername(), appUser.getPassword(), roles, modules, features);
//        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}