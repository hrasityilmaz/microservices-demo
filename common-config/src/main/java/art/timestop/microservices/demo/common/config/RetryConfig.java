package art.timestop.microservices.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import art.timestop.microservices.demo.config.RetryConfigData;

@Configuration
public class RetryConfig {

    private final RetryConfigData retryConfigData;

    public RetryConfig(RetryConfigData retryConfigData) {
        this.retryConfigData = retryConfigData;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        
        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(retryConfigData.getInitialIntervalMs());
         exponentialBackOffPolicy.setMaxInterval(retryConfigData.getMaxIntervalMs());
         exponentialBackOffPolicy.setMultiplier(retryConfigData.getMultiplier());

         retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

         SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
         simpleRetryPolicy.setMaxAttempts(retryConfigData.getMaxAttempts());

         retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }

}
