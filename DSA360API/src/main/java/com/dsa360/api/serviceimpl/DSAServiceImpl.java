package com.dsa360.api.serviceimpl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsa360.api.dao.DSADao;
import com.dsa360.api.dto.DSARegistrationDTO;
import com.dsa360.api.dto.DSA_KYC_DTO;
import com.dsa360.api.entity.DSARegistrationEntity;
import com.dsa360.api.entity.DSA_KYC_Entity;
import com.dsa360.api.exceptions.ResourceNotFoundException;
import com.dsa360.api.service.DSAService;
import com.dsa360.api.utility.DynamicID;
import com.dsa360.api.utility.FileStorageUtility;
import com.dsa360.api.utility.MailAsyncServices;
import com.dsa360.api.utility.ObjectConverter;

/**
 * @author RAM
 *
 */
@Service
public class DSAServiceImpl implements DSAService {

	@Autowired
	private DSADao dao;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	MailAsyncServices mailAsyncServices;

	@Autowired
	private FileStorageUtility fileStorageUtility;

	@Autowired
	private ObjectConverter converter;

	@Override
	public DSARegistrationDTO registerDSA(DSARegistrationDTO dsaRegistrationDTO) throws MessagingException {
		dsaRegistrationDTO.setDsaRegistrationId(DynamicID.getDynamicId());

		DSARegistrationEntity dsaRegistrationEntity = mapper.map(dsaRegistrationDTO, DSARegistrationEntity.class);

		DSARegistrationEntity registerDSA = dao.registerDSA(dsaRegistrationEntity);
		if (registerDSA != null) {

			mailAsyncServices.sendRegistrationConfirmationEmail(dsaRegistrationDTO);

			return dsaRegistrationDTO;

		}
		return null;

	}

	@Override
	public DSARegistrationDTO getDSAById(String dsaRegId) {

		DSARegistrationEntity dsaEntity = dao.getDSAById(dsaRegId);
		DSARegistrationDTO dsaDto = mapper.map(dsaEntity, DSARegistrationDTO.class);

		return dsaDto;
	}

	@Override
	public String notifyReview(String registrationId, String approvalStatus, String type) {
		DSARegistrationEntity entity = dao.notifyReview(registrationId, approvalStatus, type);

		mailAsyncServices.dsaReviewMail(entity.getEmailAddress(), entity.getFirstName() + " " + entity.getLastName(),
				approvalStatus, type);

		return approvalStatus;
	}

	@Override
	public String systemUserKyc(DSA_KYC_DTO kyc_DTO) {

		DSARegistrationDTO dsaRegDTO = getDSAById(kyc_DTO.getDsaRegistrationId());

		if (dsaRegDTO != null) {
			String kycId = DynamicID.getDynamicId();
			kyc_DTO.setDsaKycId(kycId);

			List<Path> storedFilePaths = fileStorageUtility.storeFiles(kyc_DTO.getDsaRegistrationId(),
					kyc_DTO.getPassportFile(), kyc_DTO.getDrivingLicenceFile(), kyc_DTO.getAadharCardFile(),
					kyc_DTO.getPanCardFile(), kyc_DTO.getPhotographFile(), kyc_DTO.getAddressProofFile(),
					kyc_DTO.getBankPassbookFile());

			DSA_KYC_Entity entity = (DSA_KYC_Entity) converter.dtoToEntity(kyc_DTO);

			DSARegistrationEntity dsaById = dao.systemUserKyc(entity, storedFilePaths);

			List<String> docs = new ArrayList<String>();
			docs.add(entity.getAadharCard());
			docs.add(entity.getAddressProof());
			docs.add(entity.getBankPassbook());
			docs.add(entity.getDrivingLicence());
			docs.add(entity.getPanCard());
			docs.add(entity.getPassport());

			mailAsyncServices.sendKycSubmittedEmail(dsaById.getEmailAddress(), kycId, dsaById.getDsaRegistrationId(),
					dsaById.getFirstName() + " " + dsaById.getLastName(), dsaById.getContactNumber(),
					dsaById.getStreetAddress(), docs);
		} else {
			throw new ResourceNotFoundException("Invalid DSA Registration Id = " + kyc_DTO.getDsaRegistrationId());
		}

		return "KYC Submitted";
	}

}
