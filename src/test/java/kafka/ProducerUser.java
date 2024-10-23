package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import static utils.PropertiesKafka.propertiesProducer;
public class ProducerUser {
    public static void postMessageTopic(String topic , String message){
        String gerarkey = String.valueOf(Math.random());
        Producer<String , String> producer = new KafkaProducer<>(propertiesProducer());
        producer.send(new ProducerRecord<>(topic,gerarkey,message));
        System.out.println("Enviando mensagem para o topico !!! " + message);
    }
}
