package com.dsa360.api.notification;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dsa360.api.exceptions.SomethingWentWrongException;

/**
 * @author RAM
 *
 */
@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public void dsaRegistrationConfirmationMail(String to, String dsaName, String dsaId, String registeredName,
			String contactInfo) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			Context context = new Context();
			context.setVariable("dsaName", dsaName);
			context.setVariable("dsaId", dsaId);
			context.setVariable("registeredName", registeredName);
			context.setVariable("contactInfo", contactInfo);

			String html = templateEngine.process("dsa-registration", context);

			helper.setFrom(sender, "DSA 360 Solution");
			helper.setTo(to);
			helper.setSubject("Welcome to DSA 360 Solution: DSA Registration Confirmed!");
			helper.setText(html, true);

			mailSender.send(message);
		} catch (MailSendException | MailAuthenticationException | MessagingException
				| UnsupportedEncodingException e) {

			throw new SomethingWentWrongException("Failed to send the email", e);

		}

	}

	@Override
	public void dsaKycConfirmationMail(String to, String kycId, String dsaId, String dsaName, String contact,
			String address, List<String> docs) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			Context context = new Context();
			context.setVariable("dsaName", dsaName);
			context.setVariable("dsaId", dsaId);
			context.setVariable("kycId", kycId);
			context.setVariable("registeredName", dsaName);
			context.setVariable("email", to);
			context.setVariable("contactNumber", contact);
			context.setVariable("address", address);
			context.setVariable("docs", docs);

			String html = templateEngine.process("kyc_submission", context);

			helper.setFrom(sender, "DSA 360 Solution");
			helper.setTo(to);
			helper.setSubject("KYC Submission Confirmed - " + dsaId);
			helper.setText(html, true);

			mailSender.send(message);
		} catch (MailSendException | MailAuthenticationException | MessagingException
				| UnsupportedEncodingException e) {

			throw new SomethingWentWrongException("Failed to send the email", e);

		}

	}

	@Override
	public void dsaReviewMail(String to, String dsaName, String reviewStatus, String type) {
		String html = null;
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			Context context = new Context();
			context.setVariable("dsaName", dsaName);
			context.setVariable("reviewStatus", reviewStatus);
			if ("reg".equals(type)) {
				html = templateEngine.process("registration_review", context);
				helper.setSubject("DSA Registration : " + reviewStatus);

			} else if ("kyc".equals(type)) {
				html = templateEngine.process("kyc_review", context);
				helper.setSubject("DSA KYC : " + reviewStatus);

			}

			helper.setFrom(sender, "DSA 360 Solution");
			helper.setTo(to);
			helper.setText(html, true);

			mailSender.send(message);
		} catch (MailSendException | MailAuthenticationException | MessagingException
				| UnsupportedEncodingException e) {
			throw new SomethingWentWrongException("Failed to send the email", e);

		}
	}

}
