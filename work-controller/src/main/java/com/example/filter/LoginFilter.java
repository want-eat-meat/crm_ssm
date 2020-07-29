package com.example.filter;

import com.example.pojo.TblUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        TblUser user = (TblUser) req.getSession().getAttribute("USER");
        String uri = req.getRequestURI();
        if(uri.contains("login") || uri.contains("/image") || uri.contains("/jquery") || uri.endsWith(".ico")){
            chain.doFilter(req, resp);
        }else{
            if(user != null){
                chain.doFilter(req, resp);
            }else{
                resp.sendRedirect(req.getContextPath() + "/login.html");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
