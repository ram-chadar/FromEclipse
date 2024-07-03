package com.dsa360.api.daoimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.dsa360.api.dao.SystemUserDao;
import com.dsa360.api.entity.SystemUser;
import com.dsa360.api.security.CustomUserDetail;

/**
 * @author RAM
 *
 */
@Repository
public class SystemUserDaoImpl implements SystemUserDao {
	private static Logger LOG = LogManager.getLogger(SystemUserDaoImpl.class);

	@Autowired
	private SessionFactory factory;

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Override
	public CustomUserDetail loadUserByUserId(String userId) {
		Session session = factory.getCurrentSession();
		CustomUserDetail user = new CustomUserDetail();
		SystemUser usr = null;
		try {
			usr = session.get(SystemUser.class, userId);
			if (usr != null) {
				user.setUserName(userId);
				user.setPassword(usr.getPassword());
				user.setRoles(usr.getRoles());
				user.setStatus(usr.getStatus());
			}
			LOG.info("Loaded User ={}", userId);
		} catch (Exception e) {
			LOG.error("Exception = {}", e.getMessage());
			e.printStackTrace();
		}
		return user;
	}

}
