package org.sid.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sid.Entities.AppUser;
import org.sid.Entities.CustomUserDetails;
import org.sid.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// C'est le '1 er' Filter de l'Authentification
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;


    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    // Spring Security Filter Chain, fait appel a la '1 ere' methode "attemptAuthentication"
    // apres avoir saisie de login et pass
    // Ici on vas juste recuperer user et pass avec l'objet Request
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        return super.attemptAuthentication(request, response);
        AppUser appUser = null;
        try {
            appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword())
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    // Apres la verification de password de user/roles recu de la '2eme' methode ( UserDetailsServiceImpl.loadUserByUsername() )
    // Spring Security Filter Chain, execute la '3eme' methode "successfulAuthentication"
    // la methode genere le token JWT, on appel lib JWT, on construit jwt avec les different params
    // then we add it to the header response sous la forme: "Autorisation : Bearer JWT"
    // then we send it to HTTP Client
    // pour la signature : RSA ou HMAC
    // RSA need 2 key private and public
    // HMAC need only private key pour calculer la signature
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        User springUser = (User) authResult.getPrincipal();
        CustomUserDetails springUser = (CustomUserDetails) authResult.getPrincipal();
        //"authResult" c'est l'objet authentifier,
        // getPrincipal() pour avoir User
        // getAuthorities() pour avoir Roles
        List<String> roles = new ArrayList<>();
        List<String> modules = new ArrayList<>();
        List<String> features = new ArrayList<>();

        /*springUser.getAuthorities().forEach(authority -> {
            System.out.println(authority);
            roles.add(authority.getAuthority());
        });*/
        springUser.getRoles().forEach(role -> {
            roles.add(role.getAuthority());
        });
        springUser.getModules().forEach(module -> {
            modules.add(module.getAuthority());
        });
        springUser.getFeatures().forEach(feature -> {
            features.add(feature.getAuthority());
        });

        System.out.println(roles);
        System.out.println(modules);
        System.out.println(features);

        String myJwtToken = JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(springUser.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[roles.size()])) /* Claim Personalisé je peux mettre ce que je veux */
                .withArrayClaim("modules", modules.toArray(new String[modules.size()])) /* Claim Personalisé je peux mettre ce que je veux */
                .withArrayClaim("features", features.toArray(new String[features.size()])) /* Claim Personalisé je peux mettre ce que je veux */
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityParams.JWT_EXPIRATION)) /* 10 day */
                // je calcule la signature pour garantir que personne d'autre n'est capable de generer le token valid
                .sign(Algorithm.HMAC256(SecurityParams.PRIVATE_SECRET));
        response.addHeader(SecurityParams.JWT_HEADER_NAME, SecurityParams.JWT_HEADER_PREFIX + myJwtToken);
    }
}