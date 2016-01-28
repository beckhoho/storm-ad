package com.watchme.ad.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.watchme.ad.bean.AdPolicy;
import com.watchme.ad.bean.UserAdPolicy;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 排序bolt
 * 
 * @author wangqiang
 *
 */
public class AdGet2RedisBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	public static final String AD_ENTRY = "str";
	Logger logger = LoggerFactory.getLogger(AdGet2RedisBolt.class);

	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	@Override
	public void execute(Tuple input) {
		System.out.println("------------------->>>>>>>>>>>>>>>>进入AdSortBolt..........");
		// 开始时间
		long startTime = System.currentTimeMillis();

		String data = (String) input.getString(0);
		data = data.trim();
		if (!"".equals(data) && data != null) {

			final UserAdPolicy userAdPolicy = new UserAdPolicy();
			List<AdPolicy> adPolicyList = new ArrayList<AdPolicy>();

			String[] urls = { "www.baidu.com", "www.sina.com", "www.qq.com", "www.letv.com", "www.360.com" };
			Random r = new Random();

			String[] values = data.split("\\$");
			String curId = values[0];
			String userId = values[1];

			// 从redis里面获取其他信息
			for (int i = 5; i < 12; i++) {
				AdPolicy adPolicy = new AdPolicy();
				adPolicy.setPutNum(i - 5);
				adPolicy.setDayPutNum(i - 2);
				adPolicy.setUrl(urls[r.nextInt(5)]);
				adPolicyList.add(adPolicy);
			}

			userAdPolicy.setCurId(curId);
			userAdPolicy.setUserId(userId);
			userAdPolicy.setAdPolicyList(adPolicyList);
			System.out.println("=======================原始数据============================");
			for (int i = 0; i < adPolicyList.size(); i++) {
				System.out.println("计数=" + i + ",内容：userId=" + userId + ",putNum=" + adPolicyList.get(i).getPutNum()
						+ ",dayPutNum=" + adPolicyList.get(i).getDayPutNum() + ",url=" + adPolicyList.get(i).getUrl());
			}
			System.out.println("=======================原始数据============================");

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

}
