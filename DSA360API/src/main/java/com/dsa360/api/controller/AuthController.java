package com.dsa360.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsa360.api.aspect.TrackExecutionTime;
import com.dsa360.api.dto.LogedInUserDetailModel;
import com.dsa360.api.security.CustomUserDetail;
import com.dsa360.api.security.CustomUserDetailService;
import com.dsa360.api.service.SystemUserService;
import com.dsa360.api.utility.JwtUtil;
/**
 * @author RAM
 *
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
	private static Logger LOG = LogManager.getLogger(AuthController.class);

	@Autowired
	SystemUserService userService;

	@Autowired
	CustomUserDetailService customUserDetailService;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	// completed
	@PostMapping("/login-user")
	@TrackExecutionTime
	public ResponseEntity<LogedInUserDetailModel> loginAdmin(@RequestParam String username, @RequestParam String password)
			throws AuthenticationException {
		
		LOG.info("Trying to login = {}", username);

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
				
		SecurityContextHolder.getContext().setAuthentication(authentication); // check 
		
		CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
		
		LOG.info("Logged In = {}", username);
		
		final String token = jwtUtil.generateToken(authentication);
		
		LogedInUserDetailModel model=new LogedInUserDetailModel(userDetail.getUsername(), userDetail.getAuthorities(),userDetail.getStatus(),token);

		return new ResponseEntity<LogedInUserDetailModel> (model,HttpStatus.OK);

	}

}
