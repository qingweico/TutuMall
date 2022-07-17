package cn.qingweico.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

/**
 * @author zqw
 * @date 2021/11/10
 */
public class JwtUtils {

    private static final int EXPIRATION = 5 * 1000;
    private static final String SIGNATURE = "admin";

    public static String createToken(String username) {
        JwtBuilder jwtBuilder = Jwts.builder();
        return jwtBuilder.setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("username", username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, SIGNATURE)
                .compact();
    }

    public static void parse(String token) {
        JwtParser parser = Jwts.parser();
        parser.setSigningKey(SIGNATURE).parseClaimsJws(token);
    }
}
