package com.turel.services

import com.turel.config.ZookeeperManager
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by chaimturkel on 12/8/16.
  */
@Service
class ZookeeperReport extends LazyLogging{

  @Autowired
  var zookeeperManager : ZookeeperManager = _

  def displayZookeeperStatus(): Unit ={
    logger.info("****************************")
    logger.info("displaying zookeepers status")
    zookeeperManager.zookeeperInfo.getZookeeperData().foreach(zoo =>{
      logger.info(s"Host: ${zoo.connection}")
      logger.info(s"Ruko: ${zoo.props.getProperty(zoo.PropertyRuko)}")
      logger.info(s"Mode: ${zoo.props.getProperty(zoo.PropertyMode)}")
      logger.info(s"Connections: ${zoo.props.getProperty(zoo.PropertyConnections)}")
      logger.info(s"Node Count: ${zoo.props.getProperty(zoo.PropertyNodeCount)}")
      logger.info(s"Sent: ${zoo.props.getProperty(zoo.PropertySent)}")
      logger.info(s"Received: ${zoo.props.getProperty(zoo.PropertyReceived)}")
      logger.info(s"Latency: ${zoo.props.getProperty(zoo.PropertyLatency)}")
    })
    logger.info("****************************")
  }


  def displayZookeeperBrokers(): Unit ={
    logger.info("****************************")
    logger.info("displaying Brokers")
    val brokerHosts = zookeeperManager.zookeeperInfo.getBrokerHosts
    if (brokerHosts.size == 0) logger.info("no brokers")
    brokerHosts.foreach(broker => logger.info("Broker:" + broker))
    logger.info("****************************")
  }

  def displayZookeeperBrokerTopics(): Unit ={
    logger.info("****************************")
    logger.info("displaying Topics")
    val topics = zookeeperManager.zookeeperInfo.getBrokerTopics()
    if (topics.size == 0) logger.info("no Topics")
    topics.foreach(topic => logger.info("Topic:" + topic))
    logger.info("****************************")
  }

}
