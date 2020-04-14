package com.guruinfotech.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.guruinfotech.security.config.MyUserDetailService;
import com.guruinfotech.security.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter{
	@Autowired
	private MyUserDetailService myUserDetailService;
	@Autowired
	private JwtUtil util;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String tokenHeader = request.getHeader("Authorization");
		String username = null;
		String token= null;
		if(tokenHeader!=null && tokenHeader.startsWith("Bearer ")) {
			token = tokenHeader.substring(7);
			username = util.extractUsername(token);
		}
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails loadUserByUsername = myUserDetailService.loadUserByUsername(username);
			
			if(myUserDetailService.map.containsKey(loadUserByUsername.getUsername()) && util.validateToken(token, loadUserByUsername)) {
				System.out.println("Token Validated");
				System.out.println("Expire :"+util.extractExpiration(token));
				UsernamePasswordAuthenticationToken utoken = new UsernamePasswordAuthenticationToken(loadUserByUsername, null, loadUserByUsername.getAuthorities());
				utoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(utoken);
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
