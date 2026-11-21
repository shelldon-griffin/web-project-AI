package com.itheima.filter;

import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 登录校验过滤器：登录接口直接放行，其他请求需要携带有效的 JWT 令牌。
 */

//@Component // 由 Spring 管理，Spring Boot 会自动将这个 Filter 注册到 Servlet 容器。
@Slf4j
//@WebFilter(urlPatterns = "/*")  // 所有请求都拦截，2. /emps/*，访问/emps下的所有资源，都会被拦截
public class TokenFilter implements Filter {

    // 容器初始化过滤器时执行一次，可在这里准备过滤器需要的资源。
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("TokenFilter 初始化完成");
    }

    // 每次匹配到该过滤器的请求都会执行这个方法。
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 转换为 HTTP 对象，才能读取请求头、请求路径和设置 HTTP 状态码。（请求体中包括 JWT 令牌 Token）
        // 局部变量使用不同名称，避免与方法参数 request、response 重名。
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. 获取应用内的请求路径
        String requestURI = httpRequest.getRequestURI();

        // 2. 路径包含 login 就放行。
        if (requestURI.contains("/login")) {
            log.info("登录请求，直接放行");
            chain.doFilter(request, response);  // 放行代码
            return; // 放行后结束本次校验，避免继续检查登录请求的 token。
        }

        // 3. 按当前前端约定，从 token 请求头中读取 JWT。
        String jwt = httpRequest.getHeader("token");

        // 4. null、空字符串、只有空白字符的令牌都视为未登录。
        if (!StringUtils.hasText(jwt)) {
            log.info("JWT 令牌为空，拒绝访问");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
            return;
        }

        // 5. 解析时会校验签名和有效期；格式错误、被篡改或已过期的令牌均不放行。
        try {
            JwtUtils.parseJWT(jwt);  //校验令牌
        } catch (JwtException | IllegalArgumentException e) {
            // 记录失败类型，不输出完整令牌；无效令牌属于预期的校验失败。
            log.info("JWT 令牌校验失败，拒绝访问：{}", e.getClass().getSimpleName());
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 6. 令牌有效，将请求交给后续过滤器或 Controller 继续处理。
        log.info("令牌有效，放行");
        chain.doFilter(request, response);
    }

    // 容器销毁过滤器前执行一次，可在这里释放资源。
    @Override
    public void destroy() {
        log.info("TokenFilter 已销毁");
    }
}
