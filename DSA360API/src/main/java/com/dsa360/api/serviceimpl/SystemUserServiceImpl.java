package com.dsa360.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dsa360.api.constants.UserStatus;
import com.dsa360.api.dao.SystemUserDao;
import com.dsa360.api.exceptions.UserDeactivatedException;
import com.dsa360.api.exceptions.UserSuspendedException;
import com.dsa360.api.security.CustomUserDetail;
import com.dsa360.api.service.SystemUserService;

/**
 * @author RAM
 *
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {

	@Autowired
	public BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private SystemUserDao dao;

	@Override
	public CustomUserDetail loadUserByUserId(String userId) {
		CustomUserDetail customUserDetail = dao.loadUserByUserId(userId);

		String status = customUserDetail.getStatus();

		if (status.equalsIgnoreCase(UserStatus.DEACTIVATED.getValue())) {
			throw new UserDeactivatedException(UserStatus.DEACTIVATED);

		}
		if (status.equalsIgnoreCase(UserStatus.SUSPENDED.getValue())) {
			throw new UserSuspendedException(UserStatus.SUSPENDED);
		}

		return customUserDetail;

	}

}
