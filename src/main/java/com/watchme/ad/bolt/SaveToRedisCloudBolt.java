package com.watchme.ad.bolt;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.watchme.ad.bean.MessageToRedis;
import com.watchme.ad.bean.UserAdPolicy;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

public class SaveToRedisCloudBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SaveToRedisCloudBolt.class);
	private static final String LOG_ENTRY = "str";
	private int expireTime = 3600 * 24 * 7;// 過期時間 7天
	private OutputCollector collector;

	private Jedis jedis;

	public void insertToRedis(List<MessageToRedis> redisRecords) throws Exception {
		for (MessageToRedis message : redisRecords) {
			String key = message.getKey();
			try {
				switch (message.getType()) {
				case LONG:
					jedis.incrBy(key, Long.parseLong(message.getValue()));
					break;
				case STRING:
					jedis.set(key, message.getValue());
					break;
				case SET:
					jedis.zadd(key, Double.parseDouble(message.getValue()), message.getValue());
					break;
				default:
					break;
				}
				jedis.expire(key, expireTime);
			} catch (Exception e) {
				throw new Exception(e.getMessage() + " (error message is：" + message + ")");
			}
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.jedis = new Jedis("192.168.75.131", 6379);
	}

	@Override
	public void execute(Tuple input) {
		System.out.println("------------------->>>>>>>>>>>>>>>>进入SaveToRedisCloudBolt..........");
		//开始时间
		long startTime=System.currentTimeMillis();
		
		UserAdPolicy entry = (UserAdPolicy) input.getValueByField(LOG_ENTRY);
		List<MessageToRedis> redisRecords = entry.getMessagesToRedis();
		try {
			if (redisRecords.isEmpty())
				logger.warn("pair is null,so this is not data to save");
			else
				insertToRedis(redisRecords);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			
			
			//结束时间
			long endTime=System.currentTimeMillis();
			float excTime=(float)(endTime-startTime)/1000;
			System.out.println("SaveToRedisCloudBolt执行时间："+excTime+"s");
			
			collector.ack(input);
		}
		
	}

}
