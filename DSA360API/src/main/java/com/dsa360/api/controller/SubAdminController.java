package com.dsa360.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsa360.api.service.DSAService;

@RestController
@RequestMapping("/sub-admin")
public class SubAdminController {

	@Autowired
	private DSAService dsaService;

	@GetMapping("/notify-approval-status")
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_SUB-ADMIN')")
	public ResponseEntity<String> notifyReview(@RequestParam String registrationId,
			@RequestParam String approvalStatus,String type) {

		String status = dsaService.notifyReview(registrationId, approvalStatus,type);
		return new ResponseEntity<String>(status, HttpStatus.OK);

	}

}
