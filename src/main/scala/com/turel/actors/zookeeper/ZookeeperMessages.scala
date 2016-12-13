package com.turel.actors.zookeeper

import java.util.Properties

import com.turel.zookeeper.ZookeeperData

/**
  * Created by chaimturkel on 12/11/16.
  */
case class ZookeeperCommandRequest(command : String)
case class ZookeeperCommandResponse(result : String)
case object ZookeeperStatusRequest
case class ZookeeperStatusResponse(props : Properties)
case class ZookeeperStatusDataResponse(data : List[ZookeeperData])

case object Timeout

