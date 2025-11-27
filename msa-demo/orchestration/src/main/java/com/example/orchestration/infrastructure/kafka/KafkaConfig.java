package com.example.orchestration.infrastructure.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic orderCreateTopic() {
        return new NewTopic("order-create", 1, (short)1);
    }

    @Bean
    public NewTopic stockReserveTopic() {
        return new NewTopic("stock-reserve", 1, (short)1);
    }

    @Bean
    public NewTopic paymentCreateTopic() {
        return new NewTopic("payment-create", 1, (short)1);
    }

    @Bean
    public NewTopic sagaEventTopic() {
        return new NewTopic("saga-event", 1, (short)1);
    }





    @Bean
    //싱글턴 빈 객체 등록
    public ProducerFactory<String, String> producerFactory() {
        // Spring Kafka Producer를 직접 생성하지 않고, 반드시 ProducerFactory를 통해 생성해야 한다고 명시함.

        Map<String, Object> properties = new HashMap<>();
        // Kafka Producer 설정값들을 저장하는 Map
        // Kafka 공식 문서에서 producer config는 모두 key-value 형태(Map)으로 입력하도록 규정되어 있음.

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        // Kafka 주소를 설정함.

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 메시지의 key를 byte[]로 직렬화할 때 사용할 클래스를 지정
        // Kafka Producer는 네트워크로 전송 가능한 byte[] 단위로만 메시지를 처리함.
        // 공식문서에서는 serializer.class는 반드시 설정해야하고, 변환되지 않은 객체는 전송 불가.

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 메시지의 value 직렬화 방식 설정
        // 실제 동작 과정은 key 직렬화 방식과 동일함.

        return new DefaultKafkaProducerFactory<>(properties);
        // Spring Kafka가 제공하는 ProducerFactory 구현체
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        // Kafka 메시지를 보내는 high-level API 제공 객체 생성.
        // Spring Kafka 공식 문서에서 KafkaTemplate은 KafkaProducer의 wrapper.

        return new KafkaTemplate<>(producerFactory());
    }



    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId");
        // Consumer가 어디까지 메시지를 읽었는지 저장하는 것이 offset이다.
        // Kafka는 offset을 _consumer_offsets 내부 토픽에 저장하는데
        // 저장 기준이 바로 GroupId이다.
        // 즉 groupId가 있어야 몇 번째까지 읽었다를 저장하고 이어서 읽을 수 있다.

        // 같은 그룹 내 다른 Consumer가 같은 메시지를 두 번 읽지 않도록 한다.

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

        // @KafkaListener가 메시지를 병렬적으로 가져오기 위한 컨테이너 팩토리
        // Spring Kafka 공식 문서 기준 : ListenerContainer는 Consumer 스레드 풀을 관리함
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory
            = new ConcurrentKafkaListenerContainerFactory<>();

        // Consumer 생성 전략을 위에서 만든 consumerFactory로 지정
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());

        return kafkaListenerContainerFactory;
    }

}
