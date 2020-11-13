package com.imooc.bigdata.ss
// sparkSQL
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkSql {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[2]") //如果传到hadoop000， 需要被注释掉，会通过脚本argument传入
    val context = new StreamingContext(sparkConf,Seconds(5) )//assign interval for batch
    context.checkpoint("checkpoint/.")
    val lines = context.socketTextStream("hadoop000", 9527)//对接网络socket

    val word= lines.flatMap(_.split(","))
    word.foreachRDD(rdd=>{
      val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
      import spark.implicits._

      // Convert RDD[String] to DataFrame
      val wordsDataFrame = rdd.toDF("word")

      // Create a temporary view
      wordsDataFrame.createOrReplaceTempView("words")

      // Do word count on DataFrame using SQL and print it
      val wordCountsDataFrame =
        spark.sql("select word, count(*) as total from words group by word")
      wordCountsDataFrame.show()
    })

    context.start() //start streaming
    context.awaitTermination()
  }

}
