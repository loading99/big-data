package com.imooc.bigdata.project.utils

import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put, Scan}
import org.apache.hadoop.hbase.util.Bytes

object HbaseClient {
  private val configuration = HBaseConfiguration.create()
  configuration.set("hbase.zookeeper.quorum","hadoop000:2181")
  private val connection = ConnectionFactory.createConnection(configuration)

  def main(args: Array[String]): Unit = {
    query("2020102700","2020102700","access_user_hour")
  }
  def getTable(tablename:String)={
    connection.getTable(TableName.valueOf(tablename))
  }


  /**
   *
   * @param start: start row for scanner exclusive
   * @param stop: end row for scanner inclusive
   * @param tablename table to query
   */
  def query(start:String,stop:String,tablename:String)={
    val table = connection.getTable(TableName.valueOf(tablename))
    val scan = new Scan()
    scan.setStartRow(start.getBytes)
    scan.setStopRow(stop.getBytes)
    val iter = table.getScanner(scan).iterator()

    while(iter.hasNext){
      val result = iter.next()

      while(result.advance()){
        val cell= result.current()
        val row = new String(CellUtil.cloneRow(cell))
        val family = new String(CellUtil.cloneFamily(cell) )
        val v= new String(CellUtil.cloneValue(cell))
        print(row+"..."+family+"..."+v)
      }
    }
  }

  def insert(tablename: String)={

    val table = connection.getTable(TableName.valueOf(tablename))
    val put =new Put("4".getBytes())
    put.addColumn(Bytes.toBytes("o"),Bytes.toBytes("name"),Bytes.toBytes("name1"))
    put.addColumn(Bytes.toBytes("o"),Bytes.toBytes("sex"),Bytes.toBytes("m"))
    put.addColumn(Bytes.toBytes("o"),Bytes.toBytes("age"),Bytes.toBytes("19"))

    table.put(put)
    if (table!=null){table.close()}
  }
}
