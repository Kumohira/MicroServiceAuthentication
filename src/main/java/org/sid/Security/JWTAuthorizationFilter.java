package org.sid.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// C'est le '2eme' Filter de l'Authorization, lorsque l'user demande l'accés aux ressources.
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    // Spring Security Filter Chain, fait appel a 'doFilterInternal'
    // on recupere JWT
    // on verifié si la requete contient l'entete 'Autorization'
    // si oui, je recalcule la signature du JWT
    // s'il est bonne je recupere username + roles
    // je demande a spring d'authentifier ce User, il le met dans son context
    // il verifier s'il posséde le droit d'accés a la ressource
    // si oui il laisse passer au dispatcher servlet
    // then DispatcherServlet fait appel a l'api rest de l'application
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Autoriser les requete GET POST PUT DELETE et PATCH
        response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH");
        // CORS
        response.addHeader("Access-Control-Allow-Origin", "*");
        // j'autorise au navigateur de m'envoyer des requete qui contient ces entetes.
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, authorization");
        // j'expose ces entete a etre lu par des client externe (angular...)
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
        if (request.getMethod().equals("OPTIONS")) {
            // Si une req est envoyé avec OPTION
            // je reponds avec OK, je vais pas chercher le Token
            // tout ce qui nous interesse dans la reponse c'est les entetes.
            response.setStatus(HttpServletResponse.SC_OK);
        } else if (request.getRequestURI().equals("/login")) {
            // pas besoin de chercher Token
            filterChain.doFilter(request, response);
            //return;
        } else {
            String myAuthorization = request.getHeader(SecurityParams.JWT_HEADER_NAME);
            if (myAuthorization == null || !myAuthorization.startsWith(SecurityParams.JWT_HEADER_PREFIX)) {
                filterChain.doFilter(request, response);
                //return;
            } else {
                // les roles sont stocké dans le JWT, pas besoin d'aller a la BD
                String myJwtToken = myAuthorization.substring(SecurityParams.JWT_HEADER_PREFIX.length());
                Algorithm myAlgorithm = Algorithm.HMAC256(SecurityParams.PRIVATE_SECRET);

                JWTVerifier jwtVerifier = JWT.require(myAlgorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(myJwtToken);

                String username = decodedJWT.getSubject();
                List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
                // je definie un User de spring
                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username, null, authorities);
                // je dis a spring Authentifié moi ce User
                SecurityContextHolder.getContext().setAuthentication(user);
                // je passe au Filter suivant (la suite c que spring verifier est ce qu'il posséde droit...)
                filterChain.doFilter(request, response);
            }
        }
    }
}
