package test.springboot.restclientreactive.router;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class InfoHandler {

    public Mono<ServerResponse> test(ServerRequest request) {
        return ServerResponse.ok().build();
    }
}
