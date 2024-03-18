package security.jwt;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * jwt快速开始
 *
 * @author fulin-peng
 * 2024-03-18  15:54
 */
public class JwtQuick {

    static String secretString = "yourSecretKeyYourSecretKeyYourSecretKeyYourSec"; // 确保密钥长度符合要求

    public static void main(String[] args) {
        String jwt = generate(secretString);
        System.out.println("Generated JWT: " + jwt);
        Jws<Claims> claims = resolve(jwt);
        System.out.println("Subject: " + claims.getBody().getSubject());
        System.out.println("Expiration: " + claims.getBody().getExpiration());
    }

    /**
     * 生成JWT
     * 2024/3/18 0018 15:55
     * @author fulin-peng
     */
    public static String generate(String secretString){
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 3600000; // 设置过期时间为1小时
        Date exp = new Date(expMillis);
        // 对于 HMAC ,使用你的安全密钥。使用正确的长度对于安全性至关重要。
        SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
        return Jwts.builder()
                .setSubject("user123") // 设置主题或者说用户ID
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(exp) // 设置过期时间
                .signWith(secretKey) // 设置签名算法和密钥
                .compact();
    }

    /**
     * 解析JWT
     * 2024/3/18 0018 17:35
     * @author fulin-peng
     */
    public static Jws<Claims> resolve(String jwt){
        SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt);
    }

}
