package com.watchme.ad.bolt;

import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.watchme.ad.bean.UserAdPolicy;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class AdRulesBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	public static final String AD_ENTRY = "str";
	Logger logger = LoggerFactory.getLogger(AdRulesBolt.class);

	private KieSession kSession;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		kSession = kContainer.newKieSession("ksession-rules");

	}

	@Override
	public void execute(Tuple input) {
		System.out.println("------------------->>>>>>>>>>>>>>>>进入AdRulesBolt..........");
		//开始时间
		long startTime=System.currentTimeMillis();
		
		UserAdPolicy userAdPolicy = (UserAdPolicy) input.getValues().get(0);
		if (!"".equals(userAdPolicy) && userAdPolicy != null) {
			System.out.println("AdRuleBolt:CurId="+userAdPolicy.getCurId());
			try {
				kSession.insert(userAdPolicy);
				kSession.fireAllRules();
			} catch (Exception e) {
				logger.error("drools to handle log [" + userAdPolicy + "] is failure !");
				logger.error(e.getMessage());
			}
			collector.emit(new Values(userAdPolicy));
		} else {
			logger.error("log content is empty !");
		}
		
		//结束时间
		long endTime=System.currentTimeMillis();
		float excTime=(float)(endTime-startTime)/1000;
		System.out.println("AdRulesBolt执行时间："+excTime+"s");
		
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(AD_ENTRY));
	}

}
