package com.turel.zookeeper

/**
  * Created by chaimturkel on 12/7/16.
  */
object ZookeeperCommands {
  //Lists the outstanding sessions and ephemeral nodes. This only works on the leader.
  val dump = "dump"
  //Print details about serving environment
  val envi = "envi"
  //Shuts down the server. This must be issued from the machine the ZooKeeper server is running on.
  val kill = "kill"
  //List outstanding requests
  val reqs = "reqs"
  //Tests if server is running in a non-error state. The server will respond with imok if it is running. Otherwise it will not respond at all.
  val ruok = "ruok"
  //Reset statistics returned by stat command.
  val srst = "srst"
  val stat = "stat"
}
