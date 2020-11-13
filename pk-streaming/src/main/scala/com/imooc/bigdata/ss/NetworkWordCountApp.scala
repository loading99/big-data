package com.imooc.bigdata.ss

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object NetworkWordCountApp {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]") //如果传到hadoop000， 需要被注释掉，会通过脚本argument传入
    val context = new StreamingContext(sparkConf,Seconds(5) )//assign interval for batch
    context.checkpoint("checkpoint/.")
    val lines = context.socketTextStream("hadoop000", 9527)//对接网络socket
    val result = lines.flatMap(_.split(",")).map((_, 1)).updateStateByKey[Int](updateFunction _)
    result.print()

    context.start() //start streaming
    context.awaitTermination()
  }

  def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
    val current= newValues.sum
    val old = runningCount.getOrElse(0)
    Some(current+old)
  }

}
