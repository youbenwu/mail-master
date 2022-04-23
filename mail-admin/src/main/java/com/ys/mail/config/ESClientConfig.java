package com.ys.mail.config;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author 24
 * @date 2022/2/9 14:39
 * @description es restHighLevelClient
 */
@Configuration
public class ESClientConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder){
        return new RestHighLevelClient(restClientBuilder.setHttpClientConfigCallback(requestConfig ->
                requestConfig.setKeepAliveStrategy((response, context) -> TimeUnit.MINUTES.toMillis(3))));
    }
}
