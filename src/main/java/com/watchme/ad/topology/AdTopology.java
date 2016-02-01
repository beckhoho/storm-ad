package com.watchme.ad.topology;

import com.google.common.collect.ImmutableList;
import com.watchme.ad.bolt.AdEsperBolt;
import com.watchme.ad.bolt.AdGet2RedisCloudBolt;
import com.watchme.ad.bolt.AdRulesBolt;
import com.watchme.ad.bolt.AdSortBolt;
import com.watchme.ad.bolt.SaveToRedisCloudBolt;
import com.watchme.ad.util.MessageScheme;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class AdTopology {

	private static AdGet2RedisCloudBolt ADGET2REDISCLOUDBOLT = new AdGet2RedisCloudBolt();
	private static AdSortBolt ADSORTBOLT = new AdSortBolt();
	private static AdEsperBolt ADESPERBOLT = new AdEsperBolt();
	private static AdRulesBolt ADRULESBOLT = new AdRulesBolt();
	private static SaveToRedisCloudBolt SAVETOREDISCLOUDBOLT = new SaveToRedisCloudBolt();

	public static void main(String[] args) {
		try {
			String kafkaZookeeper = "master:2181,slave1:2181,slave2:2181";
			BrokerHosts brokerHosts = new ZkHosts(kafkaZookeeper);

			SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, "ad-topic2", "/ad2", "id");
			kafkaConfig.scheme = new SchemeAsMultiScheme(new MessageScheme());
			kafkaConfig.zkServers = ImmutableList.of("master", "slave1", "slave2");
			kafkaConfig.zkPort = 2181;
			KafkaSpout kafkaSpout = new KafkaSpout(kafkaConfig);

			TopologyBuilder builder = new TopologyBuilder();
			builder.setSpout("adSpout", kafkaSpout, 2);
			builder.setBolt("adGet2RedisBolt", ADGET2REDISCLOUDBOLT, 2).shuffleGrouping("adSpout");
			builder.setBolt("adSortBolt", ADSORTBOLT, 2).shuffleGrouping("adGet2RedisCloudBolt");
			builder.setBolt("adEsperBolt", ADESPERBOLT, 2).shuffleGrouping("adSortBolt");
			builder.setBolt("adRulesBolt", ADRULESBOLT, 2).shuffleGrouping("adEsperBolt");
			builder.setBolt("saveToRedisBolt", SAVETOREDISCLOUDBOLT, 2).shuffleGrouping("adRulesBolt");

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
