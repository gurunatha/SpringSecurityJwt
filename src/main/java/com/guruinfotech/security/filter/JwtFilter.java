package com.guruinfotech.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.guruinfotech.security.config.MyUserDetailService;
import com.guruinfotech.security.model.UserToken;
import com.guruinfotech.security.service.UserTokenService;
import com.guruinfotech.security.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private MyUserDetailService myUserDetailService;
	@Autowired
	private UserTokenService service;

	@Autowired
	private JwtUtil util;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String tokenHeader = request.getHeader("Authorization");
		String username = null;
		String token = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Authentication in Jwt Filter :" + authentication);
		if (authentication == null || authentication.getName().equalsIgnoreCase("anonymousUser")) {
			if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
				token = tokenHeader.substring(7);
				username = util.extractUsername(token);
			}
			System.out.println(authentication);
			if (username != null && authentication == null) {
				UserDetails loadUserByUsername = myUserDetailService.loadUserByUsername(username);
				UserToken userToken = service.findByName(username);
				if (userToken!=null && userToken.getToken().equals(token) && util.validateToken(token, loadUserByUsername)) {
					System.out.println("Token Validated");
					System.out.println("Expire :" + util.extractExpiration(token));
					UsernamePasswordAuthenticationToken utoken = new UsernamePasswordAuthenticationToken(
							loadUserByUsername, null, loadUserByUsername.getAuthorities());
					utoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(utoken);
				}
			}
		}
		filterChain.doFilter(request, response);

	}

}
