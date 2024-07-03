package com.dsa360.api.utility;

import org.springframework.stereotype.Component;

import com.dsa360.api.dto.DSA_KYC_DTO;
import com.dsa360.api.entity.DSARegistrationEntity;
import com.dsa360.api.entity.DSA_KYC_Entity;

@Component
public class ObjectConverter {


	public Object dtoToEntity(Object sourceObject) {
		DSA_KYC_Entity entity = null;
		if (sourceObject instanceof DSA_KYC_DTO) {
			DSA_KYC_DTO dto = (DSA_KYC_DTO) sourceObject;
			DSARegistrationEntity dsaRegistrationEntity = new DSARegistrationEntity();
			dsaRegistrationEntity.setDsaRegistrationId(dto.getDsaRegistrationId());
			entity = new DSA_KYC_Entity();
			entity.setDsaKycId(dto.getDsaKycId());
			entity.setDsaRegistration(dsaRegistrationEntity);
			entity.setBankName(dto.getBankName());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setIfscCode(dto.getIfscCode());

			entity.setPassport(dto.getPassportFile().getOriginalFilename());
			entity.setDrivingLicence(dto.getDrivingLicenceFile().getOriginalFilename());
			entity.setAadharCard(dto.getAadharCardFile().getOriginalFilename());
			entity.setPanCard(dto.getPanCardFile().getOriginalFilename());
			entity.setPhotograph(dto.getPhotographFile().getOriginalFilename());
			entity.setAddressProof(dto.getAddressProofFile().getOriginalFilename());
			entity.setBankPassbook(dto.getBankPassbookFile().getOriginalFilename());

			entity.setApprovalStatus(dto.getApprovalStatus());

		}

		return entity;

	}

}
