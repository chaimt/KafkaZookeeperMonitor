package com.turel.actors.zookeeper

import java.util.Properties

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{ActorRef, OneForOneStrategy, Props}
import com.turel.actors.BaseActor
import com.turel.utils.Netcat
import com.turel.zookeeper.{ZookeeperCommands, ZookeeperConnectionInfo, ZookeeperData}
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
  * Created by chaimturkel on 12/11/16.
  */
//@Component("zookeeperManagerActor")
class ZookeeperManagerActor extends BaseActor with LazyLogging {

  @Value("${zookeeper.connections}")
  val zookeeperConnections: String = "localhost"

  var reportRequest: Option[ActorRef] = None
  var zookeepers: Map[ActorRef, ZookeeperConnectionInfo] = Map.empty
  var zookeepersStatus: Map[ActorRef, ZookeeperData] = Map.empty

  def buildChildren(): Unit ={
    logger.info("buildChildren")
    val zookeeperHosts = zookeeperConnections.split(",").toList
    zookeepers = zookeeperHosts.map(hostPort => {
      val split: Array[String] = hostPort.split(":")
      if (split.size == 1) {
        logger.info("split.size == 1")
        val actor = context.actorOf(Props(classOf[ZookeeperActor],hostPort, 2181), hostPort)
        (actor, ZookeeperConnectionInfo(hostPort, 2181))
      }
      else {
        logger.info("split.size == 1 else")
        val port = Integer.parseInt(split(1))
        val actor = context.actorOf(Props(classOf[ZookeeperActor],split(0), port),hostPort)
        (actor, ZookeeperConnectionInfo(hostPort, port))
      }
    }).toMap
  }

  override def preStart(): Unit = {
    buildChildren()
  }

  def sendReportIfFull(timeout: Boolean): Unit = {
    //send report
    if (zookeepersStatus.size == zookeepers.size || timeout) {
      reportRequest.map(_ ! ZookeeperStatusDataResponse(zookeepersStatus.values.toList))
      reportRequest = None
      zookeepersStatus = Map.empty
    }

  }

  override def receive: Receive = {

    case ZookeeperStatusRequest => {
      reportRequest = Some(sender())
      zookeepers.keys.foreach(_ ! ZookeeperStatusRequest)
      import scala.concurrent.duration._
      import scala.concurrent.ExecutionContext.Implicits.global
      context.system.scheduler.scheduleOnce(1 seconds, self, Timeout)
    }

    case ZookeeperStatusResponse(props) => {
      val zookeeperInfo: ZookeeperConnectionInfo = zookeepers(sender())
      zookeepersStatus = zookeepersStatus + (sender() -> ZookeeperData(zookeeperInfo.connectionStr, props))
      sendReportIfFull(false)
    }

    case Timeout => {
      sendReportIfFull(true)
    }

  }

  import scala.concurrent.duration._

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 minute) {
      case _: Exception => Restart
    }
}
