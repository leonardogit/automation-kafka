package steps;

import constants.Constants;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import kafka.ConsumerUser;
import kafka.ProducerUser;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KafkaSteps {

    Constants constants = new Constants();

    @Dado("que eu envie a mensagem no topico")
    public void test1SendMessageProducer(){
        ProducerUser.postMessageTopic(constants.topicname , constants.message);
    }

    @Dado("consumo a mensagem enviada e valido seu conteudo")
    public void test2ConsumerMessageProducer(){
        assertThat(ConsumerUser.getMessageTopic(constants.topicname), is("messagetest"));
    }
}