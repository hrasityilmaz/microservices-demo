package art.timestop.microservices.demo.twitter.to.kafka.service.runner.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import art.timestop.microservices.demo.config.TwitterToKafkaServiceConfigData;
import art.timestop.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import art.timestop.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Component
public class TwitterKafkaStreamRunner implements  StreamRunner{

    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final TwitterKafkaStatusListener twitKafkaStatusListener;

    private TwitterStream twitterStream;

    public TwitterKafkaStreamRunner(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData,
            TwitterKafkaStatusListener twitKafkaStatusListener) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
        this.twitKafkaStatusListener = twitKafkaStatusListener;
    }

    @Override
    public void start() throws Exception {
       twitterStream = new TwitterStreamFactory().getInstance();
       twitterStream.addListener(twitKafkaStatusListener);
       addFilter();
    }

    @PreDestroy
    public void shutDown(){
        if(twitterStream != null){
            log.info("Closing twitter stream..");
            twitterStream.shutdown();
        }
    }

    private void addFilter(){
       String[] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
       FilterQuery filterQuery = new FilterQuery(keywords);
       twitterStream.filter(filterQuery);
       log.info("Started filtering... {}", Arrays.toString(keywords));
    }

}
