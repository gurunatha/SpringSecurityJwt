package com.guruinfotech.security.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.guruinfotech.security.model.UserModel;
import com.guruinfotech.security.repository.UserRepository;

@Service
public class MyUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder pass;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel findByUsername = repo.findByUsername(username);
		return new User(findByUsername.getUsername(), pass.encode(findByUsername.getPassword()), new ArrayList<>());
	}

}
