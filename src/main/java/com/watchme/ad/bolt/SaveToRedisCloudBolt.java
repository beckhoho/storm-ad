package com.watchme.ad.bolt;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.watchme.ad.bean.MessageToRedis;
import com.watchme.ad.bean.UserAdPolicy;
import com.watchme.ad.util.JedisServer;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class SaveToRedisCloudBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SaveToRedisCloudBolt.class);
	private static final String AD_ENTRY = "str";
	private OutputCollector collector;

	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		System.out.println("------------------->>>>>>>>>>>>>>>>进入SaveToRedisCloudBolt..........");
		// 开始时间
		long startTime = System.currentTimeMillis();

		UserAdPolicy entry = (UserAdPolicy) input.getValueByField(AD_ENTRY);
		List<MessageToRedis> redisRecords = entry.getMessagesToRedis();
		try {
			if (redisRecords.isEmpty())
				logger.warn("pair is null,so this is not data to save");
			else
				insertToRedis(redisRecords);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {

			// 结束时间
			long endTime = System.currentTimeMillis();
			float excTime = (float) (endTime - startTime) / 1000;
			System.out.println("SaveToRedisCloudBolt执行时间：" + excTime + "s");

			collector.ack(input);
		}

	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	private void insertToRedis(List<MessageToRedis> redisRecords) throws Exception {
		for (MessageToRedis message : redisRecords) {
			String key = message.getKey();
			try {
				switch (message.getType()) {
				case STRING:
					JedisServer.hsetItem(0, "ad-url", key, message.getValue().getBytes());
					break;
				default:
					break;
				}
			} catch (Exception e) {
				throw new Exception(e.getMessage() + " (error message is：" + message + ")");
			} finally {

			}
		}
	}

}
