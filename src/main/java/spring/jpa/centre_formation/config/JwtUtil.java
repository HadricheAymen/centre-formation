package spring.jpa.centre_formation.config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key SECRET_KEY;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
    }
    private long validityInMilliseconds = 24*2*3600000; 



    // Générer un token JWT
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", String.join(",", roles))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith( SECRET_KEY)
                .compact();
    }

    // Extraire le nom d'utilisateur du token JWT
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extraire les rôles du token JWT
    public List<String> extractRoles(String token) {
        String roles = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("roles", String.class);
        return List.of(roles.split(","));
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        List<GrantedAuthority> authorities = extractRoles(token).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // Ajouter "ROLE_" pour Spring Security
                .collect(Collectors.toList());
        for (GrantedAuthority ga : authorities)
        {
         System.out.println("ga:"+ga.getAuthority())   ;
        }


        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }









    // Vérifier si le token est expiré
    public boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }

    // Valider le token JWT
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
