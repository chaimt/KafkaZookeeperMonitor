package com.turel.zookeeper

import java.io.IOException
import java.util
import java.util.Objects

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.turel.kafka.BrokersData
import kafka.utils.ZkUtils
import org.apache.zookeeper.{KeeperException, ZooKeeper}

/**
  * Created by chaimturkel on 12/6/16.
  */
class KafkaBrokersList {
  private def getBrokersList(zookeeper: ZooKeeper, brokerIds: List[String]): List[BrokersData] = {
    val objectMapper = new ObjectMapper
    brokerIds.map(id => {
      try{
        val brokerInfo: JsonNode = objectMapper.readTree(zookeeper.getData(ZkUtils.BrokerIdsPath + "/" + id, false, null))
        BrokersData(id,brokerInfo.at("/host").asText(),brokerInfo.at("/port").asText(),brokerInfo.at("/version").asText())
      }
      catch {
        case _ : KeeperException => null
        case _: InterruptedException => null
        case _ : IOException => null
      }
    }).filter(Objects.nonNull).distinct
  }

  private def getTopicList(zookeeper: ZooKeeper, brokerIds: List[String]): List[String] = {
    val objectMapper = new ObjectMapper
    brokerIds.map(id => {
      try{
        val brokerInfo: JsonNode = objectMapper.readTree(zookeeper.getData(ZkUtils.BrokerTopicsPath, false, null))
        "ID - %s. Host - %s : %s [V-%s]".format(id,brokerInfo.at("/host").asText(), brokerInfo.at("/port").asText(),brokerInfo.at("/version").asText())
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
      import collection.JavaConverters._
      val firstActive = zookeepers.values.iterator.next
      val children: util.List[String] = firstActive.getChildren(ZkUtils.BrokerIdsPath, false)
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

  def getBrokerTopics(zookeepers: Map[String, ZooKeeper]): List[String] = {
    fetchPath(zookeepers,ZkUtils.BrokerIdsPath,getTopicList)
  }


}
