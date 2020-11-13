package com.imooc.bigdata.DataBase

import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs


object scalaDBApp {
  def main(args: Array[String]): Unit = {
    DBs.setupAll()
    query()
    println("-----------")
    //update()
    insert("pktest","test-group",1,99)
    println("------------")
    query()
  }

  def query(): Unit={
    DB.readOnly{ implicit session =>
      SQL("SELECT * FROM offsets_storage")
        .map(rs=>Offset(
          rs.string("topic"),
          rs.string("groupid"),
          rs.int("partitions"),
          rs.long("offset")
        ))
        .list().apply()
    }.foreach(println)
  }

  //set new offset
  def update(): Unit={
    DB.autoCommit{implicit session=>
      SQL("update offsets_storage set offset=? where topic=? and groupid=? and partitions=?")
        .bind(30,"pktest","test_group",2)
        .update()
    }
  }

  def insert(topic:String,groupid:String,partitions:Int,offset:Long): Unit={
    DB.autoCommit{implicit session=>
      SQL("insert into offsets_storage(topic,groupid,partitions,offset) values(?,?,?,?)")
        .bind(topic,groupid,partitions,offset)
        .update().apply()
    }
  }
}

case class Offset(topic:String,groupId:String,partition:Int,offset:Long)