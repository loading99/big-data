package com.imooc.bigdata.ss

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object CoreJoinApp {

  def main(args: Array[String]): Unit = {

      val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getName)
      val sc = new SparkContext(conf)

      val buffer = new ListBuffer[(String, Boolean)]()
      buffer.append(("pk",true))
      val listRDD: RDD[(String, Boolean)] = sc.parallelize(buffer)

      val input = new ListBuffer[(String, String)]()
      input.append(("pk","202213,pk"))
      input.append(("test","2012fs,pk"))
      val value: RDD[(String, String)] = sc.parallelize(input)

      val filterRdd: RDD[(String, (Option[Boolean], String))] = listRDD.rightOuterJoin(value)

      filterRdd.filter(x=>{x._2._1.getOrElse(false)!=true}).map(x=>x._2._2)foreach(println)

      sc.stop()
  }

}
