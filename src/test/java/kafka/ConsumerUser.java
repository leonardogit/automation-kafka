package kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

import static utils.PropertiesKafka.propertiesConsumer;

public class ConsumerUser {

    private static String message;

    public static String getMessageTopic(String topic){

        Consumer<String , String > consumer = new KafkaConsumer<>(propertiesConsumer(topic));
        consumer.subscribe(Collections.singletonList(topic));

        final ConsumerRecords <String , String > records = consumer.poll(Duration.ofSeconds(10));
        records.forEach(record -> {
            Object objectTopic = record.value();
            message = objectTopic.toString();
            System.out.println("Consumindo do topico : " + message);
        });

        consumer.commitAsync();
        consumer.close();

        return message;
    }

}
