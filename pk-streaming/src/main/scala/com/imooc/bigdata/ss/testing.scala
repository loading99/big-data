package com.imooc.bigdata.ss

object testing {
  def main(args: Array[String]): Unit = {
    val s="2020-10-26 10:52:29,383 INFO com.imooc.bigdata.controller.logController [http-nio-9527-exec-2] 1603680749243"
    print(s.split(" ").last)
  }
}
