package com.dsa360.api.notification;

import java.util.List;

/**
 * @author RAM
 *
 */
public interface NotificationService {

	public void dsaRegistrationConfirmationMail(String to, String dsaName, String dsaId, String registeredName, String contactInfo);

	public void dsaReviewMail(String to, String dsaName, String reviewStatus, String type);

	public void dsaKycConfirmationMail(String to,String kycId,String dsaId,String dsaName,String contact,String address,List<String> docs);
}
