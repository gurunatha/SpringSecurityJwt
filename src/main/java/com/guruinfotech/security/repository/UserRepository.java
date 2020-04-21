package com.guruinfotech.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guruinfotech.security.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer>{

	UserModel findByUsername(String username);

}
