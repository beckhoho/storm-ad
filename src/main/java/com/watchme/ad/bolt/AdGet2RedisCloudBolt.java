package com.watchme.ad.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.watchme.ad.bean.AdPolicy;
import com.watchme.ad.bean.UserAdPolicy;
import com.watchme.ad.util.JedisUtil;
import com.watchme.ad.util.SerializationUtil;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import redis.clients.jedis.Jedis;

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
	
	private Jedis jedis;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.jedis = JedisUtil.getPool("192.168.75.131", 6379).getResource();
	}

	@Override
	public void execute(Tuple input) {
		System.out.println("------------------->>>>>>>>>>>>>>>>进入AdGet2RedisCloudBolt..........");
		// 开始时间
		long startTime = System.currentTimeMillis();

		String data = (String) input.getString(0);
		data = data.trim();
		if (!"".equals(data) && data != null) {

			UserAdPolicy userAdPolicy = new UserAdPolicy();
			List<AdPolicy> adPolicyList = new ArrayList<AdPolicy>();

			String[] values = data.split("\\$");
			String curId = values[0]; // 唯一流水
			String userId = values[1]; // 用户名
			
			byte[] bs = jedis.get(userId.getBytes());
			adPolicyList = (List<AdPolicy>) SerializationUtil.deserialize(bs);
			
			userAdPolicy.setCurId(curId);
			userAdPolicy.setUserId(userId);
			userAdPolicy.setAdPolicyList(adPolicyList);

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
