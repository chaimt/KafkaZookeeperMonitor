package com.turel.controllers

import akka.actor.{ActorSelection, ActorSystem}
import akka.util.Timeout
import com.turel.actors.BaseActor
import com.turel.actors.zookeeper.{ZookeeperStatusDataResponse, ZookeeperStatusRequest}
import com.turel.config.ZookeeperConfig
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping}
import org.springframework.web.context.request.async.DeferredResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by chaimturkel on 12/8/16.
  */
@Controller
class HomeController extends LazyLogging {

  @Autowired
  var zookeeperConfig: ZookeeperConfig = _

  @Autowired
  var actorSystem: ActorSystem = _

  @GetMapping(Array("/"))
  def index(): String = {
    "index";
  }

  @RequestMapping(value = Array("/zookeepers"), method = Array(GET))
  def zookeepersTop(model: Model): DeferredResult[String] = {
    val result = new DeferredResult[String]
    import akka.pattern.ask
    val zookeeperManager: ActorSelection = actorSystem.actorSelection("/user/" + BaseActor.zookeeperManagerActorId)
    implicit val timeout = Timeout(2 seconds)

    zookeeperManager ? ZookeeperStatusRequest onSuccess {
      case answer => {
        val zooList = answer.asInstanceOf[ZookeeperStatusDataResponse]
        import scala.collection.JavaConverters._
        val d = zooList.data
        var list = d.asJava
        model.addAttribute("zookeeperTopDisplay", list);
        result.setResult("zookeepers")
      }
    }
    return result;
  }

  @RequestMapping(value = Array("/brokers"), method = Array(GET))
  def brokers(model: Model): String = {
    import scala.collection.JavaConverters._
    var list = zookeeperConfig.zookeeperManager.getBrokerHosts().asJava
    model.addAttribute("brokersDisplay", list);
    "brokers";
  }

  @RequestMapping(value = Array("/topics"), method = Array(GET))
  def topics(model: Model): String = {
    import scala.collection.JavaConverters._
    var list = zookeeperConfig.zookeeperManager.getBrokerTopics().asJava
    model.addAttribute("topicsDisplay", list);
    "topics";
  }

}
