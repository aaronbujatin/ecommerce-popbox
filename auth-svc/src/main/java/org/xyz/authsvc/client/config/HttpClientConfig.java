package org.xyz.authsvc.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.xyz.authsvc.client.UserClient;
import org.xyz.authsvc.client.util.HttpClientLogger;

@Slf4j
@Configuration
public class HttpClientConfig {
    @Value("${api.product.base-url}")
    private String productBaseUrl;
    @Value("${api.order.base-url}")
    private String orderBaseUrl;
    @Value("${api.user.base-url}")
    private String userBaseUrl;

    @Bean
    public UserClient userClient() {
        RestClient restClient = createRestClient(userBaseUrl);
        return restClientFactory(restClient, UserClient.class);
    }

    private RestClient createRestClient(String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(new BufferingClientHttpRequestFactory(
                                new SimpleClientHttpRequestFactory()
                        )
                )
                .requestInterceptor((request, body, execution) -> {
                    HttpClientLogger.logRequest(request, body);
                    ClientHttpResponse response = execution.execute(request, body);
                    HttpClientLogger.logResponse(response);
                    return response;
                })
                .build();
    }

    private <T> T restClientFactory(RestClient restClient, Class<T> tClass) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(tClass);
    }
}
