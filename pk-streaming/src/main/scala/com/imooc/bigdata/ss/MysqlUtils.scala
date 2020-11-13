package com.imooc.bigdata.ss

import java.sql.{Connection, DriverManager}


object MysqlUtils {

  def getConnection()={
    Class.forName("com.mysql.jdbc.Driver")
    DriverManager.getConnection("jdbc:mysql://hadoop000:3306/pk","root","root")
  }

  def closeConnection(connection: Connection)={
    if (null!=connection){
      connection.close()
    }
  }
}
