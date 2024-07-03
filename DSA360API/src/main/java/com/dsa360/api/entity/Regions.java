package com.dsa360.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author RAM
 */
@Entity
@Table(name = "regions")
public class Regions {
	
	@Id
	@Column(name = "region_id",nullable = false,unique = true)
	private long regionId;
	
	@Column(name = "region_name",nullable = false,unique = true)
	private String regionName;
	
	@Column(name = "region_code",nullable = false,unique = true)
	private String regionCode;
	
	@ManyToOne
	@JoinColumn(name = "system_user_id")
	@JsonIgnore
	private SystemUser systemUser;
	
	public Regions() {
		// TODO Auto-generated constructor stub
	}

	public Regions(long regionId, String regionName, String regionCode, SystemUser systemUser) {
		super();
		this.regionId = regionId;
		this.regionName = regionName;
		this.regionCode = regionCode;
		this.systemUser = systemUser;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	
	
	

}
