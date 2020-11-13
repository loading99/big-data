package com.imooc.bigdata.kafka;
import  org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


import java.util.Properties;

public class producer {

    public static void main(String[] args) {
        Properties properties= new Properties();
        properties.put("acks", "all");
        properties.put("bootstrap.servers", "hadoop000:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for(int i = 0; i<10; i++){
            producer.send(new ProducerRecord<String, String>("my-replicated-topic","k"+i,"v"+i));
        }
        System.out.println("Message sent");
        producer.close();
    }
}
