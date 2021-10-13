package test.springboot.restclientreactive.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Producer {

    @Value("${api.sgx.url:https://api.sgx.com}")
    String sgxApiUrl;

    @Bean("api.sgx")
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder.baseUrl(sgxApiUrl).build();
    }
}
