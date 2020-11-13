package com.imooc.bigdata.sss

/**
 * Author: Zhijian Hu
 * Different kinds of sink method
 */

import java.sql.{Connection, DriverManager, PreparedStatement}

import sourceApp.Eventwindow
import com.imooc.bigdata.utils.propertyUtils
import com.imooc.bigdata.utils.{RedisUtils, propertyUtils}
import org.apache.spark.sql
import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.functions.{to_timestamp, window}
import redis.clients.jedis.Jedis

object sinkApp {
  /**
   * simple output to console
   * @param df
   */
  def consolesink(df:sql.DataFrame)={
    df
      .writeStream
      .format("console")
      .outputMode("update")
      .option("truncate","false")
      .start()
      .awaitTermination()
  }

  def filesink(spark:SparkSession): Unit ={
    import spark.implicits._
    val stream = spark.readStream.format("socket")
      .option("host", "hadoop000")
      .option("port", 9999)
      .load.as[String]

    val df = stream.map(x => {
      val splits = x.split(",")
      (splits(0), splits(1))
    }).toDF("time","animal")

    df
      .writeStream
      .format("json")
      .option("path","/pk-sss")
      .option("checkpointLocation","chk")
      .start()
      .awaitTermination()

  }


  def kafkasink(df:sql.DataFrame)={
    df
      .writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers","hadoop000:9092")
      .option("topic","ssstopic")
      .option("checkpointLocation","chk")
      .start()
      .awaitTermination()
  }

  /**
   * Write result to MySql in hadoop000
   * @param df: SQL.DataFrame
   */
  def foreachSink(df:sql.DataFrame)={

    val writer =new ForeachWriter[Row] {
      var connection:Connection = _
      var pstmt:PreparedStatement = _
      var batchcount=0
      override def process(value: Row): Unit = {
        println("Processing")
        val word = value.getString(0)
        val cnt  = value.getLong(1).toInt
        pstmt.setString(1,word)
        pstmt.setInt(2,cnt)
        pstmt.setString(3,word)
        pstmt.setInt(4,cnt)
        pstmt.addBatch()

        batchcount+=1
        if(batchcount>=10){
          pstmt.executeBatch()
          batchcount =0
        }

      }

      override def close(errorOrNull: Throwable): Unit ={
        println("closing Connection")
        pstmt.executeBatch()  //execute the final batch
        batchcount =0
        connection.close()
      }

      override def open(partitionId: Long, epochId: Long): Boolean = {
        println(s"open Connection: $partitionId,$epochId")
        val propertyfile=propertyUtils.conf("DB.properties")
        Class.forName("com.mysql.jdbc.Driver")
        val url = propertyfile.get("MySql_URL").toString
        val username = propertyfile.get("MySql_user").toString
        val password = propertyfile.get("MySql_password").toString
        connection = DriverManager.getConnection(url,username,password)

        val sql=
          """
            |insert into t_wc(word,cnt)
            |values(?,?)
            |on duplicate key update word=?,cnt=?
            |""".stripMargin

        pstmt=connection.prepareStatement(sql)
        connection!=null && !connection.isClosed && pstmt !=null
      }
    }
      df
      .writeStream
        .foreach(writer)
      .outputMode("update")
      .start()
      .awaitTermination()

  }

  /**
   * write data to redis using foreach sink
   * @param df
   */

  def RedisSink(df:sql.DataFrame)={

    val writer = new ForeachWriter[Row] {
      var clients: Jedis = _

      override def close(errorOrNull: Throwable): Unit = {

        if (clients != null) {
          clients.close()
        }
      }

      override def open(partitionId: Long, epochId: Long): Boolean = {
        clients = RedisUtils.getJedisClient
        clients != null

      }

      override def process(value: Row): Unit = {
        val day = value.getString(0)
        val province = value.getString(1)
        val count = value.getLong(2)

        clients.hset("day-province-count" + day, province, count + "")
      }
    }

    df
      .writeStream
      .foreach(writer)
      .outputMode("update")
      .option("checkpointLocation","./chk")
      .start()
      .awaitTermination()
  }

}
