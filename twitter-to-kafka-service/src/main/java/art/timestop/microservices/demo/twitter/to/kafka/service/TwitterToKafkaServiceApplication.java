package art.timestop.microservices.demo.twitter.to.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import art.timestop.microservices.demo.twitter.to.kafka.service.init.StreamInitilaizer;
import art.timestop.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;

@SpringBootApplication
@ComponentScan(basePackages="art.timestop.microservices.demo")
public class TwitterToKafkaServiceApplication implements CommandLineRunner{


    private static final Logger log = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
    private final StreamRunner streamRunner;
    private final StreamInitilaizer streamInitilaizer;

    public TwitterToKafkaServiceApplication(StreamRunner streamRunner, StreamInitilaizer streamInitilaizer) {
        this.streamRunner = streamRunner;
        this.streamInitilaizer = streamInitilaizer;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("app starting...");
        streamInitilaizer.init();
        streamRunner.start();
    }

}
