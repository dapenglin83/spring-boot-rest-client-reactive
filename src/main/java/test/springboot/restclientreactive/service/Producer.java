package test.springboot.restclientreactive.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Producer {

    @Bean("api.sgx")
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.sgx.com").build();
    }
}
