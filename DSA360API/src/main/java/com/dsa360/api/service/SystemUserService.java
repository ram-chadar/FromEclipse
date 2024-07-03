package com.dsa360.api.service;

import org.springframework.transaction.annotation.Transactional;
import com.dsa360.api.security.CustomUserDetail;

@Transactional
public interface SystemUserService {
	public abstract CustomUserDetail loadUserByUserId(String userId);

}
