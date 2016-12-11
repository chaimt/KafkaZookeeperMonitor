package com.turel.zookeeper

import java.util.Properties
import java.util.concurrent.atomic.AtomicBoolean

import com.turel.kafka.{BrokersData, TopicsData}
import com.turel.utils.Netcat
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.zookeeper.ZooKeeper

import scala.util.Try

/**
  * Created by chaimturkel on 12/6/16.
  */
class ZookeeperManager(zookeeperHosts: List[String]) extends LazyLogging {
  var zookeepers: Map[String, Try[ZooKeeper]] = Map.empty
  val zookeeperParams: List[ZookeeperConnectionInfo] = {
    zookeeperHosts.map(hostPort => {
      val split: Array[String] = hostPort.split(":")
      if (split.size == 1) {
        ZookeeperConnectionInfo(hostPort, 2181)
      }
      else {
        ZookeeperConnectionInfo(split(0), Integer.parseInt(split(1)))
      }
    })
  }
  val built = new AtomicBoolean(false)


  def activeZookeepers = {
    zookeepers.filter(item => item._2.isSuccess).map(item => (item._1, item._2.get))
  }

  def buildZookeepers() {
//    if (!built.getAndSet(true)) {
      zookeepers = zookeeperHosts.map(host => (host, Try(new ZooKeeper(host, 10000, null)))).toMap
//    }
  }

  def getBrokerHosts(): List[BrokersData] = {
    buildZookeepers()
    (new KafkaBrokersList).getBrokerHosts(activeZookeepers)
  }

  def getBrokerTopics(): List[TopicsData] = {
    buildZookeepers()
    (new KafkaBrokersList).getBrokerTopics(activeZookeepers)
  }

  def getZookeeperData(): List[ZookeeperData] = {
    zookeeperParams.map(connecion => {
      val prop = new Properties()
      val ruokRes = Netcat.run(ZookeeperCommands.ruok, connecion.host, connecion.port)
      prop.put(ZookeeperCommands.ruok, ruokRes)
      val statRes = Netcat.run(ZookeeperCommands.stat, connecion.host, connecion.port)
      statRes.split("\n").foreach(value => {
        val split = value.split(":")
        if (split.size > 1)
          prop.put(split(0), split(1))
      })

      ZookeeperData("%s:%d".format(connecion.host, connecion.port),prop)
    })
  }

}
