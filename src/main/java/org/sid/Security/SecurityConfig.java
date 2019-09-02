package org.sid.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { // pour personaliser la configuration

    private final UserDetailsServiceImpl userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // on vas pas charger l'user dans la memoire
        // la methode qui permet de charger l'user sachant son username se trouve dans userDetailsService
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        // then il compare l'encoded password " .passwordEncoder() " saisie, avec le password encoder qui se trouve dans la BD
        // si c le meme il considere que l'user est authentified
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.csrf().disable();
        // csrf doit etre desactiver car on vas pas utiliser l'interface de login fournie par spring (STATELESS)
        // et qui contient un champ hiden de csrf pour se proteger contre les attack de Cross-Site Request Forgery
        // Cross-Site Request Forgery (CSRF) is an attack that forces an end user to execute unwanted actions on a web application in which they're currently authenticated.
        // CSRF attacks specifically target state-changing requests, not theft of data, since the attacker has no way to see the response to the forged request.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Authentification de type STATELESS (pas de session)
        http.authorizeRequests().antMatchers("/login/**", "/register/**").permitAll();
        http.authorizeRequests().antMatchers("/appUsers/**", "/appRoles/**", "/moduleFeatures/**").hasAuthority("ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        // on crée un Filter basé sur JWT (c lui qui genere le token quand user s'authentifier)
        http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
