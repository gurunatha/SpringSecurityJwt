package com.guruinfotech.security.model;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Registration {
	@NotNull
	@NotEmpty
	private String username;
	@Email
	@NotNull
	private String email;
	private String phonenumber;
	private String password;
	private String cnfmPassword;

}
