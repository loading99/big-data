package com.imooc.bigdata.sss

import java.sql.Timestamp

import com.imooc.bigdata.utils.DateUtils
import com.imooc.bigdata.utils.{DateUtils, IPUtils}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{length, to_timestamp, window}
import org.apache.spark.sql.streaming.Trigger.ProcessingTime
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object sourceApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.master("local[4]").appName(this.getClass.getName).getOrCreate()
    spark.conf.set("spark.sql.shuffle.partitions", 3)
    kafkasource(spark)

  }

  def kafkasource(spark: SparkSession) = {
    import spark.implicits._
    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "hadoop000:9092,hadoop000:9093,hadoop000:9094")
      .option("subscribe", "access-topic-prod")
      .option("startingOffsets", "latest")
      .load().selectExpr("CAST(value AS STRING)").as[String]

        df.filter(x=>{
          val splits=x.split("\t")
          splits.length==9
        })
          .map(x => {
          val splits=x.split("\t")
            val time=splits(0).split(" ").last.trim
            val ip = splits(2)
            (new Timestamp(time.toLong),DateUtils.parseToDay(time),IPUtils.parseIP(ip))

        }).toDF("timestamp","day","location")
          .withWatermark("timestamp", "10 minutes")
          .groupBy("day","location")
          .count()
  }

    def csvSource(spark: SparkSession) = {
      import spark.implicits._

      val userSchema = new StructType()
        .add("id", IntegerType)
        .add("name", StringType)
        .add("city", StringType)

      spark.readStream
        .format("csv")
        .schema(userSchema)
        .load("pk-sss/data/")
    }

    def Eventwindow(spark: SparkSession): Unit = {
      import spark.implicits._
      val stream = spark.readStream.format("socket")
        .option("host", "hadoop000")
        .option("port", 9999)
        .load.as[String]

      val df = stream.map(x => {
        val splits = x.split(",")
        (splits(0), splits(1))
      }).toDF("time", "animal")
        .withColumn("timestamp", to_timestamp($"time"))
        .withWatermark("time", "10 minutes")
        .groupBy(
          window($"time", "10 minutes", "5 minutes"), $"animal"
        ).count().sort("window")

      df
        .writeStream
        .format("console")
        .outputMode("complete")
        .option("truncate", "false")
        .start()
        .awaitTermination()

    }
  }

