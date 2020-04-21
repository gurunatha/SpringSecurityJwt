package com.guruinfotech.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.guruinfotech.security.model.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Integer>{

	UserToken findByUsername(String name);
	void deleteById(Integer id);
	
}
