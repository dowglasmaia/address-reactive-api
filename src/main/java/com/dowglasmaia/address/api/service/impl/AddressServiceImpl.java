package com.dowglasmaia.address.api.service.impl;

import br.com.dowglasmaia.openapi.model.AddressIdResponse;
import br.com.dowglasmaia.openapi.model.AddressRequest;
import br.com.dowglasmaia.openapi.model.AddressResponse;
import com.dowglasmaia.address.api.document.AddressDocument;
import com.dowglasmaia.address.api.repository.AddressRepository;
import com.dowglasmaia.address.api.service.AddressService;
import com.dowglasmaia.address.api.service.mapper.AddressMapper;
import io.micrometer.observation.annotation.Observed;
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
    @Observed(name = "insert.address")
    public Mono<AddressIdResponse> insert(Mono<AddressRequest> request) {
        return request
                .doFirst(() -> log.info("Start Method insert Address"))
                .flatMap(this::toAddressDocument)
                .flatMap(repository::save)
                .map(savedDocument -> new AddressIdResponse().id(savedDocument.getId()))
                .doOnSuccess(response -> log.info("Successfully saved address"))
                .doOnError(error -> log.error("insert Fail"))
                .onErrorResume(error -> Mono.error(new RuntimeException("Error during insert", error)));
    }

    @Override
    @Observed(name = "find.byZipCode")
    public Mono<AddressResponse> findByZipCode(String zipCode) {
        return repository.findByZip(zipCode)
                .doFirst(() -> log.info("Start Method findByZipCode with zipCode: {}", zipCode))
                .map(document -> mapper.toAddressResponse(document));
    }

    @Override
    @Observed(name = "update.address")
    public Mono<AddressResponse> update(Mono<AddressRequest> request, String addressId) {
        return request
                .flatMap(requestMap ->
                        repository.findById(addressId)
                                .doFirst(() -> log.info("Start Method update with Address: {}", requestMap))
                                .switchIfEmpty(Mono.error(new RuntimeException("Address not found for update")))
                                .flatMap(document -> {
                                    document.setStreet(requestMap.getStreet());
                                    document.setNumber(requestMap.getNumber());
                                    document.setCity(requestMap.getCity());
                                    document.setState(requestMap.getState().getValue());
                                    document.setZip(requestMap.getZip());
                                    return repository.save(document)
                                            .map(mapper::toAddressResponse);
                                })
                                .onErrorResume(error -> Mono.error(new RuntimeException("Error during update", error)))
                                .doOnSuccess(response -> log.info("Update successfully performed! "))
                                .doOnError(error -> log.error("Update Fail"))
                );
    }

    @Override
    @Observed(name = "delete.address")
    public Mono<Void> delete(String addressId) {
        return repository.findById(addressId)
                .doFirst(() -> log.info("Start Method delete with AddressId: {}", addressId))
                .flatMap(addressDocument -> repository.deleteById(addressId))
                .onErrorResume(error -> Mono.error(new RuntimeException("Error during delete", error)));
    }

    private Mono<AddressDocument> toAddressDocument(AddressRequest request) {
        AddressDocument document = new AddressDocument();
        document.setCity(request.getCity());
        document.setStreet(request.getStreet());
        document.setState(request.getState().getValue());
        document.setZip(request.getZip());
        document.setNeighborhood(request.getNeighborhood());
        document.setNumber("");
        return Mono.just(document);
    }
}
