package com.dowglasmaia.address.api.integration;

import com.dowglasmaia.address.api.integration.model.ViaCpeModel;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Service
public class ViaCepApiIntegration {


    @Value("${client.viaCep.uri}")
    private String viaCepUri;

    @Autowired
    private WebClient.Builder webClientBuilder;


    @Observed(name = "getAddressByZipCode.via_cep")
    public Mono<ViaCpeModel> getAddressByZipCode(String zipCode) {
        log.info("Start getUserAccountByUserId with :" + zipCode);
        WebClient webClient = webClientBuilder.baseUrl(viaCepUri).build();

        return webClient
                .get()
                .uri(viaCepUri + zipCode+"/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, parseError4xx("SERVICE", "URL" + "ID"))
                .onStatus(HttpStatusCode::is5xxServerError, parseError5xx("SERVICE", "URL" + "ID"))
                .bodyToMono(ViaCpeModel.class)
                .doOnSuccess(response -> log.info("Sucesso"))
                .doOnError(error -> log.error("Error"));
    }


    public Function<ClientResponse, Mono<? extends Throwable>> parseError4xx(String service, String baseUrl) {
        //var uri = UriComponentsBuilder.fromHttpUrl(baseUrl).build().getPath();
        return response -> response
                .bodyToMono(String.class)
                .switchIfEmpty(Mono.error(() -> new RestClientException("IsEmpty" + service)))
                .flatMap(body -> {
                    log.error("Error: {}", body);
                    return Mono.error(() -> new RestClientException("IsEmpty: " +body));
                });
    }

    public Function<ClientResponse, Mono<? extends Throwable>> parseError5xx(String service, String baseUrl) {
       // var uri = UriComponentsBuilder.fromHttpUrl(baseUrl).build().getPath();
        return response -> response
                .bodyToMono(String.class)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Error 5xx")))
                .flatMap(body -> Mono.error(() -> new RuntimeException("Error 5xx")));
    }
}
