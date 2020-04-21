package com.guruinfotech.security.controller;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;

import com.guruinfotech.security.config.MyUserDetailService;
import com.guruinfotech.security.model.AuthenticationRequest;
import com.guruinfotech.security.model.AuthenticationResponse;
import com.guruinfotech.security.model.Registration;
import com.guruinfotech.security.model.UserModel;
import com.guruinfotech.security.model.UserToken;
import com.guruinfotech.security.repository.UserRepository;
import com.guruinfotech.security.service.UserTokenService;
import com.guruinfotech.security.util.JwtUtil;

@RestController
public class ProfileController {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	private UserTokenService userTokenService;
	
	@GetMapping("/index")
	public String get() {
		
		return "Profile Data Your Accessing";
	}
	@GetMapping("/secure")
	public String secure() {
		
		return "Secure Data";
	}
	
	
	@GetMapping("/glogout")
	public String logout() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("Logout successfully for this user :"+name);
		Long deleteToken = userTokenService.deleteToken(name);
		System.out.println(deleteToken);
		return "logout Success";
	}
	@PostMapping("/registration")
	public String registration(@RequestBody @Valid Registration reg) {
		if(reg.getPassword().equals(reg.getCnfmPassword())) {
			UserModel u = new UserModel();
			u.setUsername(reg.getUsername());
			u.setPassword(reg.getPassword());
			u.setPhoneNumber(reg.getPhonenumber());
			u.setEmail(reg.getEmail());
			repo.save(u);
		}
		
		return "Registraton Success";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		UserModel model = new UserModel(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		System.out.println(SecurityContextHolder.getContext().getAuthentication());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		UserToken saveUserToken = userTokenService.saveUserToken(jwt);
		if(saveUserToken==null) {
			throw new RuntimeException("Exception in while saving token");
		}
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	} 
}
