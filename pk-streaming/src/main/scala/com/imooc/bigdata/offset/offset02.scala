package com.imooc.bigdata.offset
import com.imooc.bigdata.DataBase.MySqlOffsets
import com.imooc.bigdata.kafka.kafkaParameters
import org.apache.kafka.common.TopicPartition
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import scalikejdbc.config.DBs

/**
 * use Mysql to maintain offsets automatically
 * exactly once operation, fault tolerant
 */

object offset02 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]") //如果使用控制台运行， 需要被注释掉，会通过脚本argument传入
    val ssc = new StreamingContext(sparkConf,Seconds(10))   // new context

    DBs.setupAll()


    //set kafka parameters
    val kafkaParams =kafkaParameters()

    //launch DirectStream
    val groupId="pk-spark-group-1"
    val topics=Array("my-replicated-topic")

    //query offsets
    val offsets: collection.Map[TopicPartition,Long]=MySqlOffsets.getOffset(groupId, topics)

    for ((k,v)<-offsets){
      println(s"topic: $k")
      println(s"Last Offsets: $v")
    }

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams,offsets)
    )

    stream.foreachRDD {
      rdd =>{if(!rdd.isEmpty()) {
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd.foreachPartition { iter =>
          val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
          println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        }

        MySqlOffsets.storeOffset(offsetRanges)
      }
      }
    }
    ssc.start() //start streaming
    ssc.awaitTermination()
  }

}
