package com.guruinfotech.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guruinfotech.security.model.UserToken;
import com.guruinfotech.security.repository.UserTokenRepository;
import com.guruinfotech.security.util.JwtUtil;

@Service
public class UserTokenService {
	@Autowired
	private UserTokenRepository userTokenRepo;
	
	public UserToken saveUserToken(String Jwt) {
		JwtUtil util = new JwtUtil();
		Date extractIssuedDate = util.extractIssuedDate(Jwt);
		Date extractExpiration = util.extractExpiration(Jwt);
		String extractUsername = util.extractUsername(Jwt);
		UserToken token = new UserToken(extractUsername, Jwt, extractIssuedDate.toString(), extractExpiration.toString());
		UserToken save = userTokenRepo.save(token);
		return save;
	}
	
	public Long deleteToken(String name) {
		UserToken findByUsername = userTokenRepo.findByUsername(name);
		
		try {
			 userTokenRepo.deleteById(findByUsername.getId());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return (long) 1;
	}
	
	public UserToken findByName(String name) {
		UserToken findByUsername = userTokenRepo.findByUsername(name);
		return findByUsername;
	}

}
