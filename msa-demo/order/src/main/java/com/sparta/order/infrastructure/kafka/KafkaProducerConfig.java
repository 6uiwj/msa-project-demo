package com.sparta.order.infrastructure.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

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

}
