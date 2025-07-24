package art.timestop.microservices.demo.kafka.producer.config.service.impl;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import art.timestop.microservices.demo.kafka.avro.model.TwitterAvroModel;
import art.timestop.microservices.demo.kafka.producer.config.service.KafkaProducer;
import jakarta.annotation.PreDestroy;


@Service
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {

    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaProducer.class);

    private final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

    public TwitterKafkaProducer(KafkaTemplate<Long, TwitterAvroModel> template) {
        this.kafkaTemplate = template;
    }


    @PreDestroy
    public void close() {
        if(kafkaTemplate != null){
            log.info("Closing kafka producer");
            kafkaTemplate.destroy();
        }
    }

    @Override
    public void send(String topicName, Long key, TwitterAvroModel message) {
        log.info("Sending message='{}' to topic='{}'", message, topicName);
        CompletableFuture<SendResult<Long, TwitterAvroModel>> future = 
                kafkaTemplate.send(topicName, key, message);
        addCallBack(message, future);
    }

   private void addCallBack(TwitterAvroModel message, CompletableFuture<SendResult<Long, TwitterAvroModel>> future) {
    future.whenComplete((result, ex) -> {
        if (ex == null) {
            log.debug("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().timestamp(),
                        System.nanoTime());
        } else {
            log.error("Unable to send message=[{}] due to: {}", message, ex.getMessage());
        }
    });
   }

}

