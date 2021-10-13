package test.springboot.restclientreactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import test.springboot.restclientreactive.data.*;

import java.util.List;

@Service
public class SGXService {

    @Autowired
    @Qualifier("api.sgx")
    WebClient webClient;

    public Mono<StockInfo> getStockInfo(String code) {
        ParameterizedTypeReference<SGXResponse<List<StockInfo>>> parameterizedTypeReference =
                new ParameterizedTypeReference<SGXResponse<List<StockInfo>>>(){};
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/marketmetadata/v2")
                        .queryParam("stock-code", code)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(parameterizedTypeReference)
                .map(response -> {
                    Meta meta = response.getMeta();
                    if ("200".equals(meta.getCode())) {
                        List<StockInfo> data = response.getData();
                        if (data == null || data.isEmpty()) {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
                        } else {
                            return data.get(0);
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.valueOf(meta.getCode()), meta.getMessage());
                    }
                });
    }

    public Mono<Quote> getPrice(String code, String params) {
        ParameterizedTypeReference<SGXResponse<PricesData>> parameterizedTypeReference =
                new ParameterizedTypeReference<SGXResponse<PricesData>>(){};
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/securities/v1.1/stocks/code/{code}")
                        .build(code))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(parameterizedTypeReference)
                .map(response -> {
                    Meta meta = response.getMeta();
                    if ("200".equals(meta.getCode())) {
                        PricesData prices = response.getData();
                        Quote[] quotes = prices.getPrices();
                        if (quotes != null && quotes.length > 0) {
                            return quotes[0];
                        } else {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.valueOf(meta.getCode()), meta.getMessage());
                    }
                });
    }

    public Mono<List<CorporateAction>> getCorporateActions(String ibmcode, int pageStart, int pageSize, String params) {
        ParameterizedTypeReference<SGXResponse<List<CorporateAction>>> parameterizedTypeReference =
                new ParameterizedTypeReference<SGXResponse<List<CorporateAction>>>() {
                };
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/corporateactions/v1.0")
                        .queryParam("ibmcode", ibmcode)
                        .queryParam("pagestart", pageStart)
                        .queryParam("pagesize", pageSize)
                        .queryParam("params", params)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(parameterizedTypeReference)
                .map(response -> {
                    Meta meta = response.getMeta();
                    if ("200".equals(meta.getCode())) {
                        List<CorporateAction> data = response.getData();
                        if (data != null && !data.isEmpty()) {
                            return data;
                        } else {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.valueOf(meta.getCode()), meta.getMessage());
                    }
                });
    }
}
