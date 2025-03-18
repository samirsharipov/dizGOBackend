package uz.dizgo.erp.security;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Role;

import java.util.Date;

@Component
public class JwtProvider {
//    static long expireTime = 1000L * 60;
    static long expireTime = 1000L * 60 * 60 * 24 * 30 * 6;
    static String secretKey = "BuTokenniMaxfiySuziHechKimBilmasin1234567890";

    Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    public String generateToken(String username, Role role) {
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .claim("role", role.getName())
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.debug("expired token {}", String.valueOf(e));
        } catch (MalformedJwtException e) {
            logger.debug("malFormed jwt exception {}", String.valueOf(e));
        } catch (UnsupportedJwtException e) {
            logger.debug("unsupported{}", String.valueOf(e));
        } catch (IllegalArgumentException e) {
            logger.debug("Illegal{}", String.valueOf(e));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}
