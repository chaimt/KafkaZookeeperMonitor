package com.turel.controllers

import java.util.Properties

import com.turel.config.ZookeeperManager
import com.turel.zookeeper.ZookeeperData
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
  var zookeeperManager : ZookeeperManager = _

  @GetMapping(Array("/"))
  def index() : String ={
    "index";
  }

  @RequestMapping(value = Array("/zookeepers"), method = Array(GET))
  def  zookeepersTop(model : Model) : String = {
    import scala.collection.JavaConverters._
    var list = zookeeperManager.zookeeperInfo.getZookeeperData.asJava
    model.addAttribute("zookeeperTopDisplay", list);
    "zookeepers";
  }

}
