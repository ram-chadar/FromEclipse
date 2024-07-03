package com.dsa360.api.daoimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dsa360.api.dao.DSADao;
import com.dsa360.api.entity.DSARegistrationEntity;
import com.dsa360.api.entity.DSA_KYC_Entity;
import com.dsa360.api.exceptions.ResourceAlreadyExistsException;
import com.dsa360.api.exceptions.ResourceNotFoundException;
import com.dsa360.api.exceptions.SomethingWentWrongException;

/**
 * @author RAM
 *
 */
@Repository
public class DSADaoImpl implements DSADao {

	private static final Logger logger = LoggerFactory.getLogger(DSADaoImpl.class);

	@Autowired
	private SessionFactory factory;

	@Override
	public DSARegistrationEntity getDSAById(String dsaID) {
		DSARegistrationEntity dsaRegistrationEntity = null;
		try (Session session = factory.openSession()) {
			dsaRegistrationEntity = session.get(DSARegistrationEntity.class, dsaID);

			if (dsaRegistrationEntity != null) {
				return dsaRegistrationEntity;
			} else {
				throw new ResourceNotFoundException("Data not found with id = " + dsaID);
			}

		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			logger.error("Data not found with id = " + dsaID);
			throw new SomethingWentWrongException("Data not found with id = " + dsaID);
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occurred during get DSA with id = " + dsaID, e);
			throw new SomethingWentWrongException("Exception occurred during get DSA with id = " + dsaID);
		}
	}

	@Override
	public DSARegistrationEntity registerDSA(DSARegistrationEntity dsaRegistrationEntity) {
		Transaction transaction = null;
		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();
			session.save(dsaRegistrationEntity);
			transaction.commit();
			logger.info("DSA registration successful for: {}",
					dsaRegistrationEntity.getDsaRegistrationId() + " " + dsaRegistrationEntity.getFirstName());

			return dsaRegistrationEntity;
		} catch (PersistenceException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.getMessage();
			logger.error("Duplicate entry error occurred during DSA registration - check unique fields");
			throw new ResourceAlreadyExistsException(
					"Duplicate entry error occurred during DSA registration - check unique fields");
		}

		catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			logger.error("Exception occurred during DSA registration", e);
			throw new SomethingWentWrongException("Exception occurred during DSA registration");
		}

	}

	@Override
	public DSARegistrationEntity notifyReview(String id, String approvalStatus, String type) {
		Transaction transaction = null;
		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();

			if ("reg".equals(type)) {
				DSARegistrationEntity dsaRegistrationEntity = session.get(DSARegistrationEntity.class, id);
				dsaRegistrationEntity.setApprovalStatus(approvalStatus);
				session.update(dsaRegistrationEntity);
				transaction.commit();
				return dsaRegistrationEntity;
			} else if ("kyc".equals(type)) {
				DSA_KYC_Entity dsa_KYC_Entity = session.get(DSA_KYC_Entity.class, id);
				dsa_KYC_Entity.setApprovalStatus(approvalStatus);
				session.update(dsa_KYC_Entity);
				transaction.commit();

				getDSAById(dsa_KYC_Entity.getDsaRegistration().getDsaRegistrationId());

				return getDSAById(dsa_KYC_Entity.getDsaRegistration().getDsaRegistrationId());
			} else {
				throw new SomethingWentWrongException("Type mismatch to update review = " + type);
			}

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			logger.error("Exception occurred during notify registration review status", e);
			throw new SomethingWentWrongException("Exception occurred during notify registration review status");
		}

	}

	@Override
	public DSARegistrationEntity systemUserKyc(DSA_KYC_Entity dsa_KYC_Entity, List<Path> storedFilePaths) {
		Transaction transaction = null;
		try (Session session = factory.openSession()) {
			transaction = session.beginTransaction();

			DSA_KYC_Entity dsaKycByDsaId = getDsaKycByDsaId(dsa_KYC_Entity.getDsaRegistration().getDsaRegistrationId());
			if (dsaKycByDsaId == null) {
				dsa_KYC_Entity.setAttempt(1);
				session.save(dsa_KYC_Entity);
			} else {
				// Ensure we have the latest version from the database
				DSA_KYC_Entity managedEntity = session.get(DSA_KYC_Entity.class, dsaKycByDsaId.getDsaKycId());
				if (managedEntity != null) {
					managedEntity.setBankName(dsa_KYC_Entity.getBankName());
					managedEntity.setAccountNumber(dsa_KYC_Entity.getAccountNumber());
					managedEntity.setIfscCode(dsa_KYC_Entity.getIfscCode());
					managedEntity.setPassport(dsa_KYC_Entity.getPassport());
					managedEntity.setDrivingLicence(dsa_KYC_Entity.getDrivingLicence());
					managedEntity.setAadharCard(dsa_KYC_Entity.getAadharCard());
					managedEntity.setPanCard(dsa_KYC_Entity.getPanCard());
					managedEntity.setPhotograph(dsa_KYC_Entity.getPhotograph());
					managedEntity.setAddressProof(dsa_KYC_Entity.getAddressProof());
					managedEntity.setBankPassbook(dsa_KYC_Entity.getBankPassbook());
					managedEntity.setApprovalStatus(dsa_KYC_Entity.getApprovalStatus());
					managedEntity.setAttempt(dsaKycByDsaId.getAttempt() + 1);
					session.update(managedEntity);
				}
			}

			transaction.commit();

			return getDSAById(dsa_KYC_Entity.getDsaRegistration().getDsaRegistrationId());

		} catch (OptimisticLockException ole) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error("Optimistic locking failed during save KYC details in DB ", ole);
			throw new SomethingWentWrongException("Optimistic locking failed during save KYC details in DB");
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			// Rollback files if any error occurs in DAO layer
			for (Path path : storedFilePaths) {
				try {
					Files.deleteIfExists(path);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			logger.error("Exception occurred during save KYC details in DB ", e);
			throw new SomethingWentWrongException("Exception occurred during save KYC detailsin DB");
		}

	}

	@Override
	public DSA_KYC_Entity getDsaKycByDsaId(String dsaRegistrationId) {
		DSA_KYC_Entity dsa_KYC_Entity = null;
		try (Session session = factory.openSession()) {

			@SuppressWarnings("deprecation")
			Criteria criteria = session.createCriteria(DSA_KYC_Entity.class, dsaRegistrationId);
			criteria.createAlias("dsaRegistration", "dsaReg");
			criteria.add(Restrictions.eq("dsaReg.dsaRegistrationId", dsaRegistrationId));
			@SuppressWarnings("unchecked")
			List<DSA_KYC_Entity> list = criteria.list();
			if (!list.isEmpty()) {
				dsa_KYC_Entity = (DSA_KYC_Entity) list.get(0);
			}
			return dsa_KYC_Entity;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occurred fetch KYC data by registration id", e);
			throw new SomethingWentWrongException("Exception occurred fetch KYC data by registration id");
		}
	}

}
