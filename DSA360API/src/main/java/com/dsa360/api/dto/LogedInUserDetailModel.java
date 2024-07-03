package com.dsa360.api.dto;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author RAM
 *
 */
public class LogedInUserDetailModel {

	private String username;
	private Collection<? extends GrantedAuthority> authorities;
	private String status;
	private String token;

	public LogedInUserDetailModel() {
		// Default constructor
	}

	public LogedInUserDetailModel(String username, Collection<? extends GrantedAuthority> authorities, String status) {
		super();
		this.username = username;
		this.authorities = authorities;
		this.status = status;
	}
	
	

	public LogedInUserDetailModel(String username, Collection<? extends GrantedAuthority> authorities, String status,
			String token) {
		super();
		this.username = username;
		this.authorities = authorities;
		this.status = status;
		this.token = token;
	}

	public LogedInUserDetailModel(String username, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.username = username;
		this.authorities = authorities;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
