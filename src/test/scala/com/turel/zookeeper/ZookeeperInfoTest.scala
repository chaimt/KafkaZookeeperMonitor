package com.turel.zookeeper

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by chaimturkel on 12/8/16.
  */
class ZookeeperInfoTest extends FlatSpec with Matchers {

  "localhost zookeeper" should "show get data informtion" in{
    val zoo  = new ZookeeperInfo(List("localhost"))
    val data: List[ZookeeperData] = zoo.getZookeeperData()
    data.size shouldEqual 1
//    data(0).props.
  }

}
