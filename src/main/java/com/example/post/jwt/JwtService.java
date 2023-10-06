package com.example.post.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
   private  String SECRET_KEY;
    @Value("${jwt.expired}")
    private Long expired;
    public String generateToken(UserDetails userDetails){
        return Jwts
                .builder()
                .signWith(getSignInKey())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expired))
                .addClaims(getAuthority(userDetails))
                .compact();
    }
    private Map<String , Object> getAuthority(UserDetails userDetails){
           return Map.of("roles",
                   userDetails.getAuthorities()
                           .stream()
                           .map(GrantedAuthority::getAuthority)
                           .toList()
           );

    }
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String getUsername(String token){
        return extraClaims(token,Claims::getSubject);
    }
    public <T> T extraClaims(String token, Function<Claims,T> extraClaims){
        Claims claims = extraAllClaims(token);
        return extraClaims.apply(claims);
    }
    private Claims extraAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isValid(String token){
      return isTokenExpired(token);
    }
    private boolean isTokenExpired(String token){
        Date expired = extraClaims(token,Claims::getExpiration);
        return expired.after(new Date());
    }

}
