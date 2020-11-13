package com.imooc.bigdata.ss

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}


object TransformApp {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]") //如果传到hadoop000， 需要被注释掉，会通过脚本argument传入
    val context = new StreamingContext(sparkConf,Seconds(5) )//assign interval for batch

    val data=List("pk")
    val dataRDD = context.sparkContext.parallelize(data).map(x => (x, true))

    val lines = context.socketTextStream("hadoop000", 9527)//对接网络socket
    //Dstream join RDD
    lines.map(x=>(x.split(",")(1),x)).transform(rdd=>rdd.leftOuterJoin(dataRDD)).
      filter(x=>{x._2._2.getOrElse(false)!=true}).map(x=>x._2._1).print()

    context.start() //start streaming
    context.awaitTermination()

  }
}
