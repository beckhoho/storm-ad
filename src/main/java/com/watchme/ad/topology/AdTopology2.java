package com.watchme.ad.topology;

import com.watchme.ad.bolt.AdEsperBolt;
import com.watchme.ad.bolt.AdGet2RedisCloudBolt;
import com.watchme.ad.bolt.AdRulesBolt;
import com.watchme.ad.bolt.AdSortBolt;
import com.watchme.ad.bolt.SaveToRedisCloudBolt;
import com.watchme.ad.spout.AdSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class AdTopology2 {

	private static AdSpout ADSPOUT = new AdSpout();
	private static AdGet2RedisCloudBolt ADGET2REDISCLOUDBOLT = new AdGet2RedisCloudBolt();
	private static AdSortBolt ADSORTBOLT = new AdSortBolt();
	private static AdEsperBolt ADESPERBOLT = new AdEsperBolt();
	private static AdRulesBolt ADRULESBOLT = new AdRulesBolt();
	private static SaveToRedisCloudBolt SAVETOREDISCLOUDBOLT = new SaveToRedisCloudBolt();

	public static void main(String[] args) {
		try {

			TopologyBuilder builder = new TopologyBuilder();
			builder.setSpout("adSpout", ADSPOUT, 1);
			builder.setBolt("adGet2RedisBolt", ADGET2REDISCLOUDBOLT, 1).shuffleGrouping("adSpout");
			builder.setBolt("adSortBolt", ADSORTBOLT, 1).shuffleGrouping("adGet2RedisBolt");
			builder.setBolt("adEsperBolt", ADESPERBOLT, 1).shuffleGrouping("adSortBolt");
			builder.setBolt("adRulesBolt", ADRULESBOLT, 1).shuffleGrouping("adEsperBolt");
			builder.setBolt("saveToRedisBolt", SAVETOREDISCLOUDBOLT, 1).shuffleGrouping("adRulesBolt");

			Config config = new Config();
			config.setDebug(true);
			if (args != null && args.length > 0) {
				config.setNumWorkers(1);
				StormSubmitter.submitTopology(args[0], config, builder.createTopology());
			} else {
				LocalCluster cluster = new LocalCluster();
				cluster.submitTopology("AdTopology", config, builder.createTopology());
				Utils.sleep(500000);
				cluster.killTopology("AdTopology");
				cluster.shutdown();
			}
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
		}
	}

}
