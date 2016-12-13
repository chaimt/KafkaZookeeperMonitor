package com.turel.actors

import akka.actor.Actor
import com.turel.utils.spring.SpringExtention
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Haim.Turkel on 7/23/2015.
 * paths to actors
 */

abstract class BaseActor extends Actor{
  @Autowired
  val springExtention : SpringExtention = null
}

object  BaseActor{
  val zookeeperManagerActorId = "zookeeperManagerActor"
}
