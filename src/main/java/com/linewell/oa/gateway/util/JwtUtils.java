package com.linewell.oa.gateway.util;

import cn.hutool.core.date.DateTime;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author Blysin
 * @since 2019-01-11
 */
public class JwtUtils {
    private static int EMPIRE_TIME = 60 * 60 * 1000;//一小时
    private static String ISSUER = "LINEWELL";
    private static Algorithm algorithm = Algorithm.HMAC256(ISSUER);

    /**
     * 获取token
     * @param userUnid
     * @return
     */
    public static String getToken(String userUnid) {
        long time = System.currentTimeMillis() + EMPIRE_TIME;
        Date hour = new Date(time);

        return JWT.create()
                .withKeyId(userUnid)
                .withExpiresAt(hour)
                .withIssuer(ISSUER)
                .sign(algorithm);
    }

    /**
     * 校验token，返回用户id
     *
     * @param token
     * @return
     */
    public static boolean validateToken(String token) {
        boolean result = false;
        try {
            DecodedJWT jwt = JWT.decode(token);
            if (jwt.getExpiresAt().after(new Date())) {
                String userUnid = jwt.getKeyId();
                String issuer = jwt.getIssuer();
                if (StringUtils.isNotEmpty(userUnid) || StringUtils.isNotEmpty(issuer) || ISSUER.equals(issuer)) {
                    result = true;
                }
            }
        } catch (JWTVerificationException exception){
        }
        return result;
    }
}
