package com.watchme.ad.bean;

import java.io.Serializable;
import java.sql.Date;

public class AdPolicy implements Serializable{
	private static final long serialVersionUID = 1L;
	// 投放总次数
	private int putNum;
	// 每天推送次数
	private int dayPutNum;
	// 投放时间间隔
	private int putInterval;
	// 投送开始时间
	private Date putStartDate;
	// 投送结束时间
	private Date putEndDate;
	// 广告url
	private String url;
	// 排序
	private int order;

	public int getPutNum() {
		return putNum;
	}

	public void setPutNum(int putNum) {
		this.putNum = putNum;
	}

	public int getDayPutNum() {
		return dayPutNum;
	}

	public void setDayPutNum(int dayPutNum) {
		this.dayPutNum = dayPutNum;
	}

	public int getPutInterval() {
		return putInterval;
	}

	public void setPutInterval(int putInterval) {
		this.putInterval = putInterval;
	}

	public Date getPutStartDate() {
		return putStartDate;
	}

	public void setPutStartDate(Date putStartDate) {
		this.putStartDate = putStartDate;
	}

	public Date getPutEndDate() {
		return putEndDate;
	}

	public void setPutEndDate(Date putEndDate) {
		this.putEndDate = putEndDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
