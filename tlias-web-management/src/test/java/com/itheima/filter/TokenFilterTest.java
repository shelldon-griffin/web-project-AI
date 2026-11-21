package com.itheima.filter;

import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenFilterTest {

    private final TokenFilter filter = new TokenFilter();

    @Test
    void loginWithoutTokenIsAllowed() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertSame(request, chain.getRequest());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    void loginWithContextPathIsAllowed() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/tlias/login");
        request.setContextPath("/tlias");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertSame(request, chain.getRequest());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/loginHistory", "/depts/login", "/login/extra"})
    void otherPathsContainingLoginStillRequireToken(String path) throws Exception {
        assertUnauthorized(new MockHttpServletRequest("GET", path));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void missingOrBlankTokenIsRejected(String token) throws Exception {
        assertUnauthorized(protectedRequest(token));
    }

    @Test
    void malformedTokenIsRejected() throws Exception {
        assertUnauthorized(protectedRequest("not-a-jwt"));
    }

    @Test
    void expiredTokenIsRejected() throws Exception {
        // 与当前 JwtUtils 的签名密钥保持一致，确保验证的是过期情况。
        String token = Jwts.builder()
                .setSubject("expired-test")
                .signWith(SignatureAlgorithm.HS256, "SVRIRUlNQQ==")
                .setExpiration(new Date(System.currentTimeMillis() - 60_000))
                .compact();
        assertThrows(ExpiredJwtException.class, () -> JwtUtils.parseJWT(token));

        assertUnauthorized(protectedRequest(token));
    }

    @Test
    void tamperedSignatureIsRejected() throws Exception {
        String token = validToken();
        int signatureStart = token.lastIndexOf('.') + 1;
        char replacement = token.charAt(signatureStart) == 'A' ? 'B' : 'A';
        String tamperedToken = token.substring(0, signatureStart) + replacement
                + token.substring(signatureStart + 1);

        assertUnauthorized(protectedRequest(tamperedToken));
    }

    @Test
    void validTokenIsAllowed() throws Exception {
        MockHttpServletRequest request = protectedRequest(validToken());
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertSame(request, chain.getRequest());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    private String validToken() {
        return JwtUtils.generateJwt(Map.of("id", 10, "username", "itheima"));
    }

    private MockHttpServletRequest protectedRequest(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/depts");
        if (token != null) {
            request.addHeader("token", token);
        }
        return request;
    }

    private void assertUnauthorized(MockHttpServletRequest request) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertNull(chain.getRequest(), "校验失败时不能调用后续过滤链");
    }
}
