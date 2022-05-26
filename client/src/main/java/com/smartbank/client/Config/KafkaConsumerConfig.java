package com.smartbank.client.Config;


import com.smartbank.client.Model.Account;
import com.smartbank.client.Model.AmountUpdate;
import com.smartbank.client.Model.Saldo;
import com.smartbank.client.Model.Transaction;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    //Config 1 - account
    public Map<String, Object> accountConsumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Account.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "batch");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Account> accountConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(accountConsumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Account> accountKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Account> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountConsumerFactory());
        return factory;
    }

    //Config 2 - saldoUpdate
    public Map<String, Object> saldoUpdateConsumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Saldo.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "batch");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Saldo> saldoUpdateConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(saldoUpdateConsumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Saldo> saldoUpdateKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Saldo> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(saldoUpdateConsumerFactory());
        return factory;
    }

    //Config 3 - amountUpdate
    public Map<String, Object> amountUpdateConsumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, AmountUpdate.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "batch");
        return props;
    }

    @Bean
    public ConsumerFactory<String, AmountUpdate> amountUpdateConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(amountUpdateConsumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AmountUpdate> amountUpdateKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AmountUpdate> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(amountUpdateConsumerFactory());
        return factory;
    }

}
