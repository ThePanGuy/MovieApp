package com.myprojects.demo.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.myprojects.demo.dto.DecodedData;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class JwtUtilities {
    public static DecodedData tryToDecode(String token, boolean refresh) {
        DecodedData data = new DecodedData();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        data.setUsername(decodedJWT.getSubject());
        if (!refresh) {
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Arrays.stream(roles).forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role));
            });
            data.setAuthorities(authorities);
        }
        return data;
    }

    public static String createAccessToken(String username, List<String> roles, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public static String createRefreshToken(UserDetails user, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public static Map<String, String> generateTokensMap(String access_token, String refresh_token) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        return tokens;
    }
}
