package com.turel.controllers

import com.turel.config.ZookeeperConfig
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping}

/**
  * Created by chaimturkel on 12/8/16.
  */
@Controller
class HomeController extends LazyLogging{

  @Autowired
  var zookeeperConfig : ZookeeperConfig = _

  @GetMapping(Array("/"))
  def index() : String ={
    "index";
  }

  @RequestMapping(value = Array("/zookeepers"), method = Array(GET))
  def  zookeepersTop(model : Model) : String = {
    import scala.collection.JavaConverters._
    var list = zookeeperConfig.zookeeperManager.getZookeeperData.asJava
    model.addAttribute("zookeeperTopDisplay", list);
    "zookeepers";
  }

  @RequestMapping(value = Array("/brokers"), method = Array(GET))
  def  brokers(model : Model) : String = {
    import scala.collection.JavaConverters._
    var list = zookeeperConfig.zookeeperManager.getBrokerHosts().asJava
    model.addAttribute("brokersDisplay", list);
    "brokers";
  }

  @RequestMapping(value = Array("/topics"), method = Array(GET))
  def  topics(model : Model) : String = {
    import scala.collection.JavaConverters._
    var list = zookeeperConfig.zookeeperManager.getBrokerTopics().asJava
    model.addAttribute("topicsDisplay", list);
    "topics";
  }

}
