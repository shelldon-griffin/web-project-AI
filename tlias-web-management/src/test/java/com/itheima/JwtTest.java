package com.itheima;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    //整个JWT令牌，分为3个部分，使用 . 隔开。前两个部分使用base64编码，最后一个部分是校验，使用签名算法得到
    //生成JWT令牌
    @Test
    public void testGenJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 10);  //添加信息
        claims.put("username", "itheima");

        //使用HS256签名算法，后面另一个字符串的是密钥
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, "aXRjYXN0")
                .addClaims(claims) // 添加自定义信息（需要Map类型的数据）
                .setExpiration(new Date(System.currentTimeMillis() + 2 * 3600 * 1000)) //指定令牌有效时期 2个小时
                .compact();  // 生成令牌

        System.out.println(jwt);
    }


    //解析JWT令牌，令牌被篡改或者过期会报错
    @Test
    public void testParseJwt() {
        //这里的密钥，必须和我们创建时使用的一样
        Claims claims = Jwts.parser().setSigningKey("aXRjYXN0")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoiaXRoZWltYSIsImV4cCI6MTc5NTI1MDUzMX0.GBlqeQleVwPFAY3wUG8Y6W7wqQtgvuwcdSTY73olHeM")
                .getBody();
        // {id=10, username=itheima, exp=1795250531}
        System.out.println(claims);
    }

}
