package com.guruinfotech.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
public class UserModel {
	@Id
	@GeneratedValue
	private Integer id;
	private String username;
	private String password;
	private String email;
	private String phoneNumber;
	
	public UserModel(String name, String password) {
		super();
		this.username = name;
		this.password = password;
	}
	

}
