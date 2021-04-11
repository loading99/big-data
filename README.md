
# Spark Streaming Based real time data processing
Implemented Big data Frameworks to process real time data.

## Dependencies
JDK: 1.8  
Scala: 2.12.10  
Maven 3.6.3  
Hadoop：2.6.0-cdh5.16.2  
Zookeeper 3.4.5  
Spark 3.0.0
Kafka 2.5.0
Flume 1.6.0-cdh5.16.2

## WorkFlow
1. The project first upload data to server hadoop000 which is a local 
server I built through virtual machine. The 
port for uploading is http://hadoop000:9527/pk-web/upload, 
<strong>The log generator is hidden intentionally</strong>, as required by data source provider.
You can use your own log data source in log-service/com/imooc/bigdata/log/utils/test
2. Pk-log-web is the module that is responsible for receiving log data.
Start a Spring boot server at the server hadoop000:9527 and receive log data through address above.
3. The data departs from Spring log receiver and goes to Apache Flume and Kafka, finally goes into Spark.
4. We will process the data by Spark Streaming and Spark Structured Streaming.
5. The analysis is stored in Hbase and redis for further analysis.