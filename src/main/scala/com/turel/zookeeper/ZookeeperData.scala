package com.turel.zookeeper

import java.util.Properties

import scala.beans.BeanProperty

/**
  * Created by chaimturkel on 12/8/16.
  */
case class ZookeeperData(@BeanProperty connection : String, @BeanProperty props : Properties) {
  val PropertyMode = "Mode"
  val PropertyReceived = "Received"
  val PropertySent = "Sent"
  val PropertyConnections = "Connections"
  val PropertyOutstanding = "Outstanding"
  val PropertyNodeCount = "Node count"
  val PropertyLatency = "Latency min/avg/max"
  val PropertyRuko = ZookeeperCommands.ruok

}
