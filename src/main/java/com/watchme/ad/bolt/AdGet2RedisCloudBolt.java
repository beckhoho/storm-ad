package com.watchme.ad.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.jsonbeans.Json;
import com.watchme.ad.bean.AdPBean;
import com.watchme.ad.bean.AdPolicy;
import com.watchme.ad.bean.UserAdPolicy;
import com.watchme.ad.util.JedisUtils;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 从redis获取数据
 * 
 * @author wangqiang
 *
 */
public class AdGet2RedisCloudBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	public static final String AD_ENTRY = "str";
	Logger logger = LoggerFactory.getLogger(AdGet2RedisCloudBolt.class);

	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		System.out.println("------------------->>>>>>>>>>>>>>>>进入AdGet2RedisCloudBolt..........");
		// 开始时间
		long startTime = System.currentTimeMillis();

		String data = (String) input.getString(0);
		data = data.trim();
		if (!"".equals(data) && data != null) {

			// 拼装用户广告信息
			UserAdPolicy userAdPolicy = getuserAdPolicy(data);

			collector.emit(new Values(userAdPolicy));

		}

		// 结束时间
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		System.out.println("AdSortBolt执行时间：" + excTime + "s");

		collector.ack(input);

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(AD_ENTRY));

	}
	
	/**
	 * 获取用户广告策略信息
	 * @param data
	 * @return
	 */
	private UserAdPolicy getuserAdPolicy(String data) {
		UserAdPolicy userAdPolicy = new UserAdPolicy();
		List<AdPolicy> adPolicyList = new ArrayList<AdPolicy>();
		Json json = new Json();

		String[] values = data.split("\\$");
		String curNumber = values[0]; // 唯一流水
		String serialNumber = values[1]; // 用户流水

		System.out.println("serialNumber = " + serialNumber);

		/**
		 * 第一步：根据用户流水获取组信息 第二步：再根据用户组获取广告策略信息
		 * 
		 */
		List<String> groupIds = JedisUtils.getList(serialNumber);
		for (String groupId : groupIds) {
			// 获取广告策略信息
			List<String> AdPJsonStrs = JedisUtils.getList(groupId);
			for (String AdPJsonStr : AdPJsonStrs) {
				AdPolicy adPolicy = new AdPolicy();
				// 转成对象
				AdPBean adPBean = json.fromJson(AdPBean.class, AdPJsonStr);

				adPolicy.setAdId(adPBean.getAdId());
				adPolicy.setDayPutNum(adPBean.getDayPutNum());
				adPolicy.setDemandId(adPBean.getDemandId());
				adPolicy.setDomainGroup(adPBean.getDomainGroup());
				adPolicy.setDomainLimit(adPBean.getDomainLimit());
				adPolicy.setGroupId(adPBean.getGroupId());
				adPolicy.setPid(adPBean.getPid());
				adPolicy.setPriority(adPBean.getPriority());
				adPolicy.setPutEndDate(adPBean.getPutEndDate());
				adPolicy.setPutInterval(adPBean.getPutInterval());
				adPolicy.setPutMode(adPBean.getPutMode());
				adPolicy.setPutNum(adPBean.getPutNum());
				adPolicy.setPutStartDate(adPBean.getPutStartDate());
				adPolicy.setType(adPBean.getType());
				adPolicy.setUrl(adPBean.getUrl());
				
				//这里再获取截至现在的次数信息
				int curDayPutNum = 3;
				int curPutNum = 20;
				adPolicy.setCurDayPutNum(curDayPutNum);
				adPolicy.setCurPutNum(curPutNum);
				
				adPolicyList.add(adPolicy);
			}
		}
		userAdPolicy.setCurNumber(curNumber);
		userAdPolicy.setSerialNumber(serialNumber);
		userAdPolicy.setAdPolicyList(adPolicyList);

		System.out.println("=======================原始数据============================");
		for (int i = 0; i < adPolicyList.size(); i++) {
			System.out.println("计数=" + i + ",内容：serialNumber=" + serialNumber + ",putNum="
					+ adPolicyList.get(i).getPutNum() + ",dayPutNum=" + adPolicyList.get(i).getDayPutNum() + ",url="
					+ adPolicyList.get(i).getUrl());
		}
		System.out.println("=======================原始数据============================");

		return userAdPolicy;
	}

}
