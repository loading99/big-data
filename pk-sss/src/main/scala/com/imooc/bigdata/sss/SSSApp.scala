package com.imooc.bigdata.sss

import org.apache.spark.sql.SparkSession

object SSSApp {
  def main(args: Array[String]): Unit = {
    val spark =SparkSession.builder()
//      .master("local[2]")
//      .appName(this.getClass.getName)
      .getOrCreate()
    spark.conf.set("spark.sql.shuffle.partitions", 3)

    val df = sourceApp.kafkasource(spark)
    sinkApp.RedisSink(df)

  }
}
