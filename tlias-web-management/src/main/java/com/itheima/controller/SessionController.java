package com.itheima.controller;

import com.itheima.pojo.Result;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * HttpSession演示
 */
@Slf4j
@RestController
public class SessionController {

    //设置Cookie   存储到浏览器，cookie是一个键值对，就是我们下面新建cookie对象中的值
    @GetMapping("/c1")
    public Result cookie1(HttpServletResponse response){
        response.addCookie(new Cookie("login_username","itheima")); //设置Cookie/响应Cookie
        return Result.success();
    }

    //获取Cookie，会把当前服务器地址和端口的全部cookies都携带上（在请求头中）
    @GetMapping("/c2")
    public Result cookie2(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("login_username")){
                System.out.println("login_username: "+cookie.getValue()); //输出name为login_username的cookie
            }
        }
        return Result.success();
    }


    // nginx（前端部署的服务器）可以作为一个负载均衡服务器，由它来决定这个请求是到哪个后端服务器上！！！
    // Session 编号：abc123  （一个session例子，只有这个编号会发给客户端，
    // 作为cookie键值对的值 JSESSIONID=abc123 。只会存储到一个服务器上）
    //
    //里面保存：
    //userId = 100
    //username = 张三
    @GetMapping("/s1")  // session 通常只传 Session ID，会话数据留在服务器
    public Result session1(HttpSession session){
        log.info("HttpSession-s1: {}", session.hashCode());

        session.setAttribute("loginUser", "tom"); //往session中存储数据
        return Result.success();
    }

    @GetMapping("/s2")
    public Result session2(HttpSession session){
        log.info("HttpSession-s2: {}", session.hashCode());

        Object loginUser = session.getAttribute("loginUser"); //从session中获取数据
        log.info("loginUser: {}", loginUser);
        return Result.success(loginUser);
    }
}
