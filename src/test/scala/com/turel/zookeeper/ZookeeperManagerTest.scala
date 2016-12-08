package com.turel.zookeeper

import com.turel.config.ZookeeperManager
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by chaimturkel on 12/8/16.
  */
class ZookeeperManagerTest extends FlatSpec with Matchers{

  "manager connection" should "support multiple connections" in{
    val manager = new ZookeeperManager
    manager.zookeeperConnections = "a:1,b:2,c"
    manager.init()
    manager.zookeeperInfo.zookeeperParams.size shouldEqual 3
    manager.zookeeperInfo.zookeeperParams(0).host shouldEqual "a"
    manager.zookeeperInfo.zookeeperParams(0).port shouldEqual 1
    manager.zookeeperInfo.zookeeperParams(1).host shouldEqual "b"
    manager.zookeeperInfo.zookeeperParams(1).port shouldEqual 2
    manager.zookeeperInfo.zookeeperParams(2).host shouldEqual "c"
    manager.zookeeperInfo.zookeeperParams(2).port shouldEqual 2181
  }

}
