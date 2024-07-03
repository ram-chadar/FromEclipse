package com.dsa360.api.dao;

import java.nio.file.Path;
import java.util.List;

import com.dsa360.api.entity.DSARegistrationEntity;
import com.dsa360.api.entity.DSA_KYC_Entity;

public interface DSADao {
	public abstract DSARegistrationEntity getDSAById(String dsaID);

	public abstract DSARegistrationEntity registerDSA(DSARegistrationEntity dsaRegistrationEntity);

	public abstract DSARegistrationEntity notifyReview(String registrationId, String approvalStatus,String type);

	public abstract DSARegistrationEntity systemUserKyc(DSA_KYC_Entity dsa_KYC_Entity,List<Path> storedFilePaths );

	public abstract DSA_KYC_Entity getDsaKycByDsaId(String dsaRegistrationId);

}
