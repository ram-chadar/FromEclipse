package com.dsa360.api.controller;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dsa360.api.dto.DSARegistrationDTO;
import com.dsa360.api.dto.DSA_KYC_DTO;
import com.dsa360.api.dto.DSA_KYC_JSON_BODY;
import com.dsa360.api.service.DSAService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author RAM
 *
 */
@RestController
@RequestMapping("/public")
public class PublicApiController {

	@Autowired
	private DSAService dsaService;

	@PostMapping("/register-dsa")
	public ResponseEntity<DSARegistrationDTO> registerDto(@RequestBody @Valid DSARegistrationDTO dsaRegistrationDTO)
			throws MessagingException {

		DSARegistrationDTO dsaRegistration = dsaService.registerDSA(dsaRegistrationDTO);
		return new ResponseEntity<DSARegistrationDTO>(dsaRegistration, HttpStatus.CREATED);

	}

	@GetMapping("/get-dsa-registartion/{dsaId}")
	public ResponseEntity<DSARegistrationDTO> getDsaRegistrationData(@PathVariable String dsaId) {

		DSARegistrationDTO dsaById = dsaService.getDSAById(dsaId);

		return new ResponseEntity<DSARegistrationDTO>(dsaById, HttpStatus.OK);

	}

	@PostMapping("/syatem-user-kyc")
	public ResponseEntity<String> systemUserKyc(@RequestParam MultipartFile passport,
			@RequestParam MultipartFile drivingLicence, @RequestParam MultipartFile aadharCard,
			@RequestParam MultipartFile panCard, @RequestParam MultipartFile photograph,
			@RequestParam MultipartFile addressProof, @RequestParam MultipartFile bannkPassbook,

			@RequestParam String jsonBody) {

		ObjectMapper objectMapper = new ObjectMapper();
		DSA_KYC_DTO dsa_KYC_DTO = new DSA_KYC_DTO();
		DSA_KYC_JSON_BODY obj = null;
		try {
			obj = objectMapper.readValue(jsonBody, DSA_KYC_JSON_BODY.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		dsa_KYC_DTO.setDsaRegistrationId(obj.getDsaRegistrationId());
		dsa_KYC_DTO.setBankName(obj.getBankName());
		dsa_KYC_DTO.setAccountNumber(obj.getAccountNumber());
		dsa_KYC_DTO.setIfscCode(obj.getIfscCode());

		dsa_KYC_DTO.setPassportFile(passport);
		dsa_KYC_DTO.setDrivingLicenceFile(drivingLicence);
		dsa_KYC_DTO.setAadharCardFile(aadharCard);
		dsa_KYC_DTO.setPanCardFile(panCard);
		dsa_KYC_DTO.setPhotographFile(photograph);
		dsa_KYC_DTO.setAddressProofFile(addressProof);
		dsa_KYC_DTO.setBankPassbookFile(bannkPassbook);

		String message = dsaService.systemUserKyc(dsa_KYC_DTO);

		return ResponseEntity.ok(message);

	}

}
