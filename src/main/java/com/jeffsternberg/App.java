package com.jeffsternberg;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Collections;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Kakfa!" );

        produce();

        consume();

        System.out.println( "And we're out. See ya!" );
    }

    private static void produce() {
        Properties kafkaProps = new Properties();

        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);

        System.out.println( "Kafka producer instantiated." );

        for (int i = 0; i < 100; i++)
            producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), "Hello Kafka" + Integer.toString(i)));

        System.out.println( "Sent some data to the producer." );

        producer.close();
    }

    private static void consume() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "TestCounter");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Collections.singletonList("test"));

        System.out.println( "Consumer subscribed to the test topic." );

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            System.out.printf("got %d records\n", records.count());

            if (records.isEmpty()) {
                break;
            }

            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("topic = %s, partition = %s, offset = %d, key = %s, value = %s\n",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());
            }
            try {
                consumer.commitSync();
            } catch (CommitFailedException e) {
                System.out.println("commit failed" + e);
            }
        }
    }
}
