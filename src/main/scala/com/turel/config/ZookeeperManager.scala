package com.turel.config

import java.util
import javax.annotation.PostConstruct

import com.turel.zookeeper.ZookeeperInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
  * Created by chaimturkel on 12/8/16.
  */
@Component
class ZookeeperManager {
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  @Value("${zookeeper.connections}")
  private val zookeeperConnections: String = null

  var zookeeperInfo : ZookeeperInfo = _

  @PostConstruct
  def init(): Unit ={
    logger.info("Zookeeper Connections: " + zookeeperConnections)
    val asList: util.List[String] = util.Arrays.asList(zookeeperConnections)
    import collection.JavaConverters._
    zookeeperInfo = new ZookeeperInfo(util.Arrays.asList(zookeeperConnections).asScala.toList)
  }

}
