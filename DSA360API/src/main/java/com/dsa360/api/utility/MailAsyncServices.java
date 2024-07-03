package com.dsa360.api.utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.dsa360.api.dto.DSARegistrationDTO;
import com.dsa360.api.exceptions.SomethingWentWrongException;
import com.dsa360.api.notification.NotificationService;

@Component
public class MailAsyncServices {

	@Autowired
	private NotificationService notificationService;

	@Async("asyncExecutor")
	public void sendRegistrationConfirmationEmail(DSARegistrationDTO dsaRegistrationDTO) {
		String to = dsaRegistrationDTO.getEmailAddress();
		String dsaName = dsaRegistrationDTO.getFirstName() + " " + dsaRegistrationDTO.getLastName();
		String dsaId = String.valueOf(dsaRegistrationDTO.getDsaRegistrationId());
		String registeredName = dsaRegistrationDTO.getFirstName() + " " + dsaRegistrationDTO.getMiddleName() + " "
				+ dsaRegistrationDTO.getLastName();
		String contactInfo = dsaRegistrationDTO.getContactNumber();

		try {
			notificationService.dsaRegistrationConfirmationMail(to, dsaName, dsaId, registeredName, contactInfo);
		}

		catch (SomethingWentWrongException e) {
			Throwable cause = e.getCause();
			throw new SomethingWentWrongException(e.getMessage(), cause);
		}
	}

	@Async("asyncExecutor")
	public void sendKycSubmittedEmail(String to, String kycId, String dsaId, String dsaName, String contact,
			String address, List<String> docs) {

		try {
			notificationService.dsaKycConfirmationMail(to, kycId, dsaId, dsaName, contact, address, docs);
		}

		catch (SomethingWentWrongException e) {
			Throwable cause = e.getCause();
			throw new SomethingWentWrongException(e.getMessage(), cause);
		}
	}

	@Async("asyncExecutor")
	public void dsaReviewMail(String to, String dsaName, String reviewStatus, String type) {

		try {
			notificationService.dsaReviewMail(to, dsaName, reviewStatus, type);

		} catch (SomethingWentWrongException e) {
			Throwable cause = e.getCause();
			throw new SomethingWentWrongException(e.getMessage(), cause);
		}

	}

}
