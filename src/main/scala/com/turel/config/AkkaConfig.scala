package com.turel.config

import akka.actor.{ActorRef, ActorSelection, ActorSystem, Props}
import com.turel.actors.BaseActor
import com.turel.actors.zookeeper.{ZookeeperActor, ZookeeperManagerActor, ZookeeperStatusRequest}
import com.turel.utils.spring.SpringExtention
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation._

/**
 * Created by Haim.Turkel on 7/26/2015.
 */
@Configuration
@ComponentScan(basePackages = Array("com.turel.actors", "com.turel.utils.spring"))
class AkkaConfig extends LazyLogging {

  @Autowired
  val applicationContext: ApplicationContext = null

  @Autowired
  val springExtension: SpringExtention = null

  @Bean
  def akkaConfiguration(): Config = {
    ConfigFactory.load ()
  }

  @Bean
  def actorSystem(): ActorSystem = {
    val system = ActorSystem
      .create("ZookeeperKafaManager", akkaConfiguration())
    springExtension.initialize(applicationContext)
    system.actorOf(springExtension.props(BaseActor.zookeeperManagerActorId),BaseActor.zookeeperManagerActorId)
//    val of1 = system.actorOf(Props(classOf[ZookeeperManagerActor]),BaseActor.zookeeperManagerActorId)
    logger.info("akka started")
    system
  }

}
