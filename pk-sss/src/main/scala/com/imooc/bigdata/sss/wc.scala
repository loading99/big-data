package com.imooc.bigdata.sss

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.streaming.Trigger.ProcessingTime


object wc {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
//      .master("local[2]")
//      .appName("wc")
      .getOrCreate()
    import spark.implicits._

    val lines = spark.readStream.format("socket")
      .option("host", "hadoop000")
      .option("port", 9999)
      .load()

    val words = lines.as[String].flatMap(_.split(" "))
      .createOrReplaceTempView("wc")

    val query=spark.sql(
      """
        |select
        |value,count(1) as cnt
        |from
        |wc
        |group by value
        |
        |""".stripMargin
    )

    query.writeStream
      .outputMode("complete")
      .format("console")
      .trigger(ProcessingTime("10 seconds"))
      .start().awaitTermination()
}
}
