package org.sid.Security;

import org.sid.Entities.AppUser;
import org.sid.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

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
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(appRole -> {
            authorities.add(new SimpleGrantedAuthority(appRole.getRoleName()));
        });
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}