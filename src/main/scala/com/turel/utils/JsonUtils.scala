package com.turel.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

/**
  * Created by chaimturkel on 12/11/16.
  */
object JsonUtils {
  def getText(node : JsonNode, attrib : String) ={
    val at: JsonNode = node.at(s"/$attrib")
    at match {
      case a : ArrayNode => a.toString()
      case b : JsonNode => b.asText()
    }
  }
}
