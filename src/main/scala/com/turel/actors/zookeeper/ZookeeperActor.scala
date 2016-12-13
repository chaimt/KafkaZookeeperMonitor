package com.turel.actors.zookeeper

import java.util.Properties

import com.turel.actors.BaseActor
import com.turel.utils.Netcat
import com.turel.zookeeper.ZookeeperCommands
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.zookeeper.ZooKeeper

/**
  * Created by chaimturkel on 12/11/16.
  */
class ZookeeperActor(host : String, port : Int) extends BaseActor with LazyLogging{

  private val keeper = new ZooKeeper(host, 10000, null)

  override def receive: Receive = {
    case ZookeeperCommandRequest(command) => {
      sender() ! ZookeeperCommandResponse(Netcat.run(command, host, port))
    }
    case ZookeeperStatusRequest => {
      val props = new Properties()
      val ruokRes = Netcat.run(ZookeeperCommands.ruok, host, port)
      val statRes = Netcat.run(ZookeeperCommands.stat, host, port)
      statRes.split("\n").foreach(value => {
        val split = value.split(":")
        if (split.size > 1)
          props.put(split(0), split(1))
      })
      sender() ! ZookeeperStatusResponse(props)
    }
  }
}
