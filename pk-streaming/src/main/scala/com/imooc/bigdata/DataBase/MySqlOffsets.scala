package com.imooc.bigdata.DataBase

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import scalikejdbc.{DB, SQL}

object MySqlOffsets extends OffsetManager {

  override def getOffset(groupId:String,topics:Array[String]):
  collection.Map[TopicPartition,Long] = {
    DB.readOnly{
      implicit session=>{
        SQL("SELECT * FROM offsets_storage where groupid=? and topic=?")
          .bind(groupId, topics.head)
          .map(x=>{
            (new TopicPartition(x.string("topic"),x.int("partitions")),x.long("offset"))
          }).list().apply()
      }
    }.toMap
  }

  override def storeOffset(ran:Array[OffsetRange]): Unit=
   {
     ran.map(x => {
       DB.autoCommit { implicit session => {
         SQL(
           """
             |INSERT INTO offsets_storage(topic,groupid,partitions,offset) values(?,?,?,?)
             |on duplicate key update offset=?
             |""".stripMargin)
           .bind(x.topic, "pk-spark-group-1", x.partition, x.untilOffset, x.untilOffset)
           .update().apply()
       }
       }
     }
     )
  }
}

