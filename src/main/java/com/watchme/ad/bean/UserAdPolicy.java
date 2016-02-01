package com.watchme.ad.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserAdPolicy implements Serializable {
	private static final long serialVersionUID = 1L;
	// 唯一标识
	private String curNumber;
	// 用户流水
	private String serialNumber;
	// 是否投放过广告
	private boolean pushFlag;
	// 广告策略LIST
	private List<AdPolicy> adPolicyList;
	private List<MessageToRedis> messagesToRedis = new ArrayList<MessageToRedis>();

	public String getCurNumber() {
		return curNumber;
	}

	public void setCurNumber(String curNumber) {
		this.curNumber = curNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public boolean isPushFlag() {
		return pushFlag;
	}

	public void setPushFlag(boolean pushFlag) {
		this.pushFlag = pushFlag;
	}

	public List<AdPolicy> getAdPolicyList() {
		return adPolicyList;
	}

	public void setAdPolicyList(List<AdPolicy> adPolicyList) {
		this.adPolicyList = adPolicyList;
	}

	public List<MessageToRedis> getMessagesToRedis() {
		return messagesToRedis;
	}

	public void setMessagesToRedis(List<MessageToRedis> messagesToRedis) {
		this.messagesToRedis = messagesToRedis;
	}

}
