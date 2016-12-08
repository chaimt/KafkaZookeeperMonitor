package com.turel.config

import javax.annotation.PostConstruct

import com.turel.zookeeper.ZookeeperManager
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
  * Created by chaimturkel on 12/8/16.
  */
@Component
class ZookeeperConfig extends LazyLogging{

  @Value("${zookeeper.connections}")
  var zookeeperConnections: String = null

  var zookeeperManager : ZookeeperManager = _

  @PostConstruct
  def init(): Unit ={
    logger.info("Zookeeper Connections: " + zookeeperConnections)
    zookeeperManager = new ZookeeperManager(zookeeperConnections.split(",").toList)
  }

}
