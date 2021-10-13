package test.springboot.restclientreactive.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import test.springboot.restclientreactive.service.SGXService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration(proxyBeanMethods = false)
public class SGXRouter {
    @Autowired
    SGXService sgxService;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return nest(path("/sgx"),
                nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
                        route(GET("/info/{code}"), request -> {
                                String code = request.pathVariable("code");
                                return sgxService.getStockInfo(code).flatMap(info ->
                                        ServerResponse.ok().contentType(APPLICATION_JSON)
                                                .body(BodyInserters.fromValue(info)));
                            }
                        ).andRoute(GET("/price/{code}"), request -> {
                            String code = request.pathVariable("code");
                            String param = request.queryParam("param").orElse("b,s,o,h,l,lt");
                            return sgxService.getPrice(code, param).flatMap(quote ->
                                    ServerResponse.ok().contentType(APPLICATION_JSON)
                                            .body(BodyInserters.fromValue(quote)));
                        }).andRoute(GET("/corporateaction/{code}"), request -> {
                            String code = request.pathVariable("code");
                            int pageStart = request.queryParam("pagestart").map(Integer::valueOf)
                                    .orElse(0);
                            int pageSize = request.queryParam("pagesize").map(Integer::valueOf)
                                    .orElse(20);
                            String param = request.queryParam("param")
                                    .orElse("id,anncType,datePaid,exDate,name,particulars,recDate");
                            return sgxService.getCorporateActions(code, pageStart, pageSize, param)
                                    .flatMap(quote -> ServerResponse.ok().contentType(APPLICATION_JSON)
                                            .body(BodyInserters.fromValue(quote)));
                        })
                )
        );
    }
}
