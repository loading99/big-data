package com.imooc.bigdata.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.protocol.types.Field;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class consumer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers","hadoop000:9092");
        properties.put("group.id","pk-group");
        properties.setProperty("enable.auto.commit", "true"); //自动提交偏移量
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<Object,Object> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("test"));

        while (true){
            ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord record: records){
                System.out.println(record.topic()+"\t"+record.key()+
                        "\t"+record.value()+"\t"+record.offset());
            }
        }
    }

}
