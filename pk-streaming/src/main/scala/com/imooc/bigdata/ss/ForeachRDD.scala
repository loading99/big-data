package com.imooc.bigdata.ss

import java.sql.Connection

import org.apache.spark.{SPARK_BRANCH, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object ForeachRDD {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]") //如果传到hadoop000， 需要被注释掉，会通过脚本argument传入
    val context = new StreamingContext(sparkConf,Seconds(5) )//assign interval for batch
    context.checkpoint("checkpoint/.")
    val lines = context.socketTextStream("hadoop000", 9527)//对接网络socket

    val result = lines.flatMap(_.split(",")).map((_, 1)).reduceByKey(_+_)
    result.foreachRDD(rdd=> {
      rdd.foreachPartition(partition=>{
          val connection:Connection= MysqlUtils.getConnection()

          partition.foreach(part=>{
          val sql=s"insert into wc(word,cnt) values('${part._1}','${part._2}')"
          connection.createStatement().execute(sql)
        })

        MysqlUtils.closeConnection(connection)
      })
    })
    context.start() //start streaming
    context.awaitTermination()
  }


}
