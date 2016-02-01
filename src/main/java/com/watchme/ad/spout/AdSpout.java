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
        String str1="tt08980000000743$08980000000743$oss1";
        String str2="tt08980000000883$08980000000883$oss2";
        String str3="tt08980000000705$08980000000705$oss3";
        String str4="tt089800000001$089800000001$oss4";
        String str5="tt08980000001281$08980000001281$oss5";
        String str6="tt08980000003733$08980000003733$oss6";
        String str7="tt08980000006805$08980000006805$oss7";
        String str8="tt08980000009386$08980000009386$oss8";
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
