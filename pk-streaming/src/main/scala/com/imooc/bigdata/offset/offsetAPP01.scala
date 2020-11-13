package com.imooc.bigdata.offset

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

/**
 * use checkpoint to maintain offsets
 */
object offsetAPP01 {
  def main(args: Array[String]): Unit = {
    val checkpointDirectory="offset/checkpoint"
    val context = StreamingContext.getOrCreate(checkpointDirectory, functionToCreateContext _)
    context.start() //start streaming
    context.awaitTermination()
  }


  def functionToCreateContext(): StreamingContext = {
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]") //如果传到hadoop000， 需要被注释掉，会通过脚本argument传入
    val ssc = new StreamingContext(sparkConf,Seconds(10))   // new context

    val checkpointDirectory="offset/checkpoint"
    ssc.checkpoint(checkpointDirectory)   // set checkpoint directory

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop000:9092,hadoop000:9093,hadoop000:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("my-replicated-topic")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    stream.foreachRDD(rdd=>{
      if(!rdd.isEmpty()){
        print("--------"+rdd.count())
      }
    })
    ssc
  }

}
