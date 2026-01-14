package spring.jpa.centre_formation.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    

    public JwtTokenFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extraire le token de l'en-tête Authorization
        String token = resolveToken(request);
        if (token != null && jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            System.out.println("valide...");
            // Si le jeton est valide, extraire les informations d'authentification
            String username = jwtUtil.extractUsername(token);
            System.out.println("username:"+username);
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                                                                    jwtUtil.getAuthentication(token);
                    //new UsernamePasswordAuthenticationToken(username, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        else
        {
            System.out.println("non valide...");
        }

        filterChain.doFilter(request, response);
    }

    // Résoudre le token JWT dans l'en-tête Authorization
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("token:"+bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Extraire le token
        }
        return null;
    }

    
}

