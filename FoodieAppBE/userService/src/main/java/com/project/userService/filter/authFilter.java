package com.project.userService.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class authFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest  = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;

        String authHeader = httpServletRequest.getHeader("Authorization");

        System.out.println(authHeader);
        // if authHeader is null or not start with Bearer then throw exception
        if(authHeader==null|| !authHeader.startsWith("Bearer")){
            throw new ServletException("Token is missing.....");
        }
        else {
            String token = authHeader.substring(7);

            // this will provide us claims(Data) send through token by decoding it using parser()
            Claims claims = Jwts.parser().setSigningKey("xyZUser").parseClaimsJws(token).getBody();

            System.out.println("Claims = "+claims);

            httpServletRequest.setAttribute("attr1",claims.get("email"));
            httpServletRequest.setAttribute("attr2",claims.get("role"));
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
