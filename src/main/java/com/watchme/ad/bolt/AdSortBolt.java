package com.watchme.ad.bolt;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.watchme.ad.bean.AdComparator;
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
public class AdSortBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	public static final String AD_ENTRY = "str";
	Logger logger = LoggerFactory.getLogger(AdSortBolt.class);

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

		UserAdPolicy entry = (UserAdPolicy) input.getValueByField(AD_ENTRY);
		List<AdPolicy> adPolicys = entry.getAdPolicyList();

		// 排序
		/**
		 * 这里简单的通过order字段来排序
		 */
		Collections.sort(adPolicys, new AdComparator<AdPolicy>());
		
		collector.emit(new Values(entry));
		
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
