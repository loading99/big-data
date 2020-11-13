package com.imooc.bigdata.DataBase

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import org.sparkproject.jetty.server.RequestLog.Collection

trait OffsetManager {


  def storeOffset(ran:Array[OffsetRange]):Unit={}


  def getOffset(groupId:String,topics:Array[String]):collection.Map[TopicPartition,Long] ={
    Map[TopicPartition,Long]()
  }
}
