package com.dsa360.api.service;

import javax.mail.MessagingException;

import org.springframework.transaction.annotation.Transactional;

import com.dsa360.api.dto.DSARegistrationDTO;
import com.dsa360.api.dto.DSA_KYC_DTO;

/**
 * @author RAM
 *
 */
@Transactional
public interface DSAService {
	
	public abstract DSARegistrationDTO getDSAById(String dsaID);

	public abstract DSARegistrationDTO registerDSA(DSARegistrationDTO dsaRegistrationDTO) throws MessagingException;

	public abstract String notifyReview(String registrationId, String approvalStatus,String type);

	public abstract String systemUserKyc(DSA_KYC_DTO kyc_DTO);
}
