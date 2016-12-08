package com.turel.zookeeper

import com.turel.config.ZookeeperConfig
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by chaimturkel on 12/8/16.
  */
class ZookeeperManagerTest extends FlatSpec with Matchers{

  "manager connection" should "support multiple connections" in{
    val config = new ZookeeperConfig
    config.zookeeperConnections = "a:1,b:2,c"
    config.init()
    config.zookeeperManager.zookeeperParams.size shouldEqual 3
    config.zookeeperManager.zookeeperParams(0).host shouldEqual "a"
    config.zookeeperManager.zookeeperParams(0).port shouldEqual 1
    config.zookeeperManager.zookeeperParams(1).host shouldEqual "b"
    config.zookeeperManager.zookeeperParams(1).port shouldEqual 2
    config.zookeeperManager.zookeeperParams(2).host shouldEqual "c"
    config.zookeeperManager.zookeeperParams(2).port shouldEqual 2181
  }

}
