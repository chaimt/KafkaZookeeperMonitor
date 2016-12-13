package com.turel.zookeeper

/**
  * Created by chaimturkel on 12/7/16.
  */
case class ZookeeperConnectionInfo(host : String, port : Int){
  def connectionStr = s"$host:$port"
}
