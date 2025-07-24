package art.timestop.microservices.demo.twitter.to.kafka.service.init.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import art.timestop.microservices.demo.config.KafkaConfigData;
import art.timestop.microservices.demo.kafka.admin.client.KafkaAdminClient;
import art.timestop.microservices.demo.twitter.to.kafka.service.init.StreamInitilaizer;

public class KafkaStreamInitializer implements StreamInitilaizer{

    private static final Logger log = LoggerFactory.getLogger(KafkaStreamInitializer.class);
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;


    public KafkaStreamInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        log.info("Topics with name {} is ready for operation!", kafkaConfigData.getTopicNamesToCreate().toArray());
    }

}
