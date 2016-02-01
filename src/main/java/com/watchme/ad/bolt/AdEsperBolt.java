package com.watchme.ad.bolt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.watchme.ad.bean.UserAdPolicy;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class AdEsperBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	public static final String AD_ENTRY = "str";
	Logger logger = LoggerFactory.getLogger(AdEsperBolt.class);

	private EPServiceProvider epService;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.setUpEsper();

	}

	@Override
	public void execute(Tuple input) {
		System.out.println("------------------->>>>>>>>>>>>>>>>进入AdEsperBolt..........");
		// 开始时间
		long startTime = System.currentTimeMillis();

		Object entry = input.getValueByField(AD_ENTRY);
		epService.getEPRuntime().sendEvent(entry);
		collector.emit(new Values(entry));

		// 结束时间
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		System.out.println("AdEsperBolt执行时间：" + excTime + "s");

		collector.ack(input);

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(AD_ENTRY));
	}

	private void setUpEsper() {
		Configuration configuration = new Configuration();
		configuration.addEventType("UserAdPolicy", UserAdPolicy.class.getName());

		epService = EPServiceProviderManager.getDefaultProvider(configuration);
		epService.initialize();

		String epl = "select curNumber, serialNumber, adPolicyList from UserAdPolicy.win:length_batch(1)";

		EPStatement visitorsStatement = epService.getEPAdministrator().createEPL(epl);
		visitorsStatement.addListener(new UpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				if (newEvents != null) {
					for (EventBean e : newEvents) {
						// 这里可以定义一些处理方法
					}
				}
			}

		});
	}

}
