package com.watchme.ad.spout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class AdSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	public static final String LOG_ENTRY = "str";
    private  int curIdx=0;
    private SpoutOutputCollector collector;
    private List<String> logEntry1s;

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
        this.logEntry1s=getJsonObj();
	}

	@Override
	public void nextTuple() {
		if(curIdx<logEntry1s.size())
        {
            String entry=logEntry1s.get(curIdx).trim();
             collector.emit(new Values(entry));
             curIdx++;
         }

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(LOG_ENTRY));

	}
	
	
	public List<String> getJsonObj()
    {
        List<String> list=new ArrayList<String>();
        String str1=System.currentTimeMillis()+"$22100120$oss1";
        String str2=System.currentTimeMillis()+"$22100121$oss2";
        String str3=System.currentTimeMillis()+"$22100122$oss3";
        String str4=System.currentTimeMillis()+"$22100123$oss4";
        String str5=System.currentTimeMillis()+"$22100124$oss5";
        String str6=System.currentTimeMillis()+"$22100125$oss6";
        String str7=System.currentTimeMillis()+"$22100126$oss7";
        String str8=System.currentTimeMillis()+"$22100127$oss8";
        list.add(str1);
        list.add(str2);
        list.add(str3);
        list.add(str4);
        list.add(str5);
        list.add(str6);
        list.add(str7);
        list.add(str8);
        return  list;
    }
}
