package com.dowglasmaia.address.api.service.impl;

import br.com.dowglasmaia.openapi.model.AddressIdResponse;
import br.com.dowglasmaia.openapi.model.AddressRequest;
import br.com.dowglasmaia.openapi.model.AddressResponse;
import com.dowglasmaia.address.api.document.AddressDocument;
import com.dowglasmaia.address.api.repository.AddressRepository;
import com.dowglasmaia.address.api.service.AddressService;
import com.dowglasmaia.address.api.service.mapper.AddressMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository repository;
    @Autowired
    private AddressMapper mapper;

    @Override
    public Mono<AddressIdResponse> insert(Mono<AddressRequest> request) {
        log.info("Start Method insert Address");
        return request.flatMap(requestMapper -> {
                    AddressDocument requestDocument = mapper.toAddressDocument(requestMapper);
                    return repository.save(requestDocument);
                })
                .map(saveDocument -> new AddressIdResponse().id(saveDocument.getId()));
    }

    @Override
    public Mono<AddressResponse> findByZipCode(String zipCode) {
        log.info("Start Method findByZipCode with zipCode: {}", zipCode);

        return repository.findByZip(zipCode)
                .map(document -> mapper.toAddressResponse(document));
    }

    @Override
    public Mono<AddressResponse> update(Mono<AddressRequest> request, String addressId) {
        return request.flatMap(requestMap ->
                repository.findById(addressId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Address not found for update")))
                        .flatMap(document -> {
                            document.setStreet(requestMap.getStreet());
                            document.setNumber(requestMap.getNumber());
                            document.setCity(requestMap.getCity());
                            document.setState(requestMap.getState().getValue());
                            document.setZip(requestMap.getZip());

                            return repository.save(document);
                        })
                        .map(mapper::toAddressResponse)
                        .onErrorResume(error -> Mono.error(new RuntimeException("Error during update", error)))
        );
    }

    @Override
    public Mono<Void> delete(String addressId) {
        return null;
    }
}
