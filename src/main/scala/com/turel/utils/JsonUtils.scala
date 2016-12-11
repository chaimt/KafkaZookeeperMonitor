package com.turel.utils

import com.fasterxml.jackson.databind.JsonNode

/**
  * Created by chaimturkel on 12/11/16.
  */
object JsonUtils {
  def getText(node : JsonNode, attrib : String) = node.at(s"/$attrib").asText()
}
