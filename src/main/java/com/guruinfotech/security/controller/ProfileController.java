package com.guruinfotech.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.guruinfotech.security.config.MyUserDetailService;
import com.guruinfotech.security.model.AuthenticationRequest;
import com.guruinfotech.security.model.AuthenticationResponse;
import com.guruinfotech.security.util.JwtUtil;

@RestController
public class ProfileController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;

	@GetMapping("/index")
	public String get() {
		return "Profile Data Your Accessing";
	}
	
	@GetMapping("/glogout")
	public String logout() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("LOgout successfully for this user :"+name);
		userDetailsService.map.remove(name);
		return "logout Success";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		userDetailsService.map.put(userDetails.getUsername(),jwt );
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	} 
}
