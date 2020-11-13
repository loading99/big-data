package com.imooc.bigdata.kafka

import org.apache.kafka.common.serialization.StringDeserializer

object kafkaParameters {
  def apply(): Map[String,Object]={

    val groupID="pk-spark-group-1"
    val para= Map[String, Object](
      "bootstrap.servers" -> "hadoop000:9092,hadoop000:9093,hadoop000:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupID,
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    para
  }
}
