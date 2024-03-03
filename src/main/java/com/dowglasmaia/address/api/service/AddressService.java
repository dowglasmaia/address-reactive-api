package com.dowglasmaia.address.api.service;

import br.com.dowglasmaia.openapi.model.AddressIdResponse;
import br.com.dowglasmaia.openapi.model.AddressRequest;
import br.com.dowglasmaia.openapi.model.AddressResponse;
import reactor.core.publisher.Mono;

public interface AddressService {

    Mono<AddressIdResponse> insert(AddressRequest request);

    Mono<AddressResponse> findByZipCode(String zipCode);


}
