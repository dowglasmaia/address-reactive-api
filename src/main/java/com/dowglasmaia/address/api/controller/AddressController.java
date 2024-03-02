package com.dowglasmaia.address.api.controller;

import br.com.dowglasmaia.openapi.api.AddressApi;
import br.com.dowglasmaia.openapi.model.AddressRequest;
import br.com.dowglasmaia.openapi.model.AddressResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping(value = "/api/v1/adresses")
public class AddressController implements AddressApi {
    @Override
    public Mono<ResponseEntity<AddressResponse>> createAddress(
            String clientId,
            Mono<AddressRequest> addressRequest,
            ServerWebExchange exchange) {
        log.info("Start endpoint createAddress");

        throw new NotImplementedException();
    }

    @Override
    public Mono<ResponseEntity<AddressResponse>> findByClientById(
            String clientId,
            ServerWebExchange exchange) {
        log.info("Start endpoint findByClientById");

        throw new NotImplementedException();
    }
}
