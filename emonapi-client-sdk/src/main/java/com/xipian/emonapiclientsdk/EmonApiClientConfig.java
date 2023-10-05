package com.xipian.emonapiclientsdk;

import com.xipian.emonapiclientsdk.client.EmonApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/5
 */
@Configuration
@ConfigurationProperties(prefix = "emonapi.client")
@Data
@ComponentScan
public class EmonApiClientConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public EmonApiClient emonApiClient(){
        return new EmonApiClient(accessKey,secretKey);
    }

}
