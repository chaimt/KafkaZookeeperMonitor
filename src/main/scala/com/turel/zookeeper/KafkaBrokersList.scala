package com.turel.zookeeper

import java.io.IOException
import java.util
import java.util.Objects

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.turel.kafka.{BrokersData, PartitionData, TopicsData}
import com.turel.utils.JsonUtils
import kafka.utils.ZkUtils
import org.apache.zookeeper.{KeeperException, ZooKeeper}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * Created by chaimturkel on 12/6/16.
  */
class KafkaBrokersList {
  private def getBrokersList(zookeeper: ZooKeeper, brokerIds: List[String]): List[BrokersData] = {
    val objectMapper = new ObjectMapper
    brokerIds.map(id => {
      try{
        val brokerInfo: JsonNode = objectMapper.readTree(zookeeper.getData(ZkUtils.BrokerIdsPath+s"/$id", false, null))
        BrokersData(id,brokerInfo.at("/host").asText(),brokerInfo.at("/port").asText(),brokerInfo.at("/version").asText())
      }
      catch {
        case _ : KeeperException => null
        case _: InterruptedException => null
        case _ : IOException => null
      }
    }).filter(Objects.nonNull).distinct
  }

  private def getTopicList(zookeeper: ZooKeeper, topics: List[String]): List[TopicsData] = {
    val objectMapper = new ObjectMapper
    topics
      .filter(_ != "__consumer_offsets") //ignore kafka topics
      .map(topic => {
      try{
        val topicInfo: JsonNode = objectMapper.readTree(zookeeper.getData(ZkUtils.BrokerTopicsPath + s"/$topic", false, null))
        val partitions = topicInfo.at("/partitions").elements()
        var  paritions : ArrayBuffer[PartitionData] = ArrayBuffer()
        while (partitions.hasNext){
          val next : ArrayNode = partitions.next().asInstanceOf[ArrayNode]
          if (next.size()>0) {
            val node = next.get(0)
            val partitionId = node.asText()
            val state: JsonNode = objectMapper.readTree(zookeeper.getData(s"/brokers/topics/$topic/partitions/$partitionId/state", false, null))

            if (state != null) {
              paritions += PartitionData(JsonUtils.getText(state, "controller_epoch"), JsonUtils.getText(state, "leader"), JsonUtils.getText(state, "leader_epoch"), JsonUtils.getText(state, "isr"))
            }
          }
        }
        TopicsData(topic,topicInfo.at("/version").asText(),paritions.asJava)
      }
      catch {
        case _ : KeeperException => null
        case _: InterruptedException => null
        case _ : IOException => null
      }
    }).filter(Objects.nonNull).distinct
  }


  private def fetchPath[T](zookeepers: Map[String, ZooKeeper], path : String, funcData:(ZooKeeper,List[String]) => List[T]): List[T] ={
    if (zookeepers==null || zookeepers.values.size == 0)
      List.empty
    else{
      val firstActive = zookeepers.values.iterator.next
      val children: util.List[String] = firstActive.getChildren(path, false)
      val data = funcData(firstActive, children.asScala.toList)
      if (data==null)
        List.empty
      else
        data
    }
  }

  def getBrokerHosts(zookeepers: Map[String, ZooKeeper]): List[BrokersData] = {
    fetchPath(zookeepers,ZkUtils.BrokerIdsPath,getBrokersList)
  }

  def getBrokerTopics(zookeepers: Map[String, ZooKeeper]): List[TopicsData] = {
    fetchPath(zookeepers,ZkUtils.BrokerTopicsPath,getTopicList)
  }


}
