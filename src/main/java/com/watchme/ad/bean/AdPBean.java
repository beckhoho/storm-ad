package com.watchme.ad.bean;

import java.io.Serializable;

/**
 * 
 * @author suzg
 * @description 广告策略bean
 *
 */
public class AdPBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pid;// 策略ID
	private int dayPutNum;// 当天投放总次数
	private String demandId;// 需求ID
	private String domainGroup;// 域组
	private String domainLimit;// 域限制
	private int priority;// 优先级
	private String putEndDate;// 结束时间
	private int putInterval;// 间隔
	private String putMode;// 投放类型
	private int putNum;// 投放总次数
	private String putStartDate;// 开始时间
	private String groupId;// 用户组ID
	private String adId;// 广告ID
	private String type;// 类型1：cp 2：移动（待定）
	private String url;// url

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getDayPutNum() {
		return dayPutNum;
	}

	public void setDayPutNum(int dayPutNum) {
		this.dayPutNum = dayPutNum;
	}

	public String getDemandId() {
		return demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	public String getDomainGroup() {
		return domainGroup;
	}

	public void setDomainGroup(String domainGroup) {
		this.domainGroup = domainGroup;
	}

	public String getDomainLimit() {
		return domainLimit;
	}

	public void setDomainLimit(String domainLimit) {
		this.domainLimit = domainLimit;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPutInterval() {
		return putInterval;
	}

	public void setPutInterval(int putInterval) {
		this.putInterval = putInterval;
	}

	public String getPutMode() {
		return putMode;
	}

	public void setPutMode(String putMode) {
		this.putMode = putMode;
	}

	public int getPutNum() {
		return putNum;
	}

	public void setPutNum(int putNum) {
		this.putNum = putNum;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPutEndDate() {
		return putEndDate;
	}

	public void setPutEndDate(String putEndDate) {
		this.putEndDate = putEndDate;
	}

	public String getPutStartDate() {
		return putStartDate;
	}

	public void setPutStartDate(String putStartDate) {
		this.putStartDate = putStartDate;
	}

}
