package com.watchme.ad.bean;

import java.io.Serializable;

public class UserGroupRelationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serialNumber;
	private String groupIds;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

}
