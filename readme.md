# Automação com kafka + Java + Cucumber se integrando com GitHub Actions


### Bem vindo ao repositório do projeto de testes automatizados de kafka.

Esse projeto tem como objetivo de forma simples e inicial demonstrar como são os primeiros passos para se automatizar casos de testes em um tópico kafka e também executa-los em uma pipeline automatizada que nesse caso iremos utilizar o gitHubActions

### Mas afinal o que é o kafka ??

O Apache Kafka é um sistema open-source de mensageria do tipo publish/subscribe desenvolvido pela equipe do LinkedIn , basicamente uma plataforma de distribuiçao de eventos
aonde é caracterizado por um conjunto de máquinas (brokers) trabalhando para produzir e processar dados no momento em que o evento acontece. 

### Como funciona o kafka ? 

O Apache Kafka é composto por alguns componentes principais:

- Mensagens: Um dado (ou evento) no Kafka é chamado de mensagem e um conjunto de mensagens é chamado de lote (ou batch);
- Tópicos: Local onde as mensagens são armazenadas;
- Partições: Os tópicos são divididos em partições;
- Segmentos de log: As mensagens do Kafka são escritas em arquivos de logs dentro das partições;
- Broker: É o servidor do Kafka, responsável por receber as mensagens dos producers, escrever as mensagens no disco e disponibilizar para os consumers;
- Producer: Serviço(s) responsável(eis) por enviar enviar as mensagens para o Kafka;
- Consumer: Serviço(s) responsável(eis) por ler as mensagens do Kafka.

### E como podemos simular um teste automatizado em kafka ? 

Basicamente teremos alguns pré requisitos para você executar esse projeto em sua máquina eles são :
- Docker Instalado
- Java Instalado
- Maven instalado
- Intelijj ou ide a sua escolha instalada

A partir disso podemos ir para a parte do código do projeto :

### Explicando a estrutura do projeto....

O projeto está estruturado da seguinte forma :

- pom.xml
- Constants
- Kafka
- Steps
- Utils 
- RunCucumberTest
- resources/features
- resources/applcation.properties

Explicando cada um :

- No **pom.xml** adicionamos as dependências necessárias para executarmos nossos testes 

```
<dependencies>
    <!-- Dependências do Cucumber -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit</artifactId>
        <version>6.10.4</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>6.10.4</version>
        <scope>test</scope>
    </dependency>

    <!-- Dependências do Kafka -->
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>2.8.0</version>
    </dependency>

    <!-- Dependências do JUnit -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>

```

Também adicionamos o **Build** com a seguinte extrutura :

```
<build>
    <plugins>
        <!-- Plugin para compilar o código Java -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>

        <!-- Plugin para executar os testes -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.3</version>
            <configuration>
                <includes>
                    <include>**/*.java</include>
                </includes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

- Na Pasta **constants** , temos a classe constants , esta classe herda de DefaultProperties e carrega as propriedades do arquivo application.properties. Ela contém as constantes que serão utilizadas no projeto, como o nome do tópico e a mensagem a ser enviada.


```
public class Constants extends DefaultProperties {
public String topicname = loadProperties().getProperty("topicname");
public String message = loadProperties().getProperty("message");
}
```

- Na Pasta **kafka** , temos a classe **ConsumerUser** , esta classe é responsável por consumir mensagens de um tópico Kafka. Ela utiliza um consumidor Kafka para se inscrever em um tópico e processar as mensagens recebidas.
Em que é caracterizada por

**Criação do Consumidor Kafka**

`Consumer<String, String> consumer = new KafkaConsumer<>(propertiesConsumer(topic));`

**Inscrição no topico**

`consumer.subscribe(Collections.singletonList(topic));`

**Pooling de mensagens**
`final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(30));`
O método poll é chamado para buscar mensagens do tópico. Ele espera por até 30 segundos (Duration.ofSeconds(30)) para receber mensagens. O resultado é armazenado em records, que contém todas as mensagens recebidas durante esse período.

**Processamento e percorrer cada mensagem**

`records.forEach(record -> {
Object objectTopic = record.value();
message = objectTopic.toString();
System.out.println("Consumindo do topico: " + message);
});`

**Commit assincrono e o fechamento do consumidor**

`consumer.commitAsync();`

`consumer.close();`

E por fim o **Retorno da Última Mensagem**

`return message;`



- Na Pasta **kafka** , também tempos a classe **ProducerUser** , esta classe é responsável por enviar mensagens para um tópico Kafka. Ela utiliza um produtor Kafka para enviar mensagens para o tópico especificado, ela é caracterizada por :

**Geração da chave aleatória :**

`String gerarkey = String.valueOf(Math.random());`

**OBS** : No Kafka, as mensagens podem ser enviadas com uma chave opcional. A chave é usada para determinar a partição para a qual a mensagem será enviada. Se a chave for nula, o Kafka distribuirá as mensagens de forma aleatória entre as partições.


**Criação de um produtor Kafka**

`Producer<String, String> producer = new KafkaProducer<>(propertiesProducer());`

Aqui, um produtor Kafka é criado. O KafkaProducer é uma classe do cliente Kafka que é usada para enviar mensagens para um cluster Kafka. O produtor é parametrizado com String para a chave e String para o valor da mensagem, ou seja, tanto a chave quanto a mensagem são do tipo String.

O método propertiesProducer() (que não está mostrado no código sera mostrado na proxima etapa) provavelmente retorna um objeto Properties que contém as configurações necessárias para o produtor Kafka, como o endereço do broker Kafka, o serializador de chave e valor, entre outras configurações.

**Envio da Mensagem**

`producer.send(new ProducerRecord<>(topic, gerarkey, message));`

Aqui, o método send do produtor Kafka é chamado para enviar uma mensagem. A mensagem é encapsulada em um objeto ProducerRecord, que contém:

- O nome do tópico (topic) para o qual a mensagem será enviada.
- A chave da mensagem (gerarkey), que foi gerada aleatoriamente.
- O valor da mensagem (message), que é o conteúdo da mensagem a ser enviada.
- O Kafka usa a chave para determinar em qual partição do tópico a mensagem será armazenada. Se a chave for nula, o Kafka escolherá uma partição aleatoriamente.

**E por fim , fecha o produtor e loga uma mensagem para rastreio durante a execução** 

`producer.close();`

`System.out.println("Enviando mensagem para o topico: " + message);`

- Ja a pasta steps tem a classe KafkaSteps que  é caracterizada pelos passos definidos para os testes Cucumber. Ela utiliza as classes ProducerUser e ConsumerUser para enviar e consumir mensagens do Kafka, respectivamente.

```
  Constants constants = new Constants();

  @Dado("que eu envie a mensagem no topico")
  public void test1SendMessageProducer() {
  ProducerUser.postMessageTopic(constants.topicname, constants.message);
  }

  @Entao("consumo a mensagem enviada e valido seu conteudo com sucesso")
  public void test2ConsumerMessageProducer() {
  assertThat(ConsumerUser.getMessageTopic(constants.topicname), is("messagetest"));
  }
```

-Ja a pasta utils tem algumas configuraçoes necessárias que são caracterizadas por :

**Classe PropertiesKafka:**

Esta classe define as propriedades necessárias para configurar o produtor e o consumidor Kafka.

```
    public static Properties propertiesProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
```

- Cria um objeto Properties para armazenar as configurações do **Kafka Producer**.
- Define o endereço do broker Kafka (neste caso, 127.0.0.1:9092).
- Configura o serializador da chave como StringSerializer, que converte a chave da mensagem (uma String) em bytes.
- Configura o serializador do valor como StringSerializer, que converte o valor da mensagem (uma String) em bytes.
- Retorna o objeto Properties com todas as configurações.

```
  public static Properties propertiesConsumer(String topic) {
  Properties properties = new Properties();
  properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
  properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, topic);
  properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
  properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
  properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
  return properties;
  }
```

- Cria um objeto Properties para armazenar as configurações do **Kafka Consumer**.
- Define o endereço do broker Kafka (neste caso, 127.0.0.1:9092).
- Configura o Group ID do consumidor, que agrupa consumidores que compartilham o mesmo ID.
- Configura o desserializador da chave como StringDeserializer, que converte a chave da mensagem (uma String) de bytes para String.
- Configura o desserializador do valor como StringDeserializer, que converte o valor da mensagem (uma String) de bytes para String.
- Define que o consumidor começará a ler as mensagens a partir do início do log, se não houver offset anterior.
- Desabilita o commit automático de offset, exigindo que o consumidor faça o commit manualmente.
- Retorna o objeto Properties com todas as configurações.

Na pasta utils temos a classe **PropertiesKafka**

Esta classe é responsável por carregar as propriedades do arquivo application.properties.
``` 
public class DefaultProperties {
public Properties loadProperties() {
try {
InputStream inStream = getClass().getClassLoader().getResourceAsStream("application.properties");
Properties prop = new Properties();
prop.load(inStream);
return prop;
} catch (Exception e) {
System.out.println("Arquivo não encontrado.....");
return null;
}
}
}
```

**_OBS : Também é criada a classe `RunCucumberTest` que irá realizar a orquestração de execução da bateria de features de testes do cucumber_**

E por fim temos a pasta de resources aonde ficará nosso arquivo cucumber (define o cenário de teste para o envio e consumo de mensagens no Kafka.)  e nosso application.properties  (que  contém as propriedades do projeto)


### E como podemos Executar o projeto ? ###

Uma vez que o arquivo docker-compose.yml foi inicializado e sua máquina já está rodando o server com os containers do kafka , basta executa o comando :

`mvn test`

# BONUS #

Nesse projeto também configuramos para rodar na pipeline integrada do gitHub , aonde se caracteriza na pasta .github/workflows no arquivo yml (integration-tests) em que será realizado nas etapas :

- Faz o checkout do código do repositório.
- Instala o Docker Compose.
- Verifica se o Docker Compose foi instalado corretamente.
- Inicia os containers definidos no arquivo docker-compose.yml.
- Executa os testes do projeto Maven, que envolvem Kafka e Cucumber.
