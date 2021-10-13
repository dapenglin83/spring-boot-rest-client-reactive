package test.springboot.restclientreactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import test.springboot.restclientreactive.data.CorporateAction;
import test.springboot.restclientreactive.data.Quote;
import test.springboot.restclientreactive.data.SGXResponse;
import test.springboot.restclientreactive.data.StockInfo;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestClientReactiveApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@BeforeEach
	public void setUp() {
		webTestClient = webTestClient
				.mutate()
				.responseTimeout(Duration.ofMillis(30000))
				.build();
	}

	@Test
	public void testInfo() {
		String code = "D05";
		webTestClient
				// Create a GET request to test an endpoint
				.get().uri("/sgx/info/" + code)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				// and use the dedicated DSL to test assertions against the response
				.expectStatus().isOk()
				.expectBody(StockInfo.class).value(info -> {
					assertThat(info.getStockCode()).isEqualTo(code);
				});
	}


	@Test
	public void testPrice() {
		String code = "D05";
		double price = 30.0;
		webTestClient
				// Create a GET request to test an endpoint
				.get().uri("/sgx/price/" + code)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				// and use the dedicated DSL to test assertions against the response
				.expectStatus().isOk()
				.expectBody(Quote.class).value(quote -> {
					assertThat(quote.getLast()).isGreaterThan(price);
				});
	}

	@Test
	public void testCorporateAction() {
		String code = "1L01";
		String name = "DBS GROUP HOLDINGS LTD";
		ParameterizedTypeReference<List<CorporateAction>> parameterizedTypeReference =
				new ParameterizedTypeReference<List<CorporateAction>>() {};
		webTestClient
				// Create a GET request to test an endpoint
				.get().uri("/sgx/corporateaction/" + code)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				// and use the dedicated DSL to test assertions against the response
				.expectStatus().isOk()
				.expectBody(parameterizedTypeReference)
				.value(actions -> {
					assertThat(actions.size()).isGreaterThan(0);
					assertThat(actions.get(0).getName()).isEqualTo(name);
				});
	}

}
