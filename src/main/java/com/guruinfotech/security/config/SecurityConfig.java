package com.guruinfotech.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.guruinfotech.security.filter.CustomFilter;
import com.guruinfotech.security.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtFilter filter;
	@Autowired
	private CustomFilter customFilter;

	@Autowired
	private UserDetailsService myUserDEtailService;

	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin();
		http.csrf().disable().authorizeRequests().antMatchers("/h2-console*/**","/registration","/authenticate", "/index").permitAll().anyRequest()
				.authenticated().and().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		// http.addFilter(filter); // Error Becoze Before or after need to be set

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDEtailService);
	}

}
