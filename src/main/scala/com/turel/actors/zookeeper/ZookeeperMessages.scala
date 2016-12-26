package com.turel.actors.zookeeper

import java.util.Properties

import com.turel.zookeeper.ZookeeperData

/**
  * Created by chaimturkel on 12/11/16.
  */
trait ZookeeperListRequest
trait ZookeeperListResponse[A]{
  val data : List[A]
}

case class ZookeeperCommandRequest(command : String)
case class ZookeeperCommandResponse(result : String)
case object ZookeeperStatusRequest extends ZookeeperListRequest
case class ZookeeperStatusResponse(props : Properties)
case class ZookeeperStatusDataResponse(data : List[ZookeeperData]) extends ZookeeperListResponse[ZookeeperData]

case object Timeout

