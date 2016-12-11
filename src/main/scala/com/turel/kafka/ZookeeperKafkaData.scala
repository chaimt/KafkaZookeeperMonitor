package com.turel.kafka

import scala.beans.BeanProperty

/**
  * Created by chaimturkel on 12/11/16.
  */
case class TopicsData (@BeanProperty name : String, @BeanProperty version : String, @BeanProperty parition : List[PartitionData])
case class PartitionData (@BeanProperty controllerEpoch : String, @BeanProperty leader : String, @BeanProperty leaderEpoch : String, @BeanProperty isr : String)
case class BrokersData(@BeanProperty id : String, @BeanProperty host : String, @BeanProperty port : String, @BeanProperty version : String)
