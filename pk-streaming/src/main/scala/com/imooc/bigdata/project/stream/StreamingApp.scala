package com.imooc.bigdata.project.stream

import com.imooc.bigdata.kafka.kafkaParameters
import com.imooc.bigdata.project.utils.{DateUtils, HbaseClient}

import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}


object StreamingApp {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]") //如果使用控制台运行， 需要被注释掉，会通过脚本argument传入
    sparkConf.set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition","100") //limit records speed from kafka

    val ssc = new StreamingContext(sparkConf,Seconds(10))   // new context

    //set kafka parameters
    val kafkaParams =kafkaParameters()

    //launch DirectStream
    val groupId="pk-spark-group-1"
    val topics=Array("access-topic-prod")
    //query offsets
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    //业务处理
    val table= "access_user_hour"
    val logStream=stream.filter(x=>{
      val splits=x.value().split("\t")
      splits.length==9
    }).
      map(x=>{
      //data cleaning
      val splits =x.value().split("\t")
      val time=splits(0).split(" ").last.trim   //log timestamp
      (DateUtils.parsetoFormat(time),splits(1).trim.toLong,splits(5))
    })

    //统计用户在某个小时 阅览时间总数
    logStream.map(x=>{
      ((x._1,x._3),x._2)
    }).reduceByKey(_+_).foreachRDD(rdd=>{
      rdd.foreachPartition(partition=> {
        val chart = HbaseClient.getTable(table)
        partition.foreach(r => {
          chart.incrementColumnValue(
            (r._1._1 + "_" + r._1._2).getBytes,
            "o".getBytes,
            "time".getBytes,
            r._2
          )
        })
        chart.close()
      })
        }
      )

    ssc.start() //start streaming
    ssc.awaitTermination()
  }

}
