package com.watchme.ad.util;

import java.util.Comparator;

import com.watchme.ad.bean.AdPolicy;

public class AdComparator<T> implements Comparator<T> {

	@Override
	public int compare(Object o1, Object o2) {
		AdPolicy adPolicy1 = (AdPolicy) o1;
		AdPolicy adPolicy2 = (AdPolicy) o2;
		/**
		 * 排序的规则可以在这里定义
		 */
		Integer order1 = adPolicy1.getOrder();
		Integer order2 = adPolicy2.getOrder();
		return order1.compareTo(order2);
	}
	
	
}
