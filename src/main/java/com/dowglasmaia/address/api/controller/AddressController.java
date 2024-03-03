package com.dowglasmaia.address.api.controller;

import br.com.dowglasmaia.openapi.api.AddressesApi;
import br.com.dowglasmaia.openapi.model.AddressIdResponse;
import br.com.dowglasmaia.openapi.model.AddressRequest;
import br.com.dowglasmaia.openapi.model.AddressResponse;
import com.dowglasmaia.address.api.service.AddressService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping(value = "/api/v1")
public class AddressController implements AddressesApi {
    @Autowired
    private AddressService service;

    @Override
    public Mono<ResponseEntity<AddressIdResponse>> createAddress(Mono<AddressRequest> addressRequest, ServerWebExchange exchange) {
        log.info("Start endpoint createAddress");
        return addressRequest
                .flatMap(request -> {
                    log.info("Body Request: {}", addressRequest);
                    return service.insert(request)
                            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
                });
    }

    @Override
    public Mono<ResponseEntity<AddressResponse>> findByZipCode(String zipCode, ServerWebExchange exchange) {
        log.info("Start endpoint findByZipCode");
        return service.findByZipCode(zipCode)
                .flatMap(addressResponse -> Mono.just(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(addressResponse)));
    }

}
