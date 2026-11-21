package com.itheima.interceptor;

import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//自定义拦截器(@Component 交给 IOC 容器管理，以后使用对象中的方法，不用 new 一个新对象)
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    //目标资源方法执行前执行。 返回true：放行    返回false：不放行（此时就不会执行对应接口的服务）
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 获取应用内的请求路径
        String requestURI = request.getRequestURI();

        // 2. 路径包含 login 就放行。
        if (requestURI.contains("/login")) {
            log.info("登录请求，直接放行");
            return true; // 放行代码。
        }

        // 3. 按当前前端约定，从 token 请求头中读取 JWT。
        String jwt = request.getHeader("token");

        // 4. null、空字符串、只有空白字符的令牌都视为未登录。
        if (!StringUtils.hasText(jwt)) {
            log.info("JWT 令牌为空，拒绝访问");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
            return false;
        }

        // 5. 解析时会校验签名和有效期；格式错误、被篡改或已过期的令牌均不放行。
        try {
            JwtUtils.parseJWT(jwt);  //校验令牌
        } catch (JwtException | IllegalArgumentException e) {
            // 记录失败类型，不输出完整令牌；无效令牌属于预期的校验失败。
            log.info("JWT 令牌校验失败，拒绝访问：{}", e.getClass().getSimpleName());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 6. 令牌有效，将请求交给后续过滤器或 Controller 继续处理。
        log.info("令牌有效，放行");
        return true; //true表示放行
    }

    //目标资源方法执行后执行(不执行也没关系)
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle ... ");
    }

    //视图渲染完毕后执行，最后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion .... ");
    }
}