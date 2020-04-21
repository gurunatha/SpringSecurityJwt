package com.guruinfotech.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERTOKEN")
@Data
@NoArgsConstructor
public class UserToken {
	@Id
	@GeneratedValue
	private Integer id;
	private String username;
	private String token;
	private String issuedDate;
	private String expiryTime;
	public UserToken(String username, String token, String issuedDate, String expiryTime) {
		super();
		this.username = username;
		this.token = token;
		this.issuedDate = issuedDate;
		this.expiryTime = expiryTime;
	}
	

}
