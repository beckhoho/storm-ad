package com.watchme.ad.bean;

import java.util.ArrayList;
import java.util.List;

public class UserAdPolicy {
	// 唯一标识
	private String curId;
	// 用户名
	private String userId;
	// 是否投放过广告
	private boolean pushFlag;
	// 广告策略LIST
	private List<AdPolicy> adPolicyList;
	private List<MessageToRedis> messagesToRedis = new ArrayList<MessageToRedis>();

	public String getCurId() {
		return curId;
	}

	public void setCurId(String curId) {
		this.curId = curId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
