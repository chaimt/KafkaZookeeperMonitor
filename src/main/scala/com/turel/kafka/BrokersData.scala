package com.turel.kafka

import scala.beans.BeanProperty

/**
  * Created by chaimturkel on 12/8/16.
  */
case class BrokersData(@BeanProperty id : String, @BeanProperty host : String, @BeanProperty port : String, @BeanProperty version : String) {

}
