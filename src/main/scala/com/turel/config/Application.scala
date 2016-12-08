package com.turel.config

import javax.annotation.PostConstruct

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.boot._
import org.springframework.boot.autoconfigure._
import org.springframework.boot.system.ApplicationPidFileWriter
import org.springframework.context.annotation.ComponentScan

/**
  * Created by Chaim Turkel on 7/15/2015.
  */
@SpringBootApplication
@ComponentScan(basePackages = Array("com.turel.config", "com.turel.services", "com.turel.controllers"))
class Application extends LazyLogging{

  @PostConstruct
  def init(): Unit = {
    logger.info("server started")
  }

}

object Application {
  def main(args: Array[String]) {
    val configuration: Array[Object] = Array(classOf[Application])
    val springApplication = new SpringApplication(configuration:_*)
    springApplication.addListeners(new ApplicationPidFileWriter())
    springApplication.run(args:_*)
  }
}

