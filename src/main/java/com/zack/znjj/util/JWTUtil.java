package com.zack.znjj.util;



import com.zack.znjj.common.restful.Const;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Slf4j
public class JWTUtil {
    private static String secret;

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = secret + Const.JWT_SECRET;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建jwt
     *
     * @param uid
     * @param subject
     * @return
     * @throws Exception
     */
    public static String createJWT(Integer uid, String subject) throws Exception {
        Key key = generalKey() ;//这里是加密解密的key。
        String compactJws = Jwts.builder()//返回的字符串便是我们的jwt串了
                .setSubject(subject)//设置主题
                .claim("uid", uid)
                .signWith(SignatureAlgorithm.HS512, key)//设置算法（必须）
                .compact();//这个是全部设置完成后拼成jwt串的方法
        return compactJws;
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

}
